package gate.cloud.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.zip.GZIPInputStream;

/**
 * A URL stream handler that can be used to read data compressed in GZip format.
 * This implementation is not feature complete - it only has the functionality 
 * required to open GATE documents from gzipped files! 
 */
public class GZIPURLStreamHandler extends URLStreamHandler {

    /**
   * A URL connection that has the minimal implementation for uncompressing 
   * GZip content. 
   */
    public class GZIPURLConnection extends URLConnection {

        public GZIPURLConnection() throws IOException {
            super(originalURL);
        }

        /**
     * A URLConnection from the original URL.
     */
        protected URLConnection originalConnection;

        @Override
        public void connect() throws IOException {
            if (!connected) {
                this.originalConnection = originalURL.openConnection();
                connected = true;
            }
        }

        @Override
        public InputStream getInputStream() throws IOException {
            if (!connected) connect();
            return new GZIPInputStream(originalConnection.getInputStream());
        }
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new GZIPURLConnection();
    }

    /**
   * The URL we are wrapping.
   */
    protected URL originalURL;

    public GZIPURLStreamHandler(URL wrappedUrl) {
        super();
        this.originalURL = wrappedUrl;
    }
}
