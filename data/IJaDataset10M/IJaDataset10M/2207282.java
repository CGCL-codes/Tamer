package com.gnizr.db.dao;

import com.gnizr.db.dao.MachineTag;
import junit.framework.TestCase;

public class TestMachineTag extends TestCase {

    public void testToString() throws Exception {
        MachineTag mt = new MachineTag();
        assertEquals("", mt.toString());
        mt = new MachineTag("ns", "predicate", "value");
        assertEquals("ns:predicate=value", mt.toString());
        mt = new MachineTag(null, "predicate", "value");
        assertEquals("gn:predicate=value", mt.toString());
        mt = new MachineTag(null, "predicate", "\"value value\"");
        assertEquals("gn:predicate=\"value value\"", mt.toString());
        mt = new MachineTag("ns", "predicate", "\"value value\"");
        assertEquals("ns:predicate=\"value value\"", mt.toString());
    }

    public void testEquals() throws Exception {
        MachineTag m1 = new MachineTag(null, "pred", "value");
        MachineTag m2 = new MachineTag("gn", "pred", "value");
        MachineTag m3 = new MachineTag(null, "ppp", "value");
        assertTrue(m1.equals(m2));
        assertFalse(m1.equals(m3));
        assertFalse(m2.equals(m3));
    }
}
