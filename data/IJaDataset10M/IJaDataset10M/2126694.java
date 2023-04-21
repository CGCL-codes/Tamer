package org.apache.http.protocol;

import java.io.IOException;
import java.net.InetAddress;
import org.apache.http.HttpConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpInetConnection;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.ProtocolException;

/**
 * RequestTargetHost is responsible for adding <code>Host</code> header. This 
 * interceptor is required for client side protocol processors. 
 *
 *
 * @version $Revision: 781114 $
 * 
 * @since 4.0
 */
public class RequestTargetHost implements HttpRequestInterceptor {

    public RequestTargetHost() {
        super();
    }

    public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
        if (request == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }
        if (context == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        }
        ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
        String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT") && ver.lessEquals(HttpVersion.HTTP_1_0)) {
            return;
        }
        if (!request.containsHeader(HTTP.TARGET_HOST)) {
            HttpHost targethost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
            if (targethost == null) {
                HttpConnection conn = (HttpConnection) context.getAttribute(ExecutionContext.HTTP_CONNECTION);
                if (conn instanceof HttpInetConnection) {
                    InetAddress address = ((HttpInetConnection) conn).getRemoteAddress();
                    int port = ((HttpInetConnection) conn).getRemotePort();
                    if (address != null) {
                        targethost = new HttpHost(address.getHostName(), port);
                    }
                }
                if (targethost == null) {
                    if (ver.lessEquals(HttpVersion.HTTP_1_0)) {
                        return;
                    } else {
                        throw new ProtocolException("Target host missing");
                    }
                }
            }
            request.addHeader(HTTP.TARGET_HOST, targethost.toHostString());
        }
    }
}
