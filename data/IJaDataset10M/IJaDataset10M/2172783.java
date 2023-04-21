package gov.lanl.adore.demo;

/**
 * <code>TutorialException</code> signals that a processing exception of some
 * sort occurred.
 * 
 * @author Ryan Chute <rchute@lanl.gov>
 * 
 */
public class TutorialException extends Exception {

    /**
     * Constructs a <code>TutorialException</code> with the specified details
     * message
     * 
     * @param message
     *            the detail message
     * 
     */
    public TutorialException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with a message and cause.
     * 
     * @param message
     *            brief description of cause
     * @param cause
     *            the cause (which is saved for later retrieval by the
     *            {@link #getCause()} method.
     * 
     */
    public TutorialException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the cause.
     * 
     * @param cause
     *            the cause (which is saved for later retrieval by the
     *            {@link #getCause()} method.
     * 
     */
    public TutorialException(Throwable cause) {
        super("TutorialException", cause);
    }
}
