import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

class BusRestClient {
    public static void main(String[] args) {

        try {
            // Construct manually a JSON object in Java, for testing purposes an object with an object

            Scanner sc= new Scanner(System.in);
            System.out.print("Enter your request type (availability or ticket): ");

            String requestType = sc.nextLine();

            JSONObject data = new JSONObject();
            URL url = null;

            if (requestType.equals("availability")) {

                System.out.print("Enter the origin and destination(X/Y): ");
                String ond = sc.nextLine();
                data.put("ond", ond);

                System.out.print("Enter passenger count: ");
                String paxCount = sc.nextLine();
                data.put("paxCount", Integer.parseInt(paxCount));

                System.out.print("Enter the preferred date(YYYY/MM/DD): ");
                String date = sc.nextLine();
                data.put("scheduledDateStr", date);

                url = new URL("http://localhost:8080/api/availability");
            }

            if (requestType.equals("ticket")) {

                System.out.print("Enter the origin and destination(X/Y): ");
                String ond = sc.nextLine();
                data.put("ond", ond);

                System.out.print("Enter passenger count: ");
                String paxCount = sc.nextLine();
                data.put("paxCount", Integer.parseInt(paxCount));

                System.out.print("Enter the preferred date(YYYY/MM/DD): ");
                String date = sc.nextLine();
                data.put("scheduledDateStr", date);

                System.out.print("Enter the quoted price: ");
                String price = sc.nextLine();
                data.put("totalPrice", Integer.parseInt(price));

                url = new URL("http://localhost:8080/api/createBooking");
            }

            if (!requestType.equals("availability") && !requestType.equals("ticket")) {
                System.exit(0);
            }

            HttpURLConnection httpConnection  = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Accept", "application/json");

            // Writes the JSON parsed as string to the connection
            DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
            wr.write(data.toString().getBytes());
            int responseCode = httpConnection.getResponseCode();

            BufferedReader bufferedReader;

            // Creates a reader buffer
            if (responseCode > 199 && responseCode < 300) {
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()));
            }

            // To receive the response
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();

            // Prints the response
            System.out.println(content.toString());

        } catch (Exception e) {
            System.out.println("Error Message");
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }
}
