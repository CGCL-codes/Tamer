package edu.umd.cs.guitar.replayer;

import junit.framework.TestCase;

/**
 *jUnit test for Ripper
 */
public class FakeTest extends TestCase {

    public FakeTest(String testName) {
        super(testName);
    }

    /**
     * This is a fake test that asserts true
     */
    public void testNothing() {
        assertTrue(true);
    }
}
