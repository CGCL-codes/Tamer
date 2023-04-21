package org.bwapi.bridge.swig;

import org.bwapi.bridge.model.BwapiPointable;

public class ChokepointSpacelessSetIterator implements BwapiPointable {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    public ChokepointSpacelessSetIterator(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    public static long getCPtr(ChokepointSpacelessSetIterator obj) {
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
                bridgeJNI.delete_ChokepointSpacelessSetIterator(swigCPtr);
            }
            swigCPtr = 0;
        }
    }

    public ChokepointSpacelessSetIterator(SWIGTYPE_p_std__setT_Chokepoint_p_t original) {
        this(bridgeJNI.new_ChokepointSpacelessSetIterator(SWIGTYPE_p_std__setT_Chokepoint_p_t.getCPtr(original)), true);
    }

    public boolean hasNext() {
        return bridgeJNI.ChokepointSpacelessSetIterator_hasNext(swigCPtr, this);
    }

    public SWIGTYPE_p_Chokepoint next() {
        long cPtr = bridgeJNI.ChokepointSpacelessSetIterator_next(swigCPtr, this);
        return (cPtr == 0) ? null : new SWIGTYPE_p_Chokepoint(cPtr, false);
    }
}
