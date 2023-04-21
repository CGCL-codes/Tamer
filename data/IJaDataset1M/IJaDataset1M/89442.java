package iclab.exceptions;

/**
 * @author b0rxa
 *
 */
@SuppressWarnings("serial")
public class ICParserException extends ICException {

    /**
	 * 
	 */
    public ICParserException() {
    }

    /**
	 * @param message
	 */
    public ICParserException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public ICParserException(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public ICParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
