package net.face2face.util;

import java.util.*;

/**
 * List that only allows a maximum number of item.
 * elements are pushed in the list in order eventually erasing the oldest element.
 * @author Patrice
 */
public class TimedCache<E> {

    private LinkedList<TimedElement> elements;

    private int maximumSize;

    private long lifeTime;

    /** 
     * Creates a new instance of TimeCache.
     * @param maximumSize the maximum number of element this might hold.
     * @param lifeTime the maximum time an object can be kept in cache (in ms).
     */
    public TimedCache(int maximumSize, long lifeTime) {
        this.maximumSize = maximumSize;
        this.lifeTime = lifeTime;
        elements = new LinkedList<TimedElement>();
    }

    /**
     * Adds an elemnt to the cache.
     */
    public void add(E element) {
        elements.addFirst(new TimedElement(element));
        if (elements.size() > maximumSize) elements.removeLast();
        long expiryTime = System.currentTimeMillis() - lifeTime;
        while (elements.getLast().cacheTime < expiryTime) {
            elements.removeLast();
        }
    }

    /**
     * Returns an Iterator of the elements in the cache.
     * Elements come from yougest to oldest.
     */
    public Iterator<E> iterator() {
        return new ElementIterator(elements.iterator());
    }

    /**
     * Checks cache emptyness.
     * @return true when empty.
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public int size() {
        return elements.size();
    }

    /**
     * Iterator of E from an Iteration of TimedElement
     */
    private class ElementIterator implements Iterator<E> {

        private Iterator<TimedElement> timedElements;

        public ElementIterator(Iterator<TimedElement> timedElements) {
            this.timedElements = timedElements;
        }

        public boolean hasNext() {
            return timedElements.hasNext();
        }

        public E next() {
            return timedElements.next().element;
        }

        public void remove() {
            timedElements.remove();
        }
    }

    /**
     * encapsulates time and element.
     */
    private class TimedElement {

        TimedElement(E element) {
            this.element = element;
            cacheTime = System.currentTimeMillis();
        }

        long cacheTime;

        E element;
    }
}
