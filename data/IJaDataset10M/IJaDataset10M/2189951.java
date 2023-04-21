package org.xbill.DNS;

import junit.framework.TestCase;

public class FlagsTest extends TestCase {

    public void test_string() {
        assertEquals("aa", Flags.string(Flags.AA));
        assertTrue(Flags.string(12).startsWith("flag"));
        try {
            Flags.string(-1);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
        }
        try {
            Flags.string(0x10);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
        }
    }

    public void test_value() {
        assertEquals(Flags.CD, Flags.value("cd"));
        assertEquals(13, Flags.value("FLAG13"));
        assertEquals(-1, Flags.value("FLAG" + 0x10));
        assertEquals(-1, Flags.value("THIS IS DEFINITELY UNKNOWN"));
        assertEquals(-1, Flags.value(""));
    }

    public void test_isFlag() {
        try {
            Flags.isFlag(-1);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
        }
        assertTrue(Flags.isFlag(0));
        assertFalse(Flags.isFlag(1));
        assertFalse(Flags.isFlag(2));
        assertFalse(Flags.isFlag(3));
        assertFalse(Flags.isFlag(4));
        assertTrue(Flags.isFlag(5));
        assertTrue(Flags.isFlag(6));
        assertTrue(Flags.isFlag(7));
        assertTrue(Flags.isFlag(8));
        assertTrue(Flags.isFlag(9));
        assertTrue(Flags.isFlag(10));
        assertTrue(Flags.isFlag(11));
        assertFalse(Flags.isFlag(12));
        assertFalse(Flags.isFlag(13));
        assertFalse(Flags.isFlag(14));
        assertFalse(Flags.isFlag(14));
        try {
            Flags.isFlag(16);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
        }
    }
}
