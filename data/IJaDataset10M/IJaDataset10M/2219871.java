package org.bwapi.bridge.swig;

import org.bwapi.bridge.model.BwapiPointable;

public class WeaponTypeSetIterator implements BwapiPointable {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    public WeaponTypeSetIterator(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    public static long getCPtr(WeaponTypeSetIterator obj) {
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
                bridgeJNI.delete_WeaponTypeSetIterator(swigCPtr);
            }
            swigCPtr = 0;
        }
    }

    public WeaponTypeSetIterator(SWIGTYPE_p_std__setT_BWAPI__WeaponType_t original) {
        this(bridgeJNI.new_WeaponTypeSetIterator(SWIGTYPE_p_std__setT_BWAPI__WeaponType_t.getCPtr(original)), true);
    }

    public boolean hasNext() {
        return bridgeJNI.WeaponTypeSetIterator_hasNext(swigCPtr, this);
    }

    public SWIG_WeaponType next() {
        return new SWIG_WeaponType(bridgeJNI.WeaponTypeSetIterator_next(swigCPtr, this), true);
    }
}
