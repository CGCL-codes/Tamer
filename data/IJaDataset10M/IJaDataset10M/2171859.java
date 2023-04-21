package org.apache.http.cookie;

import net.jcip.annotations.Immutable;
import org.apache.http.ProtocolException;

/**
 * Signals that a cookie is in some way invalid or illegal in a given
 * context
 *
 * 
 * @since 4.0
 */
@Immutable
public class MalformedCookieException extends ProtocolException {

    private static final long serialVersionUID = -6695462944287282185L;

    /**
     * Creates a new MalformedCookieException with a <tt>null</tt> detail message.
     */
    public MalformedCookieException() {
        super();
    }

    /** 
     * Creates a new MalformedCookieException with a specified message string.
     * 
     * @param message The exception detail message
     */
    public MalformedCookieException(String message) {
        super(message);
    }

    /**
     * Creates a new MalformedCookieException with the specified detail message and cause.
     * 
     * @param message the exception detail message
     * @param cause the <tt>Throwable</tt> that caused this exception, or <tt>null</tt>
     * if the cause is unavailable, unknown, or not a <tt>Throwable</tt>
     */
    public MalformedCookieException(String message, Throwable cause) {
        super(message, cause);
    }
}
