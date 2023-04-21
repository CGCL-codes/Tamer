package org.apache.commons.httpclient;

/**
 * Signals that an error has occurred.
 * 
 * @author Ortwin Gl?ck
 * @version $Revision: 1.1.4.2 $ $Date: 2007/06/15 06:53:58 $
 * @since 3.0
 */
public class HttpClientError extends Error {

    /**
     * Creates a new HttpClientError with a <tt>null</tt> detail message.
     */
    public HttpClientError() {
        super();
    }

    /**
     * Creates a new HttpClientError with the specified detail message.
     * @param message The error message
     */
    public HttpClientError(String message) {
        super(message);
    }
}
