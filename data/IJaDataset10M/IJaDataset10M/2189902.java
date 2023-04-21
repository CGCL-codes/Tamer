package org.bwapi.bridge.swig;

import org.bwapi.bridge.model.BwapiPointable;

public class SWIG_Position implements BwapiPointable {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    public SWIG_Position(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    public static long getCPtr(SWIG_Position obj) {
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
                bridgeJNI.delete_SWIG_Position(swigCPtr);
            }
            swigCPtr = 0;
        }
    }

    public SWIG_Position() {
        this(bridgeJNI.new_SWIG_Position__SWIG_0(), true);
    }

    public SWIG_Position(SWIG_TilePosition position) {
        this(bridgeJNI.new_SWIG_Position__SWIG_1(SWIG_TilePosition.getCPtr(position), position), true);
    }

    public SWIG_Position(int x, int y) {
        this(bridgeJNI.new_SWIG_Position__SWIG_2(x, y), true);
    }

    public boolean opEquals(SWIG_Position position) {
        return bridgeJNI.SWIG_Position_opEquals(swigCPtr, this, SWIG_Position.getCPtr(position), position);
    }

    public boolean opNotEquals(SWIG_Position position) {
        return bridgeJNI.SWIG_Position_opNotEquals(swigCPtr, this, SWIG_Position.getCPtr(position), position);
    }

    public boolean opLessThan(SWIG_Position position) {
        return bridgeJNI.SWIG_Position_opLessThan(swigCPtr, this, SWIG_Position.getCPtr(position), position);
    }

    public SWIG_Position opPlus(SWIG_Position position) {
        return new SWIG_Position(bridgeJNI.SWIG_Position_opPlus(swigCPtr, this, SWIG_Position.getCPtr(position), position), true);
    }

    public SWIG_Position opMinus(SWIG_Position position) {
        return new SWIG_Position(bridgeJNI.SWIG_Position_opMinus(swigCPtr, this, SWIG_Position.getCPtr(position), position), true);
    }

    public SWIG_Position opAdd(SWIG_Position position) {
        return new SWIG_Position(bridgeJNI.SWIG_Position_opAdd(swigCPtr, this, SWIG_Position.getCPtr(position), position), false);
    }

    public SWIG_Position opSubtract(SWIG_Position position) {
        return new SWIG_Position(bridgeJNI.SWIG_Position_opSubtract(swigCPtr, this, SWIG_Position.getCPtr(position), position), false);
    }

    public double getDistance(SWIG_Position position) {
        return bridgeJNI.SWIG_Position_getDistance(swigCPtr, this, SWIG_Position.getCPtr(position), position);
    }

    public double getLength() {
        return bridgeJNI.SWIG_Position_getLength(swigCPtr, this);
    }

    public SWIGTYPE_p_int x() {
        return new SWIGTYPE_p_int(bridgeJNI.SWIG_Position_x(swigCPtr, this), false);
    }

    public SWIGTYPE_p_int y() {
        return new SWIGTYPE_p_int(bridgeJNI.SWIG_Position_y(swigCPtr, this), false);
    }

    public int xConst() {
        return bridgeJNI.SWIG_Position_xConst(swigCPtr, this);
    }

    public int yConst() {
        return bridgeJNI.SWIG_Position_yConst(swigCPtr, this);
    }
}
