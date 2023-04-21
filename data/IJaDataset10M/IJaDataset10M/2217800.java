package project.fantalk.api;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.google.appengine.repackaged.com.google.common.util.Base64;

public class BasicAuthTest {

    public static void main(String[] args) {
        new BasicAuthTest().test();
    }

    private void test() {
        try {
            URL url = new URL("http://api.fanfou.com/account/verify_credentials.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String userpass = "中文测试:123456";
            String basicAuth = Base64.encode(userpass.getBytes("UTF-8"));
            conn.addRequestProperty("Authorization", "Basic " + basicAuth);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            InputStream in = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuffer sb = new StringBuffer();
            String tmp = null;
            while ((tmp = br.readLine()) != null) {
                sb.append(tmp);
            }
            System.out.println(sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
