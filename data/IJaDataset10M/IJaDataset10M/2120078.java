package edu.uga.galileo.idt.exceptions;

/**
 * An <code>Exception</code> used when no <code>DAO</code> can be found
 * based on the user's request.
 * 
 * @author <a href="mailto:mdurant@uga.edu">Mark Durant</a>
 * @version 1.0
 */
public final class NoAvailableDAOException extends Exception {

    /**
	 * Default serial version UID.
	 * 
	 * TODO: generate a real one when we're done...?
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Create a new <code>NoAvailablePoolException</code> object.
	 * 
	 * @param msg The message to associate with this exception.
	 */
    public NoAvailableDAOException(String msg) {
        super(msg);
    }
}
