package myjhttp.core;

import java.util.Properties;

public class HttpMIME {

    private static Properties MIME;

    private HttpMIME() {
    }

    private static void makeMIME() {
        MIME = new Properties();
        MIME.clear();
        MIME.setProperty(".gif", "image/gif");
        MIME.setProperty(".png", "image/png");
        MIME.setProperty(".jpeg", "image/jpeg");
        MIME.setProperty(".jpg", "image/jpeg");
        MIME.setProperty(".html", "text/html");
        MIME.setProperty(".htm", "text/html");
        MIME.setProperty(".css", "text/css");
        MIME.setProperty(".mp3", "audio/mp3");
        MIME.setProperty(".txt", "text/plain");
        MIME.setProperty(".js", "text/javascript");
    }

    public static String getProperty(String key) {
        if (MIME == null) makeMIME();
        return MIME.getProperty(key);
    }
}
