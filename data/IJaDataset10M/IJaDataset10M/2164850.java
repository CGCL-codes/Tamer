package org.joda.primitives.list.impl;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections.list.AbstractTestList;
import org.joda.primitives.list.IntList;
import org.joda.primitives.iterator.IntIterator;

/**
 * Abstract base class for testing AbstractIntList subclasses.
 *
 * @author Stephen Colebourne
 * @author Jason Tiscione
 * @version CODE GENERATED
 * @since 1.0
 */
public abstract class AbstractTestIntList extends AbstractTestList {

    public AbstractTestIntList(String name) {
        super(name);
    }

    /**
     * Override to indicate that clone is not supported for this object.
     */
    public boolean isCloneSupported() {
        return true;
    }

    public boolean isNullSupported() {
        return false;
    }

    public Integer[] getFullNonNullElements() {
        return new Integer[] { new Integer(2), new Integer(-2), new Integer(38), new Integer(0), new Integer(10000), new Integer(202), new Integer(Integer.MIN_VALUE), new Integer(Integer.MAX_VALUE) };
    }

    public Integer[] getOtherNonNullElements() {
        return new Integer[] { new Integer(-33), new Integer(66), new Integer(-99) };
    }

    public void testIsModifiable() {
        resetFull();
        IntList plist = (IntList) collection;
        assertEquals(isAddSupported() || isRemoveSupported() || isSetSupported(), plist.isModifiable());
    }

    public void testToValueArray() {
        resetFull();
        IntList plist = (IntList) collection;
        int[] values = plist.toIntArray();
        int i = 0;
        for (IntIterator it = plist.iterator(); it.hasNext(); i++) {
            int next = it.nextInt();
            assertEquals(values[i], next);
        }
    }

    public void testToValueArrayInsert() {
        resetFull();
        IntList plist = (IntList) collection;
        int[] array = new int[2];
        try {
            plist.toIntArray(array, -1);
            fail();
        } catch (IndexOutOfBoundsException ex) {
        }
        int[] values = plist.toIntArray();
        int[] result = plist.toIntArray(null, 1);
        assertEquals(0, result[0]);
        for (int i = 1; i < result.length; i++) {
            assertEquals(values[i - 1], result[i]);
        }
        array = new int[2];
        array[0] = 2;
        array[1] = 6;
        result = plist.toIntArray(array, 1);
        assertEquals(2, array[0]);
        assertEquals(6, array[1]);
        assertEquals(plist.size() + 1, result.length);
        assertEquals(2, result[0]);
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], result[i + 1]);
        }
        array = new int[values.length + 2];
        Arrays.fill(array, 2);
        result = plist.toIntArray(array, 1);
        assertSame(array, result);
        assertEquals(2, array[0]);
        assertEquals(2, array[array.length - 1]);
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], result[i + 1]);
        }
    }

    public void testRemoveRange() {
        if (isRemoveSupported() == false) {
            return;
        }
        resetFull();
        int size = collection.size();
        IntList plist = (IntList) collection;
        plist.removeRange(size - 4, size - 2);
        ((List<?>) confirmed).remove(size - 4);
        ((List<?>) confirmed).remove(size - 4);
        verify();
    }

    public void testContainsAllArray() {
        resetFull();
        IntList plist = (IntList) collection;
        assertEquals(true, plist.containsAll((int[]) null));
    }

    public void testAddAllArray() {
        if (isAddSupported() == false) {
            return;
        }
        resetFull();
        IntList plist = (IntList) collection;
        plist.addAll((int[]) null);
        verify();
    }

    public void testAddAllArrayIndexed() {
        if (isAddSupported() == false) {
            return;
        }
        resetFull();
        IntList plist = (IntList) collection;
        plist.addAll(0, (int[]) null);
        verify();
    }

    public void testRemoveAllArray() {
        if (isRemoveSupported() == false) {
            return;
        }
        resetFull();
        IntList plist = (IntList) collection;
        plist.removeAll((int[]) null);
        verify();
    }

    public void testRetainAllArray() {
        if (isRemoveSupported() == false) {
            return;
        }
        resetFull();
        IntList plist = (IntList) collection;
        plist.retainAll((int[]) null);
        confirmed.clear();
        verify();
    }

    public void testFirstInt_empty() {
        resetEmpty();
        IntList plist = (IntList) collection;
        try {
            plist.firstInt();
            fail();
        } catch (IndexOutOfBoundsException ex) {
        }
    }

    public void testFirstInt_notEmpty() {
        if (isAddSupported() == false) {
            return;
        }
        resetEmpty();
        IntList plist = (IntList) collection;
        plist.add(0);
        plist.add(6);
        assertEquals(0, plist.firstInt());
    }

    public void testLastInt_empty() {
        resetEmpty();
        IntList plist = (IntList) collection;
        try {
            plist.lastInt();
            fail();
        } catch (IndexOutOfBoundsException ex) {
        }
    }

    public void testLastInt_notEmpty() {
        if (isAddSupported() == false) {
            return;
        }
        resetEmpty();
        IntList plist = (IntList) collection;
        plist.add(0);
        plist.add(6);
        assertEquals(6, plist.lastInt());
    }

    public void testFirst_empty() {
        resetEmpty();
        IntList plist = (IntList) collection;
        assertNull(plist.first());
    }

    public void testFirst_notEmpty() {
        if (isAddSupported() == false) {
            return;
        }
        resetEmpty();
        IntList plist = (IntList) collection;
        plist.add(0);
        plist.add(6);
        assertEquals(new Integer(0), plist.first());
    }

    public void testLast_empty() {
        resetEmpty();
        IntList plist = (IntList) collection;
        assertNull(plist.last());
    }

    public void testLast_notEmpty() {
        if (isAddSupported() == false) {
            return;
        }
        resetEmpty();
        IntList plist = (IntList) collection;
        plist.add(0);
        plist.add(6);
        assertEquals(new Integer(6), plist.last());
    }

    public void testClone() {
        resetFull();
        IntList coll = (IntList) collection;
        if (isCloneSupported()) {
            IntList coll2 = (IntList) coll.clone();
            assertTrue(coll != coll2);
            assertEquals(coll, coll2);
        } else {
            try {
                coll.clone();
                fail();
            } catch (UnsupportedOperationException ex) {
            }
        }
    }
}
