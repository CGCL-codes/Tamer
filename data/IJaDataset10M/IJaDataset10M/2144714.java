package org.jtools.io;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

final class Handler extends URLStreamHandler {

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        try {
            return new IOFileURLConnection(url, IOFileUtils.createFile(url.toURI()));
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }
}
