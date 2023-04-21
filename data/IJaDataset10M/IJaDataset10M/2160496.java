package potato.primitives;

/**
 * An exception class to signal unexpected (i.e., actually erroneous) primitive
 * failure. This exception should be used when there is no expected fallback
 * solution in the image for handling this failure.
 * 
 * @author Michael Haupt
 */
public class UnexpectedPrimitiveFailedException extends PrimitiveFailedException {

    UnexpectedPrimitiveFailedException() {
    }

    UnexpectedPrimitiveFailedException(Exception cause) {
        super(cause);
    }

    UnexpectedPrimitiveFailedException(String cause) {
        super(cause);
    }
}
