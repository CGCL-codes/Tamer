package org.jscsi.core.utils;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests the Utils class of correctness.
 * 
 * @author Volker Wildi
 */
public class UtilsTest {

    /**
   * Tests the conversion from a signed byte to an unsigned integer number.
   */
    @Test
    public void testGetUnsignedInt() {
        assertEquals(0xF0, Utils.getUnsignedInt((byte) 0xF0));
    }

    /**
   * Tests the conversion from a signed short to an unsigned long number.
   */
    @Test
    public void testGetUnsignedLongFromShort() {
        assertEquals(0xBD18L, Utils.getUnsignedLong((short) 0xBD18));
    }

    /**
   * Tests the conversion from a signed integer to an unsigned long number.
   */
    @Test
    public void testGetUnsignedLongFromInt() {
        assertEquals(0xC254F92AL, Utils.getUnsignedLong(0xC254F92A));
    }
}
