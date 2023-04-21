package at.fhjoanneum.aim.sdi.project.exceptions;

public class AddUserException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
	 * Creates a new exception, when a user- object cannot be created.
	 * @param msg, a String value that is the message body of the original exception.
	 */
    public AddUserException(String msg) {
        super(msg);
    }
}
