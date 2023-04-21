package net.sf.jdpa;

/**
 * @author Andreas Nilsson
 */
public class ClassNotAvailableException extends RuntimeException {

    public ClassNotAvailableException() {
    }

    public ClassNotAvailableException(String message) {
        super(message);
    }

    public ClassNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassNotAvailableException(Throwable cause) {
        super(cause);
    }
}
