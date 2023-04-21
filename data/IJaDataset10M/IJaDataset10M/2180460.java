package com.javaexchange;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RandomGUIDTest extends TestCase {

    private Log logger;

    /**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
    public RandomGUIDTest(String testName) {
        super(testName);
        logger = LogFactory.getLog(this.getClass());
        logger.debug("RandomGUIDTest");
    }

    /**
	 * @return the suite of tests being tested
	 */
    public static Test suite() {
        return new TestSuite(RandomGUIDTest.class);
    }

    /**
	 * Basic test. Just verifies that two GUIDs are unique.
	 */
    public void test_simple() {
        RandomGUID guid1, guid2;
        for (int i = 0; i < 100; i++) {
            guid1 = new RandomGUID();
            guid2 = new RandomGUID();
            assertFalse("Duplicate GUIDs", guid1.toString().equals(guid2.toString()));
        }
    }
}
