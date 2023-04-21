package com.bulletphysics.extras.gimpact;

/**
 *
 * @author jezek2
 */
class PairSet {

    private Pair[] array;

    private int size = 0;

    public PairSet() {
        array = new Pair[32];
        for (int i = 0; i < array.length; i++) {
            array[i] = new Pair();
        }
    }

    public void clear() {
        size = 0;
    }

    public int size() {
        return size;
    }

    public Pair get(int index) {
        if (index >= size) throw new IndexOutOfBoundsException();
        return array[index];
    }

    @SuppressWarnings("unchecked")
    private void expand() {
        Pair[] newArray = new Pair[array.length << 1];
        for (int i = array.length; i < newArray.length; i++) {
            newArray[i] = new Pair();
        }
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }

    public void push_pair(int index1, int index2) {
        if (size == array.length) {
            expand();
        }
        array[size].index1 = index1;
        array[size].index2 = index2;
        size++;
    }

    public void push_pair_inv(int index1, int index2) {
        if (size == array.length) {
            expand();
        }
        array[size].index1 = index2;
        array[size].index2 = index1;
        size++;
    }
}
