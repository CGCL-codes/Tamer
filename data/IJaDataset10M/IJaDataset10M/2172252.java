package net.jadoth.lang.exceptions;

/**
 * @author Thomas M�nz
 *
 */
public class NumberRangeException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -352881442598223726L;

    /**
	 * 
	 */
    public NumberRangeException() {
        super();
    }

    /**
	 * @param message
	 */
    public NumberRangeException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public NumberRangeException(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public NumberRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
