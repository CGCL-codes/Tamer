package com.agilejava.blammo;

/**
 * A general purpose but Blammo specific RuntimeException based Exception class.
 * May be specialized in the future.
 * 
 * @author Wilfred Springer
 */
public class BlammoException extends RuntimeException {

    /**
	 * The serialVersionUID required by the serialization specification.
	 */
    private static final long serialVersionUID = -8652799936435441312L;

    /**
	 * Constructs a new instance wrapping a {@link ClassNotFoundException}.
	 * 
	 * @param cnfe
	 *            The <code>ClassNotFoundException</code> to be wrapped by a
	 *            new instance.
	 */
    public BlammoException(ClassNotFoundException cnfe) {
        super(cnfe);
    }

    /**
	 * Constructs a new instance wrapping a {@link InstantiationException}.
	 * 
	 * @param ie
	 *            The <code>InstantiationException</code> to be wrapped by a
	 *            new instance.
	 */
    public BlammoException(InstantiationException ie) {
        super(ie);
    }

    /**
	 * Constructs a new instance wrapping a {@link IllegalAccessException}.
	 * 
	 * 
	 * @param iae
	 *            The <code>IllegalAccessException</code> to be wrapped by a
	 *            new instance of this class.
	 */
    public BlammoException(IllegalAccessException iae) {
        super(iae);
    }
}
