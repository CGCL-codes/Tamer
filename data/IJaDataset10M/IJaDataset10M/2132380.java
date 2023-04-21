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
public class NxRevoluteJointDesc extends NxJointDesc {

    private long swigCPtr;

    protected NxRevoluteJointDesc(long cPtr, boolean cMemoryOwn) {
        super(JPhysXAdapterJNI.SWIGNxRevoluteJointDescUpcast(cPtr), cMemoryOwn);
        swigCPtr = cPtr;
    }

    protected static long getCPtr(NxRevoluteJointDesc obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            JPhysXAdapterJNI.delete_NxRevoluteJointDesc(swigCPtr);
        }
        swigCPtr = 0;
        super.delete();
    }

    public void setLimit(NxJointLimitPairDesc value) {
        JPhysXAdapterJNI.NxRevoluteJointDesc_limit_set(swigCPtr, this, NxJointLimitPairDesc.getCPtr(value), value);
    }

    public NxJointLimitPairDesc getLimit() {
        long cPtr = JPhysXAdapterJNI.NxRevoluteJointDesc_limit_get(swigCPtr, this);
        return (cPtr == 0) ? null : new NxJointLimitPairDesc(cPtr, false);
    }

    public void setMotor(NxMotorDesc value) {
        JPhysXAdapterJNI.NxRevoluteJointDesc_motor_set(swigCPtr, this, NxMotorDesc.getCPtr(value), value);
    }

    public NxMotorDesc getMotor() {
        long cPtr = JPhysXAdapterJNI.NxRevoluteJointDesc_motor_get(swigCPtr, this);
        return (cPtr == 0) ? null : new NxMotorDesc(cPtr, false);
    }

    public void setSpring(NxSpringDesc value) {
        JPhysXAdapterJNI.NxRevoluteJointDesc_spring_set(swigCPtr, this, NxSpringDesc.getCPtr(value), value);
    }

    public NxSpringDesc getSpring() {
        long cPtr = JPhysXAdapterJNI.NxRevoluteJointDesc_spring_get(swigCPtr, this);
        return (cPtr == 0) ? null : new NxSpringDesc(cPtr, false);
    }

    public void setProjectionDistance(float value) {
        JPhysXAdapterJNI.NxRevoluteJointDesc_projectionDistance_set(swigCPtr, this, value);
    }

    public float getProjectionDistance() {
        return JPhysXAdapterJNI.NxRevoluteJointDesc_projectionDistance_get(swigCPtr, this);
    }

    public void setProjectionAngle(float value) {
        JPhysXAdapterJNI.NxRevoluteJointDesc_projectionAngle_set(swigCPtr, this, value);
    }

    public float getProjectionAngle() {
        return JPhysXAdapterJNI.NxRevoluteJointDesc_projectionAngle_get(swigCPtr, this);
    }

    public void setFlags(long value) {
        JPhysXAdapterJNI.NxRevoluteJointDesc_flags_set(swigCPtr, this, value);
    }

    public long getFlags() {
        return JPhysXAdapterJNI.NxRevoluteJointDesc_flags_get(swigCPtr, this);
    }

    public void setProjectionMode(int value) {
        JPhysXAdapterJNI.NxRevoluteJointDesc_projectionMode_set(swigCPtr, this, value);
    }

    public int getProjectionMode() {
        return JPhysXAdapterJNI.NxRevoluteJointDesc_projectionMode_get(swigCPtr, this);
    }

    public NxRevoluteJointDesc() {
        this(JPhysXAdapterJNI.new_NxRevoluteJointDesc(), true);
    }

    public void setToDefault(boolean fromCtor) {
        JPhysXAdapterJNI.NxRevoluteJointDesc_setToDefault__SWIG_0(swigCPtr, this, fromCtor);
    }

    public void setToDefault() {
        JPhysXAdapterJNI.NxRevoluteJointDesc_setToDefault__SWIG_1(swigCPtr, this);
    }

    public boolean isValid() {
        return JPhysXAdapterJNI.NxRevoluteJointDesc_isValid(swigCPtr, this);
    }
}
