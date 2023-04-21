package com.cube42.util.logging;

import junit.framework.TestSuite;
import com.cube42.util.testing.Cube42UnitTestCase;

/**
 * Unit test code for AllPassFilter_UT.
 * <P>
 *
 * @author   Matt Paulin
 * @version  $Id: AllPassFilter_UT.java,v 1.4 2003/03/12 00:27:51 zer0wing Exp $
 */
public class AllPassFilter_UT extends Cube42UnitTestCase {

    /**
     * constructor takes name of test method
     *
     * @param name          The name of the test method to be run.
     */
    public AllPassFilter_UT(String name) {
        super(name);
    }

    /**
     * Test that everything gets set to true in the AllPassFilter
     */
    public void testTheObvious() {
        AllPassFilter filter = new AllPassFilter();
        assertTrue(filter.allowDebug());
        assertTrue(filter.allowInfo());
        assertTrue(filter.allowWarning());
        assertTrue(filter.allowError());
        assertTrue(filter.allowFatal());
        assertTrue(filter.allowNetTrace());
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
        TestSuite suite = (new TestSuite(AllPassFilter_UT.class));
        System.out.println("Testing the AllPassFilter class");
        junit.textui.TestRunner.run(suite);
    }
}
