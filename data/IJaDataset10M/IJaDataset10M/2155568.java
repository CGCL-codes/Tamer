package edu.umn.gis.mapscript;

public class OWSRequest {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected OWSRequest(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(OWSRequest obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            mapscriptJNI.delete_OWSRequest(swigCPtr);
        }
        swigCPtr = 0;
    }

    public int getNumParams() {
        return mapscriptJNI.OWSRequest_NumParams_get(swigCPtr, this);
    }

    public void setType(MS_REQUEST_TYPE value) {
        mapscriptJNI.OWSRequest_type_set(swigCPtr, this, value.swigValue());
    }

    public MS_REQUEST_TYPE getType() {
        return MS_REQUEST_TYPE.swigToEnum(mapscriptJNI.OWSRequest_type_get(swigCPtr, this));
    }

    public void setContenttype(String value) {
        mapscriptJNI.OWSRequest_contenttype_set(swigCPtr, this, value);
    }

    public String getContenttype() {
        return mapscriptJNI.OWSRequest_contenttype_get(swigCPtr, this);
    }

    public void setPostrequest(String value) {
        mapscriptJNI.OWSRequest_postrequest_set(swigCPtr, this, value);
    }

    public String getPostrequest() {
        return mapscriptJNI.OWSRequest_postrequest_get(swigCPtr, this);
    }

    public void setHttpcookiedata(String value) {
        mapscriptJNI.OWSRequest_httpcookiedata_set(swigCPtr, this, value);
    }

    public String getHttpcookiedata() {
        return mapscriptJNI.OWSRequest_httpcookiedata_get(swigCPtr, this);
    }

    public OWSRequest() {
        this(mapscriptJNI.new_OWSRequest(), true);
    }

    public int loadParams() {
        return mapscriptJNI.OWSRequest_loadParams(swigCPtr, this);
    }

    public void setParameter(String name, String value) {
        mapscriptJNI.OWSRequest_setParameter(swigCPtr, this, name, value);
    }

    public String getName(int index) {
        return mapscriptJNI.OWSRequest_getName(swigCPtr, this, index);
    }

    public String getValue(int index) {
        return mapscriptJNI.OWSRequest_getValue(swigCPtr, this, index);
    }

    public String getValueByName(String name) {
        return mapscriptJNI.OWSRequest_getValueByName(swigCPtr, this, name);
    }
}
