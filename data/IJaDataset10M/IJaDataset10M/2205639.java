package org.encog.ml.data;

/**
 * Used by the machine learning methods classes to indicate a data error.
 */
public class MLlDataError extends RuntimeException {

    /**
	 * Serial id for this class.
	 */
    private static final long serialVersionUID = 7167228729133120101L;

    /**
	 * Construct a message exception.
	 * 
	 * @param msg
	 *            The exception message.
	 */
    public MLlDataError(final String msg) {
        super(msg);
    }

    /**
	 * Construct an exception that holds another exception.
	 * 
	 * @param t
	 *            The other exception.
	 */
    public MLlDataError(final Throwable t) {
        super(t);
    }
}
