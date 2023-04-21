package org.jazzteam.bpe.parser.exceptions;

/**
 * Exception for errors in process template parsing.
 * 
 * @author skars
 * @version $Rev: $
 */
public class ParserException extends Exception {

    /** Identifier of instance to serialization. */
    private static final long serialVersionUID = -2579062762456938565L;

    /**
	 * Constructs exception.
	 */
    public ParserException() {
    }

    /**
	 * Constructs exception.
	 * 
	 * @param message
	 *            Error message.
	 */
    public ParserException(String message) {
        super(message);
    }

    /**
	 * Constructs exception.
	 * 
	 * @param cause
	 *            Cause of exception.
	 */
    public ParserException(Throwable cause) {
        super(cause);
    }

    /**
	 * Constructs exception.
	 * 
	 * @param message
	 *            Error message.
	 * @param cause
	 *            Cause of exception.
	 */
    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
