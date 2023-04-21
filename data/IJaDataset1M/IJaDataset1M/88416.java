package org.jsecurity.authz;

/**
 * Exception thrown when attempting to execute an authorization action when a successful
 * authentication hasn't yet occurred.
 *
 * <p>Authorizations can only be performed after a successful
 * authentication because authorization data (roles, permissions, etc) must always be associated
 * with a known identity.  Such a known identity can only be obtained upon a successful log-in.
 *
 * @author Les Hazlewood
 * @since 0.1
 */
public class UnauthenticatedException extends AuthorizationException {

    /**
     * Creates a new UnauthenticatedException.
     */
    public UnauthenticatedException() {
        super();
    }

    /**
     * Constructs a new UnauthenticatedException.
     *
     * @param message the reason for the exception
     */
    public UnauthenticatedException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnauthenticatedException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public UnauthenticatedException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new UnauthenticatedException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public UnauthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
