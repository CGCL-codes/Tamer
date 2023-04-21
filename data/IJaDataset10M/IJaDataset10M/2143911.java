package org.jsmg.exceptions;

/**
 * Variable id could not be found.
 */
public class VariableIdNotFoundException extends RuntimeException {

    /**
	 * Constructor with message.
	 * @param message message
	 */
    public VariableIdNotFoundException(String message) {
        super(message);
    }

    /** Serial. */
    private static final long serialVersionUID = -7027389182388894854L;
}
