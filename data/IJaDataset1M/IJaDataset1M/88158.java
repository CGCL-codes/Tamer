package net.sf.xml2cb.util;

import junit.framework.TestCase;

public class StringUtilsTest extends TestCase {

    public void testRightSpace() {
        assertEquals("toto", StringUtils.rightSpaces("toto", 4));
        assertEquals("toto ", StringUtils.rightSpaces("toto", 5));
        assertEquals("toto      ", StringUtils.rightSpaces("toto", 10));
        assertEquals("  ", StringUtils.rightSpaces("", 2));
    }

    public void testRightSpaceErrors() {
        try {
            StringUtils.rightSpaces("toto", 3);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }

    public void testLeftZeroes() {
        assertEquals("0", StringUtils.leftZeroes("", 1));
        assertEquals("800", StringUtils.leftZeroes("800", 3));
        assertEquals("00052", StringUtils.leftZeroes("52", 5));
        assertEquals("0000000000", StringUtils.leftZeroes("0", 10));
    }

    public void testLeftZeroesErrors() {
        try {
            StringUtils.rightSpaces("123", 2);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }
}
