package sexpression.stream;

/**
 * This exception is thrown by S-Expression parse methods if the verbatim string
 * which is offered for parsing is an invalid format.
 * 
 * @author Kyle
 * 
 */
public class InvalidVerbatimStreamException extends Exception {

    /**
	 * Default Serial Version UID
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * This is the constructor for InvalidVerbatimStringExpression. It simply
	 * invokes super.
	 * 
	 * @param message
	 */
    public InvalidVerbatimStreamException(String message) {
        super(message);
    }
}
