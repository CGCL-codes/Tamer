package jimo.osgi.tests.other;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for jimo.osgi.tests.other");
        suite.addTestSuite(EventsTest.class);
        return suite;
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }
}
