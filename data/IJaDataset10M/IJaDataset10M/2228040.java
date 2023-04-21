package org.itracker.services.exceptions;

import org.junit.Test;
import junit.framework.TestCase;

public class SystemConfigurationExceptionTest extends TestCase {

    @Test
    public void testConstructor() {
        SystemConfigurationException e = new SystemConfigurationException();
        assertTrue(e instanceof Exception);
        e = new SystemConfigurationException("my_message");
        assertEquals("e.message", "my_message", e.getMessage());
        e = new SystemConfigurationException("my_message", "my_key");
        assertEquals("e.message", "my_message", e.getMessage());
        assertEquals("e.key", "my_key", e.getKey());
    }

    @Test
    public void testSetKey() {
        SystemConfigurationException e = new SystemConfigurationException();
        e.setKey("my_key");
        assertEquals("e.key", "my_key", e.getKey());
        e = new SystemConfigurationException();
        assertNotNull("e.key", e.getKey());
    }
}
