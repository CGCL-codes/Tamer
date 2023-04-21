package de.tuberlin.cs.cis.ocl.model.uml.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for de.tuberlin.cs.cis.ocl.model.uml
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
        TestSuite suite = new TestSuite("Test for de.tuberlin.cs.cis.ocl.model.uml");
        suite.addTest(new TestSuite(TestUmlTypeDescriptor.class));
        suite.addTest(new TestSuite(TestUmlFacade.class));
        suite.addTest(new TestSuite(TestUmlContextChecker.class));
        suite.addTest(new TestSuite(TestUmlEvaluator.class));
        suite.addTest(new TestSuite(TestUmlTypeMapping.class));
        return suite;
    }
}
