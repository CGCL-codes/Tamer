package edu.ucsd.ncmir.volume.volume;

import JSci.maths.vectors.DoubleVector;

/**
* The HomogeneousPoint class provides an object for encapsulating a 3D location in
* homogeneous coordinates.
* @version 1.0
* @author Steve Lamont
*/
public class HomogeneousPoint extends DoubleVector {

    private static final long serialVersionUID = 42L;

    /**
     * Constructs a 3D homogeneous vector.
     * @param x x component.
     * @param y y component.
     * @param z z component.
     */
    public HomogeneousPoint(double x, double y, double z) {
        super(4);
        this.setComponent(0, x);
        this.setComponent(1, y);
        this.setComponent(2, z);
        this.setComponent(3, 1.0);
    }

    /**
     * Returns the x, y, and z components in a user provided array.
     * @param xyz array of length 3.
     */
    public void get(double[] xyz) {
        xyz[0] = this.getComponent(0);
        xyz[1] = this.getComponent(1);
        xyz[2] = this.getComponent(2);
    }

    /**
     * Returns the x, y, and z components in an allocated array.
     * @return array of length 3.
     */
    public double[] get() {
        double[] xyz = new double[3];
        this.get(xyz);
        return xyz;
    }

    @Override
    public String toString() {
        return String.format("[%10.4f, %10.4f, %10.4f]", this.getComponent(0), this.getComponent(1), this.getComponent(2));
    }
}
