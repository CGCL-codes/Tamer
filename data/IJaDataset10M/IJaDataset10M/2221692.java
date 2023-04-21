package de.javatt.data.scenario.io.verification;

import junit.framework.TestCase;

public class TestFileProperty extends TestCase {

    public void testFileProperty() {
        java.io.File file = new java.io.File("testscenario/GUI-Click-Scenario.scn");
        FileProperty myFileProperty = new FileProperty();
        myFileProperty.setObject(file);
        assertNotNull(myFileProperty.getPropertyValue("Exists"));
        assertTrue(myFileProperty.getPropertyValue("Exists") instanceof Boolean);
        assertEquals(new Boolean(true), myFileProperty.getPropertyValue("Exists"));
        assertNotNull(myFileProperty.getPropertyValue("Size"));
        assertTrue(myFileProperty.getPropertyValue("Size") instanceof Long);
        assertEquals(new Long(872), myFileProperty.getPropertyValue("Size"));
    }
}
