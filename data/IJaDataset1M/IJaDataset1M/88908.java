package gov.nasa.jpf.util;

public class ConstGrowth implements Growth {

    final int v;

    public ConstGrowth(int v) {
        if (v < 1 || v > 1000000000) {
            throw new IllegalArgumentException();
        }
        this.v = v;
    }

    public int grow(int oldSize, int minNewSize) {
        int newSize = oldSize + v;
        if (newSize < minNewSize) {
            newSize = minNewSize + (v >> 1);
        }
        return newSize;
    }
}
