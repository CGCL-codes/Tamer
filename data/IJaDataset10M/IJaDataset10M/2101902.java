package org.oclc.da.ndiipp.helper.textformat.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *  Base class for all test cases in this package.
 * @author stanesca
 * Created on Feb 8, 2005
 */
public class AllTests {

    /**
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    /**
	 * TEST
	 * @return Test
	 */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.oclc.da.ndiipp.helper.textformat.test");
        suite.addTestSuite(SubjectHeadingPathFormatterTest.class);
        return suite;
    }
}
