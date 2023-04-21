package com.excilys.sugadroid.util.exceptions;

public class DayNotLoadedException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public DayNotLoadedException() {
    }

    public DayNotLoadedException(String detailMessage) {
        super(detailMessage);
    }

    public DayNotLoadedException(Throwable throwable) {
        super(throwable);
    }

    public DayNotLoadedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
