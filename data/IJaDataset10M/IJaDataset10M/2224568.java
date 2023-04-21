package dk.qabi.imapfs;

import java.net.URLStreamHandler;
import java.net.URLConnection;
import java.net.URL;
import java.io.IOException;

public class IMAPStreamHandler extends URLStreamHandler {

    protected URLConnection openConnection(URL u) throws IOException {
        return null;
    }
}
