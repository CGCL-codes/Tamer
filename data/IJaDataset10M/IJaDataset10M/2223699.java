package javax.ejb;

/**
 * This exception indicates that a request carried a null transaction context,
 * but the target object requires an active transaction.
 */
public class EJBTransactionRequiredException extends EJBException {

    /**
	 * Constructs an EJBTransactionRequiredException with no detail message.
	 */
    public EJBTransactionRequiredException() {
    }

    /**
	 * Constructs an EJBTransactionRequiredException with the specified detailed
	 * message.
	 */
    public EJBTransactionRequiredException(String message) {
        super(message);
    }
}
