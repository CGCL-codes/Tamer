package ru.krondix.ir.sparse;

import junit.framework.TestCase;

public class SparseArrayListTest extends TestCase {

    public void testSetAndGet() throws Exception {
        SparseArrayList<Integer> list = new SparseArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                continue;
            }
            list.set(i, i);
        }
        list.set(5, 5);
        for (int i = 0; i < 10; i++) {
            assertEquals(i, (int) list.get(i));
        }
        list.set(5, 6);
        for (int i = 0; i < 10; i++) {
            assertEquals(i != 5 ? i : 6, (int) list.get(i));
        }
    }
}
