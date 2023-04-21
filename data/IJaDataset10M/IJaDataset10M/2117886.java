package org.settings4j.exception;

import junit.framework.TestCase;

public class Settings4jExceptionTest extends TestCase {

    public void testSettings4jException() {
        final String errorKey = "error.no.writeable.connector.found";
        Settings4jException settings4jException;
        settings4jException = new Settings4jException(errorKey);
        assertNotNull(settings4jException.toString());
        assertTrue(settings4jException.toString().contains(errorKey));
        assertEquals("Content '{0}' cannot be writen. No writeable Connector found", settings4jException.getMessage());
        settings4jException = new Settings4jException(errorKey, new RuntimeException("test"));
        assertEquals("Content '{0}' cannot be writen. No writeable Connector found", settings4jException.getMessage());
        settings4jException = new Settings4jException(errorKey, new String[] { "arg1" });
        assertNotNull(settings4jException.toString());
        assertTrue(settings4jException.toString().contains(errorKey));
        assertTrue(settings4jException.toString().contains("Param"));
        assertEquals("Content 'arg1' cannot be writen. No writeable Connector found", settings4jException.getMessage());
        settings4jException = new Settings4jException(errorKey, new String[] { "arg1" }, new RuntimeException("test"));
        assertEquals("Content 'arg1' cannot be writen. No writeable Connector found", settings4jException.getMessage());
    }
}
