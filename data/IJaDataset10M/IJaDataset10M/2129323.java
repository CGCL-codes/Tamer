package net.sf.bulimgr.business.service.exception;

/**
 * Exception when the user login fails.
 * 
 * @author Detlev Struebig
 *
 */
public class BulimgrLoginAlreadyExistsException extends Exception {

    /** serialVersionUID */
    private static final long serialVersionUID = 2219022697379631613L;

    /**
	 * 
	 */
    public BulimgrLoginAlreadyExistsException() {
    }

    /**
	 * 
	 * @param message
	 */
    public BulimgrLoginAlreadyExistsException(String message) {
        super(message);
    }

    /**
	 * 
	 * @param message
	 * @param cause
	 */
    public BulimgrLoginAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
