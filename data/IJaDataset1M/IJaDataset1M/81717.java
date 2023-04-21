package edu.uga.galileo.voci.exception;

/**
 * An <code>Exception</code> used when no <code>DAO</code> can be found
 * based on the role's request.
 * 
 * @author <a href="mailto:cbking@uga.edu">Charles King</a>
 *
 */
public class NoSuchRoleException extends Exception {

    /**
	 * Default serial version UID.
	 * 
	 * TODO: generate a real one when we're done...?
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Create a new <code>NoSuchRoleException</code> object.
	 * 
	 * @param msg
	 *            The message to associate with this exception.
	 */
    public NoSuchRoleException(String msg) {
        super(msg);
    }
}
