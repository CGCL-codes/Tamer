package com.skype;

/**
 * Exception to throw when a timeout occurs.
 */
public class TimeOutException extends SkypeException {

    /**
	 * serialVersionUID needed for serialisation.
	 */
    private static final long serialVersionUID = -5760422025501667771L;

    /**
	 * Constructor.
	 * @param message exception message.
	 */
    TimeOutException(String message) {
        super(message);
    }
}
