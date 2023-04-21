package pcgen.base.lang;

/**
 * @author Thomas Parker (thpr@users.sourceforge.net)
 * 
 * An UnreachableError is an error caused by code that the programmer thought
 * was Unreachable. This is preferred to an InternalError, as an InternalError
 * should be reserved for VirtualMachine errors. This error, on the other hand,
 * indicates that the original developer did not consider certain situations (or
 * additional features were added to an object or the language that the
 * developer did not have available).
 * 
 * The use of an Error rather than an Exception is preferred, due to the (bad)
 * habit of certain code catching Exception or RuntimeException. The use of an
 * error prevents this from being caught in those situations, ensuring that the
 * problem will be exposed.
 */
public class UnreachableError extends Error {

    /**
	 * For object serialization
	 */
    private static final long serialVersionUID = -5431319083877458886L;

    /**
	 * Create a new UnreachableError with no message and no cause.
	 */
    public UnreachableError() {
        super();
    }

    /**
	 * Create a new UnreachableError with the given message
	 * 
	 * @param arg0
	 *            The message indicating the cause of UnreachableError
	 */
    public UnreachableError(String arg0) {
        super(arg0);
    }

    /**
	 * Create a new UnreachableError with the given cause
	 * 
	 * @param arg0
	 *            The cause of the UnreachableError
	 */
    public UnreachableError(Throwable arg0) {
        super(arg0);
    }

    /**
	 * Create a new UnreachableError with the given message and cause
	 * 
	 * @param arg0
	 *            The message indicating the cause of UnreachableError
	 * @param arg1
	 *            The cause of the UnreachableError
	 */
    public UnreachableError(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
