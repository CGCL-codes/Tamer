package com.versant.core.util;

/**
 * Growable int[]. This is based com.sosnoski.util.array.IntArray from
 * Sosnoski Software Solutions, Inc.
 */
public final class IntArray {

    private int[] buf;

    private int size;

    public IntArray() {
        this(64);
    }

    public IntArray(int capacity) {
        buf = new int[capacity];
    }

    public int size() {
        return size;
    }

    private void ensureCapacity(int len) {
        if (size + len > buf.length) {
            int n = buf.length * 3 / 2 + 1;
            if (size + len > n) {
                n = size + len;
            }
            int[] a = new int[n];
            System.arraycopy(buf, 0, a, 0, size);
            buf = a;
        }
    }

    public void add(int v) {
        ensureCapacity(size + 1);
        buf[size++] = v;
    }

    /**
     * Add a value at a specified index in the array.
     */
    public void add(int index, int value) {
        ensureCapacity(size + 1);
        if (index == size) {
            buf[size++] = value;
        } else {
            System.arraycopy(buf, index, buf, index + 1, size - index);
            buf[index] = value;
        }
    }

    /**
     * Constructs and returns a simple array containing the same data as held
     * in this growable array.
     */
    public int[] toArray() {
        int[] a = new int[size];
        System.arraycopy(buf, 0, a, 0, size);
        return a;
    }

    public void clear() {
        size = 0;
    }

    /**
     * Retrieve the value present at an index position in the array.
     */
    public int get(int index) {
        return buf[index];
    }
}
