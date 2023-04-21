package org.apache.commons.httpclient.auth;

/**
 * Authentication credentials required to respond to a authentication 
 * challenge are invalid
 *
 * @author <a href="mailto:oleg@ural.ru">Oleg Kalnichevski</a>
 * 
 * @since 3.0
 */
public class InvalidCredentialsException extends AuthenticationException {

    /**
     * Creates a new InvalidCredentialsException with a <tt>null</tt> detail message. 
     */
    public InvalidCredentialsException() {
        super();
    }

    /**
     * Creates a new InvalidCredentialsException with the specified message.
     * 
     * @param message the exception detail message
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }

    /**
     * Creates a new InvalidCredentialsException with the specified detail message and cause.
     * 
     * @param message the exception detail message
     * @param cause the <tt>Throwable</tt> that caused this exception, or <tt>null</tt>
     * if the cause is unavailable, unknown, or not a <tt>Throwable</tt>
     */
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
