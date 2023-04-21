package info.piwai.yasdic.example.countadroid.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import android.util.Log;

/**
 * A remote counter that uses a HttpClient to contact a Url.
 * 
 * The expected behavior is the following : calling the counterURL should return
 * a one line response with only the count (being a String).
 * 
 * If the call is made with an "increment" parameter, the count is incremented
 * before being returned. See "coutadroid/etc/web/count.php" for an example
 * script.
 * 
 * @author Pierre-Yves Ricau (py.ricau+countadroid@gmail.com)
 * 
 */
public class RemoteCounter implements ICounter {

    private static final String TAG = RemoteCounter.class.getSimpleName();

    private HttpClient httpClient;

    private String counterURL;

    public int increment() {
        return getCountFromUrl(counterURL + "?increment");
    }

    public int getCount() {
        return getCountFromUrl(counterURL);
    }

    private int getCountFromUrl(String url) {
        HttpGet request = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(request);
            int status = response.getStatusLine().getStatusCode();
            if (status != HttpStatus.SC_OK) {
                ByteArrayOutputStream ostream = new ByteArrayOutputStream();
                response.getEntity().writeTo(ostream);
                Log.e(TAG, ostream.toString());
            } else {
                InputStream content = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content), 10);
                String count = reader.readLine();
                content.close();
                return Integer.parseInt(count);
            }
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return -1;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String getCounterURL() {
        return counterURL;
    }

    public void setCounterURL(String counterURL) {
        this.counterURL = counterURL;
    }
}
