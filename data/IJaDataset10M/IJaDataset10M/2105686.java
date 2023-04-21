package org.bwapi.bridge.swig;

import org.bwapi.bridge.model.BwapiPointable;

public class TechTypeConstantSetIterator implements BwapiPointable {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    public TechTypeConstantSetIterator(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    public static long getCPtr(TechTypeConstantSetIterator obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    public long getCPtr() {
        return swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0) {
            if (swigCMemOwn) {
                swigCMemOwn = false;
                bridgeJNI.delete_TechTypeConstantSetIterator(swigCPtr);
            }
            swigCPtr = 0;
        }
    }

    public TechTypeConstantSetIterator(SWIGTYPE_p_std__setT_BWAPI__TechType_const_p_t original) {
        this(bridgeJNI.new_TechTypeConstantSetIterator(SWIGTYPE_p_std__setT_BWAPI__TechType_const_p_t.getCPtr(original)), true);
    }

    public boolean hasNext() {
        return bridgeJNI.TechTypeConstantSetIterator_hasNext(swigCPtr, this);
    }

    public SWIG_TechType next() {
        long cPtr = bridgeJNI.TechTypeConstantSetIterator_next(swigCPtr, this);
        return (cPtr == 0) ? null : new SWIG_TechType(cPtr, false);
    }
}
