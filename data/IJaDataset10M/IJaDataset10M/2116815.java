package com.od.jtimeseries.timeseries.impl;

import java.util.*;

/**
* Created by IntelliJ IDEA.
* User: nick
* Date: 01-Mar-2009
* Time: 14:32:41
* To change this template use File | Settings | File Templates.
*
* Reproduce SubList logic which has default access in AbstractList
*/
class ModCountSubList<E> extends AbstractList<E> implements RandomAccess, ModCountList<E> {

    private ModCountList<E> modCountList;

    private int offset;

    private int size;

    private long expectedModCount;

    ModCountSubList(ModCountList<E> list, int fromIndex, int toIndex) {
        if (fromIndex < 0) throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > list.size()) throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex) throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
        modCountList = list;
        offset = fromIndex;
        size = toIndex - fromIndex;
        expectedModCount = modCountList.getModCount();
    }

    public E set(int index, E element) {
        rangeCheck(index);
        checkForComodification();
        return modCountList.set(index + offset, element);
    }

    public E get(int index) {
        rangeCheck(index);
        checkForComodification();
        return modCountList.get(index + offset);
    }

    public int size() {
        checkForComodification();
        return size;
    }

    public void add(int index, E element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        checkForComodification();
        modCountList.add(index + offset, element);
        expectedModCount = modCountList.getModCount();
        size++;
        modCount++;
    }

    public E remove(int index) {
        rangeCheck(index);
        checkForComodification();
        E result = modCountList.remove(index + offset);
        expectedModCount = modCountList.getModCount();
        size--;
        modCount++;
        return result;
    }

    public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        int cSize = c.size();
        if (cSize == 0) return false;
        checkForComodification();
        modCountList.addAll(offset + index, c);
        expectedModCount = modCountList.getModCount();
        size += cSize;
        modCount++;
        return true;
    }

    public Iterator<E> iterator() {
        return listIterator();
    }

    public ListIterator<E> listIterator(final int index) {
        checkForComodification();
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        return new ListIterator<E>() {

            private ListIterator<E> i = modCountList.listIterator(index + offset);

            public boolean hasNext() {
                return nextIndex() < size;
            }

            public E next() {
                if (hasNext()) return i.next(); else throw new NoSuchElementException();
            }

            public boolean hasPrevious() {
                return previousIndex() >= 0;
            }

            public E previous() {
                if (hasPrevious()) return i.previous(); else throw new NoSuchElementException();
            }

            public int nextIndex() {
                return i.nextIndex() - offset;
            }

            public int previousIndex() {
                return i.previousIndex() - offset;
            }

            public void remove() {
                i.remove();
                expectedModCount = modCountList.getModCount();
                size--;
                modCount++;
            }

            public void set(E o) {
                i.set(o);
            }

            public void add(E o) {
                i.add(o);
                expectedModCount = modCountList.getModCount();
                size++;
                modCount++;
            }
        };
    }

    public List<E> subList(int fromIndex, int toIndex) {
        return new ModCountSubList<E>(this, fromIndex, toIndex);
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ",Size: " + size);
    }

    private void checkForComodification() {
        if (modCountList.getModCount() != expectedModCount) throw new ConcurrentModificationException();
    }

    public long getModCount() {
        return modCount;
    }
}
