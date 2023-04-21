package net.sourceforge.jacuda.jni;

import net.sourceforge.jacuda.exception.ArraysDifferInLengthException;

/**
 * basic c++ implementation
 * @author wohlgemuth
 *
 */
public class Array implements net.sourceforge.jacuda.Array {

    public static native float[] doAdd(float[] a, float[] b, int size);

    public static native float[] doDivide(float[] a, float[] b, int size);

    public static native float[] doMultiply(float[] a, float[] b, int size);

    public static native float[] doSubstract(float[] a, float[] b, int size);

    /**
	 * loads our library
	 */
    static {
        try {
            System.loadLibrary("Jacuda");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public float[] add(float[] a, float[] b) {
        if (a.length != b.length) {
            throw new ArraysDifferInLengthException();
        }
        return doAdd(a, b, a.length);
    }

    public float[] divide(float[] a, float[] b) {
        if (a.length != b.length) {
            throw new ArraysDifferInLengthException();
        }
        return doDivide(a, b, a.length);
    }

    public float[] multiply(float[] a, float[] b) {
        if (a.length != b.length) {
            throw new ArraysDifferInLengthException();
        }
        return doMultiply(a, b, a.length);
    }

    public float[] substract(float[] a, float[] b) {
        if (a.length != b.length) {
            throw new ArraysDifferInLengthException();
        }
        return doSubstract(a, b, a.length);
    }
}
