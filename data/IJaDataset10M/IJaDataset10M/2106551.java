package javax.media.ding3d.vecmath;

/**
 * Indicates that an operation cannot be completed properly because
 * of a mismatch in the sizes of object attributes.
 */
public class MismatchedSizeException extends RuntimeException {

    /**
 * Create the exception object with default values.
 */
    public MismatchedSizeException() {
    }

    /**
 * Create the exception object that outputs a message.
 * @param str the message string to be output.
 */
    public MismatchedSizeException(String str) {
        super(str);
    }
}
