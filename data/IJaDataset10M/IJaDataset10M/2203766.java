package com.gargoylesoftware.htmlunit.protocol.data;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * Stream handler for data URLs.
 *
 * @version $Revision: 6701 $
 * @author Marc Guillemot
 */
public class Handler extends URLStreamHandler {

    /**
     * Returns a new URLConnection for this URL.
     * @param url the JavaScript URL
     * @return the connection
     */
    @Override
    protected URLConnection openConnection(final URL url) {
        return new DataURLConnection(url);
    }
}
