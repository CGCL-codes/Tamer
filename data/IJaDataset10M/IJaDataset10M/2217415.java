package de.felixbruns.jotify.gateway.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.felixbruns.jotify.gateway.GatewayApplication;

public class ContentHandler implements HttpHandler {

    private static Map<String, String> contentTypes;

    static {
        contentTypes = new HashMap<String, String>();
        contentTypes.put("html", "text/html");
        contentTypes.put("css", "text/css");
        contentTypes.put("js", "text/javascript");
        contentTypes.put("png", "image/png");
        contentTypes.put("gif", "image/gif");
        contentTypes.put("jpg", "image/jpeg");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        OutputStream body = exchange.getResponseBody();
        String path = exchange.getRequestURI().getPath();
        byte[] buffer = new byte[8192];
        int read;
        String resource;
        if (path.equals("/")) {
            resource = "resources/index.html";
        } else {
            resource = "resources" + path;
        }
        String extension = resource.substring(resource.lastIndexOf('.') + 1);
        String contentType = contentTypes.get(extension);
        InputStream stream = GatewayApplication.class.getResourceAsStream(resource);
        if (stream != null) {
            headers.set("Content-Type", contentType);
            headers.set("Cache-Control", "max-age=604800");
            exchange.sendResponseHeaders(200, stream.available());
            while ((read = stream.read(buffer)) != -1) {
                body.write(buffer, 0, read);
            }
        } else {
            byte[] bytes = "404 Not Found".getBytes();
            exchange.sendResponseHeaders(404, bytes.length);
            body.write(bytes);
        }
        body.close();
    }
}
