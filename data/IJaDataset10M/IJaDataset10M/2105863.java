package com.javascales.monitoring.inspector.profile;

import junit.framework.TestCase;

public class TestDurationInspector extends TestCase {

    /**
     * Asserts the our DurationInspector has the description "Duration"
     */
    public void testGetDescription() {
        DurationInspector di = new DurationInspector();
        String val = di.getDescription();
        assertEquals(val, "Duration");
    }
}
