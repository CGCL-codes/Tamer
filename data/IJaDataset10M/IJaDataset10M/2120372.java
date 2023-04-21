package net.sf.kdgcommons.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import junit.framework.TestCase;

public class TestCombiningIterable extends TestCase {

    public void testEmptyConstructor() throws Exception {
        CombiningIterable<String> iterable = new CombiningIterable<String>();
        Iterator<String> itx = iterable.iterator();
        assertFalse(itx.hasNext());
    }

    public void testSingleEmptyList() throws Exception {
        List<String> list = Collections.emptyList();
        CombiningIterable<String> iterable = new CombiningIterable<String>(list);
        Iterator<String> itx = iterable.iterator();
        assertFalse(itx.hasNext());
    }

    public void testSingleList() throws Exception {
        List<String> list = Arrays.asList("foo", "bar");
        CombiningIterable<String> iterable = new CombiningIterable<String>(list);
        Iterator<String> itx = iterable.iterator();
        assertEquals("foo", itx.next());
        assertEquals("bar", itx.next());
        assertFalse(itx.hasNext());
    }

    public void testMultipleLists() throws Exception {
        List<String> list1 = Arrays.asList("foo", "bar");
        List<String> list2 = Arrays.asList("baz");
        CombiningIterable<String> iterable = new CombiningIterable<String>(list1, list2);
        Iterator<String> itx = iterable.iterator();
        assertEquals("foo", itx.next());
        assertEquals("bar", itx.next());
        assertEquals("baz", itx.next());
        assertFalse(itx.hasNext());
    }

    public void testCanProduceMultipleIndependentIterables() throws Exception {
        List<String> list1 = Arrays.asList("foo", "bar");
        List<String> list2 = Arrays.asList("baz");
        CombiningIterable<String> iterable = new CombiningIterable<String>(list1, list2);
        Iterator<String> itx1 = iterable.iterator();
        Iterator<String> itx2 = iterable.iterator();
        assertEquals("foo", itx1.next());
        assertEquals("bar", itx1.next());
        assertEquals("baz", itx1.next());
        assertFalse(itx1.hasNext());
        assertEquals("foo", itx2.next());
        assertEquals("bar", itx2.next());
        assertEquals("baz", itx2.next());
        assertFalse(itx2.hasNext());
        Iterator<String> itx3 = iterable.iterator();
        assertEquals("foo", itx3.next());
        assertEquals("bar", itx3.next());
        assertEquals("baz", itx3.next());
        assertFalse(itx3.hasNext());
    }

    public void testRemoveFromModifiableList() throws Exception {
        List<String> list1 = new ArrayList<String>(Arrays.asList("foo", "bar"));
        List<String> list2 = new ArrayList<String>(Arrays.asList("baz"));
        CombiningIterable<String> iterable = new CombiningIterable<String>(list1, list2);
        Iterator<String> itx = iterable.iterator();
        assertEquals("foo", itx.next());
        itx.remove();
        assertEquals(1, list1.size());
        assertEquals("bar", list1.get(0));
        assertEquals("bar", itx.next());
        assertEquals("baz", itx.next());
        itx.remove();
        assertEquals(0, list2.size());
    }

    public void testRemoveFromUnodifiableListThrows() throws Exception {
        List<String> list1 = Arrays.asList("foo", "bar");
        List<String> list2 = Arrays.asList("baz");
        CombiningIterable<String> iterable = new CombiningIterable<String>(list1, list2);
        Iterator<String> itx = iterable.iterator();
        assertEquals("foo", itx.next());
        try {
            itx.remove();
            fail("able to remove from unmodifiable list");
        } catch (UnsupportedOperationException ex) {
        }
    }

    public void testIteratingOffEndWillThrow() throws Exception {
        List<String> list = Arrays.asList("foo", "bar");
        CombiningIterable<String> iterable = new CombiningIterable<String>(list);
        Iterator<String> itx = iterable.iterator();
        assertEquals("foo", itx.next());
        assertEquals("bar", itx.next());
        try {
            itx.next();
            fail("able to iterate off end of list");
        } catch (NoSuchElementException ex) {
        }
    }
}
