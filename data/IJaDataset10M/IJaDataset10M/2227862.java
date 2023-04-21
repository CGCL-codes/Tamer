package com.wozgonon.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LogScale32Test {

    @Test
    public void testIntLog2() {
        assertEquals(0, LogScale32.IntLog2(0));
        assertEquals(0, LogScale32.IntLog2(1));
        assertEquals(1, LogScale32.IntLog2(2));
        assertEquals(3, LogScale32.IntLog2(8));
        assertEquals(20, LogScale32.IntLog2(1024 * 1024));
    }

    @Test
    public void testGetByInt() {
        assertEquals(LogScale32.BIT0, LogScale32.getByInt(1));
        assertEquals(LogScale32.BIT1, LogScale32.getByInt(2));
        assertEquals(LogScale32.BIT20, LogScale32.getByInt(1024 * 1024));
    }

    @Test
    public void testGetByLog2Int() {
        assertEquals(LogScale32.BIT0, LogScale32.getByLog2Int(0));
        assertEquals(LogScale32.BIT1, LogScale32.getByLog2Int(1));
        assertEquals(LogScale32.BIT31, LogScale32.getByLog2Int(31));
    }

    @Test
    public void testWarmth() {
        assertEquals(LogScale32.BIT5.getColor(), LogScale32.warmth(16));
    }

    @Test
    public void testLog2Warmth() {
        assertEquals(LogScale32.BIT4.getColor(), LogScale32.log2Warmth(4));
    }
}
