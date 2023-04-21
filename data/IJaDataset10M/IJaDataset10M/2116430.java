package org.apache.http.client;

import net.jcip.annotations.Immutable;
import org.apache.http.ProtocolException;

/**
 * Signals violation of HTTP specification caused by an invalid redirect
 * 
 * 
 * @since 4.0
 */
@Immutable
public class RedirectException extends ProtocolException {

    private static final long serialVersionUID = 4418824536372559326L;

    /**
     * Creates a new RedirectException with a <tt>null</tt> detail message. 
     */
    public RedirectException() {
        super();
    }

    /**
     * Creates a new RedirectException with the specified detail message.
     * 
     * @param message The exception detail message
     */
    public RedirectException(String message) {
        super(message);
    }

    /**
     * Creates a new RedirectException with the specified detail message and cause.
     * 
     * @param message the exception detail message
     * @param cause the <tt>Throwable</tt> that caused this exception, or <tt>null</tt>
     * if the cause is unavailable, unknown, or not a <tt>Throwable</tt>
     */
    public RedirectException(String message, Throwable cause) {
        super(message, cause);
    }
}
