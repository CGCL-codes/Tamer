package javax.xml.parsers;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static final Test suite() {
        TestSuite suite = tests.TestSuiteFactory.createTestSuite();
        suite.addTestSuite(javax.xml.parsers.DocumentBuilderTest.class);
        return suite;
    }
}
