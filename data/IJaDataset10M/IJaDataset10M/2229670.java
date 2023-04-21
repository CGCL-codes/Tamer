package com.jdkcn.exception;

/**
 * @author <a href="mailto:rory.cn@gmail.com">somebody</a>
 * @since 2006-9-22
 * @version $Id UsernameAlreadyExistException.java$
 */
public class UsernameAlreadyExistException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4577398403173447317L;

    /**
	 * 
	 */
    public UsernameAlreadyExistException() {
        super();
    }

    /**
	 * @param message
	 * @param cause
	 */
    public UsernameAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * @param message
	 */
    public UsernameAlreadyExistException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public UsernameAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
