package com.cube42.util.testing;

import junit.framework.TestSuite;
import com.cube42.util.exception.Cube42NullParameterException;
import com.cube42.util.system.SubSystemID;

/**
 * Unit test code for SystemTestResultCollection_UT.
 * <P>
 *
 * @author   Matt Paulin
 * @version  $Id: SystemTestResultCollection_UT.java,v 1.4 2003/03/12 00:27:48 zer0wing Exp $
 */
public class SystemTestResultCollection_UT extends Cube42UnitTestCase {

    /**
     * constructor takes name of test method
     *
     * @param name          The name of the test method to be run.
     */
    public SystemTestResultCollection_UT(String name) {
        super(name);
    }

    /**
     * Test adding system results
     */
    public void testAddingIDs() {
        SystemTestResult r1;
        SystemTestResult r2;
        SystemTestResult r3;
        SystemTestResult str;
        SystemTestID testID1;
        testID1 = new SystemTestID(100, SystemTestType.FUNCTIONAL_TEST, new SubSystemID("Target"), "TestID1", null, null, null);
        r1 = new SystemTestResult(testID1);
        r2 = new SystemTestResult(testID1);
        r3 = new SystemTestResult(testID1);
        SystemTestResultCollection collection = new SystemTestResultCollection();
        collection.addSystemTestResult(r1);
        assertEquals(1, collection.size());
        collection.addSystemTestResult(r1);
        assertEquals(2, collection.size());
        collection.addSystemTestResult(r2);
        assertEquals(3, collection.size());
        collection.addSystemTestResult(r3);
        assertEquals(4, collection.size());
        try {
            collection.addSystemTestResult(null);
            fail("Cube42NullParameterException should be thrown");
        } catch (Cube42NullParameterException e) {
            assertEquals("systemTestResult", e.getParameters().toArray()[0]);
        }
    }

    /**
     * Sets up data for the test <p>
     * This is done once before the start of every test.
     */
    public void setUp() {
    }

    /**
     * Undoes all that was done in setUp, clean up after test <p>
     * This is done once at the end of every test.
     */
    public void tearDown() {
    }

    /**
     * Runs this unit tests using junit.textui.TestRunner
     */
    public static void main(String[] args) {
        TestSuite suite = (new TestSuite(SystemTestResultCollection_UT.class));
        System.out.println("Testing the SystemTestResultCollection class");
        junit.textui.TestRunner.run(suite);
    }
}
