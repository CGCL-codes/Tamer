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
public class NxEffectorDesc {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected NxEffectorDesc(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(NxEffectorDesc obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            JPhysXAdapterJNI.delete_NxEffectorDesc(swigCPtr);
        }
        swigCPtr = 0;
    }

    public void setUserData(SWIGTYPE_p_void value) {
        JPhysXAdapterJNI.NxEffectorDesc_userData_set(swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getUserData() {
        long cPtr = JPhysXAdapterJNI.NxEffectorDesc_userData_get(swigCPtr, this);
        return (cPtr == 0) ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public void setName(String value) {
        JPhysXAdapterJNI.NxEffectorDesc_name_set(swigCPtr, this, value);
    }

    public String getName() {
        return JPhysXAdapterJNI.NxEffectorDesc_name_get(swigCPtr, this);
    }

    public void setToDefault() {
        JPhysXAdapterJNI.NxEffectorDesc_setToDefault(swigCPtr, this);
    }

    public boolean isValid() {
        return JPhysXAdapterJNI.NxEffectorDesc_isValid(swigCPtr, this);
    }

    public int getType() {
        return JPhysXAdapterJNI.NxEffectorDesc_getType(swigCPtr, this);
    }
}
