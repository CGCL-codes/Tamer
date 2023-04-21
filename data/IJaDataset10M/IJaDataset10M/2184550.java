package x.java.net.protocol.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author qiangli
 * 
 */
public class URLConnection extends java.net.URLConnection {

    /**
	 * @param url
	 */
    public URLConnection(URL url) {
        super(parse(url));
    }

    private static URL parse(URL url) {
        String path = url.getPath();
        int idx = path.indexOf("!");
        if (idx > 0) {
            path = path.substring(0, idx);
        }
        return URLConnection.class.getResource(path);
    }

    public void connect() throws IOException {
    }

    public InputStream getInputStream() throws IOException {
        try {
            return url.openStream();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
