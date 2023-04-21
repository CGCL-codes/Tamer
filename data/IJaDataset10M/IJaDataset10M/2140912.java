package net.sourceforge.jeval.function;

/**
 * This exception is thrown when an error occurs while processing a function.
 */
public class FunctionException extends Exception {

    private static final long serialVersionUID = 4767250768467137620L;

    /**
	 * This constructor takes a custom message as input.
	 * 
	 * @param message
	 *            A custom message for the exception to display.
	 */
    public FunctionException(String message) {
        super(message);
    }

    /**
	 * This constructor takes an exception as input.
	 * 
	 * @param exception
	 *            An exception.
	 */
    public FunctionException(Exception exception) {
        super(exception);
    }

    /**
	 * This constructor takes an exception as input.
	 * 
	 * @param message
	 *            A custom message for the exception to display.
	 * @param exception
	 *            An exception.
	 */
    public FunctionException(String message, Exception exception) {
        super(message, exception);
    }
}
