package de.jcommandlineparser;

public class OptionNotSetException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7290494216583272459L;

    public OptionNotSetException() {
    }

    public OptionNotSetException(String message) {
        super(message);
    }

    public OptionNotSetException(Throwable cause) {
        super(cause);
    }

    public OptionNotSetException(String message, Throwable cause) {
        super(message, cause);
    }
}
