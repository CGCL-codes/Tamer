package com.jme.image;

import java.io.IOException;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;

/**
 * 
 * @author Joshua Slack
 *
 */
public class Texture2D extends Texture {

    private WrapMode wrapS = WrapMode.EdgeClamp;

    private WrapMode wrapT = WrapMode.EdgeClamp;

    public Texture createSimpleClone() {
        return createSimpleClone(new Texture2D());
    }

    @Override
    public Texture createSimpleClone(Texture rVal) {
        rVal.setWrap(WrapAxis.S, wrapS);
        rVal.setWrap(WrapAxis.T, wrapT);
        return super.createSimpleClone(rVal);
    }

    /**
     * <code>setWrap</code> sets the wrap mode of this texture for a
     * particular axis.
     * 
     * @param axis
     *            the texture axis to define a wrapmode on.
     * @param mode
     *            the wrap mode for the given axis of the texture.
     * @throws IllegalArgumentException
     *             if axis or mode are null
     */
    public void setWrap(WrapAxis axis, WrapMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("mode can not be null.");
        } else if (axis == null) {
            throw new IllegalArgumentException("axis can not be null.");
        }
        switch(axis) {
            case S:
                this.wrapS = mode;
                break;
            case T:
                this.wrapT = mode;
                break;
        }
    }

    /**
     * <code>setWrap</code> sets the wrap mode of this texture for all axis.
     * 
     * @param mode
     *            the wrap mode for the given axis of the texture.
     * @throws IllegalArgumentException
     *             if mode is null
     */
    public void setWrap(WrapMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("mode can not be null.");
        }
        this.wrapS = mode;
        this.wrapT = mode;
    }

    /**
     * <code>getWrap</code> returns the wrap mode for a given coordinate axis
     * on this texture.
     * 
     * @param axis
     *            the axis to return for
     * @return the wrap mode of the texture.
     * @throws IllegalArgumentException
     *             if axis is null
     */
    public WrapMode getWrap(WrapAxis axis) {
        switch(axis) {
            case S:
                return wrapS;
            case T:
                return wrapT;
        }
        throw new IllegalArgumentException("invalid WrapAxis: " + axis);
    }

    @Override
    public Type getType() {
        return Type.TwoDimensional;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Texture2D)) {
            return false;
        }
        Texture2D that = (Texture2D) other;
        if (this.getWrap(WrapAxis.S) != that.getWrap(WrapAxis.S)) return false;
        if (this.getWrap(WrapAxis.T) != that.getWrap(WrapAxis.T)) return false;
        return super.equals(other);
    }

    @Override
    public void write(JMEExporter e) throws IOException {
        super.write(e);
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(wrapS, "wrapS", WrapMode.EdgeClamp);
        capsule.write(wrapT, "wrapT", WrapMode.EdgeClamp);
    }

    @Override
    public void read(JMEImporter e) throws IOException {
        super.read(e);
        InputCapsule capsule = e.getCapsule(this);
        wrapS = capsule.readEnum("wrapS", WrapMode.class, WrapMode.EdgeClamp);
        wrapT = capsule.readEnum("wrapT", WrapMode.class, WrapMode.EdgeClamp);
    }
}
