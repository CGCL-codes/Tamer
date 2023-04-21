package com.googlecode.javacpp;

import com.googlecode.javacpp.annotation.Cast;
import com.googlecode.javacpp.annotation.Name;

/**
 *
 * @author Samuel Audet
 */
@Name("long")
public class CLongPointer extends Pointer {

    public CLongPointer(int size) {
        allocateArray(size);
    }

    public CLongPointer(Pointer p) {
        super(p);
    }

    private native void allocateArray(int size);

    @Override
    public CLongPointer position(int position) {
        return (CLongPointer) super.position(position);
    }

    public long get() {
        return get(0);
    }

    public native long get(int i);

    public CLongPointer put(long l) {
        return put(0, l);
    }

    public native CLongPointer put(int i, @Cast("long") long l);
}
