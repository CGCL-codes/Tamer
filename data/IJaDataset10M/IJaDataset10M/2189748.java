package de.javatt.data.scenario.io;

import junit.framework.TestCase;

public class TestFile extends TestCase {

    public void testFile() {
        File file = new File();
        file.setDirectory("testscenario");
        file.setEnableSubString("true");
        file.setName("GUI-Click-Sc");
        assertEquals("testscenario", file.getDirectory());
        assertTrue(file.getEnableSubString());
        assertEquals("GUI-Click-Sc", file.getName());
        assertNotNull(file.getResource());
        Object resourceObject = file.getResource();
        assertTrue(resourceObject instanceof java.io.File);
        java.io.File resource = (java.io.File) resourceObject;
        assertTrue(resource.exists());
        assertEquals("GUI-Click-Scenario.scn", resource.getName());
        file.setEnableSubString("false");
        assertNull(file.getResource());
    }

    public void testFileNullDir() {
        File file = new File();
        file.setEnableSubString("true");
        file.setName("GUI-Click-Sc");
        assertNull(file.getResource());
    }

    public void testFileInvalidDir() {
        File file = new File();
        file.setEnableSubString("true");
        file.setDirectory("Bla");
        file.setName("GUI-Click-Sc");
        assertNull(file.getResource());
    }
}
