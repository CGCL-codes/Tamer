package ch.iserver.ace.util;

/**
 *
 */
public class InterruptedRuntimeException extends RuntimeException {

    /**
	 * 
	 */
    public InterruptedRuntimeException() {
        super();
    }

    /**
	 * @param message
	 */
    public InterruptedRuntimeException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public InterruptedRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public InterruptedRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
