package org.jopenchart;

public class ArrayOfInt {

    private int[] a = new int[5];

    private int size = 0;

    public void add(int x1) {
        if (size >= a.length) {
            int[] newa = new int[size * 2];
            System.arraycopy(a, 0, newa, 0, size);
            a = newa;
        }
        a[size] = x1;
        size++;
    }

    public int[] toArray() {
        int[] r = new int[size];
        System.arraycopy(a, 0, r, 0, size);
        return r;
    }

    public int[] getArray() {
        return a;
    }

    public int getSize() {
        return size;
    }

    public void clear() {
        size = 0;
    }

    public boolean isEmpty() {
        return size <= 0;
    }

    public int get(int i) {
        return a[i];
    }
}
