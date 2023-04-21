package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;

/**
 * RequestConnControl is responsible for adding <code>Connection</code> header 
 * to the outgoing requests, which is essential for managing persistence of 
 * <code>HTTP/1.0</code> connections. This interceptor is recommended for 
 * client side protocol processors.
 *
 *
 * @version $Revision: 744532 $
 * 
 * @since 4.0
 */
public class RequestConnControl implements HttpRequestInterceptor {

    public RequestConnControl() {
        super();
    }

    public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
        if (request == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }
        String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT")) {
            return;
        }
        if (!request.containsHeader(HTTP.CONN_DIRECTIVE)) {
            request.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        }
    }
}
