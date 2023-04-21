package org.jaxlib.col.junit;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import jaxlib.util.AccessType;
import jaxlib.util.AccessTypeInfo;
import jaxlib.util.AccessTypeSet;
import org.jaxlib.junit.ObjectTestCase;

/**
 * Testcase for standalone iterators (iterators not backed by a collection).
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: IteratorTestCase.java 2730 2009-04-21 01:12:29Z joerg_wassmer $
 */
public abstract class IteratorTestCase extends ObjectTestCase {

    protected IteratorTestCase(String name) {
        super(name);
    }

    /**
   * Creates a new iterator to test. 
   *
   * @see #getExpectedIteratorSize()
   *
   * @since JaXLib 1.0
   */
    protected abstract Iterator createIterator();

    /**
   * Returns the number of <tt>next()</tt> elements the iterator returned by <tt>createIterator()</tt> should have, or <tt>-1</tt> if
   * no specific size is expected. If <tt>size >=0 </tt>, the tests will fail if the iterator has more elements available.
   * <p>
   * If the iterator is instance of <tt>ListIterator</tt>, the returned value has to be <tt> >= 0</tt>.
   * </p>
   *
   * @see #createIterator()
   *
   * @since JaXLib 1.0
   */
    protected abstract int getExpectedIteratorSize();

    final AccessTypeSet checkAccessTypes(Iterator it) {
        if (it instanceof AccessTypeInfo) {
            AccessTypeSet acc = ((AccessTypeInfo) it).accessTypes();
            assertNotNull("((AccessTypeInfo) acc).accessTypes()", acc);
            assertTrue("((AccessTypeInfo) acc).accessTypes().allows(AccessType.READ)", acc.allows(AccessType.READ));
            return acc;
        } else return null;
    }

    /**
   * Each time a testmethod requests <tt>getExpectedIteratorSize()</tt>, it should validate the result through this method.
   * This method reports a warning for <tt>expectedSize <= 0</tt>. 
   * For <tt>expectedSize < -1</tt> it throws an <tt>IllegalStateException</tt>.<br>
   *
   * @param expectedSize  the value returned by <tt>getExpectedIteratorSize()</tt>.
   *
   * @throws IllegalStateException if <tt>expectedSize < -1</tt>.
   *
   * @see #getExpectedIteratorSize()
   * @see #reportWarning(String)
   *
   * @since JaXLib 1.0
   */
    protected void checkExpectedSize(int expectedSize) {
        if (expectedSize < -1) throw new IllegalStateException("IteratorTestCase.getExpectedIteratorSize() returned a value < -1: " + expectedSize);
        if (expectedSize <= 0) reportWarning("IteratorTestCase.getExpectedIteratorSize() returned " + expectedSize + ".");
    }

    /**
   * @return <tt>createIterator()</tt>
   *
   * @see #createIterator()
   */
    protected final Object createObject() {
        Iterator it = createIterator();
        if (it == null) throw new IllegalStateException("createIterator() returned null.");
        checkAccessTypes(it);
        return it;
    }

    /**
   * Tests the iterator's <tt>hasNext()</tt> and <tt>next()</tt> methods.
   * If the iterator also implements the <tt>Enumeration</tt> interface, this method checks that <tt>hasNext()</tt> and
   * <tt>hasMoreElements()</tt> are always returning the same result.<br>
   * A class should never implement both interfaces giving them a different functionality.
   *
   * @see Iterator#hasNext()
   * @see Iterator#next()
   * @see Enumeration
   *
   * @since JaXLib 1.0
   */
    public void testNext() {
        Iterator it = createIterator();
        int expectedSize = getExpectedIteratorSize();
        checkExpectedSize(expectedSize);
        Enumeration enu = (it instanceof Enumeration) ? (Enumeration) it : null;
        int i = 0;
        while ((i++ != expectedSize) && (it.hasNext())) {
            if (enu != null) assertTrue("((Enumeration) it).hasMoreElements()", enu.hasMoreElements());
            it.next();
        }
        if (expectedSize != -1) assertEquals("(count of true for iterator.hasNext()) == expectedSize", expectedSize, i);
        assertEquals("iterator.hasNext()", it.hasNext(), false);
        if (enu != null) assertEquals("((Enumeration) it).hasMoreElements()", enu.hasMoreElements(), false);
        try {
            it.next();
            fail("expected NoSuchElementException for iterator.next(), but iterator has not thrown any exception.");
        } catch (final RuntimeException ex) {
            if (!(ex instanceof RuntimeException)) reportWarning("expected NoSuchElementException for iterator.next(), but iterator has thrown instance of " + ex.getClass().getName());
        }
    }

    /**
   * Tests the iterator's <tt>remove()</tt> method.
   * If the iterator implements the <tt>AccessTypeInfo</tt> interface, this method also tests if the <tt>accessTypes()</tt> are ok for
   * the remove method.
   *
   * @see Iterator#remove()
   * @see Iterator#hasNext()
   * @see Iterator#next()
   * @see AccessTypeInfo
   * @see AccessTypeInfo#accessTypes()
   *
   * @since JaXLib 1.0
   */
    public void testRemove() {
        Iterator it = createIterator();
        int expectedSize = getExpectedIteratorSize();
        checkExpectedSize(expectedSize);
        AccessTypeSet acc = checkAccessTypes(it);
        Enumeration enu = (it instanceof Enumeration) ? (Enumeration) it : null;
        try {
            it.remove();
            if ((acc != null) && (acc.denies(AccessType.REMOVE))) fail("iterator implements jaxlib.util.AccessTypeInfo and denies the REMOVE operation, but iterator has allowed it."); else fail("expected IllegalStateException for iterator.remove(), but iterator has not thrown any exception.");
        } catch (final NullPointerException ex) {
            throw ex;
        } catch (final IllegalStateException ex) {
        } catch (final UnsupportedOperationException ex) {
        } catch (final RuntimeException ex) {
            reportWarning("expected IllegalStateException or UnsupportedOperationException for iterator.remove(), but iterator has thrown instance of " + ex.getClass().getName());
        }
        try {
            int i = 0;
            while ((i++ != expectedSize) && (it.hasNext())) {
                if (enu != null) assertTrue("((Enumeration) it).hasMoreElements()", enu.hasMoreElements());
                it.next();
                it.remove();
                if ((acc != null) && (acc.denies(AccessType.REMOVE))) fail("iterator implements jaxlib.util.AccessTypeInfo and denies the REMOVE operation, but iterator has allowed it.");
            }
            if (expectedSize != -1) assertEquals("(count of true for iterator.hasNext()) == expectedSize", expectedSize, i);
        } catch (final NullPointerException ex) {
            throw ex;
        } catch (final UnsupportedOperationException ex) {
            return;
        }
        assertEquals("iterator.hasNext()", it.hasNext(), false);
        if (enu != null) assertEquals("((Enumeration) it).hasMoreElements()", enu.hasMoreElements(), false);
        try {
            it.next();
            fail("expected NoSuchElementException for iterator.next(), but iterator has not thrown any exception.");
        } catch (final RuntimeException ex) {
            if (!(ex instanceof NoSuchElementException)) reportWarning("expected NoSuchElementException for iterator.next(), but iterator has thrown instance of " + ex.getClass().getName());
        }
    }
}
