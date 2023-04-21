package org.openscience.cdk.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * TestSuite that uses tests wether all public methods in the standard
 * module are tested. Unlike Emma, it does not test that all code is
 * tested, just all methods.
 *
 * @cdk.module test-standard
 */
public class StandardCoverageTest extends CoverageTest {

    private static final String CLASS_LIST = "standard.javafiles";

    public StandardCoverageTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        super.loadClassList(CLASS_LIST);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(StandardCoverageTest.class);
        return suite;
    }

    public void testCoverage() {
        assertTrue(super.runCoverageTest());
    }
}
