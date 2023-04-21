package org.bwapi.bridge.swig;

import org.bwapi.bridge.model.BwapiPointable;

public class SWIG_TilePosition implements BwapiPointable {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    public SWIG_TilePosition(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    public static long getCPtr(SWIG_TilePosition obj) {
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
                bridgeJNI.delete_SWIG_TilePosition(swigCPtr);
            }
            swigCPtr = 0;
        }
    }

    public SWIG_TilePosition() {
        this(bridgeJNI.new_SWIG_TilePosition__SWIG_0(), true);
    }

    public SWIG_TilePosition(SWIG_Position position) {
        this(bridgeJNI.new_SWIG_TilePosition__SWIG_1(SWIG_Position.getCPtr(position), position), true);
    }

    public SWIG_TilePosition(int x, int y) {
        this(bridgeJNI.new_SWIG_TilePosition__SWIG_2(x, y), true);
    }

    public boolean opEquals(SWIG_TilePosition TilePosition) {
        return bridgeJNI.SWIG_TilePosition_opEquals(swigCPtr, this, SWIG_TilePosition.getCPtr(TilePosition), TilePosition);
    }

    public boolean opNotEquals(SWIG_TilePosition TilePosition) {
        return bridgeJNI.SWIG_TilePosition_opNotEquals(swigCPtr, this, SWIG_TilePosition.getCPtr(TilePosition), TilePosition);
    }

    public boolean opLessThan(SWIG_TilePosition TilePosition) {
        return bridgeJNI.SWIG_TilePosition_opLessThan(swigCPtr, this, SWIG_TilePosition.getCPtr(TilePosition), TilePosition);
    }

    public SWIG_TilePosition opPlus(SWIG_TilePosition position) {
        return new SWIG_TilePosition(bridgeJNI.SWIG_TilePosition_opPlus(swigCPtr, this, SWIG_TilePosition.getCPtr(position), position), true);
    }

    public SWIG_TilePosition opMinus(SWIG_TilePosition position) {
        return new SWIG_TilePosition(bridgeJNI.SWIG_TilePosition_opMinus(swigCPtr, this, SWIG_TilePosition.getCPtr(position), position), true);
    }

    public SWIG_TilePosition opAdd(SWIG_TilePosition position) {
        return new SWIG_TilePosition(bridgeJNI.SWIG_TilePosition_opAdd(swigCPtr, this, SWIG_TilePosition.getCPtr(position), position), false);
    }

    public SWIG_TilePosition opSubtract(SWIG_TilePosition position) {
        return new SWIG_TilePosition(bridgeJNI.SWIG_TilePosition_opSubtract(swigCPtr, this, SWIG_TilePosition.getCPtr(position), position), false);
    }

    public double getDistance(SWIG_TilePosition position) {
        return bridgeJNI.SWIG_TilePosition_getDistance(swigCPtr, this, SWIG_TilePosition.getCPtr(position), position);
    }

    public double getLength() {
        return bridgeJNI.SWIG_TilePosition_getLength(swigCPtr, this);
    }

    public boolean isValid() {
        return bridgeJNI.SWIG_TilePosition_isValid(swigCPtr, this);
    }

    public SWIGTYPE_p_int x() {
        return new SWIGTYPE_p_int(bridgeJNI.SWIG_TilePosition_x(swigCPtr, this), false);
    }

    public SWIGTYPE_p_int y() {
        return new SWIGTYPE_p_int(bridgeJNI.SWIG_TilePosition_y(swigCPtr, this), false);
    }

    public int xConst() {
        return bridgeJNI.SWIG_TilePosition_xConst(swigCPtr, this);
    }

    public int yConst() {
        return bridgeJNI.SWIG_TilePosition_yConst(swigCPtr, this);
    }
}
