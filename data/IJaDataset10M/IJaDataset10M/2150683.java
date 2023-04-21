package edu.umn.gis.mapscript;

public class shapefileObj {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected shapefileObj(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(shapefileObj obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            mapscriptJNI.delete_shapefileObj(swigCPtr);
        }
        swigCPtr = 0;
    }

    public String getSource() {
        return mapscriptJNI.shapefileObj_source_get(swigCPtr, this);
    }

    public int getType() {
        return mapscriptJNI.shapefileObj_type_get(swigCPtr, this);
    }

    public int getNumshapes() {
        return mapscriptJNI.shapefileObj_numshapes_get(swigCPtr, this);
    }

    public rectObj getBounds() {
        long cPtr = mapscriptJNI.shapefileObj_bounds_get(swigCPtr, this);
        return (cPtr == 0) ? null : new rectObj(cPtr, false);
    }

    public int getLastshape() {
        return mapscriptJNI.shapefileObj_lastshape_get(swigCPtr, this);
    }

    public String getStatus() {
        return mapscriptJNI.shapefileObj_status_get(swigCPtr, this);
    }

    public rectObj getStatusbounds() {
        long cPtr = mapscriptJNI.shapefileObj_statusbounds_get(swigCPtr, this);
        return (cPtr == 0) ? null : new rectObj(cPtr, false);
    }

    public int getIsopen() {
        return mapscriptJNI.shapefileObj_isopen_get(swigCPtr, this);
    }

    public shapefileObj(String filename, int type) {
        this(mapscriptJNI.new_shapefileObj(filename, type), true);
    }

    public int get(int i, shapeObj shape) {
        return mapscriptJNI.shapefileObj_get(swigCPtr, this, i, shapeObj.getCPtr(shape), shape);
    }

    public shapeObj getShape(int i) {
        long cPtr = mapscriptJNI.shapefileObj_getShape(swigCPtr, this, i);
        return (cPtr == 0) ? null : new shapeObj(cPtr, true);
    }

    public int getPoint(int i, pointObj point) {
        return mapscriptJNI.shapefileObj_getPoint(swigCPtr, this, i, pointObj.getCPtr(point), point);
    }

    public int getTransformed(mapObj map, int i, shapeObj shape) {
        return mapscriptJNI.shapefileObj_getTransformed(swigCPtr, this, mapObj.getCPtr(map), map, i, shapeObj.getCPtr(shape), shape);
    }

    public void getExtent(int i, rectObj rect) {
        mapscriptJNI.shapefileObj_getExtent(swigCPtr, this, i, rectObj.getCPtr(rect), rect);
    }

    public int add(shapeObj shape) {
        return mapscriptJNI.shapefileObj_add(swigCPtr, this, shapeObj.getCPtr(shape), shape);
    }

    public int addPoint(pointObj point) {
        return mapscriptJNI.shapefileObj_addPoint(swigCPtr, this, pointObj.getCPtr(point), point);
    }

    public DBFInfo getDBF() {
        long cPtr = mapscriptJNI.shapefileObj_getDBF(swigCPtr, this);
        return (cPtr == 0) ? null : new DBFInfo(cPtr, false);
    }
}
