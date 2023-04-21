package org.avis.management;

import java.util.Date;
import java.util.Locale;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.asyncweb.common.DefaultHttpResponse;
import org.apache.asyncweb.common.HttpRequest;
import org.apache.asyncweb.server.HttpService;
import org.apache.asyncweb.server.HttpServiceContext;
import org.apache.mina.core.buffer.IoBuffer;
import static java.lang.Math.min;
import static java.lang.System.currentTimeMillis;
import static java.util.TimeZone.getTimeZone;
import static org.apache.asyncweb.common.HttpResponseStatus.NOT_FOUND;
import static org.apache.asyncweb.common.HttpResponseStatus.NOT_MODIFIED;
import static org.apache.asyncweb.common.HttpResponseStatus.OK;
import static org.avis.logging.Log.diagnostic;

/**
 * Serves resources under a given URL to clients.
 * 
 * @author Matthew Phillips
 */
public class ResourceHttpService implements HttpService {

    private static final int MAX_CACHE_AGE = 6 * 60 * 60 * 1000;

    private static final ThreadLocal<DateFormat> httpDateFormat = new ThreadLocal<DateFormat>() {

        protected DateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
            format.setTimeZone(getTimeZone("GMT"));
            return format;
        }
    };

    private URL baseUrl;

    public ResourceHttpService(URL baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void handleRequest(HttpServiceContext context) throws Exception {
        DefaultHttpResponse response = new DefaultHttpResponse();
        String path = context.getRequest().getRequestUri().getPath().substring(1);
        URL url = new URL(baseUrl.getProtocol(), baseUrl.getHost(), baseUrl.getPort(), baseUrl.getPath() + path);
        if (!url.getPath().startsWith(baseUrl.getPath())) {
            response.setStatus(NOT_FOUND);
            context.commitResponse(response);
            return;
        }
        URLConnection connection = url.openConnection();
        long lastModified = connection.getLastModified();
        if (lastModified != 0 && lastModified <= ifModifiedSince(context.getRequest())) {
            response.setStatus(NOT_MODIFIED);
        } else {
            if (lastModified != 0) {
                response.setHeader("Last-Modified", formatDate(lastModified));
                response.setHeader("Expires", formatDate(currentTimeMillis() + MAX_CACHE_AGE));
            }
            String contentType = guessContentType(path);
            if (contentType != null) response.setHeader("Content-Type", contentType);
            try {
                response.setContent(contentsOf(connection));
                response.setStatus(OK);
            } catch (FileNotFoundException ex) {
                response.setStatus(NOT_FOUND);
            }
        }
        context.commitResponse(response);
    }

    private static long ifModifiedSince(HttpRequest request) {
        String ifModifiedSince = request.getHeader("If-Modified-Since");
        if (ifModifiedSince != null) {
            try {
                return parseDate(ifModifiedSince).getTime();
            } catch (ParseException ex) {
                diagnostic("Invalid HTTP date from client", ResourceHttpService.class, ex);
            }
        }
        return 0;
    }

    private static String formatDate(long date) {
        return httpDateFormat.get().format(new Date(date));
    }

    private static Date parseDate(String date) throws ParseException {
        return httpDateFormat.get().parse(date);
    }

    private static String guessContentType(String path) {
        String type;
        if (path.endsWith(".css")) type = "text/css; charset=UTF-8"; else if (path.endsWith(".ico")) type = "image/vnd.microsoft.icon"; else if (path.endsWith(".js")) type = "text/javascript; charset=UTF-8"; else type = URLConnection.guessContentTypeFromName(path);
        return type;
    }

    private static IoBuffer contentsOf(URLConnection connection) throws IOException {
        InputStream in = connection.getInputStream();
        try {
            IoBuffer buffer = IoBuffer.allocate(connection.getContentLength());
            byte[] bytes = new byte[min(connection.getContentLength(), 8192)];
            int bytesRead;
            while ((bytesRead = in.read(bytes)) != -1) buffer.put(bytes, 0, bytesRead);
            return buffer.flip();
        } finally {
            in.close();
        }
    }

    public void start() {
    }

    public void stop() {
    }
}
