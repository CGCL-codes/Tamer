package org.fenggui.util;

import java.lang.reflect.Array;

/**
 * Fast stack implementation
 * 
 * @author Johannes Schaback
 *
 * 
 */
public class Stack<E> {

    private E[] array;

    private int size = 0;

    private Class<E> clazz = null;

    public Stack(Class<E> clazz, int capacity) {
        this.clazz = clazz;
        array = createArray(capacity);
    }

    public synchronized void push(E e) {
        if (size == array.length) {
            E[] newArray = createArray(array.length * 2);
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }
        array[size++] = e;
    }

    public synchronized E pop() {
        if (isEmpty()) return null;
        E e = array[size - 1];
        array[size - 1] = null;
        size--;
        return e;
    }

    public void clear() {
        for (int i = 0; i < size; i++) array[i] = null;
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private E[] createArray(int capacity) {
        return (E[]) Array.newInstance(clazz, capacity);
    }

    public E peek() {
        if (isEmpty()) return null;
        return array[size - 1];
    }

    public void add(E e) {
        if (size == array.length) {
            E[] newArray = createArray(array.length * 2);
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }
        array[size++] = e;
    }

    public final int getSize() {
        return size;
    }

    public int getCapacity() {
        return array.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public synchronized boolean contains(E e) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(e)) return true;
        }
        return false;
    }
}
