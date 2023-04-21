package net.tasecurity.taslib.util;

import java.util.*;

/**
 * List isegmented in windows with notification to listener when 
 * a change from one window to another is needed.
 */
public class WindowedList<E> implements List<E> {

    public WindowedList(final WindowedList.Listener listener) {
    }

    public boolean add(final E e) {
        throw new UnsupportedOperationException("Immutable class");
    }

    public void add(int index, E element) {
        throw new UnsupportedOperationException("Immutable class");
    }

    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Immutable class");
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Immutable class");
    }

    public void clear() {
        throw new UnsupportedOperationException("Immutable class");
    }

    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Immutable class");
    }

    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Immutable class");
    }

    public boolean equals(Object o) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public E get(int index) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int indexOf(Object o) {
        throw new UnsupportedOperationException("Immutable class");
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public E remove(int index) {
        throw new UnsupportedOperationException("Immutable class");
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Immutable class");
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Immutable class");
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Immutable class");
    }

    public E set(int index, E element) {
        throw new UnsupportedOperationException("Immutable class");
    }

    public int size() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException("Immutable class");
    }

    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Immutable class");
    }

    public static interface Listener {
    }
}
