package org.homeunix.thecave.moss.data.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * @author wyatt
 * Class which provides a single view of multiple backed lists.  When the backing
 * lists change, this one will automatically update too.  You cannot change this
 * list directly - you can only change the backing lists.
 * 
 * This class currently does not scale well with extra length.  If performance is 
 * an issue, try to keep this list small.
 */
public class CompositeList<T> implements List<T> {

    private final Collection<List<? extends T>> backingLists;

    private final List<T> compositeSortedList;

    private final boolean sorted;

    private final boolean allowDuplicates;

    public CompositeList(boolean sorted, boolean allowDuplicates, Collection<List<? extends T>> backingLists) {
        this.backingLists = backingLists;
        this.compositeSortedList = new LinkedList<T>();
        this.sorted = sorted;
        this.allowDuplicates = allowDuplicates;
        updateList();
    }

    public CompositeList(boolean sorted, boolean allowDuplicates, List<? extends T>... backingLists) {
        this.backingLists = new LinkedList<List<? extends T>>();
        this.compositeSortedList = new LinkedList<T>();
        this.sorted = sorted;
        this.allowDuplicates = allowDuplicates;
        for (List<? extends T> list : backingLists) {
            this.backingLists.add(list);
        }
        updateList();
    }

    public void addList(List<T> list) {
        backingLists.add(list);
    }

    public void add(int index, T element) {
    }

    public boolean add(T o) {
        return false;
    }

    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    public void clear() {
    }

    public boolean contains(Object o) {
        for (List<? extends T> list : backingLists) {
            if (list.contains(o)) return true;
        }
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object object : c) {
            if (!this.contains(object)) return false;
        }
        return true;
    }

    public T get(int index) {
        return getCompositeSortedList().get(index);
    }

    public int indexOf(Object o) {
        return getCompositeSortedList().indexOf(o);
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public Iterator<T> iterator() {
        return getCompositeSortedList().iterator();
    }

    public int lastIndexOf(Object o) {
        return getCompositeSortedList().lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return getCompositeSortedList().listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return getCompositeSortedList().listIterator(index);
    }

    public T remove(int index) {
        return null;
    }

    public boolean remove(Object o) {
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }

    public T set(int index, T element) {
        return null;
    }

    public int size() {
        int size = 0;
        for (List<? extends T> list : backingLists) {
            size += list.size();
        }
        return size;
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return getCompositeSortedList().subList(fromIndex, toIndex);
    }

    public Object[] toArray() {
        return getCompositeSortedList().toArray();
    }

    @SuppressWarnings("hiding")
    public <T> T[] toArray(T[] a) {
        return getCompositeSortedList().toArray(a);
    }

    private List<T> getCompositeSortedList() {
        return compositeSortedList;
    }

    @SuppressWarnings("unchecked")
    public List<T> updateList() {
        compositeSortedList.clear();
        if (allowDuplicates) {
            for (List<? extends T> list : backingLists) {
                compositeSortedList.addAll(list);
            }
        } else {
            Set<T> tempSet = new HashSet<T>();
            for (List<? extends T> list : backingLists) {
                tempSet.addAll(list);
            }
            compositeSortedList.addAll(tempSet);
        }
        if (sorted) {
            try {
                Collections.sort((List<? extends Comparable>) compositeSortedList);
            } catch (ClassCastException cce) {
            } catch (NullPointerException npe) {
            }
        }
        return compositeSortedList;
    }

    @Override
    public String toString() {
        return getCompositeSortedList().toString();
    }
}
