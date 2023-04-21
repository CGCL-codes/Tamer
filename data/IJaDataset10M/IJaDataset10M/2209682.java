package com.gnizr.core.exceptions;

public class InvalidTimeRangeValueException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3853351351266144464L;

    public InvalidTimeRangeValueException() {
        super();
    }

    public InvalidTimeRangeValueException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public InvalidTimeRangeValueException(String arg0) {
        super(arg0);
    }

    public InvalidTimeRangeValueException(Throwable arg0) {
        super(arg0);
    }
}
