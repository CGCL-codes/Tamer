package com.almworks.sqlite4java;

class SWIGTYPE_p_sqlite3 {

    private long swigCPtr;

    protected SWIGTYPE_p_sqlite3(long cPtr, boolean futureUse) {
        swigCPtr = cPtr;
    }

    protected SWIGTYPE_p_sqlite3() {
        swigCPtr = 0;
    }

    protected static long getCPtr(SWIGTYPE_p_sqlite3 obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }
}
