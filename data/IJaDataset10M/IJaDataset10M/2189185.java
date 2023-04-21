package org.jenetics.util;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: Validator.java,v 1.1 2008-08-25 19:36:06 fwilhelm Exp $
 */
public final class Validator {

    private Validator() {
        super();
    }

    public static void notNull(final Object obj, final String message) {
        if (obj == null) {
            throw new NullPointerException(message + " must not be null. ");
        }
    }

    public static void checkChromosomeLength(final int length) {
        if (length < 0) {
            throw new NegativeArraySizeException("Length must be greater than zero, but was " + length + ". ");
        }
    }
}
