package com.vsetec.util;

import com.vsetec.mety.core.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author fedd
 */
public class DumbList implements List<MetImpl> {

    public static DumbList ME = new DumbList();

    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return true;
    }

    public boolean contains(Object o) {
        return false;
    }

    public Iterator<MetImpl> iterator() {
        return null;
    }

    public Object[] toArray() {
        return null;
    }

    public <T> T[] toArray(T[] a) {
        return null;
    }

    public boolean add(MetImpl e) {
        return false;
    }

    public boolean remove(Object o) {
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        return false;
    }

    public boolean addAll(Collection<? extends MetImpl> c) {
        return false;
    }

    public boolean addAll(int index, Collection<? extends MetImpl> c) {
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }

    public void clear() {
    }

    public MetImpl get(int index) {
        return null;
    }

    public MetImpl set(int index, MetImpl element) {
        return null;
    }

    public void add(int index, MetImpl element) {
    }

    public MetImpl remove(int index) {
        return null;
    }

    public int indexOf(Object o) {
        return -1;
    }

    public int lastIndexOf(Object o) {
        return -1;
    }

    public ListIterator<MetImpl> listIterator() {
        return null;
    }

    public ListIterator<MetImpl> listIterator(int index) {
        return null;
    }

    public List<MetImpl> subList(int fromIndex, int toIndex) {
        return null;
    }
}
