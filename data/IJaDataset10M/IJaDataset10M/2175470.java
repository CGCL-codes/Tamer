package com.maowu.pooling.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for com.maowu.pooling.test");
        suite.addTestSuite(SimplePoolTestEx.class);
        suite.addTestSuite(SimplePoolTest.class);
        return suite;
    }
}
