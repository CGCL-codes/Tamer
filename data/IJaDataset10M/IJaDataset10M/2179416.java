package fr.yahoo.smanciot.controller;

/**
 * 
 * @author St�phane Manciot - smanciot@yahoo.fr
 * @version 1.0
 */
public class ControllerException extends Exception {

    /**
	 * Creates new <code>D2TControllerException</code> without detail message.
	 */
    public ControllerException() {
        super();
    }

    /**
	 * Constructs an <code>D2TControllerException</code> with the specified
	 * detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
    public ControllerException(String msg) {
        super(msg);
    }

    /**
	 * Constructs an <code>D2TControllerException</code> with the specified
	 * exception.
	 * 
	 * @param ex
	 *            the exception.
	 */
    public ControllerException(Exception ex) {
        super(ex.getClass().getName() + ": " + ex.getMessage());
    }

    /**
	 * Constructs an <code>D2TControllerException</code> with the specified
	 * exception and message.
	 * 
	 * @param msg
	 *            the detail message.
	 * @param ex
	 *            the exception.
	 */
    public ControllerException(String msg, Exception ex) {
        super(msg + ": " + ex.getClass().getName() + ": " + ex.getMessage());
    }
}
