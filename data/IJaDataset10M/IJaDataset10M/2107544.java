package drcube.test.tools;

import j2meunit.framework.Test;
import j2meunit.framework.TestCase;
import j2meunit.framework.TestSuite;
import drcube.test.tools.testclasses.SaveGameManagerTest;

/**
 * Testet die Screensaver Activity.
 * 
 * @author simbuerg
 * 
 */
public class TestSaveGameManager extends TestCase {

    /**
	 * Vorbedingungen f�r Tests
	 * 
	 * @see j2meunit.framework.TestCase#setUp()
	 */
    public void setUp() {
    }

    /**
	 * Nachbedingungen f�r Tests. Aufr�umarbeiten etc.
	 * 
	 * @see j2meunit.framework.TestCase#tearDown()
	 */
    public void tearDown() {
    }

    /**
	 * Liefert die TestSuite.
	 * 
	 * @see j2meunit.framework.TestCase#suite()
	 */
    public Test suite() {
        return new TestSuite(new TestSaveGameManager().getClass(), new String[] { "testSaveGameManagerWorksRight" });
    }

    /***************************************************************************
	 * Override to run the test and assert its state.
	 * 
	 * @exception Throwable
	 *                if any exception is thrown
	 */
    protected void runTest() throws java.lang.Throwable {
        if (getName().equals("testSaveGameManagerWorksRight")) {
            testSaveGameManagerWorksRight();
        } else {
            throw new RuntimeException("You don't have a method called " + getName());
        }
    }

    /**
	 * Testet richtig gespeichert und geladen wird.
	 * 
	 */
    public void testSaveGameManagerWorksRight() {
        SaveGameManagerTest manager = new SaveGameManagerTest();
        byte[] gameInfo = new byte[2];
        gameInfo[0] = Byte.parseByte("Test");
        gameInfo[1] = Byte.parseByte("Manager");
        byte[] test;
        manager.saveGame(gameInfo);
        test = manager.getLastSavegame();
        assertEquals(gameInfo, test);
    }
}
