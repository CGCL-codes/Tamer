package net.sf.jdpa;

/**
 * @author Andreas Nilsson
 */
public class PropertyAccessException extends RuntimeException {

    public PropertyAccessException() {
    }

    public PropertyAccessException(String message) {
        super(message);
    }

    public PropertyAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyAccessException(Throwable cause) {
        super(cause);
    }
}
