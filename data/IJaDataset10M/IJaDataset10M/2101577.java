package org.explodey.com.timer;

/**
 * General App exception.
 *
 * @version  $Revision: 1.25 $
 */
public class AppException extends Exception {

    /**
     * for serialization versioning.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new AppException object.
     *
     * @param  message  message
     * @param  cause    cause
     */
    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new AppException object.
     *
     * @param  message  message
     */
    public AppException(String message) {
        super(message);
    }
}
