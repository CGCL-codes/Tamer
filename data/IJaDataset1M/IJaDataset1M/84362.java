package javax.media.ding3d;

/**
 * Indicates an assertion failure.
 */
class AssertionFailureException extends RuntimeException {

    /**
     * Create the exception object with default values.
     */
    AssertionFailureException() {
    }

    /**
     * Create the exception object that outputs message.
     * @param str the message string to be output.
     */
    AssertionFailureException(String str) {
        super(str);
    }
}
