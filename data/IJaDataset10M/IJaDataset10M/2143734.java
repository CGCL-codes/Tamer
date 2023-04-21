package sis;

import junit.framework.TestSuite;

public class AllTests extends TestSuite {

    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(sis.report.AllTests.suite());
        suite.addTest(sis.student.AllTests.suite());
        return suite;
    }
}
