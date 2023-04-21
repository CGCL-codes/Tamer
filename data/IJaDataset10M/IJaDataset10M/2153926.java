package org.cretz.swig.gen;

public class StringSetIterator {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected StringSetIterator(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(StringSetIterator obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0) {
            if (swigCMemOwn) {
                swigCMemOwn = false;
                swigutilsJNI.delete_StringSetIterator(swigCPtr);
            }
            swigCPtr = 0;
        }
    }

    public StringSetIterator(SWIGTYPE_p_std__setT_std__string_t original) {
        this(swigutilsJNI.new_StringSetIterator(SWIGTYPE_p_std__setT_std__string_t.getCPtr(original)), true);
    }

    public boolean hasNext() {
        return swigutilsJNI.StringSetIterator_hasNext(swigCPtr, this);
    }

    public String next() {
        return swigutilsJNI.StringSetIterator_next(swigCPtr, this);
    }
}
