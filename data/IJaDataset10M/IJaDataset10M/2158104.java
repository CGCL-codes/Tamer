package com.webcodefocus.blogcaster.common;

/**
 * ExceptionWrapper a common exception class to promote unified handling
 * of errors and exceptions.
 * @author Kevin McAllister
 */
public class ExceptionWrapper extends Exception {

    /**
   * 
   */
    private static final long serialVersionUID = 1L;

    public ExceptionWrapper() {
        super();
    }

    /**
   * Takes an error message.
   * @param arg0
   */
    public ExceptionWrapper(String arg0) {
        super(arg0);
    }

    /**
   * Takes an error message ana Throwable.
   * @param arg0
   * @param arg1
   */
    public ExceptionWrapper(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
   * Just encapsulates a Throwable.
   * @param arg0
   */
    public ExceptionWrapper(Throwable arg0) {
        super(arg0);
    }
}
