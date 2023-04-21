package com.griddynamics.openspaces.convergence.monitor.storage;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class EqualPropertyFilterTest {

    private SampleInfo sampleInfo;

    @Before
    public void setUp() {
        sampleInfo = new SampleInfo();
    }

    @Test
    public void testThatItAppliesToObjWithPropertyEqualToSpecifiedValue() {
        sampleInfo.setName("abc");
        EqualPropertyFilter filter = new EqualPropertyFilter("name", "abc");
        assertTrue(filter.canAccept(sampleInfo));
    }

    @Test
    public void testThatItNotAppliesToObjWithPropertyNotEqualToSpecifiedValue() {
        sampleInfo.setName("abc");
        EqualPropertyFilter filter = new EqualPropertyFilter("name", "cba");
        assertFalse(filter.canAccept(sampleInfo));
    }

    @Test
    public void testThatItNotAppliesToObjIfSpecifiedPropertyNotExists() {
        EqualPropertyFilter filter = new EqualPropertyFilter("counter", 1);
        assertFalse(filter.canAccept(sampleInfo));
    }
}
