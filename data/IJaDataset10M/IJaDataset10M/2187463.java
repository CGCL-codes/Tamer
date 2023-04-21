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
public class NxMaterial {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected NxMaterial(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(NxMaterial obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    public synchronized void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            throw new UnsupportedOperationException("C++ destructor does not have public access");
        }
        swigCPtr = 0;
    }

    public int getMaterialIndex() {
        return JPhysXAdapterJNI.NxMaterial_getMaterialIndex(swigCPtr, this);
    }

    public void loadFromDesc(NxMaterialDesc desc) {
        JPhysXAdapterJNI.NxMaterial_loadFromDesc(swigCPtr, this, NxMaterialDesc.getCPtr(desc), desc);
    }

    public void saveToDesc(NxMaterialDesc desc) {
        JPhysXAdapterJNI.NxMaterial_saveToDesc(swigCPtr, this, NxMaterialDesc.getCPtr(desc), desc);
    }

    public NxScene getScene() {
        return new NxScene(JPhysXAdapterJNI.NxMaterial_getScene(swigCPtr, this), false);
    }

    public void setDynamicFriction(float coef) {
        JPhysXAdapterJNI.NxMaterial_setDynamicFriction(swigCPtr, this, coef);
    }

    public float getDynamicFriction() {
        return JPhysXAdapterJNI.NxMaterial_getDynamicFriction(swigCPtr, this);
    }

    public void setStaticFriction(float coef) {
        JPhysXAdapterJNI.NxMaterial_setStaticFriction(swigCPtr, this, coef);
    }

    public float getStaticFriction() {
        return JPhysXAdapterJNI.NxMaterial_getStaticFriction(swigCPtr, this);
    }

    public void setRestitution(float rest) {
        JPhysXAdapterJNI.NxMaterial_setRestitution(swigCPtr, this, rest);
    }

    public float getRestitution() {
        return JPhysXAdapterJNI.NxMaterial_getRestitution(swigCPtr, this);
    }

    public void setDynamicFrictionV(float coef) {
        JPhysXAdapterJNI.NxMaterial_setDynamicFrictionV(swigCPtr, this, coef);
    }

    public float getDynamicFrictionV() {
        return JPhysXAdapterJNI.NxMaterial_getDynamicFrictionV(swigCPtr, this);
    }

    public void setStaticFrictionV(float coef) {
        JPhysXAdapterJNI.NxMaterial_setStaticFrictionV(swigCPtr, this, coef);
    }

    public float getStaticFrictionV() {
        return JPhysXAdapterJNI.NxMaterial_getStaticFrictionV(swigCPtr, this);
    }

    public void setDirOfAnisotropy(NxVec3 vec) {
        JPhysXAdapterJNI.NxMaterial_setDirOfAnisotropy(swigCPtr, this, NxVec3.getCPtr(vec), vec);
    }

    public NxVec3 getDirOfAnisotropy() {
        return new NxVec3(JPhysXAdapterJNI.NxMaterial_getDirOfAnisotropy(swigCPtr, this), true);
    }

    public void setFlags(long flags) {
        JPhysXAdapterJNI.NxMaterial_setFlags(swigCPtr, this, flags);
    }

    public long getFlags() {
        return JPhysXAdapterJNI.NxMaterial_getFlags(swigCPtr, this);
    }

    public void setFrictionCombineMode(int combMode) {
        JPhysXAdapterJNI.NxMaterial_setFrictionCombineMode(swigCPtr, this, combMode);
    }

    public int getFrictionCombineMode() {
        return JPhysXAdapterJNI.NxMaterial_getFrictionCombineMode(swigCPtr, this);
    }

    public void setRestitutionCombineMode(int combMode) {
        JPhysXAdapterJNI.NxMaterial_setRestitutionCombineMode(swigCPtr, this, combMode);
    }

    public int getRestitutionCombineMode() {
        return JPhysXAdapterJNI.NxMaterial_getRestitutionCombineMode(swigCPtr, this);
    }

    public void setUserData(SWIGTYPE_p_void value) {
        JPhysXAdapterJNI.NxMaterial_userData_set(swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
    }

    public SWIGTYPE_p_void getUserData() {
        long cPtr = JPhysXAdapterJNI.NxMaterial_userData_get(swigCPtr, this);
        return (cPtr == 0) ? null : new SWIGTYPE_p_void(cPtr, false);
    }
}
