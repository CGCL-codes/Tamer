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
public class NxDebugPoint {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected NxDebugPoint(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(NxDebugPoint obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            JPhysXAdapterJNI.delete_NxDebugPoint(swigCPtr);
        }
        swigCPtr = 0;
    }

    public void setP(NxVec3 value) {
        JPhysXAdapterJNI.NxDebugPoint_p_set(swigCPtr, this, NxVec3.getCPtr(value), value);
    }

    public NxVec3 getP() {
        long cPtr = JPhysXAdapterJNI.NxDebugPoint_p_get(swigCPtr, this);
        return (cPtr == 0) ? null : new NxVec3(cPtr, false);
    }

    public void setColor(long value) {
        JPhysXAdapterJNI.NxDebugPoint_color_set(swigCPtr, this, value);
    }

    public long getColor() {
        return JPhysXAdapterJNI.NxDebugPoint_color_get(swigCPtr, this);
    }

    public NxDebugPoint() {
        this(JPhysXAdapterJNI.new_NxDebugPoint(), true);
    }
}
