package de.grogra.ext.sunshine.objects;

import javax.vecmath.Matrix4d;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

/**
 * @author Thomas
 * 
 */
public class SunshineParalellogram extends SunshineObject {

    private float length;

    private float ior = 1f;

    private Vector3f axis;

    private boolean isLight;

    public SunshineParalellogram(float length, Vector3f axis, boolean light) {
        this.length = length;
        this.axis = axis;
        isLight = light;
    }

    public int getID() {
        return PARA_ID;
    }

    @Override
    public void setTransformMatrix(Matrix4d m) {
        transformMatrix = new Matrix4f();
        transformMatrix.set(m);
        Matrix4f sm = new Matrix4f();
        sm.setIdentity();
        sm.m00 *= axis.x;
        sm.m22 *= length;
        transformMatrix.mul(sm);
    }

    public boolean isLight() {
        return isLight;
    }

    public void setIOR(float ior) {
        this.ior = ior;
    }
}
