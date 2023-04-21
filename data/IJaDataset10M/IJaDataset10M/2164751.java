package org.jpox.samples.array;

/**
 * Container of a long array.
 * 
 * @version $Revision: 1.2 $
 */
public class LongArray implements ArrayHolderInterface {

    long id;

    long[] array1;

    long[] array2;

    public LongArray(long[] elements1, long[] elements2) {
        this.array1 = elements1;
        this.array2 = elements2;
    }

    public Object getArray1() {
        return array1;
    }

    public Object getArray2() {
        return array2;
    }
}
