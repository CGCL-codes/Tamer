package net.sf.traser.interpreter;

/**
 * Unchecked exception to report XQuery interpretation errors.
 * @author karnokd, 2007.12.17.
 * @version $Revision 1.0$
 */
public class InterpreterFault extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4154664368126762825L;

    /**
	 * Constructor.
	 */
    public InterpreterFault() {
        super("Unknown");
    }

    /**
	 * Constructor.
	 * @param message the message
	 */
    public InterpreterFault(String message) {
        super(message);
    }

    /**
	 * Constructor.
	 * @param cause the cause
	 */
    public InterpreterFault(Throwable cause) {
        super(cause);
    }

    /**
	 * Constructor.
	 * @param message the message
	 * @param cause the cause
	 */
    public InterpreterFault(String message, Throwable cause) {
        super(message, cause);
    }
}
