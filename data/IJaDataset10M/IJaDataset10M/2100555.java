package org.openrdf.model.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestAll {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.openrdf.model.util");
        suite.addTestSuite(ModelEqualityTest.class);
        return suite;
    }
}
