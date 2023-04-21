package edu.umn.gis.mapscript;

public class hashTableObj {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected hashTableObj(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(hashTableObj obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            mapscriptJNI.delete_hashTableObj(swigCPtr);
        }
        swigCPtr = 0;
    }

    public int getNumitems() {
        return mapscriptJNI.hashTableObj_numitems_get(swigCPtr, this);
    }

    public hashTableObj() {
        this(mapscriptJNI.new_hashTableObj(), true);
    }

    public int set(String key, String value) {
        return mapscriptJNI.hashTableObj_set(swigCPtr, this, key, value);
    }

    public String get(String key, String default_value) {
        return mapscriptJNI.hashTableObj_get(swigCPtr, this, key, default_value);
    }

    public int remove(String key) {
        return mapscriptJNI.hashTableObj_remove(swigCPtr, this, key);
    }

    public void clear() {
        mapscriptJNI.hashTableObj_clear(swigCPtr, this);
    }

    public String nextKey(String prevkey) {
        return mapscriptJNI.hashTableObj_nextKey(swigCPtr, this, prevkey);
    }
}
