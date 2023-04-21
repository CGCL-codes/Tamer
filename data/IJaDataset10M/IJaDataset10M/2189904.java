package jopt.js.test.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class UtilTestSuite extends TestCase {

    public UtilTestSuite(java.lang.String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(IDStoreTest.class);
        suite.addTestSuite(IntIntervalCollectionTest.class);
        suite.addTestSuite(TransitionTimeTableTest.class);
        return suite;
    }
}
