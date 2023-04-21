package org.apache.commons.collections.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Abstract class for testing the ListIterator interface.
 * <p>
 * This class provides a framework for testing an implementation of ListIterator.
 * Concrete subclasses must provide the list iterator to be tested.
 * They must also specify certain details of how the list iterator operates by
 * overriding the supportsXxx() methods if necessary.
 * 
 * @since Commons Collections 3.0
 * @version $Revision: 155406 $ $Date: 2005-02-26 13:55:26 +0100 (Sa, 26 Feb 2005) $
 * 
 * @author Rodney Waldhoff
 * @author Stephen Colebourne
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class AbstractTestListIterator extends AbstractTestIterator {

    /**
     * JUnit constructor.
     * 
     * @param testName  the test class name
     */
    public AbstractTestListIterator(String testName) {
        super(testName);
    }

    /**
     * Implement this method to return a list iterator over an empty collection.
     * 
     * @return an empty iterator
     */
    public abstract ListIterator makeEmptyListIterator();

    /**
     * Implement this method to return a list iterator over a collection with elements.
     * 
     * @return a full iterator
     */
    public abstract ListIterator makeFullListIterator();

    /**
     * Implements the abstract superclass method to return the list iterator.
     * 
     * @return an empty iterator
     */
    public Iterator makeEmptyIterator() {
        return makeEmptyListIterator();
    }

    /**
     * Implements the abstract superclass method to return the list iterator.
     * 
     * @return a full iterator
     */
    public Iterator makeFullIterator() {
        return makeFullListIterator();
    }

    /**
     * Whether or not we are testing an iterator that supports add().
     * Default is true.
     * 
     * @return true if Iterator supports add
     */
    public boolean supportsAdd() {
        return true;
    }

    /**
     * Whether or not we are testing an iterator that supports set().
     * Default is true.
     * 
     * @return true if Iterator supports set
     */
    public boolean supportsSet() {
        return true;
    }

    /**
     * The value to be used in the add and set tests.
     * Default is null.
     */
    public Object addSetValue() {
        return null;
    }

    /**
     * Test that the empty list iterator contract is correct.
     */
    public void testEmptyListIteratorIsIndeedEmpty() {
        if (supportsEmptyIterator() == false) {
            return;
        }
        ListIterator it = makeEmptyListIterator();
        assertEquals(false, it.hasNext());
        assertEquals(0, it.nextIndex());
        assertEquals(false, it.hasPrevious());
        assertEquals(-1, it.previousIndex());
        try {
            it.next();
            fail("NoSuchElementException must be thrown from empty ListIterator");
        } catch (NoSuchElementException e) {
        }
        try {
            it.previous();
            fail("NoSuchElementException must be thrown from empty ListIterator");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Test navigation through the iterator.
     */
    public void testWalkForwardAndBack() {
        ArrayList list = new ArrayList();
        ListIterator it = makeFullListIterator();
        while (it.hasNext()) {
            list.add(it.next());
        }
        assertEquals(false, it.hasNext());
        assertEquals(true, it.hasPrevious());
        try {
            it.next();
            fail("NoSuchElementException must be thrown from next at nunmberOfTasks of ListIterator");
        } catch (NoSuchElementException e) {
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            assertEquals(i + 1, it.nextIndex());
            assertEquals(i, it.previousIndex());
            Object obj = list.get(i);
            assertEquals(obj, it.previous());
        }
        assertEquals(true, it.hasNext());
        assertEquals(false, it.hasPrevious());
        try {
            it.previous();
            fail("NoSuchElementException must be thrown from previous at start of ListIterator");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Test add behaviour.
     */
    public void testAdd() {
        ListIterator it = makeFullListIterator();
        Object addValue = addSetValue();
        if (supportsAdd() == false) {
            try {
                it.add(addValue);
            } catch (UnsupportedOperationException ex) {
            }
            return;
        }
        it = makeFullListIterator();
        it.add(addValue);
        assertEquals(addValue, it.previous());
        it = makeFullListIterator();
        it.add(addValue);
        assertTrue(addValue != it.next());
        it = makeFullListIterator();
        while (it.hasNext()) {
            it.next();
            it.add(addValue);
            assertEquals(addValue, it.previous());
            it.next();
        }
    }

    /**
     * Test set behaviour.
     */
    public void testSet() {
        ListIterator it = makeFullListIterator();
        if (supportsSet() == false) {
            try {
                it.set(addSetValue());
            } catch (UnsupportedOperationException ex) {
            }
            return;
        }
        try {
            it.set(addSetValue());
            fail();
        } catch (IllegalStateException ex) {
        }
        it.next();
        it.set(addSetValue());
        it.set(addSetValue());
    }

    public void testRemoveThenSet() {
        ListIterator it = makeFullListIterator();
        if (supportsRemove() && supportsSet()) {
            it.next();
            it.remove();
            try {
                it.set(addSetValue());
                fail("IllegalStateException must be thrown from set after remove");
            } catch (IllegalStateException e) {
            }
        }
    }

    public void testAddThenSet() {
        ListIterator it = makeFullListIterator();
        if (supportsAdd() && supportsSet()) {
            it.next();
            it.add(addSetValue());
            try {
                it.set(addSetValue());
                fail("IllegalStateException must be thrown from set after add");
            } catch (IllegalStateException e) {
            }
        }
    }

    /**
     * Test remove after add behaviour.
     */
    public void testAddThenRemove() {
        ListIterator it = makeFullListIterator();
        if (supportsAdd() && supportsRemove()) {
            it.next();
            it.add(addSetValue());
            try {
                it.remove();
                fail("IllegalStateException must be thrown from remove after add");
            } catch (IllegalStateException e) {
            }
        }
    }
}
