package twitter4j;

import twitter4j.http.Response;
import twitter4j.org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.4
 */
public class StatusStream {

    private boolean streamAlive = true;

    private BufferedReader br;

    private InputStream is;

    private Response response;

    StatusStream(InputStream stream) throws IOException {
        this.is = stream;
        this.br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
    }

    StatusStream(Response response) throws IOException {
        this(response.asStream());
        this.response = response;
    }

    public Status next() throws TwitterException {
        if (!streamAlive) {
            throw new IllegalStateException("Stream already closed.");
        }
        try {
            String line;
            while (streamAlive) {
                line = br.readLine();
                if (null != line && line.length() > 0) {
                    try {
                        return new Status(line);
                    } catch (JSONException ignore) {
                    }
                }
            }
            throw new TwitterException("Stream closed.");
        } catch (IOException e) {
            try {
                is.close();
            } catch (IOException ignore) {
            }
            streamAlive = false;
            throw new TwitterException("Stream closed.", e);
        }
    }

    public void close() throws IOException {
        is.close();
        br.close();
        if (null != response) {
            response.disconnect();
        }
    }
}
