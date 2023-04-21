package org.happy.collections.lists.decorators.iterators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.collections.iterators.AbstractTestIterator;
import org.happy.collections.decorators.CollectionDecorator_1x0Test;
import org.junit.Test;

/**
 * tests the iterator which inverts the direction of the lsit
 * @author Andreas Hollmann
 *
 */
public class InversedListIterator_1x0Test extends AbstractTestIterator {

    private CollectionDecorator_1x0Test cdTest = new CollectionDecorator_1x0Test();

    public InversedListIterator_1x0Test() {
        super("ConditionIterator1x0Test");
    }

    public InversedListIterator_1x0Test(String testName) {
        super(testName);
    }

    @Override
    public Iterator<Object> makeEmptyIterator() {
        return new InversedListIterator_1x0<Object>(new ArrayList<Object>().listIterator());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<Object> makeFullIterator() {
        return new InversedListIterator_1x0<Object>(new ArrayList<Object>(cdTest.makeFullCollection()).listIterator());
    }

    @Test
    public void testHasNext() {
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 2, 3, 4, 5 }));
        ListIterator<Integer> it = InversedListIterator_1x0.of(list.listIterator());
        assertFalse(it.hasNext());
        assertEquals(new Integer(1), it.previous());
        assertTrue(it.hasNext());
        InversedListIterator_1x0.of(list.listIterator(4));
        assertTrue(it.hasNext());
        assertEquals(new Integer(1), it.next());
    }

    @Test
    public void testHasPrevious() {
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 2, 3, 4, 5 }));
        ListIterator<Integer> it = InversedListIterator_1x0.of(list.listIterator(5));
        assertFalse(it.hasPrevious());
        assertEquals((Integer) 5, it.next());
        assertTrue(it.hasPrevious());
        InversedListIterator_1x0.of(list.listIterator(4));
        assertTrue(it.hasPrevious());
        assertEquals((Integer) 5, it.previous());
    }

    @Test
    public void testNext() {
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 2, 3, 4, 5 }));
        ListIterator<Integer> it = InversedListIterator_1x0.of(list.listIterator(2));
        assertEquals((Integer) 2, it.next());
        assertEquals((Integer) 1, it.next());
    }

    @Test
    public void testNextIndex() {
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 2, 3, 4, 5 }));
        ListIterator<Integer> it = InversedListIterator_1x0.of(list.listIterator(2));
        assertEquals(1, it.nextIndex());
        it.next();
        assertEquals(0, it.nextIndex());
    }

    @Test
    public void testPrevious() {
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 2, 3, 4, 5 }));
        ListIterator<Integer> it = InversedListIterator_1x0.of(list.listIterator(2));
        assertEquals((Integer) 3, it.previous());
        assertEquals((Integer) 4, it.previous());
    }

    @Test
    public void testPreviousIndex() {
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 2, 3, 4, 5 }));
        ListIterator<Integer> it = InversedListIterator_1x0.of(list.listIterator(2));
        assertEquals(2, it.previousIndex());
        it.previous();
        assertEquals(3, it.previousIndex());
    }

    @Test
    public void testInversedListIterator_1x0() {
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(new Integer[] { 1, 2, 3, 4, 5 }));
        ListIterator<Integer> it = list.listIterator(2);
        InversedListIterator_1x0<Integer> itDecorated = InversedListIterator_1x0.of(it);
        assertEquals(it, itDecorated.getDecorated());
    }

    @Override
    public void testFullIterator() {
    }

    @Override
    public void testRemove() {
    }
}
