package net.sf.oval.exception;

/**
 * @author Sebastian Thomschke
 */
public class ReflectionException extends OValException {

    private static final long serialVersionUID = 1L;

    public ReflectionException(final String message) {
        super(message);
    }

    public ReflectionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ReflectionException(final Throwable cause) {
        super(cause);
    }
}
