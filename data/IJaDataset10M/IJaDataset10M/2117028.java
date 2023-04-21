package com.JPhysX;

/**
 * Copyright (c) 2007, Yuri Kravchik
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the Yuri Kravchik nor the names of its contributors
 *   may be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public class NxJointDesc {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected NxJointDesc(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(NxJointDesc obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            JPhysXAdapterJNI.delete_NxJointDesc(swigCPtr);
        }
        swigCPtr = 0;
    }

    public void setMaxForce(float value) {
        JPhysXAdapterJNI.NxJointDesc_maxForce_set(swigCPtr, this, value);
    }

    public float getMaxForce() {
        return JPhysXAdapterJNI.NxJointDesc_maxForce_get(swigCPtr, this);
    }

    public void setMaxTorque(float value) {
        JPhysXAdapterJNI.NxJointDesc_maxTorque_set(swigCPtr, this, value);
    }

    public float getMaxTorque() {
        return JPhysXAdapterJNI.NxJointDesc_maxTorque_get(swigCPtr, this);
    }

    public void setUserData(SWIGTYPE_p_void value) {
        JPhysXAdapterJNI.NxJointDesc_userData_set(swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getUserData() {
        long cPtr = JPhysXAdapterJNI.NxJointDesc_userData_get(swigCPtr, this);
        return (cPtr == 0) ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public void setName(String value) {
        JPhysXAdapterJNI.NxJointDesc_name_set(swigCPtr, this, value);
    }

    public String getName() {
        return JPhysXAdapterJNI.NxJointDesc_name_get(swigCPtr, this);
    }

    public void setJointFlags(long value) {
        JPhysXAdapterJNI.NxJointDesc_jointFlags_set(swigCPtr, this, value);
    }

    public long getJointFlags() {
        return JPhysXAdapterJNI.NxJointDesc_jointFlags_get(swigCPtr, this);
    }

    public void setToDefault() {
        JPhysXAdapterJNI.NxJointDesc_setToDefault(swigCPtr, this);
    }

    public boolean isValid() {
        return JPhysXAdapterJNI.NxJointDesc_isValid(swigCPtr, this);
    }

    public void setGlobalAnchor(NxVec3 wsAnchor) {
        JPhysXAdapterJNI.NxJointDesc_setGlobalAnchor(swigCPtr, this, NxVec3.getCPtr(wsAnchor), wsAnchor);
    }

    public void setGlobalAxis(NxVec3 wsAxis) {
        JPhysXAdapterJNI.NxJointDesc_setGlobalAxis(swigCPtr, this, NxVec3.getCPtr(wsAxis), wsAxis);
    }

    public int getType() {
        return JPhysXAdapterJNI.NxJointDesc_getType(swigCPtr, this);
    }

    public NxActor getActor1() {
        long cPtr = JPhysXAdapterJNI.NxJointDesc_getActor1(swigCPtr, this);
        return (cPtr == 0) ? null : new NxActor(cPtr, false);
    }

    public NxActor getActor2() {
        long cPtr = JPhysXAdapterJNI.NxJointDesc_getActor2(swigCPtr, this);
        return (cPtr == 0) ? null : new NxActor(cPtr, false);
    }

    public NxVec3 getLocalNormal1() {
        long cPtr = JPhysXAdapterJNI.NxJointDesc_getLocalNormal1(swigCPtr, this);
        return (cPtr == 0) ? null : new NxVec3(cPtr, false);
    }

    public NxVec3 getLocalNormal2() {
        long cPtr = JPhysXAdapterJNI.NxJointDesc_getLocalNormal2(swigCPtr, this);
        return (cPtr == 0) ? null : new NxVec3(cPtr, false);
    }

    public NxVec3 getLocalAxis1() {
        long cPtr = JPhysXAdapterJNI.NxJointDesc_getLocalAxis1(swigCPtr, this);
        return (cPtr == 0) ? null : new NxVec3(cPtr, false);
    }

    public NxVec3 getLocalAxis2() {
        long cPtr = JPhysXAdapterJNI.NxJointDesc_getLocalAxis2(swigCPtr, this);
        return (cPtr == 0) ? null : new NxVec3(cPtr, false);
    }

    public NxVec3 getLocalAnchor1() {
        long cPtr = JPhysXAdapterJNI.NxJointDesc_getLocalAnchor1(swigCPtr, this);
        return (cPtr == 0) ? null : new NxVec3(cPtr, false);
    }

    public NxVec3 getLocalAnchor2() {
        long cPtr = JPhysXAdapterJNI.NxJointDesc_getLocalAnchor2(swigCPtr, this);
        return (cPtr == 0) ? null : new NxVec3(cPtr, false);
    }

    public void setActor1(NxActor actor) {
        JPhysXAdapterJNI.NxJointDesc_setActor1(swigCPtr, this, NxActor.getCPtr(actor), actor);
    }

    public void setActor2(NxActor actor) {
        JPhysXAdapterJNI.NxJointDesc_setActor2(swigCPtr, this, NxActor.getCPtr(actor), actor);
    }

    public void setLocalNormal1(NxVec3 normal) {
        JPhysXAdapterJNI.NxJointDesc_setLocalNormal1(swigCPtr, this, NxVec3.getCPtr(normal), normal);
    }

    public void setLocalNormal2(NxVec3 normal) {
        JPhysXAdapterJNI.NxJointDesc_setLocalNormal2(swigCPtr, this, NxVec3.getCPtr(normal), normal);
    }

    public void setLocalAxis1(NxVec3 axis) {
        JPhysXAdapterJNI.NxJointDesc_setLocalAxis1(swigCPtr, this, NxVec3.getCPtr(axis), axis);
    }

    public void setLocalAxis2(NxVec3 axis) {
        JPhysXAdapterJNI.NxJointDesc_setLocalAxis2(swigCPtr, this, NxVec3.getCPtr(axis), axis);
    }

    public void setLocalAnchor1(NxVec3 anchor) {
        JPhysXAdapterJNI.NxJointDesc_setLocalAnchor1(swigCPtr, this, NxVec3.getCPtr(anchor), anchor);
    }

    public void setLocalAnchor2(NxVec3 anchor) {
        JPhysXAdapterJNI.NxJointDesc_setLocalAnchor2(swigCPtr, this, NxVec3.getCPtr(anchor), anchor);
    }
}
