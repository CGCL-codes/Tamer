package uk.ac.ebi.intact.model.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for Experiments.
 *
 * @author Sugath Mudali (smudali@ebi.ac.uk)
 * @version $Id: ExperimentTest.java 3830 2005-03-15 17:03:02Z skerrien $
 */
public class ExperimentTest extends TestCase {

    public ExperimentTest(String name) throws Exception {
        super(name);
    }

    /**
     * Returns this test suite. Reflection is used here to add all
     * the testXXX() methods to the suite.
     */
    public static Test suite() {
        return new TestSuite(ExperimentTest.class);
    }

    /**
     * Sets up the test fixture. Called before every test case method.
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Tears down the test fixture. Called after every test case method.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
