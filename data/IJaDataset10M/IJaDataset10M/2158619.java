package oreactor.exceptions;

public class OpenReactorWindowClosedException extends OpenReactorException {

    /**
	 * A serial version UID.  
	 */
    private static final long serialVersionUID = -824188454741797337L;

    /**
	 * Constructs a new object of this exception class.
	 * @param msg A message string for this object.
	 */
    public OpenReactorWindowClosedException(String msg) {
        super(msg);
    }

    /**
	 * Constructs a new object of this exception class.
	 * @param msg A message string for this object.
	 * @param t A nested exception.
	 */
    public OpenReactorWindowClosedException(String msg, Throwable t) {
        super(msg, t);
    }
}
