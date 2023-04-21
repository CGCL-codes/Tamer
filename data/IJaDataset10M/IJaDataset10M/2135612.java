package org.jpox.samples.array;

/**
 * Container of a boolean array.
 * 
 * @version $Revision: 1.2 $
 */
public class BooleanArray implements ArrayHolderInterface {

    long id;

    boolean[] array1;

    boolean[] array2;

    public BooleanArray(boolean[] elements1, boolean[] elements2) {
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
