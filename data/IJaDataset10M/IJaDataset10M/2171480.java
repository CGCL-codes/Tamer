package edu.ucsd.ncmir.volume.volume;

import JSci.maths.Quaternion;

/**
 *
 * @author spl
 */
public class SliceDescriptor {

    private Quaternion _quaternion;

    private double _plane;

    private double _scale;

    public SliceDescriptor(Quaternion quaternion, double plane, double scale) {
        this._quaternion = quaternion;
        this._plane = plane;
        this._scale = scale;
    }

    public Quaternion getQuaternion() {
        return this._quaternion;
    }

    public double getPlane() {
        return this._plane;
    }

    public double getScale() {
        return this._scale;
    }

    @Override
    public String toString() {
        return "Quaternion " + this._quaternion.toString() + ", plane " + this._plane + ", scale " + this._scale;
    }

    @Override
    public boolean equals(Object o) {
        boolean equal = false;
        if ((o != null) && (o instanceof SliceDescriptor)) {
            SliceDescriptor sd = (SliceDescriptor) o;
            equal = this._quaternion.equals(sd._quaternion) && (this._plane == sd._plane) && (this._scale == sd._scale);
        }
        return equal;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this._quaternion.hashCode();
        hash = 17 * hash + this.dtlb(this._plane);
        hash = 17 * hash + this.dtlb(this._scale);
        return hash;
    }

    private int dtlb(double value) {
        return (int) (Double.doubleToLongBits(value) ^ (Double.doubleToLongBits(value) >>> 32));
    }
}
