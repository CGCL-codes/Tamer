package net.sf.paperclips;

import junit.framework.TestCase;

public class MarginsTest extends TestCase {

    public void testEquals() {
        Margins m1 = new Margins(0);
        Margins m2 = new Margins(0);
        assertEquals(m1, m2);
        m1.top = 1;
        assertFalse(m1.equals(m2));
        m2.top = 1;
        assertEquals(m1, m2);
        m1.left = 1;
        assertFalse(m1.equals(m2));
        m2.left = 1;
        assertEquals(m1, m2);
        m1.right = 1;
        assertFalse(m1.equals(m2));
        m2.right = 1;
        assertEquals(m1, m2);
        m1.bottom = 1;
        assertFalse(m1.equals(m2));
        m2.bottom = 1;
        assertEquals(m1, m2);
    }
}
