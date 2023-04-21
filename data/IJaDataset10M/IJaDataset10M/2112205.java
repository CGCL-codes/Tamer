package org.sepp.exceptions;

public class WrongTypeException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1475364851995530324L;

    /**
	 * 
	 */
    public WrongTypeException(String arg0) {
        super(arg0);
    }

    /**
	 * 
	 */
    public WrongTypeException(Throwable arg0) {
        super(arg0);
    }

    /**
	 * 
	 */
    public WrongTypeException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
