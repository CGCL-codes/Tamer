package org.gdal.gdal;

public class Driver extends MajorObject {

    private long swigCPtr;

    protected Driver(long cPtr, boolean cMemoryOwn) {
        super(gdalJNI.SWIGDriverUpcast(cPtr), cMemoryOwn);
        swigCPtr = cPtr;
    }

    protected static long getCPtr(Driver obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    public void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            throw new UnsupportedOperationException("C++ destructor does not have public access");
        }
        swigCPtr = 0;
        super.delete();
    }

    protected static long getCPtrAndDisown(Driver obj) {
        if (obj != null) obj.swigCMemOwn = false;
        return getCPtr(obj);
    }

    public String getShortName() {
        return gdalJNI.Driver_ShortName_get(swigCPtr);
    }

    public String getLongName() {
        return gdalJNI.Driver_LongName_get(swigCPtr);
    }

    public String getHelpTopic() {
        return gdalJNI.Driver_HelpTopic_get(swigCPtr);
    }

    public Dataset Create(String name, int xsize, int ysize, int bands, int eType, java.util.Vector options) {
        long cPtr = gdalJNI.Driver_Create(swigCPtr, name, xsize, ysize, bands, eType, options);
        return (cPtr == 0) ? null : new Dataset(cPtr, true);
    }

    public Dataset CreateCopy(String name, Dataset src, int strict, java.util.Vector options) {
        long cPtr = gdalJNI.Driver_CreateCopy(swigCPtr, name, Dataset.getCPtr(src), strict, options);
        return (cPtr == 0) ? null : new Dataset(cPtr, true);
    }

    public int Delete(String name) {
        return gdalJNI.Driver_Delete(swigCPtr, name);
    }

    public int Register() {
        return gdalJNI.Driver_Register(swigCPtr);
    }

    public void Deregister() {
        gdalJNI.Driver_Deregister(swigCPtr);
    }
}