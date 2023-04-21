package moio.util;

/**
 * Applications should not try to catch errors since they indicate abnormal
 * conditions. An abnormal condition is something which should not occur, or
 * which should not be recovered from. This latter category includes
 * <code>ThreadDeath</code> and <code>AssertionError</code>.
 * 
 * <p>
 * A method is not required to declare any subclass of <code>Error</code> in
 * its <code>throws</code> clause which might be thrown but not caught while
 * executing the method.
 * 
 * @author Brian Jones
 * @author Tom Tromey (tromey@cygnus.com)
 * @author Eric Blake (ebb9@email.byu.edu)
 * @since 1.0
 * @status updated to 1.4
 */
public class Error extends RuntimeException {

    /**
	 * Compatible with JDK 1.0+.
	 */
    private static final long serialVersionUID = 4980196508277280342L;

    /**
	 * Create an error without a message. The cause remains uninitialized.
	 * 
	 * @see #initCause(Throwable)
	 */
    public Error() {
    }

    /**
	 * Create an error with a message. The cause remains uninitialized.
	 * 
	 * @param s
	 *            the message string
	 * @see #initCause(Throwable)
	 */
    public Error(String s) {
        super(s);
    }

    /**
	 * Create an error with a message and a cause.
	 * 
	 * @param s
	 *            the message string
	 * @param cause
	 *            the cause of this error
	 * @since 1.4
	 */
    public Error(String s, Throwable cause) {
        super(s + cause);
    }

    /**
	 * Create an error with a given cause, and a message of
	 * <code>cause == null ? null : cause.toString()</code>.
	 * 
	 * @param cause
	 *            the cause of this error
	 * @since 1.4
	 */
    public Error(Throwable cause) {
        super("" + cause);
    }
}
