package edu.harvard.mcz.imagecapture.tests;

import edu.harvard.mcz.imagecapture.Singleton;
import junit.framework.TestCase;

/** TestOfSingleton tests that the Singleton class is indeed a singleton.
 * 
 * @author Paul J. Morris
 *
 */
public class TestOfSingleton extends TestCase {

    /**
	 * @param name
	 */
    public TestOfSingleton(String name) {
        super(name);
    }

    /**
	 * Test method for {@link edu.harvard.mcz.imagecapture.Singleton#getSingletonInstance()}.
	 */
    public void testGetSingletonInstance() {
        Singleton instance = Singleton.getSingletonInstance();
        assertEquals(instance, Singleton.getSingletonInstance());
    }

    public void testGetSingletonInstanceThreadSafety() {
    }
}
