package com.volantis.testtools.mock.method;

/**
 * An {@link ExpectedCall} that returns a {@link String}.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface CallUpdaterReturnsString extends CallUpdater {

    /**
     * Sets the return value when the expected method call represented by this
     * object is received.
     *
     * <p>This is syntactic sugar to make it easier to use Object values.</p>
     *
     * @param returnValue The return value.
     *
     * @return An object to set the number of occurrences of this call.
     */
    public Occurrences returns(String returnValue);

    /**
     * Specify a description for this expectation.
     *
     * @param description The description of this expectation.
     *
     * @return This object to allow initialisation to be chained.
     */
    public CallUpdaterReturnsString description(String description);
}
