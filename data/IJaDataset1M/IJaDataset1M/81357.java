package org.manorrock.sag;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A JUnit test for the FamilyLink class.
 *
 * @author Manfred Riem (mriem@manorrock.org)
 * @version $Revision: 1.2 $
 */
public class FamilyLinkTest extends TestCase {

    /**
     * Stores a family link.
     */
    private FamilyLink familyLink;

    /**
     * Constructor.
     *
     * @param testName the name of the test.
     */
    public FamilyLinkTest(String testName) {
        super(testName);
    }

    /**
     * Setup the test.
     *
     * @throws Exception when a major error occurs.
     */
    protected void setUp() throws Exception {
        familyLink = new FamilyLink();
    }

    /**
     * Tear down the test.
     *
     * @throws Exception when a major error occurs.
     */
    protected void tearDown() throws Exception {
    }

    /**
     * Get a test suite.
     *
     * @return a test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(FamilyLinkTest.class);
        return suite;
    }

    /**
     * Test getId method.
     */
    public void testGetId() {
        assertTrue(familyLink.getId() == null);
    }

    /**
     * Test getType method.
     */
    public void testGetType() {
        assertTrue(familyLink.getType() == '\0');
    }
}
