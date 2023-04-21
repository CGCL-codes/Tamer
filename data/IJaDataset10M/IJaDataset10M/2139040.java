package net.stickycode.protocol.sticky;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class Handler extends URLStreamHandler {

    public static String PROTOCOL = "sticky";

    private static URLStreamHandlerFactory urlStreamHandlerFactory;

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return null;
    }

    public static void setUrlStreamHandlerFactory(URLStreamHandlerFactory urlStreamHandlerFactory) {
        Handler.urlStreamHandlerFactory = urlStreamHandlerFactory;
    }
}
