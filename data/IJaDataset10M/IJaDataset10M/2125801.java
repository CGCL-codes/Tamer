package de.denkselbst.niffler.util;

import java.util.BitSet;
import java.util.List;
import junit.framework.TestCase;

public class ExamplePickerTest extends TestCase {

    public void testEmpty() {
        BitSet choice = new BitSet();
        List<Integer> picked = ExamplePicker.pickExamples(choice, 1);
        assertTrue(picked.isEmpty());
        choice.set(0, 10);
        picked = ExamplePicker.pickExamples(choice, 0);
        assertTrue(picked.isEmpty());
        picked = ExamplePicker.pickExamples(choice, 1);
        assertFalse(picked.isEmpty());
    }

    public void testPickSomeOrAll() {
        BitSet choice = new BitSet();
        choice.set(0, 3);
        List<Integer> picked = ExamplePicker.pickExamples(choice, 1);
        assertEquals(1, picked.size());
        assertFalse(picked.contains(-1));
        picked = ExamplePicker.pickExamples(choice, 3);
        assertEquals(3, picked.size());
        assertFalse(picked.contains(-1));
        picked = ExamplePicker.pickExamples(choice, 4);
        assertEquals(3, picked.size());
        assertFalse(picked.contains(-1));
        picked = ExamplePicker.pickExamples(choice, 10);
        assertEquals(3, picked.size());
        assertFalse(picked.contains(-1));
    }
}
