package cgl.shindig.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Sample response from VerifyCallback.
 * {"result":[{"valid":true,"cert":
 * "-----BEGIN CERTIFICATE-----\ncontent\n-----END CERTIFICATE-----\n-----BEGIN RSA PRIVATE KEY-----\ncontent\n-----END RSA PRIVATE KEY-----\n"
 * ,"email":"zhguo@indiana.edu","userid":"testuser2"},{"valid":false,"email":
 * "testuser","userid":""}],"status":"succ"}
 */
public class VerifyCallbackExample {

    private String callbackResult = "";

    private void parse(String response) throws JSONException {
        JSONObject jsonObj = new JSONObject(response);
        String status = jsonObj.getString("status");
        if (!status.equalsIgnoreCase("succ")) {
            return;
        }
        JSONArray results = jsonObj.getJSONArray("result");
        for (int i = 0; i < results.length(); ++i) {
            JSONObject result = (JSONObject) results.get(i);
            if (result.has("cert")) {
                String credStr = result.getString("cert");
                String email = result.getString("email");
                credStr = credStr.replaceAll("\\\\n", "\n");
                System.out.println("email:" + email + "\ncredential:\n" + credStr + "\n");
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, JSONException {
        if (args.length < 1) {
            System.out.println("Missing argument");
            return;
        }
        String inputFile = args[0];
        System.out.println("Input file is " + inputFile);
        String content = IOUtils.toString(new FileInputStream(inputFile));
        VerifyCallbackExample example = new VerifyCallbackExample();
        example.parse(content);
    }
}
