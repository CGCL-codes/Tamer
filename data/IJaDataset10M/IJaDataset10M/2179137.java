package org.labrad.browser.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.UnexpectedException;

/**
 * Jetty Continuation support for GWT RPC.
 * 
 * @author Craig Day (craig@alderaan.com.au)
 */
@SuppressWarnings("serial")
public class AsyncRemoteServiceServlet extends RemoteServiceServlet {

    public static final String PAYLOAD = "com.google.gwt.payload";

    private static final String JETTY_RETRY_REQUEST_EXCEPTION = "org.mortbay.jetty.RetryRequest";

    protected String readContent(HttpServletRequest request) throws IOException, ServletException {
        String payload = (String) request.getAttribute(PAYLOAD);
        if (payload == null) {
            payload = super.readContent(request);
            request.setAttribute(PAYLOAD, payload);
        }
        return payload;
    }

    /**
   * Overridden to really throw Jetty RetryRequest Exception (as opposed to sending failure to client).
   *
   * @param caught the exception
   */
    protected void doUnexpectedFailure(Throwable caught) {
        throwIfRetryRequest(caught);
        super.doUnexpectedFailure(caught);
    }

    /**
   * Throws the Jetty RetryRequest if found.
   *
   * @param caught the exception
   */
    protected void throwIfRetryRequest(Throwable caught) {
        if (caught instanceof UnexpectedException) {
            caught = caught.getCause();
        }
        if (JETTY_RETRY_REQUEST_EXCEPTION.equals(caught.getClass().getName())) {
            throw (RuntimeException) caught;
        }
    }
}
