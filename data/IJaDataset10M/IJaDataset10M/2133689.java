package org.tigr.cloe;

/**
 * This class will be the base Exception class
 * for cloe objects.
 * @author dkatzel
 *
 *
 */
@SuppressWarnings("serial")
public class CloeException extends Exception {

    public CloeException() {
    }

    public CloeException(String message) {
        super(message);
    }

    public CloeException(Throwable cause) {
        super(cause);
    }

    public CloeException(String message, Throwable cause) {
        super(message, cause);
    }
}
