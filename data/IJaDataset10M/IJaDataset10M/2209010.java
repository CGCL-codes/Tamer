package javax.ejb;

/**
 * A ConcurrentAccessException indicates that the client has attempted an
 * invocation on a stateful session bean while another invocation is in
 * progress.
 */
public class ConcurrentAccessException extends EJBException {

    /**
	 * Constructs an ConcurrentAccessException with no detail message.
	 */
    public ConcurrentAccessException() {
    }

    /**
	 * Constructs an ConcurrentAccessException with the specified detailed
	 * message.
	 */
    public ConcurrentAccessException(String message) {
        super(message);
    }

    /**
	 * Constructs an ConcurrentAccessException with the specified detail message
	 * and a nested exception.
	 */
    public ConcurrentAccessException(String message, Exception ex) {
        super(message, ex);
    }
}
