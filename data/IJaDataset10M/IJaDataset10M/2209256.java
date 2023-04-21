package com.notuvy.test;

import org.junit.Assert;
import java.util.Iterator;

/**
 * A unit test utility for asserting equality for a whole list of items.
 * It also asserts that the lists are the same size.
 *
 * NOTE:  This can be instantiated with any Iterable collection, but not all of them
 * will work properly.  It is necessary to have a predictable iteration order so that
 * expected and actual items are comparable.
 *
 * For example, a SortedSet will work, but a HashSet will not.
 *
 * @author murali
 */
public class OrderedItemsAsserter<T> {

    private final Iterator<T> fIterator;

    public OrderedItemsAsserter(Iterable<T> pList) {
        this(pList.iterator());
    }

    public OrderedItemsAsserter(Iterator<T> pIterator) {
        fIterator = pIterator;
    }

    private void next(T pItem) {
        Assert.assertTrue(String.format("Actuals exhausted when expecting [%s].", pItem), fIterator.hasNext());
        Assert.assertEquals(pItem, fIterator.next());
    }

    private void done() {
        Assert.assertFalse("Actuals not exhausted as expected.", fIterator.hasNext());
    }

    public void assertAll(Iterable<T> pExpected) {
        for (T item : pExpected) {
            next(item);
        }
        done();
    }

    public void assertAll(T... items) {
        for (T item : items) {
            next(item);
        }
        done();
    }
}
