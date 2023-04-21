package org.yajul.thread;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests the BusyFlag class.
 * User: jdavis
 * Date: Nov 18, 2003
 * Time: 9:17:17 PM
 * @author jdavis
 */
public class BusyFlagTest extends TestCase {

    public BusyFlagTest(String name) {
        super(name);
    }

    /**
     * Test BusyFlag with a single thread.
     */
    public void testSingleThread() throws Exception {
        BusyFlag f = new BusyFlag();
        assertNull(f.getOwnerThread());
        try {
            f.acquire();
            assertEquals(Thread.currentThread(), f.getOwnerThread());
            assertEquals(1, f.getLockCount());
        } finally {
            f.release();
        }
        assertNull(f.getOwnerThread());
    }

    /**
     * Constructs a test suite for this test case, providing any required
     * Setup wrappers, or decorators as well.
     * @return Test - The test suite.
     */
    public static Test suite() {
        return new TestSuite(BusyFlagTest.class);
    }
}
