package org.gamegineer.client.core;

import junit.framework.Test;
import org.gamegineer.client.internal.core.Activator;
import org.gamegineer.test.core.BundleSuiteBuilder;

/**
 * Defines a test suite for running all tests in the fragment.
 */
public final class AllTests {

    /**
     * Initializes a new instance of the {@code AllTests} class.
     */
    public AllTests() {
        super();
    }

    /**
     * Creates a test suite consisting of all tests in the fragment.
     * 
     * @return A test suite consisting of all tests in the fragment.
     */
    public static Test suite() {
        return BundleSuiteBuilder.suite(Activator.getDefault().getBundleContext().getBundle(), "org.gamegineer.client.core.tests");
    }
}
