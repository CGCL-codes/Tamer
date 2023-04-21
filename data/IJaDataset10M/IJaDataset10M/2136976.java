package net.sf.epfe.core.exceptions;

/**
 * @author Christoph
 *
 */
public class EPFEException extends Exception {

    /**
	 * 
	 */
    public EPFEException() {
    }

    /**
	 * @param aMessage
	 */
    public EPFEException(String aMessage) {
        super(aMessage);
    }

    /**
	 * @param aCause
	 */
    public EPFEException(Throwable aCause) {
        super(aCause);
    }

    /**
	 * @param aMessage
	 * @param aCause
	 */
    public EPFEException(String aMessage, Throwable aCause) {
        super(aMessage, aCause);
    }
}
