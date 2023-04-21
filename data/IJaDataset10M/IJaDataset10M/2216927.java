package com.justin.util.individual;

import junit.framework.TestCase;

public class NumberUtilsTest extends TestCase {

    public void testIsOdd() {
        assertTrue(NumberUtils.isOdd(1));
        assertTrue(NumberUtils.isOdd(-1));
        assertFalse(NumberUtils.isOdd(0));
        assertFalse(NumberUtils.isOdd(-2));
        assertFalse(NumberUtils.isOdd(2));
    }
}
