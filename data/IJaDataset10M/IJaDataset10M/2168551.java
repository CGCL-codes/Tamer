package net.sf.urlchecker.v2.communication;

import java.io.IOException;
import javax.net.ssl.SSLHandshakeException;
import net.sf.urlchecker.communication.CommunicationFactory;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

/**
 * The Class RequestRetryHandler for the new version of the HttpClient. This
 * Handler is much like the default. It is here for future enhancements.
 * 
 * <p>
 * <b> $Id: RequestRetryHandler.java 183 2010-12-17 19:10:21Z georgosn $</b>
 * </p>
 * 
 * @author $LastChangedBy: georgosn $
 * @version $LastChangedRevision: 183 $
 */
public class RequestRetryHandler implements HttpRequestRetryHandler {

    /** {@inheritDoc} */
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        if (executionCount >= CommunicationFactory.getInstance().getMaxretries()) {
            return false;
        }
        if (executionCount >= 5) {
            return false;
        }
        if (exception instanceof NoHttpResponseException) {
            return true;
        }
        if (exception instanceof SSLHandshakeException) {
            return false;
        }
        final HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        final boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
        if (idempotent) {
            return true;
        }
        return false;
    }
}
