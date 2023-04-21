package org.gamegineer.common.core;

import junit.framework.Test;
import org.gamegineer.common.internal.core.Activator;
import org.gamegineer.common.internal.core.TestsFragmentConstants;
import org.gamegineer.test.core.BundleSuiteBuilder;

/**
 * Defines a test suite for running all tests in the fragment.
 */
public final class AllTests {

    /**
     * Initializes a new instance of the {@code AllTests} class.
     */
    public AllTests() {
    }

    public static Test suite() {
        return BundleSuiteBuilder.suite(Activator.getDefault().getBundleContext().getBundle(), TestsFragmentConstants.SYMBOLIC_NAME);
    }
}
