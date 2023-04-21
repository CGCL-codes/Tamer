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
public class NxConvexForceFieldShapeDesc extends NxForceFieldShapeDesc {

    private long swigCPtr;

    protected NxConvexForceFieldShapeDesc(long cPtr, boolean cMemoryOwn) {
        super(JPhysXAdapterJNI.SWIGNxConvexForceFieldShapeDescUpcast(cPtr), cMemoryOwn);
        swigCPtr = cPtr;
    }

    protected static long getCPtr(NxConvexForceFieldShapeDesc obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            JPhysXAdapterJNI.delete_NxConvexForceFieldShapeDesc(swigCPtr);
        }
        swigCPtr = 0;
        super.delete();
    }

    public void setMeshData(NxConvexMesh value) {
        JPhysXAdapterJNI.NxConvexForceFieldShapeDesc_meshData_set(swigCPtr, this, NxConvexMesh.getCPtr(value), value);
    }

    public NxConvexMesh getMeshData() {
        long cPtr = JPhysXAdapterJNI.NxConvexForceFieldShapeDesc_meshData_get(swigCPtr, this);
        return (cPtr == 0) ? null : new NxConvexMesh(cPtr, false);
    }

    public NxConvexForceFieldShapeDesc() {
        this(JPhysXAdapterJNI.new_NxConvexForceFieldShapeDesc(), true);
    }

    public void setToDefault() {
        JPhysXAdapterJNI.NxConvexForceFieldShapeDesc_setToDefault(swigCPtr, this);
    }

    public boolean isValid() {
        return JPhysXAdapterJNI.NxConvexForceFieldShapeDesc_isValid(swigCPtr, this);
    }
}
