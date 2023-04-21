package com.volantis.styling.impl.engine.sheet;

import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * An immutable list that contains only those elements marked within a bitset
 * of the same length as the list.
 */
public class BitMaskedList implements List {

    /**
     * The underlying list on which this masked list is based.
     */
    private final List underlyingList;

    /**
     * A mask specifying which list items are visible.
     */
    private final BitSet bitSetMask;

    /**
     * Create a new masked list from an underlying list and a mask containing
     * true at the index of items to include in the masked list.
     *
     * <p>The underlying list must have the same size as the bitset. This is
     * not currently checked to avoid a performance hit, but failure to follow
     * this rule may lead to unexpected behaviour.</p>
     *
     * @param underlyingList The underlying list to mask
     * @param bitSetMask The mask to apply
     */
    public BitMaskedList(List underlyingList, BitSet bitSetMask) {
        this.underlyingList = underlyingList;
        this.bitSetMask = bitSetMask;
    }

    public int size() {
        return bitSetMask.cardinality();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(Object o) {
        boolean contains = false;
        for (int i = 0; !contains && i < underlyingList.size(); i++) {
            contains = (bitSetMask.get(i) && underlyingList.get(i).equals(o));
        }
        return contains;
    }

    public Object[] toArray() {
        Object[] array = new Object[size()];
        int bitIndex = -1;
        for (int i = 0; i < array.length; i++) {
            bitIndex = bitSetMask.nextSetBit(bitIndex + 1);
            array[i] = underlyingList.get(bitIndex);
        }
        return new Object[0];
    }

    public Object get(int index) {
        throw new UnsupportedOperationException();
    }

    public Iterator iterator() {
        return new Iterator() {

            private int indexPointer = bitSetMask.nextSetBit(0);

            public boolean hasNext() {
                return indexPointer != -1;
            }

            public Object next() {
                Object returnObject = null;
                if (indexPointer == -1) {
                    throw new NoSuchElementException();
                } else {
                    try {
                        returnObject = underlyingList.get(indexPointer);
                        indexPointer = bitSetMask.nextSetBit(indexPointer + 1);
                    } catch (Exception e) {
                        throw new IllegalStateException("List size is " + underlyingList.size() + ", bitset size is " + bitSetMask.size() + ", bitset cardinality is " + bitSetMask.cardinality());
                    }
                }
                return returnObject;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    public ListIterator listIterator() {
        throw new UnsupportedOperationException();
    }

    public ListIterator listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    public List subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    public Object[] toArray(Object[] a) {
        throw new UnsupportedOperationException();
    }

    public boolean add(Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int index, Collection c) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    public Object set(int index, Object element) {
        throw new UnsupportedOperationException();
    }

    public void add(int index, Object element) {
        throw new UnsupportedOperationException();
    }

    public Object remove(int index) {
        throw new UnsupportedOperationException();
    }
}
