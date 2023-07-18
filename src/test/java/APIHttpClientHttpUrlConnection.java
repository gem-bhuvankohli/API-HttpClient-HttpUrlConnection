import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIHttpClientHttpUrlConnection {
    static String bookingID;

    public static void main(String[] args) {

//POST Method of HTTP Client

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost("https://restful-booker.herokuapp.com/booking");

        JSONObject payload = new JSONObject();
        payload.put("firstname", "bhuvan");
        payload.put("lastname", "kohli");
        payload.put("totalprice", 111);
        payload.put("depositpaid", true);

        JSONObject innerList = new JSONObject();
        innerList.put("checkin", "2018-01-01");
        innerList.put("checkout", "2019-01-01");

        payload.put("bookingdates", innerList);
        payload.put("additionalneeds", "Breakfast");
        Log.info("POST Request using HTTP client:\n" + payload);

//      Setting headers
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Accept", "application/json");

//      Posting the payload
        post.setEntity(new StringEntity(payload.toString(), ContentType.APPLICATION_JSON));

        try (CloseableHttpResponse response = httpClient.execute(post)) {
            int statusCode = response.getStatusLine().getStatusCode();
            Log.info("Status code: " + statusCode);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String responseBody = EntityUtils.toString(entity);
//              Fetching Booking ID
                bookingID = responseBody.split(":")[1].split(",")[0];
            }
            Log.info("Booking ID: " + bookingID + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {

//          POST Method of HTTP URL Connection
            URL url = new URL("https://restful-booker.herokuapp.com/booking");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

//          Setting Headers
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject payload2 = new JSONObject();
            payload2.put("firstname", "bhuvan3000");
            payload2.put("lastname", "kohli3000");
            payload2.put("totalprice", 222);
            payload2.put("depositpaid", true);
            JSONObject innerList2 = new JSONObject();
            innerList2.put("checkin", "2018-01-01");
            innerList2.put("checkout", "2019-01-01");
            payload2.put("bookingdates", innerList2);
            payload2.put("additionalneeds", "Breakfast");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload2.toString().getBytes());
                os.flush();
            }

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            Log.info("POST request using HTTP Url Client:");
            while ((output = br.readLine()) != null) {
                Log.info(output + "\n\n");
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

//          Authentication by successfully updating the payload using PUT method of HTTP URL connection
            URL url = new URL("https://restful-booker.herokuapp.com/booking/" + bookingID);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");

//          Setting Headers
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");

//          Using Authentication
            conn.setRequestProperty("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=");
            conn.setDoOutput(true);

//          Updated Payload
            JSONObject updatedPayload = new JSONObject();
            updatedPayload.put("firstname", "changedName");
            updatedPayload.put("lastname", "kohli");
            updatedPayload.put("totalprice", 111);
            updatedPayload.put("depositpaid", true);
            JSONObject innerList3 = new JSONObject();
            innerList3.put("checkin", "2018-01-01");
            innerList3.put("checkout", "2019-01-01");
            updatedPayload.put("bookingdates", innerList3);
            updatedPayload.put("additionalneeds", "Breakfast");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(updatedPayload.toString().getBytes());
                os.flush();
            }

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            Log.info("Authentication by successfully updating the payload using PUT method of HTTP URL connection:");
            while ((output = br.readLine()) != null) {
                Log.info(output);
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
