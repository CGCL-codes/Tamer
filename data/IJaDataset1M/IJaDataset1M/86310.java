package khl.ooo.domain;

import junit.framework.Test;
import junit.framework.TestSuite;

public class DomainTestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for khl.ooo.domain");
        suite.addTestSuite(CursusTest.class);
        suite.addTestSuite(OpleidingTest.class);
        suite.addTestSuite(VakTest.class);
        return suite;
    }
}
