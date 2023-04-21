package com.advancedpwr.record.mock;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.advancedpwr.record.AbstractRecorderTest;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for com.advancedpwr.record.mock");
        suite.addTestSuite(ListInstanceTest.class);
        suite.addTestSuite(ClassProxyFactoryTest.class);
        suite.addTestSuite(MockFactoryTest.class);
        suite.addTestSuite(ArrayTest.class);
        suite.addTestSuite(MapTest.class);
        suite.addTestSuite(MockBehaviorRecorderTest.class);
        suite.addTestSuite(URLTest.class);
        return suite;
    }
}
