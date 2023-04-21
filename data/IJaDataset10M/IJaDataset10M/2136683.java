package com.volantis.testtools.mock;

/**
 * An interface that should be implemented by all objects whose state is
 * verifiable during the test.
 */
public interface Verifiable {

    /**
     * Verify that the object is in a valid state.
     */
    public void verify();
}
