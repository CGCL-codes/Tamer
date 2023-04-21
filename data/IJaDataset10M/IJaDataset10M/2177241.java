package jegg.impl;

/**
 * Exception response sent when an attempt is made to register
 * a port under a name that already exists in the registry.
 */
public class DuplicatePortException extends Exception {

    /**
     * Constructor.
     */
    public DuplicatePortException() {
        super();
    }

    /**
     * Constructor.
     * @param message an explanation of the error.
     */
    public DuplicatePortException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     * @param throwable a nested exception
     */
    public DuplicatePortException(final Throwable throwable) {
        super(throwable);
    }

    /**
     * Constructor.
     * @param message an explanation of the error.
     * @param throwable a nested exception
     */
    public DuplicatePortException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
