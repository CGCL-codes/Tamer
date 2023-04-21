package net.sf.collections15.collection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import net.sf.collections15.AbstractTestObject;

/**
 * Abstract test class for {@link java.util.Collection} methods and contracts.
 * <p>
 * You should create a concrete subclass of this class to test any custom
 * {@link Collection} implementation.  At minimum, you'll have to 
 * implement the {@link #makeCollection()} method.  You might want to 
 * override some of the additional public methods as well:
 * <p>
 * <b>Element Population Methods</b>
 * <p>
 * Override these if your collection restricts what kind of elements are
 * allowed (for instance, if <code>null</code> is not permitted):
 * <ul>
 * <li>{@link #getFullElements()}
 * <li>{@link #getOtherElements()}
 * </ul>
 * <p>
 * <b>Supported Operation Methods</b>
 * <p>
 * Override these if your collection doesn't support certain operations:
 * <ul>
 * <li>{@link #isAddSupported()}
 * <li>{@link #isRemoveSupported()}
 * <li>{@link #areEqualElementsDistinguishable()}
 * <li>{@link #isNullSupported()}
 * <li>{@link #isFailFastSupported()}
 * </ul>
 * <p>
 * <b>Fixture Methods</b>
 * <p>
 * Fixtures are used to verify that the the operation results in correct state
 * for the collection.  Basically, the operation is performed against your
 * collection implementation, and an identical operation is performed against a
 * <i>confirmed</i> collection implementation.  A confirmed collection
 * implementation is something like <code>java.util.ArrayList</code>, which is
 * known to conform exactly to its collection interface's contract.  After the
 * operation takes place on both your collection implementation and the
 * confirmed collection implementation, the two collections are compared to see
 * if their state is identical.  The comparison is usually much more involved
 * than a simple <code>equals</code> test.  This verification is used to ensure
 * proper modifications are made along with ensuring that the collection does
 * not change when read-only modifications are made.
 * <p>
 * The {@link #collection} field holds an instance of your collection
 * implementation; the {@link #confirmed} field holds an instance of the
 * confirmed collection implementation.  The {@link #resetEmpty()} and 
 * {@link #resetFull()} methods set these fields to empty or full collections,
 * so that tests can proceed from a known state.
 * <p>
 * After a modification operation to both {@link #collection} and
 * {@link #confirmed}, the {@link #verify()} method is invoked to compare
 * the results.  You may want to override {@link #verify()} to perform
 * additional verifications.  For instance, when testing the collection
 * views of a map, {@link AbstractTestMap} would override {@link #verify()} to make
 * sure the map is changed after the collection view is changed.
 * <p>
 * If you're extending this class directly, you will have to provide 
 * implementations for the following:
 * <ul>
 * <li>{@link #makeConfirmedCollection()}
 * <li>{@link #makeConfirmedFullCollection()}
 * </ul>
 * <p>
 * Those methods should provide a confirmed collection implementation 
 * that's compatible with your collection implementation.
 * <p>
 * If you're extending {@link AbstractTestList}, {@link AbstractTestSet},
 * or {@link AbstractTestBag}, you probably don't have to worry about the
 * above methods, because those three classes already override the methods
 * to provide standard JDK confirmed collections.<P>
 * <p>
 * <b>Other notes</b>
 * <p>
 * If your {@link Collection} fails one of these tests by design,
 * you may still use this base set of cases.  Simply override the
 * test case (method) your {@link Collection} fails.
 *
 * @version $Revision: 1.1 $ $Date: 2005/05/03 22:45:38 $
 * 
 * @author Rodney Waldhoff
 * @author Paul Jack
 * @author Michael A. Smith
 * @author Neil O'Toole
 * @author Stephen Colebourne
 * @author Laurent Brucher (port to Java 5.0)
 */
public abstract class AbstractTestCollection extends AbstractTestObject<Collection<Number>> {

    /** 
     *  A collection instance that will be used for testing.
     */
    public Collection<Number> collection;

    /** 
     *  Confirmed collection.  This is an instance of a collection that is
     *  confirmed to conform exactly to the java.util.Collection contract.
     *  Modification operations are tested by performing a mod on your 
     *  collection, performing the exact same mod on an equivalent confirmed
     *  collection, and then calling verify() to make sure your collection
     *  still matches the confirmed collection.
     */
    public Collection<Number> confirmed;

    /**
     * JUnit constructor.
     * 
     * @param testName  the test class name
     */
    public AbstractTestCollection(String testName) {
        super(testName);
    }

    /**
     *  Specifies whether equal elements in the collection are, in fact,
     *  distinguishable with information not readily available.  That is, if a
     *  particular value is to be removed from the collection, then there is
     *  one and only one value that can be removed, even if there are other
     *  elements which are equal to it.  
     *
     *  <P>In most collection cases, elements are not distinguishable (equal is
     *  equal), thus this method defaults to return false.  In some cases,
     *  however, they are.  For example, the collection returned from the map's
     *  values() collection view are backed by the map, so while there may be
     *  two values that are equal, their associated keys are not.  Since the
     *  keys are distinguishable, the values are.
     *
     *  <P>This flag is used to skip some verifications for iterator.remove()
     *  where it is impossible to perform an equivalent modification on the
     *  confirmed collection because it is not possible to determine which
     *  value in the confirmed collection to actually remove.  Tests that
     *  override the default (i.e. where equal elements are distinguishable),
     *  should provide additional tests on iterator.remove() to make sure the
     *  proper elements are removed when remove() is called on the iterator.
     **/
    public boolean areEqualElementsDistinguishable() {
        return false;
    }

    /**
     *  Returns true if the collections produced by 
     *  {@link #makeCollection()} and {@link #makeFullCollection()}
     *  support the <code>add</code> and <code>addAll</code>
     *  operations.<P>
     *  Default implementation returns true.  Override if your collection
     *  class does not support add or addAll.
     */
    public boolean isAddSupported() {
        return true;
    }

    /**
     *  Returns true if the collections produced by 
     *  {@link #makeCollection()} and {@link #makeFullCollection()}
     *  support the <code>remove</code>, <code>removeAll</code>,
     *  <code>retainAll</code>, <code>clear</code> and
     *  <code>iterator().remove()</code> methods.
     *  Default implementation returns true.  Override if your collection
     *  class does not support removal operations.
     */
    public boolean isRemoveSupported() {
        return true;
    }

    /**
     * Returns true to indicate that the collection supports holding null.
     * The default implementation returns true;
     */
    public boolean isNullSupported() {
        return true;
    }

    /**
     * Returns true to indicate that the collection supports fail fast iterators.
     * The default implementation returns true;
     */
    public boolean isFailFastSupported() {
        return false;
    }

    /**
     * Returns true to indicate that the collection supports equals() comparisons.
     * This implementation returns false;
     */
    public boolean isEqualsCheckable() {
        return false;
    }

    /**
     *  Verifies that {@link #collection} and {@link #confirmed} have 
     *  identical state.
     */
    public void verify() {
        int confirmedSize = confirmed.size();
        assertEquals("Collection size should match confirmed collection's", confirmedSize, collection.size());
        assertEquals("Collection isEmpty() result should match confirmed " + " collection's", confirmed.isEmpty(), collection.isEmpty());
        Object[] confirmedValues = new Object[confirmedSize];
        Iterator<Number> iter;
        iter = confirmed.iterator();
        int pos = 0;
        while (iter.hasNext()) {
            confirmedValues[pos++] = iter.next();
        }
        boolean[] matched = new boolean[confirmedSize];
        iter = collection.iterator();
        while (iter.hasNext()) {
            Number o = iter.next();
            boolean match = false;
            for (int i = 0; i < confirmedSize; i++) {
                if (matched[i]) {
                    continue;
                }
                if (o == confirmedValues[i] || (o != null && o.equals(confirmedValues[i]))) {
                    matched[i] = true;
                    match = true;
                    break;
                }
            }
            if (!match) {
                fail("Collection should not contain a value that the " + "confirmed collection does not have: " + o + "\nTest: " + collection + "\nReal: " + confirmed);
            }
        }
        for (int i = 0; i < confirmedSize; i++) {
            if (!matched[i]) {
                fail("Collection should contain all values that are in the confirmed collection" + "\nTest: " + collection + "\nReal: " + confirmed);
            }
        }
    }

    /**
     *  Resets the {@link #collection} and {@link #confirmed} fields to empty
     *  collections.  Invoke this method before performing a modification
     *  test.
     */
    public void resetEmpty() {
        this.collection = makeCollection();
        this.confirmed = makeConfirmedCollection();
    }

    /**
     *  Resets the {@link #collection} and {@link #confirmed} fields to full
     *  collections.  Invoke this method before performing a modification
     *  test.
     */
    public void resetFull() {
        this.collection = makeFullCollection();
        this.confirmed = makeConfirmedFullCollection();
    }

    /**
     *  Returns a confirmed empty collection.
     *  For instance, an {@link java.util.ArrayList} for lists or a
     *  {@link java.util.HashSet} for sets.
     *
     *  @return a confirmed empty collection
     */
    public abstract Collection<Number> makeConfirmedCollection();

    /**
     *  Returns a confirmed full collection.
     *  For instance, an {@link java.util.ArrayList} for lists or a
     *  {@link java.util.HashSet} for sets.  The returned collection
     *  should contain the elements returned by {@link #getFullElements()}.
     *
     *  @return a confirmed full collection
     */
    public abstract Collection<Number> makeConfirmedFullCollection();

    /**
     * Return a new, empty {@link Collection} to be used for testing.
     */
    public abstract Collection<Number> makeCollection();

    /**
     *  Returns a full collection to be used for testing.  The collection
     *  returned by this method should contain every element returned by
     *  {@link #getFullElements()}.  The default implementation, in fact,
     *  simply invokes <code>addAll</code> on an empty collection with
     *  the results of {@link #getFullElements()}.  Override this default
     *  if your collection doesn't support addAll.
     */
    public Collection<Number> makeFullCollection() {
        Collection<Number> c = makeCollection();
        c.addAll(Arrays.asList(getFullElements()));
        return c;
    }

    /**
     *  Returns an empty collection for Object tests.
     */
    public Collection<Number> makeObject() {
        return makeCollection();
    }

    /**
     * Creates a new Map Entry that is independent of the first and the map.
     */
    public Map.Entry cloneMapEntry(Map.Entry entry) {
        HashMap map = new HashMap();
        map.put(entry.getKey(), entry.getValue());
        return (Map.Entry) map.entrySet().iterator().next();
    }

    /**
     *  Returns an array of objects that are contained in a collection
     *  produced by {@link #makeFullCollection()}.  Every element in the
     *  returned array <I>must</I> be an element in a full collection.<P>
     *  The default implementation returns a heterogenous array of 
     *  objects with some duplicates. null is added if allowed.
     *  Override if you require specific testing elements.  Note that if you
     *  override {@link #makeFullCollection()}, you <I>must</I> override
     *  this method to reflect the contents of a full collection.
     */
    public Number[] getFullElements() {
        if (isNullSupported()) {
            ArrayList<Number> list = new ArrayList<Number>();
            list.addAll(Arrays.asList(getFullNonNullElements()));
            list.add(4, null);
            return list.<Number>toArray(getFullNonNullElements().clone());
        } else {
            return getFullNonNullElements().clone();
        }
    }

    /**
     *  Returns an array of elements that are <I>not</I> contained in a
     *  full collection.  Every element in the returned array must 
     *  not exist in a collection returned by {@link #makeFullCollection()}.
     *  The default implementation returns a heterogenous array of elements
     *  without null.  Note that some of the tests add these elements
     *  to an empty or full collection, so if your collection restricts
     *  certain kinds of elements, you should override this method.
     */
    public Number[] getOtherElements() {
        return getOtherNonNullElements();
    }

    /**
     *  Returns an array of elements that are <I>not</I> contained in a
     *  full collection and that have a type different than Number.
     *  Every element in the returned array must not exist in a collection
     *  returned by {@link #makeFullCollection()}.
     *  The default implementation returns a heterogenous array of elements
     *  without null.
     */
    public Object[] getObjectElements() {
        return getObjectNonNullElements();
    }

    /**
     *  Returns an array of elements that are <I>not</I> contained in a
     *  full collection and that are all of type Integer.
     *  Every element in the returned array must not exist in a collection
     *  returned by {@link #makeFullCollection()}.
     *  The default implementation returns an array of Integer elements
     *  without null.
     */
    public Integer[] getIntegerElements() {
        return getIntegerNonNullElements();
    }

    /**
	 *  Returns a list of elements suitable for return by
	 *  {@link #getFullElements()}.  The array returned by this method
	 *  does not include null, but does include a variety of objects 
	 *  of different types, but all subclassed from Number. 
	 *  Override getFullElements to return the results of this method
	 *  if your collection does not support the null element.
	 */
    public Number[] getFullNonNullElements() {
        return new Number[] { 0, Byte.valueOf((byte) 1), new Integer(2), 3, new Integer(4), Float.valueOf(1.45F), new Double(5), new Float(6), Double.valueOf(7.112), 8, BigInteger.valueOf(9), new Integer(10), new Short((short) 11), new Long(12), BigDecimal.valueOf(13), 14, 15, new Byte((byte) 16) };
    }

    /**
     *  Returns the default list of objects returned by 
     *  {@link #getOtherElements()}.  Includes many objects
     *  of different types but all derived from Number.
     */
    public Number[] getOtherNonNullElements() {
        return new Number[] { new Integer(100), new Float(100), new Double(100), 101, new Short((short) 102), new Byte((byte) 103), new Long(10005), Float.valueOf(105.678F), Double.valueOf(690.376487) };
    }

    /**
     *  Returns the default list of objects returned by 
     *  {@link #getObjectElements()}.  Includes many objects
     *  of various types, all different than Number.
     */
    public Object[] getObjectNonNullElements() {
        return new Object[] { "hello", new String("from"), new Character(' '), new Date(), Locale.ENGLISH };
    }

    /**
     *  Returns the default list of integers returned by 
     *  {@link #getIntegerElements()}. Includes many objects
     *  of Integer type only.
     */
    public Integer[] getIntegerNonNullElements() {
        return new Integer[] { new Integer(200), new Integer(202), new Integer(210000), new Integer(2030021), new Integer(230), new Integer(23104731) };
    }

    /**
     *  Tests {@link Collection#add(Object)}.
     */
    public void testCollectionAdd() {
        if (!isAddSupported()) return;
        Number[] elements = getFullElements();
        for (int i = 0; i < elements.length; i++) {
            resetEmpty();
            boolean r = collection.add(elements[i]);
            confirmed.add(elements[i]);
            verify();
            assertTrue("Empty collection changed after add", r);
            assertEquals("Collection size is 1 after first add", 1, collection.size());
        }
        resetEmpty();
        int size = 0;
        for (int i = 0; i < elements.length; i++) {
            boolean r = collection.add(elements[i]);
            confirmed.add(elements[i]);
            verify();
            if (r) size++;
            assertEquals("Collection size should grow after add", size, collection.size());
            assertTrue("Collection should contain added element", collection.contains(elements[i]));
        }
    }

    /**
     *  Tests {@link Collection#addAll(Collection)}.
     */
    public void testCollectionAddAll() {
        if (!isAddSupported()) return;
        resetEmpty();
        Number[] elements = getFullElements();
        boolean r = collection.addAll(Arrays.<Number>asList(elements));
        confirmed.addAll(Arrays.<Number>asList(elements));
        verify();
        assertTrue("Empty collection should change after addAll", r);
        for (int i = 0; i < elements.length; i++) {
            assertTrue("Collection should contain added element", collection.contains(elements[i]));
        }
        resetFull();
        int size = collection.size();
        elements = getOtherElements();
        r = collection.addAll(Arrays.<Number>asList(elements));
        confirmed.addAll(Arrays.<Number>asList(elements));
        verify();
        assertTrue("Full collection should change after addAll", r);
        for (int i = 0; i < elements.length; i++) {
            assertTrue("Full collection should contain added element", collection.contains(elements[i]));
        }
        assertEquals("Size should increase after addAll", size + elements.length, collection.size());
        resetFull();
        size = collection.size();
        r = collection.addAll(Arrays.<Number>asList(getFullElements()));
        confirmed.addAll(Arrays.<Number>asList(getFullElements()));
        verify();
        if (r) {
            assertTrue("Size should increase if addAll returns true", size < collection.size());
        } else {
            assertEquals("Size should not change if addAll returns false", size, collection.size());
        }
        resetEmpty();
        Integer[] intElements = getIntegerElements();
        r = collection.addAll(Arrays.<Integer>asList(intElements));
        confirmed.addAll(Arrays.<Integer>asList(intElements));
        verify();
        assertTrue("Empty collection should change after addAll", r);
        for (int i = 0; i < intElements.length; i++) {
            assertTrue("Collection should contain added element", collection.contains(intElements[i]));
        }
    }

    /**
     *  If {@link #isAddSupported()} returns false, tests that add operations
     *  raise <code>UnsupportedOperationException.
     */
    public void testUnsupportedAdd() {
        if (isAddSupported()) return;
        resetEmpty();
        try {
            collection.add(getFullElements()[0]);
            fail("Emtpy collection should not support add.");
        } catch (UnsupportedOperationException e) {
        }
        verify();
        try {
            collection.addAll(Arrays.<Number>asList(getFullElements()));
            fail("Emtpy collection should not support addAll.");
        } catch (UnsupportedOperationException e) {
        }
        verify();
        resetFull();
        try {
            collection.add(getOtherElements()[0]);
            fail("Full collection should not support add.");
        } catch (UnsupportedOperationException e) {
        }
        verify();
        try {
            collection.addAll(Arrays.<Number>asList(getOtherElements()));
            fail("Full collection should not support addAll.");
        } catch (UnsupportedOperationException e) {
        }
        verify();
    }

    /**
     *  Test {@link Collection#clear()}.
     */
    public void testCollectionClear() {
        if (!isRemoveSupported()) return;
        resetEmpty();
        collection.clear();
        verify();
        resetFull();
        collection.clear();
        confirmed.clear();
        verify();
    }

    /**
     *  Tests {@link Collection#contains(Object)}.
     */
    public void testCollectionContains() {
        Number[] elements;
        resetEmpty();
        elements = getFullElements();
        for (int i = 0; i < elements.length; i++) {
            assertTrue("Empty collection shouldn't contain element[" + i + "]", !collection.contains(elements[i]));
        }
        verify();
        elements = getOtherElements();
        for (int i = 0; i < elements.length; i++) {
            assertTrue("Empty collection shouldn't contain element[" + i + "]", !collection.contains(elements[i]));
        }
        verify();
        Object[] objElements = getObjectElements();
        for (int i = 0; i < objElements.length; i++) {
            assertTrue("Empty collection shouldn't contain element[" + i + "]", !collection.contains(objElements[i]));
        }
        verify();
        resetFull();
        elements = getFullElements();
        for (int i = 0; i < elements.length; i++) {
            assertTrue("Full collection should contain element[" + i + "]", collection.contains(elements[i]));
        }
        verify();
        resetFull();
        elements = getOtherElements();
        for (int i = 0; i < elements.length; i++) {
            assertTrue("Full collection shouldn't contain element[" + i + "]", !collection.contains(elements[i]));
        }
        resetFull();
        objElements = getObjectElements();
        for (int i = 0; i < objElements.length; i++) {
            assertTrue("Full collection shouldn't contain element[" + i + "]", !collection.contains(objElements[i]));
        }
    }

    /**
     *  Tests {@link Collection#containsAll(Collection)}.
     */
    public void testCollectionContainsAll() {
        resetEmpty();
        Collection<Number> col = new HashSet<Number>();
        assertTrue("Every Collection should contain all elements of an " + "empty Collection.", collection.containsAll(col));
        col.addAll(Arrays.<Number>asList(getOtherElements()));
        assertTrue("Empty Collection shouldn't contain all elements of " + "a non-empty Collection.", !collection.containsAll(col));
        assertTrue("Empty Collection shouldn't contain all elements of " + "a non-empty Integer Collection.", !collection.containsAll(Arrays.<Integer>asList(getIntegerElements())));
        assertTrue("Empty Collection shouldn't contain all elements of " + "a non-empty Object Collection.", !collection.containsAll(Arrays.<Object>asList(getObjectElements())));
        verify();
        resetFull();
        assertTrue("Full collection shouldn't contain other elements", !collection.containsAll(col));
        assertTrue("Full collection shouldn't contain other Integer elements", !collection.containsAll(Arrays.<Integer>asList(getIntegerElements())));
        assertTrue("Full collection shouldn't contain other Object elements", !collection.containsAll(Arrays.<Object>asList(getObjectElements())));
        col.clear();
        col.addAll(Arrays.<Number>asList(getFullElements()));
        assertTrue("Full collection should containAll full elements", collection.containsAll(col));
        verify();
        int min = (getFullElements().length < 2 ? 0 : 2);
        int max = (getFullElements().length == 1 ? 1 : (getFullElements().length <= 5 ? getFullElements().length - 1 : 5));
        col = Arrays.<Number>asList(getFullElements()).subList(min, max);
        assertTrue("Full collection should containAll partial full " + "elements", collection.containsAll(col));
        assertTrue("Full collection should containAll itself", collection.containsAll(collection));
        verify();
        col = new ArrayList<Number>();
        col.addAll(Arrays.<Number>asList(getFullElements()));
        col.addAll(Arrays.<Number>asList(getFullElements()));
        assertTrue("Full collection should containAll duplicate full " + "elements", collection.containsAll(col));
        verify();
    }

    /**
     *  Tests {@link Collection#isEmpty()}.
     */
    public void testCollectionIsEmpty() {
        resetEmpty();
        assertEquals("New Collection should be empty.", true, collection.isEmpty());
        verify();
        resetFull();
        assertEquals("Full collection shouldn't be empty", false, collection.isEmpty());
        verify();
    }

    /**
     *  Tests the read-only functionality of {@link Collection#iterator()}.
     */
    public void testCollectionIterator() {
        resetEmpty();
        Iterator<Number> it1 = collection.iterator();
        assertEquals("Iterator for empty Collection shouldn't have next.", false, it1.hasNext());
        try {
            it1.next();
            fail("Iterator at end of Collection should throw " + "NoSuchElementException when next is called.");
        } catch (NoSuchElementException e) {
        }
        verify();
        resetFull();
        it1 = collection.iterator();
        for (int i = 0; i < collection.size(); i++) {
            assertTrue("Iterator for full collection should haveNext", it1.hasNext());
            it1.next();
        }
        assertTrue("Iterator should be finished", !it1.hasNext());
        ArrayList<Number> list = new ArrayList<Number>();
        it1 = collection.iterator();
        for (int i = 0; i < collection.size(); i++) {
            Number next = it1.next();
            assertTrue("Collection should contain element returned by " + "its iterator", collection.contains(next));
            list.add(next);
        }
        try {
            it1.next();
            fail("iterator.next() should raise NoSuchElementException " + "after it finishes");
        } catch (NoSuchElementException e) {
        }
        verify();
    }

    /**
     *  Tests removals from {@link Collection#iterator()}.
     */
    public void testCollectionIteratorRemove() {
        if (!isRemoveSupported()) return;
        resetEmpty();
        try {
            collection.iterator().remove();
            fail("New iterator.remove should raise IllegalState");
        } catch (IllegalStateException e) {
        }
        verify();
        try {
            Iterator<Number> iter = collection.iterator();
            iter.hasNext();
            iter.remove();
            fail("New iterator.remove should raise IllegalState " + "even after hasNext");
        } catch (IllegalStateException e) {
        }
        verify();
        resetFull();
        int size = collection.size();
        Iterator<Number> iter = collection.iterator();
        while (iter.hasNext()) {
            Number o = iter.next();
            if (o instanceof Map.Entry) {
                o = (Number) cloneMapEntry((Map.Entry) o);
            }
            iter.remove();
            if (!areEqualElementsDistinguishable()) {
                confirmed.remove(o);
                verify();
            }
            size--;
            assertEquals("Collection should shrink by one after " + "iterator.remove", size, collection.size());
        }
        assertTrue("Collection should be empty after iterator purge", collection.isEmpty());
        resetFull();
        iter = collection.iterator();
        iter.next();
        iter.remove();
        try {
            iter.remove();
            fail("Second iter.remove should raise IllegalState");
        } catch (IllegalStateException e) {
        }
    }

    /**
     *  Tests {@link Collection#remove(Object)}.
     */
    public void testCollectionRemove() {
        if (!isRemoveSupported()) return;
        resetEmpty();
        Number[] elements = getFullElements();
        for (int i = 0; i < elements.length; i++) {
            assertTrue("Shouldn't remove nonexistent element", !collection.remove(elements[i]));
            verify();
        }
        Number[] other = getOtherElements();
        resetFull();
        for (int i = 0; i < other.length; i++) {
            assertTrue("Shouldn't remove nonexistent other element", !collection.remove(other[i]));
            verify();
        }
        Object[] otherObject = getObjectElements();
        for (int i = 0; i < otherObject.length; i++) {
            assertTrue("Shouldn't remove nonexistent other Object element", !collection.remove(otherObject[i]));
            verify();
        }
        int size = collection.size();
        for (int i = 0; i < elements.length; i++) {
            resetFull();
            assertTrue("Collection should remove existent element: " + elements[i], collection.remove(elements[i]));
            if (!areEqualElementsDistinguishable()) {
                confirmed.remove(elements[i]);
                verify();
            }
            assertEquals("Collection should shrink after remove", size - 1, collection.size());
        }
    }

    /**
     *  Tests {@link Collection#removeAll(Collection)}.
     */
    public void testCollectionRemoveAll() {
        if (!isRemoveSupported()) return;
        resetEmpty();
        assertTrue("Emtpy collection removeAll should return false for " + "empty input", !collection.removeAll(Collections.EMPTY_SET));
        verify();
        assertTrue("Emtpy collection removeAll should return false for " + "nonempty input", !collection.removeAll(new ArrayList<Number>(collection)));
        verify();
        resetFull();
        assertTrue("Full collection removeAll should return false for " + "empty input", !collection.removeAll(Collections.EMPTY_SET));
        verify();
        assertTrue("Full collection removeAll should return false for other elements", !collection.removeAll(Arrays.<Number>asList(getOtherElements())));
        verify();
        assertTrue("Full collection removeAll should return false for other Object elements", !collection.removeAll(Arrays.<Object>asList(getObjectElements())));
        verify();
        assertTrue("Full collection removeAll should return true for full elements", collection.removeAll(new HashSet<Number>(collection)));
        confirmed.removeAll(new HashSet<Number>(confirmed));
        verify();
        resetFull();
        int size = collection.size();
        int min = (getFullElements().length < 2 ? 0 : 2);
        int max = (getFullElements().length == 1 ? 1 : (getFullElements().length <= 5 ? getFullElements().length - 1 : 5));
        Collection<Number> all = Arrays.<Number>asList(getFullElements()).subList(min, max);
        assertTrue("Full collection removeAll should work", collection.removeAll(all));
        confirmed.removeAll(all);
        verify();
        assertTrue("Collection should shrink after removeAll", collection.size() < size);
        Iterator<Number> iter = all.iterator();
        while (iter.hasNext()) {
            assertTrue("Collection shouldn't contain removed element", !collection.contains(iter.next()));
        }
    }

    /**
     *  Tests {@link Collection#retainAll(Collection)}.
     */
    public void testCollectionRetainAll() {
        if (!isRemoveSupported()) return;
        resetEmpty();
        List<Number> elements = Arrays.<Number>asList(getFullElements());
        List<Number> other = Arrays.<Number>asList(getOtherElements());
        List<Object> otherObjects = Arrays.<Object>asList(getObjectElements());
        assertTrue("Empty retainAll() should return false", !collection.retainAll(Collections.EMPTY_SET));
        verify();
        assertTrue("Empty retainAll() should return false", !collection.retainAll(elements));
        verify();
        resetFull();
        assertTrue("Collection should change from retainAll empty", collection.retainAll(Collections.EMPTY_SET));
        confirmed.retainAll(Collections.EMPTY_SET);
        verify();
        resetFull();
        assertTrue("Collection changed from retainAll other", collection.retainAll(other));
        confirmed.retainAll(other);
        verify();
        resetFull();
        assertTrue("Collection changed from retainAll other Object", collection.retainAll(otherObjects));
        confirmed.retainAll(otherObjects);
        verify();
        resetFull();
        int size = collection.size();
        assertTrue("Collection shouldn't change from retainAll elements", !collection.retainAll(elements));
        verify();
        assertEquals("Collection size shouldn't change", size, collection.size());
        if (getFullElements().length > 1) {
            resetFull();
            size = collection.size();
            int min = (getFullElements().length < 2 ? 0 : 2);
            int max = (getFullElements().length <= 5 ? getFullElements().length - 1 : 5);
            assertTrue("Collection should changed by partial retainAll", collection.retainAll(elements.subList(min, max)));
            confirmed.retainAll(elements.subList(min, max));
            verify();
            Iterator<Number> iter = collection.iterator();
            while (iter.hasNext()) {
                assertTrue("Collection only contains retained element", elements.subList(min, max).contains(iter.next()));
            }
        }
        resetFull();
        HashSet<Number> set = new HashSet<Number>(elements);
        size = collection.size();
        assertTrue("Collection shouldn't change from retainAll without " + "duplicate elements", !collection.retainAll(set));
        verify();
        assertEquals("Collection size didn't change from nonduplicate " + "retainAll", size, collection.size());
    }

    /**
     *  Tests {@link Collection#size()}.
     */
    public void testCollectionSize() {
        resetEmpty();
        assertEquals("Size of new Collection is 0.", 0, collection.size());
        resetFull();
        assertTrue("Size of full collection should be greater than zero", collection.size() > 0);
    }

    /**
     *  Tests {@link Collection#toArray()}.
     */
    public void testCollectionToArray() {
        resetEmpty();
        assertEquals("Empty Collection should return empty array for toArray", 0, collection.toArray().length);
        resetFull();
        Object[] array = collection.toArray();
        assertEquals("Full collection toArray should be same size as " + "collection", array.length, collection.size());
        Object[] confirmedArray = confirmed.toArray();
        assertEquals("length of array from confirmed collection should " + "match the length of the collection's array", confirmedArray.length, array.length);
        boolean[] matched = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            assertTrue("Collection should contain element in toArray", collection.contains(array[i]));
            boolean match = false;
            for (int j = 0; j < array.length; j++) {
                if (matched[j]) continue;
                if (array[i] == confirmedArray[j] || (array[i] != null && array[i].equals(confirmedArray[j]))) {
                    matched[j] = true;
                    match = true;
                    break;
                }
            }
            if (!match) {
                fail("element " + i + " in returned array should be found " + "in the confirmed collection's array");
            }
        }
        for (int i = 0; i < matched.length; i++) {
            assertEquals("Collection should return all its elements in " + "toArray", true, matched[i]);
        }
    }

    /**
     *  Tests {@link Collection#toArray(Object[])}.
     */
    public void testCollectionToArray2() {
        resetEmpty();
        Number[] a = new Number[] { new Integer(0), null, null };
        Number[] array = collection.toArray(a);
        Object[] objects;
        assertEquals("Given array shouldn't shrink", array, a);
        assertEquals("Last element should be set to null", a[0], null);
        verify();
        resetFull();
        try {
            collection.toArray(new Void[0]);
            fail("toArray(new Void[0]) should raise ArrayStore");
        } catch (ArrayStoreException e) {
        }
        verify();
        try {
            collection.toArray(null);
            fail("toArray(null) should raise NPE");
        } catch (NullPointerException e) {
        }
        verify();
        array = collection.toArray(new Number[0]);
        objects = collection.toArray();
        assertEquals("toArrays should be equal", Arrays.<Number>asList(array), Arrays.<Object>asList(objects));
        HashSet<Class> classes = new HashSet<Class>();
        for (int i = 0; i < array.length; i++) {
            classes.add((array[i] == null) ? null : array[i].getClass());
        }
        if (classes.size() > 1) return;
        Class cl = classes.iterator().next();
        if (Map.Entry.class.isAssignableFrom(cl)) {
            cl = Map.Entry.class;
        }
        a = (Number[]) Array.newInstance(cl, 0);
        array = collection.toArray(a);
        assertEquals("toArray(Object[]) should return correct array type", a.getClass(), array.getClass());
        assertEquals("type-specific toArrays should be equal", Arrays.<Number>asList(array), Arrays.<Object>asList(collection.toArray()));
        verify();
    }

    /**
     *  Tests <code>toString</code> on a collection.
     */
    public void testCollectionToString() {
        resetEmpty();
        assertTrue("toString shouldn't return null", collection.toString() != null);
        resetFull();
        assertTrue("toString shouldn't return null", collection.toString() != null);
    }

    /**
     *  If isRemoveSupported() returns false, tests to see that remove
     *  operations raise an UnsupportedOperationException.
     */
    public void testUnsupportedRemove() {
        if (isRemoveSupported()) return;
        resetEmpty();
        try {
            collection.clear();
            fail("clear should raise UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
        verify();
        try {
            collection.remove(null);
            fail("remove should raise UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
        verify();
        try {
            collection.removeAll(null);
            fail("removeAll should raise UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
        verify();
        try {
            collection.retainAll(null);
            fail("removeAll should raise UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
        verify();
        resetFull();
        try {
            Iterator<Number> iterator = collection.iterator();
            iterator.next();
            iterator.remove();
            fail("iterator.remove should raise UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
        verify();
    }

    /**
     *  Tests that the collection's iterator is fail-fast.  
     */
    public void testCollectionIteratorFailFast() {
        if (!isFailFastSupported()) return;
        if (isAddSupported()) {
            resetFull();
            try {
                Iterator<Number> iter = collection.iterator();
                Number o = getOtherElements()[0];
                collection.add(o);
                confirmed.add(o);
                iter.next();
                fail("next after add should raise ConcurrentModification");
            } catch (ConcurrentModificationException e) {
            }
            verify();
            resetFull();
            try {
                Iterator<Number> iter = collection.iterator();
                collection.addAll(Arrays.<Number>asList(getOtherElements()));
                confirmed.addAll(Arrays.<Number>asList(getOtherElements()));
                iter.next();
                fail("next after addAll should raise ConcurrentModification");
            } catch (ConcurrentModificationException e) {
            }
            verify();
        }
        if (!isRemoveSupported()) return;
        resetFull();
        try {
            Iterator<Number> iter = collection.iterator();
            collection.clear();
            iter.next();
            fail("next after clear should raise ConcurrentModification");
        } catch (ConcurrentModificationException e) {
        } catch (NoSuchElementException e) {
        }
        resetFull();
        try {
            Iterator<Number> iter = collection.iterator();
            collection.remove(getFullElements()[0]);
            iter.next();
            fail("next after remove should raise ConcurrentModification");
        } catch (ConcurrentModificationException e) {
        }
        resetFull();
        try {
            Iterator<Number> iter = collection.iterator();
            List<Number> sublist = Arrays.<Number>asList(getFullElements()).subList(2, 5);
            collection.removeAll(sublist);
            iter.next();
            fail("next after removeAll should raise ConcurrentModification");
        } catch (ConcurrentModificationException e) {
        }
        resetFull();
        try {
            Iterator<Number> iter = collection.iterator();
            List<Number> sublist = Arrays.<Number>asList(getFullElements()).subList(2, 5);
            collection.retainAll(sublist);
            iter.next();
            fail("next after retainAll should raise ConcurrentModification");
        } catch (ConcurrentModificationException e) {
        }
    }

    public void testSerializeDeserializeThenCompare() throws Exception {
        Object obj = makeCollection();
        if (obj instanceof Serializable && isTestSerialization()) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            out.writeObject(obj);
            out.close();
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
            Object dest = in.readObject();
            in.close();
            if (isEqualsCheckable()) {
                assertEquals("obj != deserialize(serialize(obj)) - EMPTY Collection", obj, dest);
            }
        }
        obj = makeFullCollection();
        if (obj instanceof Serializable && isTestSerialization()) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            out.writeObject(obj);
            out.close();
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
            Object dest = in.readObject();
            in.close();
            if (isEqualsCheckable()) {
                assertEquals("obj != deserialize(serialize(obj)) - FULL Collection", obj, dest);
            }
        }
    }
}
