package com.cube42.echoverse.viewer;

import junit.framework.TestSuite;
import com.cube42.echoverse.model.EntityID;
import com.cube42.util.exception.Cube42NullParameterException;
import com.cube42.util.testing.Cube42UnitTestCase;

/**
 * Unit test code for EntityTextContainer_UT.
 *
 * @author   Matt Paulin
 * @version  $Id: EntityTextContainer_UT.java,v 1.3 2003/03/12 01:55:46 zer0wing Exp $
 */
public class EntityTextContainer_UT extends Cube42UnitTestCase {

    /**
     * constructor takes name of test method
     *
     * @param name          The name of the test method to be run.
     */
    public EntityTextContainer_UT(String name) {
        super(name);
    }

    /**
     * Test the functionality of the container
     */
    public void testContainer() throws InterruptedException {
        EntityTextContainer etc;
        long timeLimit = 1000;
        String returnText;
        EntityID id1 = new EntityID(0);
        EntityID id2 = new EntityID(1);
        TestEntityTextContainerListener listener = new TestEntityTextContainerListener();
        try {
            etc = new EntityTextContainer(1000, null);
            fail("Cube42NullParameterException should be thrown");
        } catch (Cube42NullParameterException e) {
            assertEquals("listener", e.getParameters().toArray()[0]);
        }
        assertTrue(!listener.isUpdated());
        etc = new EntityTextContainer(timeLimit, listener);
        try {
            etc.addText(null, "text");
            fail("Cube42NullParameterException should be thrown");
        } catch (Cube42NullParameterException e) {
            assertEquals("entityID", e.getParameters().toArray()[0]);
        }
        try {
            etc.addText(id1, null);
            fail("Cube42NullParameterException should be thrown");
        } catch (Cube42NullParameterException e) {
            assertEquals("text", e.getParameters().toArray()[0]);
        }
        try {
            etc.getText(null);
            fail("Cube42NullParameterException should be thrown");
        } catch (Cube42NullParameterException e) {
            assertEquals("entityID", e.getParameters().toArray()[0]);
        }
        etc.addText(id1, "text");
        returnText = etc.getText(id1);
        assertTrue(returnText.trim().length() > 0);
        Thread.sleep(timeLimit * 2);
        returnText = etc.getText(id1);
        assertTrue(returnText.trim().length() == 0);
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
        TestSuite suite = (new TestSuite(EntityTextContainer_UT.class));
        System.out.println("Testing the EntityTextContainer class");
        junit.textui.TestRunner.run(suite);
    }
}
