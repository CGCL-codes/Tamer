package de.tuberlin.cs.cis.ocl.parser.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Performs all test included in the package 
 * de.tuberlin.cs.cis.ocl.parser.test. 
 *
 * @author fchabar
 */
public class AllTests {

    /**
	 * Starts the test.
	 * @param args no arguments required.
	 */
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(AllTests.class);
    }

    /**
	 * Returns the test suite for this package.
	 * @return the test suite for this package.
	 */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for de.tuberlin.cs.cis.eve.ocl.parser.test");
        suite.addTest(new TestSuite(TestOclParser.class));
        return suite;
    }
}
