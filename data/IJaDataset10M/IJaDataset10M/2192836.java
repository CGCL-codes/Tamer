package net.sf.alc.cfpj.algorithms;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.List;

public class TestRemove extends TestCase {

    private List<Integer> list;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        list = new ArrayList<Integer>();
        for (int i = 0; i < 10; ++i) list.add(i);
    }

    public void test1() {
        assertEquals(1, Algorithms.remove(list.iterator(), 7));
        assertEquals(9, list.size());
        assertEquals(null, Algorithms.find(list.iterator(), 7));
    }

    public void test2() {
        assertEquals(0, Algorithms.remove(list.subList(1, 5).iterator(), 7));
        assertEquals(10, list.size());
        assertEquals(list.get(7), Algorithms.find(list.iterator(), 7));
    }
}
