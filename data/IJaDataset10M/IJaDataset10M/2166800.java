package fll.util;

/**
 * Represents an internal error.
 */
public class FLLInternalException extends RuntimeException {

    /**
   * 
   */
    public FLLInternalException() {
        super();
    }

    /**
   * @param message
   */
    public FLLInternalException(final String message) {
        super(message);
    }

    /**
   * @param cause
   */
    public FLLInternalException(final Throwable cause) {
        super(cause);
    }

    /**
   * @param message
   * @param cause
   */
    public FLLInternalException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
