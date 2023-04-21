package com.tensegrity.palobrowser.sparsedata;

/**
 * <code>ArrayFactory</code>
 * 
 * <p>
 * Provides an encapsulation for data arrays. Arrays larger than a certain threshold
 * are implemented as sparse arrays. This only makes sense if most of the values
 * in the array are null, otherwise using sparse-arrays introduces yet more overhead.
 *
 * @author Stepan Rutz
 * @version $ID$
 */
public class ArrayFactory {

    public static final int SPARSE_ARRAY_THRESHOLD = 10000;

    private static ArrayFactory instance = new ArrayFactory();

    public static ArrayFactory getInstance() {
        return instance;
    }

    public Array newArray(int length) {
        return length < SPARSE_ARRAY_THRESHOLD ? (Array) new SimpleArray(length) : new SparseArray(length);
    }

    public Array newArray(Object data[]) {
        return data.length < SPARSE_ARRAY_THRESHOLD ? (Array) new SimpleArray(data) : new SparseArray(data);
    }
}
