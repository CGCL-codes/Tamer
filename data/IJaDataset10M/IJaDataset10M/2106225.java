package org.test.cloe.utils;

import org.tigr.cloe.utils.Range;
import junit.framework.TestCase;

public class TestRange extends TestCase {

    public TestRange(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testEquals_null() {
        Range r = new Range(0, 0);
        assertTrue(!r.equals(null));
    }

    public void testEquals_wrongClass() {
        Range r = new Range(0, 0);
        assertTrue(!r.equals(new Object()));
    }

    public void testEquals_wrongLeft() {
        Range r = new Range(0, 0);
        Range r2 = new Range(5, 0);
        assertTrue(!r.equals(r2));
    }

    public void testEquals_wrongRight() {
        Range r = new Range(0, 0);
        Range r2 = new Range(0, 5);
        assertTrue(!r.equals(r2));
    }

    public void testEquals_equalObjects() {
        Range r = new Range(0, 0);
        Range r2 = new Range(0, 0);
        assertEquals(r, r2);
    }

    public void testEquals_sameRef() {
        Range r = new Range(0, 0);
        assertEquals(r, r);
    }
}
