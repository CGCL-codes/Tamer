package net.sf.ngrease.example.hello;

import junit.framework.TestCase;

public class FieldLimitsTest extends TestCase {

    public void testBarLimit() {
        assertEquals(20, FieldLimits.bar());
    }
}
