package org.apache.commons.httpclient;

/**
 * <p>
 * Signals that an HTTP or HttpClient exception has occurred.  This
 * exception may have been caused by a transient error and the request
 * may be retried.  It may be possible to retrieve the underlying transient
 * error via the inherited {@link HttpException#getCause()} method.
 * </p>
 * 
 * @deprecated no longer used
 * 
 * @author Unascribed
 * @version $Revision: 1.1.4.2 $ $Date: 2007/06/15 06:53:58 $
 */
public class HttpRecoverableException extends HttpException {

    /**
     * Creates a new HttpRecoverableException with a <tt>null</tt> detail message.
     */
    public HttpRecoverableException() {
        super();
    }

    /**
     * Creates a new HttpRecoverableException with the specified detail message.
     *
     * @param message exception message
     */
    public HttpRecoverableException(String message) {
        super(message);
    }
}
