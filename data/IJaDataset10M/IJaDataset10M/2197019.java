package org.tagunit;

import junit.framework.TestCase;

/**
 * Test cases for the TestContextStatus class.
 *
 * @author      Simon Brown
 */
public class TestContextStatusTest extends TestCase {

    private TestContextStatus status;

    /**
   * Tests the initial values of the bean properties after construction.
   */
    public void testConstruction() {
        status = new PositiveTestContextStatus("a name");
        assertEquals("a name", status.getName());
    }

    /**
   * Tests a PositiveTestContextStatus returns a positive result.
   */
    public void testPositiveTestContextStatus() {
        status = new PositiveTestContextStatus("a name");
        assertTrue(status.getResult());
    }

    /**
   * Tests a NegativeTestContextStatus returns a negative result.
   */
    public void testNegativeTestContextStatus() {
        status = new NegativeTestContextStatus("a name");
        assertFalse(status.getResult());
    }
}
