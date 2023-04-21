package com.kenai.jbosh;

/**
 * Abstract base class for attribute implementations based on {@code Integer}
 * types.  Additional support for parsing of integer values from their
 * {@code String} representations as well as callback handling of value
 * validity checks are also provided.
 */
abstract class AbstractIntegerAttr extends AbstractAttr<Integer> {

    /**
     * Creates a new attribute object.
     * 
     * @param val attribute value
     * @throws BOSHException on parse or validation failure
     */
    protected AbstractIntegerAttr(final int val) throws BOSHException {
        super(Integer.valueOf(val));
    }

    /**
     * Creates a new attribute object.
     *
     * @param val attribute value in string form
     * @throws BOSHException on parse or validation failure
     */
    protected AbstractIntegerAttr(final String val) throws BOSHException {
        super(parseInt(val));
    }

    /**
     * Utility method intended to be called by concrete implementation
     * classes from within the {@code check()} method when the concrete
     * class needs to ensure that the integer value does not drop below
     * the specified minimum value.
     *
     * @param minVal minimum value to allow
     * @throws BOSHException if the integer value is below the specific
     *  minimum
     */
    protected final void checkMinValue(int minVal) throws BOSHException {
        int intVal = getValue();
        if (intVal < minVal) {
            throw (new BOSHException("Illegal attribute value '" + intVal + "' provided.  " + "Must be >= " + minVal));
        }
    }

    /**
     * Utility method to parse a {@code String} into an {@code Integer},
     * converting any possible {@code NumberFormatException} thrown into
     * a {@code BOSHException}.
     *
     * @param str string to parse
     * @return integer value
     * @throws BOSHException on {@code NumberFormatException}
     */
    private static int parseInt(final String str) throws BOSHException {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfx) {
            throw (new BOSHException("Could not parse an integer from the value provided: " + str, nfx));
        }
    }

    /**
     * Returns the native {@code int} value of the underlying {@code Integer}.
     * Will throw {@code NullPointerException} if the underlying
     * integer was {@code null}.
     *
     * @return native {@code int} value
     */
    public int intValue() {
        return getValue().intValue();
    }
}
