package adc.app.spec;

/**
 * This exception is thrown when a data access error occurs.
 * 
 * @author Alex
 * 
 */
public class DaoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
	 * Create an instance of this class by providing the error message and the
	 * cause of the error.
	 * 
	 * @param message
	 *            the error message
	 * @param cause
	 *            the cause of the error
	 */
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * Create an instance of this class by providing the error message.
	 * 
	 * @param message
	 *            the error message
	 */
    public DaoException(String message) {
        super(message);
    }

    /**
	 * Create an instance of this class by providing the cause of the error.
	 * 
	 * @param cause
	 *            the cause of the error
	 */
    public DaoException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
