package com.cube42.util.math;

import java.io.Serializable;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import com.cube42.util.exception.Cube42NullParameterException;

/**
 * Represents the orientation of a 3d object in space
 *
 * @author  Matt Paulin
 * @version $Id: Orientation.java,v 1.8 2003/03/12 00:28:09 zer0wing Exp $
 */
public class Orientation implements Serializable {

    /**
     * Constant that is use to convert degrees into radians
     */
    private static double RADIAN_CONVERSION = 0.0174532925194;

    /**
     * The rotation of the transform in radians
     */
    private transient double totalRotZ;

    /**
     * Transform use to store the orientation
     */
    private Matrix3d matrix;

    /**
     * Constructs the Orientation class
     */
    public Orientation() {
        super();
        this.totalRotZ = 0;
        this.matrix = new Matrix3d();
        this.matrix.setIdentity();
    }

    /**
     * Returns the angle turned in radians.  This angle is the angle wrapped
     * around the vector using the right hand rule
     *
     * @return  The angle
     */
    public double getAngle() {
        double[] a = new double[4];
        this.getAxisAngle4d().get(a);
        return a[3];
    }

    /**
     * Returns the unit vector that that in conjunction with the angle will tell
     * how the orientation is set
     *
     * @param   The unit vector telling what axis the orientation is turned
     *          around
     */
    public Vector3d getRotationVector() {
        double[] a = new double[4];
        this.getAxisAngle4d().get(a);
        return new Vector3d(a[0], a[1], a[2]);
    }

    /**
     * Turns the orientation to its left in degrees
     *
     * @param   degrees     The number of degrees to turn to the left
     */
    public void turnLeftDegrees(double degrees) {
        totalRotZ = totalRotZ + (degrees * RADIAN_CONVERSION);
        this.matrix.rotZ(this.totalRotZ);
    }

    /**
     * Turns the orientation to its right in degrees
     *
     * @param   degrees     The number of degrees to turn to the right
     */
    public void turnRightDegrees(double degrees) {
        totalRotZ = totalRotZ - (degrees * RADIAN_CONVERSION);
        this.matrix.rotZ(this.totalRotZ);
    }

    /**
     * Transforms a point3d by this rotation
     *
     * @param   The point3D that it to be set to this orientation
     */
    public void transform(Point3d point) {
        Cube42NullParameterException.checkNull(point, "point", "transform", this);
        this.matrix.transform(point);
    }

    /**
     * Transforms a vector3d by this rotation
     * <p>
     * The result is placed back in this vector
     *
     * @param   vector  the vector to transform
     * @returns The transformed vector3d
     */
    public void transform(Vector3d vector) {
        Cube42NullParameterException.checkNull(vector, "vector", "transform", this);
        this.matrix.transform(vector);
    }

    /**
     * Returns true if the orientation is set to the identity
     *
     * @return  true if the matrix is an identity matrix
     */
    public boolean isIdentity() {
        Matrix3d temp = new Matrix3d();
        temp.setIdentity();
        return this.matrix.equals(temp);
    }

    /**
     * Adds the provided orientation to this orientation and storing the result
     * as this orientation
     *
     * @param   orientation The new orientation to add
     */
    public void addOrientation(Orientation orientation) {
        Cube42NullParameterException.checkNull(orientation, "orientation", "addOrientation", this);
        this.matrix.mul(orientation.matrix);
    }

    /**
     * Sets the orientation to the provided AxisAngle4d
     *
     * @param   axisAngle4d     The axis angle to set this matrix too
     */
    public void setAxisAngle4d(AxisAngle4d axisAngle4d) {
        Cube42NullParameterException.checkNull(axisAngle4d, "axisAngle4d", "setAxisAngle4d", this);
        this.matrix.set(axisAngle4d);
    }

    /**
     * Returns an AxisAngle4D describing the current orientation
     *
     * @return  The AxisAngle4D describing the orientation
     */
    public AxisAngle4d getAxisAngle4d() {
        AxisAngle4d tempAngle = new AxisAngle4d();
        tempAngle.set(this.matrix);
        return tempAngle;
    }

    /**
     * Turns the Orientation into a string
     *
     * @return  A string depicting the Orientation
     */
    public String toString() {
        return this.matrix.toString();
    }

    /**
     * Returns a new instance of the orientation
     *
     * @return  A new instance of the orientation
     */
    public Orientation cloneOrientation() {
        Orientation ori = new Orientation();
        ori.matrix = new Matrix3d(this.matrix);
        return ori;
    }

    /**
     * Returns a Matrix3D representing this orientation
     *
     * @return  A matrix3d representing this orientation
     */
    public Matrix3d getMatrix3d() {
        return this.matrix;
    }
}
