package net.infonode.util.collection;

import java.util.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.3 $
 */
public final class CopyOnWriteArrayList {

    private static class IteratorImpl implements Iterator {

        private Object[] e;

        private int size;

        private int index;

        IteratorImpl(Object[] e, int size, int index) {
            this.e = e;
            this.size = size;
            this.index = index;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            return index < size;
        }

        public Object next() {
            return e[index++];
        }
    }

    private Object[] elements;

    private int size;

    public CopyOnWriteArrayList(int initialCapacity) {
        elements = new Object[initialCapacity];
    }

    public void add(Object element) {
        if (size >= elements.length) {
            Object[] newElements = new Object[getPreferredSize(size)];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
        elements[size++] = element;
    }

    public void remove(int index) {
        size--;
        Object[] newElements = new Object[getPreferredSize(size)];
        System.arraycopy(elements, 0, newElements, 0, index);
        System.arraycopy(elements, index + 1, newElements, index, size - index);
        elements = newElements;
    }

    public Iterator iterator() {
        return new IteratorImpl(elements, size, 0);
    }

    private static int getPreferredSize(int size) {
        return size * 3 / 2 + 1;
    }

    public int size() {
        return size;
    }

    public Object get(int index) {
        return elements[index];
    }

    public Object[] getElements() {
        return elements;
    }
}
