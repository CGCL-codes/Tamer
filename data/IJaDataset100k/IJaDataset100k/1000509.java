package net.obsearch.index.ghs.impl;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestSketch64ShortBits {

    @Test
    public void testBits() {
        long res = 0;
        res = res | (1L << 0);
        res = res | (1L << 1);
        res = res | (1L << 4);
        assertEquals("10011", Long.toBinaryString(res));
        res = 0;
        res = res | (1L << 4);
        res = res | (1L << 8);
        assertEquals("100010000", Long.toBinaryString(res));
    }
}
