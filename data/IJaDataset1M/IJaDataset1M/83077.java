package net.sf.JUnit_Penetration.exception;

/**
 * Project	JUnit-Penetration
 * Package	net.sf.JUnit_Penetration.api
 * File		PenetrationException.java
 * History	Mar 29, 2009 - initial version
 * 
 * @author	bb
 * @date	Mar 29, 2009
 *
 * Exception class for the JUnit-Penetration project.
 */
public class PenetrationException extends Exception {

    /**
	 * attributes
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Constructor
	 */
    public PenetrationException() {
    }

    /**
	 * Constructor
	 * 
	 * @param str
	 */
    public PenetrationException(String str) {
        super(str);
    }

    /**
	 * Constructor
	 * 
	 * @param e
	 */
    public PenetrationException(Throwable e) {
        super(e);
    }

    /**
	 * Constructor 
	 * 
	 * @param str
	 * @param e
	 */
    public PenetrationException(String str, Throwable e) {
        super(str, e);
    }
}
