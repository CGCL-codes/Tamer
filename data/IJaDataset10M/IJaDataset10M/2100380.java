package com.fddtool.pd.property;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for com.fddtool.pd.property");
        suite.addTest(TestProperty.suite());
        return suite;
    }
}
