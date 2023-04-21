package org.apache.shiro.authc;

/**
 * Thrown when attempting to authenticate with a principal that doesn't exist in the system (e.g.
 * by specifying a username that doesn't relate to a user account).
 *
 * <p>Whether or not an application wishes to alert a user logging in to the system of this fact is
 * at the discretion of those responsible for designing the view and what happens when this
 * exception occurs.
 *
 * @since 0.1
 */
public class UnknownAccountException extends AccountException {

    /**
     * Creates a new UnknownAccountException.
     */
    public UnknownAccountException() {
        super();
    }

    /**
     * Constructs a new UnknownAccountException.
     *
     * @param message the reason for the exception
     */
    public UnknownAccountException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnknownAccountException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public UnknownAccountException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new UnknownAccountException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public UnknownAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
