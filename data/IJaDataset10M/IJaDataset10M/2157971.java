package org.apache.harmony.nio.tests.java.nio.channels.spi;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for tests.api.java.nio.channels.spi");
        suite.addTestSuite(AbstractInterruptibleChannelTest.class);
        suite.addTestSuite(AbstractSelectorTest.class);
        suite.addTestSuite(AbstractSelectableChannelTest.class);
        suite.addTestSuite(SelectorProviderTest.class);
        suite.addTestSuite(AbstractSelectionKeyTest.class);
        return suite;
    }
}
