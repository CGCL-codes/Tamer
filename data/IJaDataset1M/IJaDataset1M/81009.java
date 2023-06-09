package gtranslate.internal.http;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

public class GzipRequestInterceptor implements HttpRequestInterceptor {

    private static final GzipRequestInterceptor instance = new GzipRequestInterceptor();

    public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
        if (!request.containsHeader("Accept-Encoding")) {
            request.addHeader("Accept-Encoding", "gzip");
        }
    }

    public static GzipRequestInterceptor getInstance() {
        return instance;
    }
}
