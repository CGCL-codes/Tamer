package org.expasy.jpl.utils.sort;

/**
 * Class for comparator of array of {@code int}s.
 * 
 * @author nikitin
 *
 */
public abstract class JPLAbstractIntsArrayIndexComparator implements JPLIArrayIndexComparator {

    private int[] array;

    public final int[] getArray() {
        return array;
    }

    public void setArray(Object array) {
        this.array = (int[]) array;
    }

    public int getArraySize() {
        return array.length;
    }

    public abstract int compare(Integer i1, Integer i2);
}
