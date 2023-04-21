package org.test.slice;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.test.slice");
        suite.addTest(org.test.slice.annotation.AllTests.suite());
        suite.addTest(org.test.slice.conic.AllTests.suite());
        suite.addTest(org.test.slice.none.AllTests.suite());
        suite.addTest(org.test.slice.minimal.AllTests.suite());
        return suite;
    }
}
