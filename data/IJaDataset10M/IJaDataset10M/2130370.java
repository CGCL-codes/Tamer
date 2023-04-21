package edu.hawaii.halealohacli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests the convenience/utility methods in Server.
 * 
 * @see Server
 * 
 * @author Zach Tomaszewski
 */
public class TestServer {

    /**
   * Tests {@link Server#isTower}.
   */
    @Test
    public void testIsTower() {
        assertTrue("Lehua is a tower", Server.isTower("Lehua"));
        assertFalse("Lehua-A is not a tower", Server.isTower("Lehua-A"));
    }

    /**
   * Tests {@link Server#isLounge}.
   */
    @Test
    public void testIsLounge() {
        assertTrue("Lehua-A is a lounge", Server.isLounge("Lehua-A"));
        assertTrue("Lehua-E is a lounge", Server.isLounge("Lehua-E"));
        assertFalse("Lehua-F is not a lounge", Server.isLounge("Lehua-F"));
        assertFalse("Lehua is not a lounge", Server.isLounge("Lehua"));
    }
}
