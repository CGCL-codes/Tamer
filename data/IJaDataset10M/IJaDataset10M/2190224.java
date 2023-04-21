package de.jcmdlineparser.options;

import de.jcmdlineparser.ParserException;

/**
 * <p>
 * A <code> NoSuchOptionException </code> is thrown, if there has been provided
 * an unknown option identifier on the command line.
 * </p>
 * 
 * @author Alexander Kerner
 * @see AbstractOption
 * 
 */
public class NoSuchOptionException extends ParserException {

    private static final long serialVersionUID = -2303903908569697593L;

    public NoSuchOptionException() {
    }

    public NoSuchOptionException(String message) {
        super(message);
    }

    public NoSuchOptionException(Throwable cause) {
        super(cause);
    }

    public NoSuchOptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
