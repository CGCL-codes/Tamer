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
public class NxParticleUpdateData {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected NxParticleUpdateData(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(NxParticleUpdateData obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            JPhysXAdapterJNI.delete_NxParticleUpdateData(swigCPtr);
        }
        swigCPtr = 0;
    }

    public void setForceMode(int value) {
        JPhysXAdapterJNI.NxParticleUpdateData_forceMode_set(swigCPtr, this, value);
    }

    public int getForceMode() {
        return JPhysXAdapterJNI.NxParticleUpdateData_forceMode_get(swigCPtr, this);
    }

    public void setNumUpdates(long value) {
        JPhysXAdapterJNI.NxParticleUpdateData_numUpdates_set(swigCPtr, this, value);
    }

    public long getNumUpdates() {
        return JPhysXAdapterJNI.NxParticleUpdateData_numUpdates_get(swigCPtr, this);
    }

    public void setBufferForce(SWIGTYPE_p_float value) {
        JPhysXAdapterJNI.NxParticleUpdateData_bufferForce_set(swigCPtr, this, SWIGTYPE_p_float.getCPtr(value));
    }

    public SWIGTYPE_p_float getBufferForce() {
        long cPtr = JPhysXAdapterJNI.NxParticleUpdateData_bufferForce_get(swigCPtr, this);
        return (cPtr == 0) ? null : new SWIGTYPE_p_float(cPtr, false);
    }

    public void setBufferFlag(SWIGTYPE_p_unsigned_int value) {
        JPhysXAdapterJNI.NxParticleUpdateData_bufferFlag_set(swigCPtr, this, SWIGTYPE_p_unsigned_int.getCPtr(value));
    }

    public SWIGTYPE_p_unsigned_int getBufferFlag() {
        long cPtr = JPhysXAdapterJNI.NxParticleUpdateData_bufferFlag_get(swigCPtr, this);
        return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_int(cPtr, false);
    }

    public void setBufferId(SWIGTYPE_p_unsigned_int value) {
        JPhysXAdapterJNI.NxParticleUpdateData_bufferId_set(swigCPtr, this, SWIGTYPE_p_unsigned_int.getCPtr(value));
    }

    public SWIGTYPE_p_unsigned_int getBufferId() {
        long cPtr = JPhysXAdapterJNI.NxParticleUpdateData_bufferId_get(swigCPtr, this);
        return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_int(cPtr, false);
    }

    public void setBufferForceByteStride(long value) {
        JPhysXAdapterJNI.NxParticleUpdateData_bufferForceByteStride_set(swigCPtr, this, value);
    }

    public long getBufferForceByteStride() {
        return JPhysXAdapterJNI.NxParticleUpdateData_bufferForceByteStride_get(swigCPtr, this);
    }

    public void setBufferFlagByteStride(long value) {
        JPhysXAdapterJNI.NxParticleUpdateData_bufferFlagByteStride_set(swigCPtr, this, value);
    }

    public long getBufferFlagByteStride() {
        return JPhysXAdapterJNI.NxParticleUpdateData_bufferFlagByteStride_get(swigCPtr, this);
    }

    public void setBufferIdByteStride(long value) {
        JPhysXAdapterJNI.NxParticleUpdateData_bufferIdByteStride_set(swigCPtr, this, value);
    }

    public long getBufferIdByteStride() {
        return JPhysXAdapterJNI.NxParticleUpdateData_bufferIdByteStride_get(swigCPtr, this);
    }

    public void setToDefault() {
        JPhysXAdapterJNI.NxParticleUpdateData_setToDefault(swigCPtr, this);
    }

    public boolean isValid() {
        return JPhysXAdapterJNI.NxParticleUpdateData_isValid(swigCPtr, this);
    }

    public NxParticleUpdateData() {
        this(JPhysXAdapterJNI.new_NxParticleUpdateData(), true);
    }
}
