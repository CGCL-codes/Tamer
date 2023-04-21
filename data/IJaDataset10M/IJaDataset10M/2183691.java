package com.cube42.subsys.syslog;

import junit.framework.TestSuite;
import com.cube42.util.exception.Cube42NullParameterException;
import com.cube42.util.testing.Cube42UnitTestCase;

/**
 * Unit test code for NetworkLogProcessor_UT.
 * <P>
 *
 * @author   Matt Paulin
 * @version  $Id: NetworkLogProcessor_UT.java,v 1.5 2003/03/12 05:01:33 zipwow Exp $
 */
public class NetworkLogProcessor_UT extends Cube42UnitTestCase {

    /**
     * constructor takes name of test method
     *
     * @param name          The name of the test method to be run.
     */
    public NetworkLogProcessor_UT(String name) {
        super(name);
    }

    /**
     * Test processing a log entry
     */
    public void testProcessesLogEntry() {
        NetworkLogProcessor processor = new NetworkLogProcessor();
        try {
            processor.processLogEntry(null);
            fail("Cube42NullParameterException should be thrown " + "if logEntry is set to null");
        } catch (Cube42NullParameterException e) {
            assertEquals(e.getParameters().toArray()[0], "logEntry");
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
        TestSuite suite = (new TestSuite(NetworkLogProcessor_UT.class));
        System.out.println("Testing the NetworkLogProcessor class");
        junit.textui.TestRunner.run(suite);
    }
}
