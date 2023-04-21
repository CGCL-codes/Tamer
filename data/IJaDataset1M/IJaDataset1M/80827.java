package com.jme.scene.state.lwjgl.records;

import com.jme.math.Matrix4f;
import com.jme.math.Quaternion;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.StateRecord;

public class LightRecord extends StateRecord {

    public ColorRGBA ambient = new ColorRGBA(-1, -1, -1, -1);

    public ColorRGBA diffuse = new ColorRGBA(-1, -1, -1, -1);

    public ColorRGBA specular = new ColorRGBA(-1, -1, -1, -1);

    private float constant = -1;

    private float linear = -1;

    private float quadratic = -1;

    private float spotExponent = -1;

    private float spotCutoff = -1;

    private boolean enabled = false;

    public Quaternion position = new Quaternion();

    public Matrix4f modelViewMatrix = new Matrix4f();

    private boolean attenuate;

    public boolean isAttenuate() {
        return attenuate;
    }

    public void setAttenuate(boolean attenuate) {
        this.attenuate = attenuate;
    }

    public float getConstant() {
        return constant;
    }

    public void setConstant(float constant) {
        this.constant = constant;
    }

    public float getLinear() {
        return linear;
    }

    public void setLinear(float linear) {
        this.linear = linear;
    }

    public float getQuadratic() {
        return quadratic;
    }

    public void setQuadratic(float quadratic) {
        this.quadratic = quadratic;
    }

    public float getSpotExponent() {
        return spotExponent;
    }

    public void setSpotExponent(float exponent) {
        this.spotExponent = exponent;
    }

    public float getSpotCutoff() {
        return spotCutoff;
    }

    public void setSpotCutoff(float spotCutoff) {
        this.spotCutoff = spotCutoff;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        ambient.set(-1, -1, -1, -1);
        diffuse.set(-1, -1, -1, -1);
        specular.set(-1, -1, -1, -1);
        constant = -1;
        linear = -1;
        quadratic = -1;
        spotExponent = -1;
        spotCutoff = -1;
        enabled = false;
        position.set(-1, -1, -1, -1);
        modelViewMatrix.loadIdentity();
    }
}
