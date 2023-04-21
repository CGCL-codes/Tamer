package org.apache.commons.math.linear;

import org.apache.commons.math.MathRuntimeException;

/**
 * Thrown when a visitor encounters an error while processing a matrix entry.
 * @version $Revision: 729673 $ $Date: 2008-12-27 21:55:12 +0100 (Sa, 27 Dez 2008) $
 */
public class MatrixVisitorException extends MathRuntimeException {

    /** Serializable version identifier */
    private static final long serialVersionUID = 3814333035048617048L;

    /**
     * Constructs a new instance with specified formatted detail message.
     * @param pattern format specifier
     * @param arguments format arguments
     */
    public MatrixVisitorException(final String pattern, final Object[] arguments) {
        super(pattern, arguments);
    }
}
