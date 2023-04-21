package drcube.test.sys;

import j2meunit.framework.Test;
import j2meunit.framework.TestCase;
import j2meunit.framework.TestSuite;
import drcube.sys.Configuration;
import drcube.test.activities.TestActivityManager;

/**
 * Testet das Singleton Configuration
 * 
 * @author simbuerg
 *
 */
public class TestConfiguration extends TestCase {

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
     * @see j2meunit.framework.TestCase#suite()
     */
    public Test suite() {
        return new TestSuite(new TestActivityManager().getClass(), new String[] { "testConfigInitialized", "testGetOption", "testSetOption" });
    }

    /***************************************
     * Override to run the test and assert its state.
     *
     * @exception Throwable if any exception is thrown
     */
    protected void runTest() throws java.lang.Throwable {
        if (getName().equals("testConfigInitialized")) {
            testConfigInitialized();
        } else if (getName().equals("testGetOption")) {
            testGetOption();
        } else if (getName().equals("testSetOption")) {
            testSetOption();
        } else {
            throw new RuntimeException("You don't have a method called " + getName());
        }
    }

    /**
     * Testet die Initialisierung des Singleton Configuration.
     */
    public void testConfigInitialized() {
        Configuration config = Configuration.getInstance();
        assertNotNull(config);
    }

    /**
     * Testet die Funktionalit�t der Configuiration.getOption() Methode.
     */
    public void testGetOption() {
        Configuration config = Configuration.getInstance();
        String configString = config.getOption("....");
        assertNotNull(configString);
    }

    /**
     * Testet die Funktionalit�t der Configuration.setOption() Methode.
     */
    public void testSetOption() {
        Configuration config = Configuration.getInstance();
        config.setOption("Test", "Test1");
        assertEquals("Test1", config.getOption("Test"));
    }
}
