package flames2d.core.error;

import flames2d.math.exception.MathException;

/**
 * An exception thrown if something inside the math process fails
 * @author Ansgar Schneider
 */
public class MathProcessError extends ProcessingError {

    /** Serial Version Number */
    static final long serialVersionUID = 002L;

    /**
	 * Constructor for a math process error
	 * @param msg the message of the exception
	 */
    public MathProcessError(String msg) {
        super(msg);
    }

    /**
	 * Constructor for a math process error
	 * @param e a math exception that was caught
	 */
    public MathProcessError(MathException e) {
        super(e);
    }
}
