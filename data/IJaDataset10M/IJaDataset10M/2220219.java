package com.volantis.testtools.mock.method;

/**
 * Provides control over the maximum number of occurrences of an expectation.
 */
public interface MaxOcurrences {

    /**
     * Set the maximum number of expectations expected.
     *
     * @param maximum The maximum.
     */
    public void max(int maximum);

    /**
     * The number of occurrences is unlimited.
     *
     * <p>Currently equivalent to <code>max(Integer.MAX_INT)</code>
     */
    public void unbounded();
}
