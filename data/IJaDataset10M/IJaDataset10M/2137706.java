package org.jzy3d.plot3d.builder.delaunay;

import org.jzy3d.maths.Coordinates;
import org.jzy3d.plot3d.builder.CoordinateValidator;

/**
 *
 */
public class DelaunayCoordinateValidator implements CoordinateValidator {

    /**
     *
     * @throws an IllegalArgumentException: Any multiple occurrences in x and y
     * are removed internally. If subsequently the length of x or y differs
     * from z an IllegalArgumentException is thrown.
     *
     */
    public DelaunayCoordinateValidator(Coordinates coords) {
        if (coords == null) {
            throw new IllegalArgumentException("function call with illegal NULL value for parameter coords.");
        }
        if (coords.getX() == null || coords.getY() == null || coords.getZ() == null) {
            throw new IllegalArgumentException("illegal result value NULL value on calling: " + "getX()" + coords.getX() + ", getY()" + coords.getY() + ", getZ()" + coords.getZ());
        }
        if (coords.getX().length != coords.getY().length || coords.getX().length != coords.getZ().length) {
            throw new IllegalArgumentException("parameter coords must represent 3D coordinates: " + "x.length=" + coords.getX().length + ", y.length=" + coords.getY().length + ", z.length=" + coords.getZ().length);
        }
        x = coords.getX();
        y = coords.getY();
        z_as_fxy = setData(coords.getZ());
    }

    protected float[][] setData(float z[]) {
        int length = z.length;
        float[][] z_as_fxy = new float[length][length];
        for (int p = 0; p < length; p++) {
            z_as_fxy[p][p] = z[p];
        }
        return z_as_fxy;
    }

    /**
     *
     * @return
     */
    public float[][] get_Z_as_fxy() {
        return this.z_as_fxy;
    }

    /**
     *
     * @return
     */
    public float[] getX() {
        return this.x;
    }

    /**
     *
     * @return
     */
    public float[] getY() {
        return this.y;
    }

    private float x[];

    private float y[];

    private float z_as_fxy[][];
}
