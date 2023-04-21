package com.cosmos.acacia.crm.client;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A list used to transfer its contents from the server
 * in portions (chunks). It delegates all list methods
 * to the current list of data, only getting the i-th item
 * may require calling the server to get more data
 *
 * @author Bozhidar Bozhanov
 *
 * @param <E>
 */
public class TransferableList<E> implements List<E>, Serializable {

    public static final int FETCH_CHUNK_SIZE = 10;

    private List<E> currentList;

    /**
     * The id by which the list is identified
     */
    private Integer id;

    /**
     * The actual size of the whole list
     */
    private int actualSize;

    private boolean isRunning;

    private static Object mutex = new Object();

    public boolean add(E e) {
        return currentList.add(e);
    }

    public void add(int index, E element) {
        currentList.add(index, element);
    }

    public boolean addAll(Collection<? extends E> c) {
        return currentList.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        return currentList.addAll(index, c);
    }

    public void clear() {
        currentList.clear();
    }

    public boolean contains(Object obj) {
        return currentList.contains(obj);
    }

    public boolean containsAll(Collection<?> c) {
        return currentList.containsAll(c);
    }

    @Override
    public boolean equals(Object obj) {
        return currentList.equals(obj);
    }

    @SuppressWarnings("unchecked")
    public E get(final int i) {
        System.out.println("get called " + i + "(size " + currentList.size());
        final int diff = i + 1 - currentList.size();
        if (i + 1 > currentList.size() && !isRunning && currentList.size() < getActualSize()) {
            int requestedElements = diff;
            currentList.addAll(LocalSession.instance().getListServer().serve(getId(), new Integer(currentList.size()), new Integer(requestedElements)));
        }
        if (!isRunning && -diff == FETCH_CHUNK_SIZE / 2 && currentList.size() < getActualSize()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    isRunning = true;
                    System.out.println("AX: " + currentList.size());
                    synchronized (mutex) {
                        currentList.addAll(LocalSession.instance().getListServer().serve(getId(), new Integer(currentList.size()), new Integer(FETCH_CHUNK_SIZE)));
                    }
                    isRunning = false;
                }
            }).start();
        }
        if (currentList.size() < getActualSize() && i == currentList.size()) {
            synchronized (mutex) {
                return currentList.get(i);
            }
        }
        return currentList.get(i);
    }

    @Override
    public int hashCode() {
        return currentList.hashCode();
    }

    public int indexOf(Object obj) {
        return currentList.indexOf(obj);
    }

    public boolean isEmpty() {
        return currentList.isEmpty();
    }

    public Iterator<E> iterator() {
        return currentList.iterator();
    }

    public int lastIndexOf(Object obj) {
        return currentList.lastIndexOf(obj);
    }

    public ListIterator<E> listIterator() {
        return currentList.listIterator();
    }

    public ListIterator<E> listIterator(int i) {
        return currentList.listIterator(i);
    }

    public E remove(int i) {
        return currentList.remove(i);
    }

    public boolean remove(Object obj) {
        return currentList.remove(obj);
    }

    public boolean removeAll(Collection<?> c) {
        return currentList.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return currentList.retainAll(c);
    }

    public E set(int index, E element) {
        return currentList.set(index, element);
    }

    public int size() {
        return currentList.size();
    }

    public List<E> subList(int i, int j) {
        return currentList.subList(i, j);
    }

    public Object[] toArray() {
        return currentList.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return currentList.toArray(a);
    }

    public List<E> getCurrentList() {
        return currentList;
    }

    public void setCurrentList(List<E> list) {
        this.currentList = list;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getActualSize() {
        return actualSize;
    }

    public void setActualSize(int actualSize) {
        this.actualSize = actualSize;
    }
}
