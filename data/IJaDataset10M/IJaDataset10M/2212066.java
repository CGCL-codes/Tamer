package hu.akarnokd.reactive4java;

/**
 * Exception for cases when too many elements are contained
 * within a collection or observable. Its the dual
 * of NoSuchElementException.
 * @author akarnokd, 2011.01.30.
 */
public class TooManyElementsException extends RuntimeException {

    /** */
    private static final long serialVersionUID = 3390531861721818769L;

    /**
	 * A message and cause-less constructo.
	 */
    public TooManyElementsException() {
        super();
    }

    /**
	 * Construct the exception by using a message and a cause.
	 * @param message the message
	 * @param cause the cause
	 */
    public TooManyElementsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * Construct the exception by using a message only.
	 * @param message the message
	 */
    public TooManyElementsException(String message) {
        super(message);
    }

    /**
	 * Construct the exception by using a cause only.
	 * @param cause the cause
	 */
    public TooManyElementsException(Throwable cause) {
        super(cause);
    }
}
