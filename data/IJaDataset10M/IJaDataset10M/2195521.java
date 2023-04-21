package exceptions;

/**
 * Should be thrown when an operation inlcuding a transition is invocated, where
 * the transition does not belong to the turingmachine.
 * 
 * @author Sven Schneider
 * @since 0.1.7
 */
public class UnknownTransitionException extends Exception {

    /** A generated serialVersionUID. */
    private static final long serialVersionUID = -3635843223439287268L;

    /**
	 * Passes the message to the superconstructor.
	 * 
	 * @param msg
	 *            the message.
	 */
    public UnknownTransitionException(String msg) {
        super(msg);
    }
}
