package edu.uga.galileo.voci.exception;

public class ImportException extends Exception {

    /**
	 * Default serial version UID.
	 * 
	 * TODO: generate a real one when we're done...?
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Create a new <code>NoSuchCommunityException</code> object.
	 * 
	 * @param msg
	 *            The message to associate with this exception.
	 */
    public ImportException(String msg) {
        super(msg);
    }
}
