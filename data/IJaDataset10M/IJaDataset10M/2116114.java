package net.sourceforge.binml.parse;

/**
 * This exception is thrown when something went wrong during the parsing of the
 * Format Description file
 * 
 * @author Emil Erlandsson
 * 
 */
public class FormatParseException extends Exception {

    /**
	 * Unique id.
	 */
    private static final long serialVersionUID = 0xC0FFEE;

    public FormatParseException(String message) {
        super(message);
    }

    public FormatParseException(String message, Throwable cause) {
        super(message);
        super.initCause(cause);
    }
}
