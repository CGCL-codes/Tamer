package org.smartcti.freeswitch.inbound;

/**
 * An AuthenticationFailedException is thrown when a login fails due to an incorrect username and/or
 * password.
 * 
 * @author srt
 * @version $Id: AuthenticationFailedException.java,v 1.2 2008/11/19 12:45:36 erik.han Exp $
 */
public class AuthenticationFailedException extends Exception {

    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 7674248607624140309L;

    /**
     * Creates a new AuthenticationFailedException with the given message.
     * 
     * @param message message describing the authentication failure
     */
    public AuthenticationFailedException(final String message) {
        super(message);
    }

    /**
     * Creates a new AuthenticationFailedException with the given message and cause.
     * 
     * @param message message describing the authentication failure
     * @param cause exception that caused the authentication failure
     */
    public AuthenticationFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
