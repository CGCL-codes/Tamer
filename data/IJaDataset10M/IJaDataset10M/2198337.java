package org.bwapi.bridge.swig;

import org.bwapi.bridge.model.BwapiPointable;

public class AIModule implements BwapiPointable {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    public AIModule(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    public static long getCPtr(AIModule obj) {
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
                bridgeJNI.delete_AIModule(swigCPtr);
            }
            swigCPtr = 0;
        }
    }

    public AIModule() {
        this(bridgeJNI.new_AIModule(), true);
    }

    public void onStart() {
        bridgeJNI.AIModule_onStart(swigCPtr, this);
    }

    public void onEnd(boolean isWinner) {
        bridgeJNI.AIModule_onEnd(swigCPtr, this, isWinner);
    }

    public void onFrame() {
        bridgeJNI.AIModule_onFrame(swigCPtr, this);
    }

    public boolean onSendText(String text) {
        return bridgeJNI.AIModule_onSendText(swigCPtr, this, text);
    }

    public void onPlayerLeft(SWIG_Player player) {
        bridgeJNI.AIModule_onPlayerLeft(swigCPtr, this, SWIG_Player.getCPtr(player), player);
    }

    public void onNukeDetect(SWIGTYPE_p_Position target) {
        bridgeJNI.AIModule_onNukeDetect(swigCPtr, this, SWIGTYPE_p_Position.getCPtr(target));
    }

    public void onUnitCreate(SWIG_Unit unit) {
        bridgeJNI.AIModule_onUnitCreate(swigCPtr, this, SWIG_Unit.getCPtr(unit), unit);
    }

    public void onUnitDestroy(SWIG_Unit unit) {
        bridgeJNI.AIModule_onUnitDestroy(swigCPtr, this, SWIG_Unit.getCPtr(unit), unit);
    }

    public void onUnitMorph(SWIG_Unit unit) {
        bridgeJNI.AIModule_onUnitMorph(swigCPtr, this, SWIG_Unit.getCPtr(unit), unit);
    }

    public void onUnitShow(SWIG_Unit unit) {
        bridgeJNI.AIModule_onUnitShow(swigCPtr, this, SWIG_Unit.getCPtr(unit), unit);
    }

    public void onUnitHide(SWIG_Unit unit) {
        bridgeJNI.AIModule_onUnitHide(swigCPtr, this, SWIG_Unit.getCPtr(unit), unit);
    }

    public void onUnitRenegade(SWIG_Unit unit) {
        bridgeJNI.AIModule_onUnitRenegade(swigCPtr, this, SWIG_Unit.getCPtr(unit), unit);
    }
}
