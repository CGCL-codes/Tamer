package org.encog.mathutil;

import org.encog.EncogError;

/**
 * Thrown when a math error happens.
 */
public class EncogMathError extends EncogError {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6219065927838486625L;

    /**
	 * Construct a message exception.
	 * 
	 * @param msg
	 *            The exception message.
	 */
    public EncogMathError(final String msg) {
        super(msg);
    }

    /**
	 * Construct an exception that holds another exception.
	 * 
	 * @param msg
	 *            A message.
	 * @param t
	 *            The other exception.
	 */
    public EncogMathError(final String msg, final Throwable t) {
        super(msg, t);
    }

    /**
	 * Construct an exception that holds another exception.
	 * 
	 * @param t
	 *            The other exception.
	 */
    public EncogMathError(final Throwable t) {
        super(t);
    }
}
