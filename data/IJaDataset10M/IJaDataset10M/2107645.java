package org.xhtmlrenderer.protocols.data;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler {

    protected void parseURL(URL u, String spec, int start, int limit) {
        String sub = spec.substring(start, limit);
        if (sub.indexOf(',') < 0) {
            throw new RuntimeException("Improperly formatted data URL");
        }
        setURL(u, "data", "", -1, "", "", sub, "", "");
    }

    protected URLConnection openConnection(URL u) throws IOException {
        return new DataURLConnection(u);
    }
}
