package net.entelijan.cobean.examples.util;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class RoundabouteIteratorTestCase {

    @Test
    public void testInteger() {
        List<Integer> li = createList(1, 2, 3);
        RoundaboutIterator<Integer> ri = new RoundaboutIterator<Integer>(li);
        assertEquals(1, ri.next().intValue());
        assertEquals(2, ri.next().intValue());
        assertEquals(3, ri.next().intValue());
        assertEquals(1, ri.next().intValue());
        assertEquals(2, ri.next().intValue());
        assertEquals(3, ri.next().intValue());
        assertEquals(1, ri.next().intValue());
        assertEquals(2, ri.next().intValue());
    }

    @Test
    public void testIntegerNull() {
        List<Integer> li = createList(1, 2, null);
        RoundaboutIterator<Integer> ri = new RoundaboutIterator<Integer>(li);
        assertEquals(1, ri.next().intValue());
        assertEquals(2, ri.next().intValue());
        assertEquals(1, ri.next().intValue());
        assertEquals(2, ri.next().intValue());
        assertEquals(1, ri.next().intValue());
    }

    @Test
    public void testString() {
        List<String> li = createList("A", "B", "C", "D");
        RoundaboutIterator<String> ri = new RoundaboutIterator<String>(li);
        assertEquals("A", ri.next());
        assertEquals("B", ri.next());
        assertEquals("C", ri.next());
        assertEquals("D", ri.next());
        assertEquals("A", ri.next());
        assertEquals("B", ri.next());
        assertEquals("C", ri.next());
        assertEquals("D", ri.next());
        assertEquals("A", ri.next());
        assertEquals("B", ri.next());
        assertEquals("C", ri.next());
        assertEquals("D", ri.next());
        assertEquals("A", ri.next());
        assertEquals("B", ri.next());
    }

    @Test
    public void testNextPrev() {
        List<String> li = createList("A", "B", "C", "D");
        RoundaboutIterator<String> ri = new RoundaboutIterator<String>(li);
        assertEquals("A", ri.next());
        assertEquals("B", ri.next());
        assertEquals("A", ri.previus());
        assertEquals("D", ri.previus());
        assertEquals("C", ri.previus());
        assertEquals("D", ri.next());
        assertEquals("A", ri.next());
    }

    @Test
    public void testStartPrev() {
        List<String> li = createList("A", "B", "C", "D");
        RoundaboutIterator<String> ri = new RoundaboutIterator<String>(li);
        assertEquals("D", ri.previus());
        assertEquals("C", ri.previus());
    }

    private <T> List<T> createList(T... val) {
        ArrayList<T> re = new ArrayList<T>();
        for (T item : val) {
            re.add(item);
        }
        return re;
    }
}
