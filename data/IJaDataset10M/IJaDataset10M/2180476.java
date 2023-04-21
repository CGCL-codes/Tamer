package com.cube42.util.rmi;

import java.rmi.RemoteException;
import junit.framework.TestSuite;
import com.cube42.util.exception.Cube42NullParameterException;
import com.cube42.util.testing.Cube42UnitTestCase;

/**
 * Unit test code for RemoteUpdaterImpl_UT.
 * <P>
 *
 * @author   Matt Paulin
 * @version  $Id: RemoteUpdaterImpl_UT.java,v 1.3 2003/03/12 00:28:13 zer0wing Exp $
 */
public class RemoteUpdaterImpl_UT extends Cube42UnitTestCase {

    /**
     * constructor takes name of test method
     *
     * @param name          The name of the test method to be run.
     */
    public RemoteUpdaterImpl_UT(String name) {
        super(name);
    }

    /**
     * Test the constructor of the RemoteUpdaterImpl
     */
    public void testConstructor() throws RemoteException {
        try {
            RemoteUpdaterImpl rui = new RemoteUpdaterImpl(null);
            fail("Cube42NullParameterException should be thrown");
        } catch (Cube42NullParameterException e) {
            assertEquals(e.getParameters().toArray()[0], "updateClass");
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
        TestSuite suite = (new TestSuite(RemoteUpdaterImpl_UT.class));
        System.out.println("Testing the RemoteUpdaterImpl class");
        junit.textui.TestRunner.run(suite);
    }
}
