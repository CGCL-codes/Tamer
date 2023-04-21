package de.nava.informa.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends TestCase {

    public AllTests(String testname) {
        super(testname);
    }

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite();
        suite.addTest(de.nava.informa.exporters.AllTests.suite());
        suite.addTest(de.nava.informa.impl.basic.AllTests.suite());
        suite.addTest(de.nava.informa.impl.hibernate.AllTests.suite());
        suite.addTest(de.nava.informa.parsers.AllTests.suite());
        suite.addTest(de.nava.informa.search.AllTests.suite());
        suite.addTest(de.nava.informa.utils.AllTests.suite());
        return suite;
    }
}
