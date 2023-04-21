package org.hip.kernel.persistency.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.hip.kernel.persistency.test");
        suite.addTestSuite(ConnectionSettingImplTest.class);
        suite.addTestSuite(PersistencyManagerImplTest.class);
        return suite;
    }
}
