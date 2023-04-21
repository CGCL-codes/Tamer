package net.sf.alc.cfpj.algorithms;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.List;

public class TestRemoveIf extends TestCase {

    private final List<Integer> list = new ArrayList<Integer>();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        list.clear();
        for (int i = 0; i < 10; ++i) list.add(i);
    }

    public void test1() {
        assertEquals(3, Algorithms.removeIf(list.iterator(), Bind.first(BinaryPredicates.<Integer>lessThanOrEqual(), 7)));
        assertEquals(7, list.size());
        assertEquals(null, Algorithms.find(list.iterator(), 7));
        assertEquals(null, Algorithms.find(list.iterator(), 8));
        assertEquals(null, Algorithms.find(list.iterator(), 9));
    }

    public void test2() {
        assertEquals(0, Algorithms.removeIf(list.subList(1, 5).iterator(), Bind.first(BinaryPredicates.<Integer>lessThanOrEqual(), 7)));
        assertEquals(10, list.size());
        assertEquals(list.get(7), Algorithms.find(list.iterator(), 7));
    }
}
