package net.jadoth.math;

/**
 * @author Thomas M�nz
 *
 */
public class MatrixException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3054324534946929181L;

    /**
	 * 
	 */
    public MatrixException() {
        super();
    }

    /**
	 * @param message
	 */
    public MatrixException(final String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public MatrixException(final Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public MatrixException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
