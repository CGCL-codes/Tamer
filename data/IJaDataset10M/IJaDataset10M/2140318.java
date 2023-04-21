package com.googlecode.gaal.data.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import org.junit.Test;

public class SparseSequenceTest {

    @Test
    public void testSparseSequenceInt() {
        SparseSequence sequence = new SparseSequence(5);
        sequence.put(10, 1);
        sequence.put(5, 2);
        sequence.put(7, 3);
        sequence.put(1, 4);
        sequence.put(3, 5);
        System.out.println(Arrays.toString(sequence.indices));
        System.out.println(Arrays.toString(sequence.data));
        assertArrayEquals(new int[] { 1, 3, 5, 7, 10 }, sequence.indices);
        assertArrayEquals(new int[] { 4, 5, 2, 3, 1 }, sequence.data);
        assertEquals(5, sequence.size());
    }

    @Test
    public void testGetIntInt() {
        SparseSequence sequence = new SparseSequence(5);
        sequence.put(10, 1);
        sequence.put(5, 2);
        sequence.put(7, 3);
        sequence.put(1, 4);
        sequence.put(3, 5);
        assertTrue(sequence.get(0, -1) == -1);
        assertTrue(sequence.get(1, -1) == 4);
        assertTrue(sequence.get(3, -1) == 5);
        assertTrue(sequence.get(5, -1) == 2);
        assertTrue(sequence.get(7, -1) == 3);
        assertTrue(sequence.get(10, -1) == 1);
    }

    @Test
    public void testGetInt() {
        SparseSequence sequence = new SparseSequence(5);
        sequence.put(10, 1);
        sequence.put(5, 2);
        sequence.put(7, 3);
        sequence.put(1, 4);
        sequence.put(3, 5);
        assertTrue(sequence.get(1) == 4);
        assertTrue(sequence.get(5) == 2);
        assertTrue(sequence.get(7) == 3);
        Exception exception = null;
        try {
            sequence.get(2);
        } catch (IndexOutOfBoundsException ex) {
            exception = ex;
        }
        assertTrue(exception != null);
        assertTrue(exception.getMessage().equals("2"));
    }

    @Test
    public void testSize() {
        SparseSequence sequence = new SparseSequence(5);
        sequence.put(10, 1);
        sequence.put(5, 2);
        sequence.put(7, 3);
        sequence.put(1, 4);
        assertEquals(4, sequence.size());
    }
}
