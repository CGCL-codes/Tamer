package com.vividsolutions.jts.operation.valid;

import com.vividsolutions.jts.geom.*;

/**
 * Contains information about the nature and location of a {@link Geometry}
 * validation error
 *
 * @version 1.7
 */
public class TopologyValidationError {

    /**
   * Not used
   * @deprecated
   */
    public static final int ERROR = 0;

    /**
   * No longer used - repeated points are considered valid as per the SFS
   * @deprecated
   */
    public static final int REPEATED_POINT = 1;

    /**
   * Indicates that a hole of a polygon lies partially or completely in the exterior of the shell
   */
    public static final int HOLE_OUTSIDE_SHELL = 2;

    /**
   * Indicates that a hole lies in the interior of another hole in the same polygon
   */
    public static final int NESTED_HOLES = 3;

    /**
   * Indicates that the interior of a polygon is disjoint
   * (often caused by set of contiguous holes splitting the polygon into two parts)
   */
    public static final int DISCONNECTED_INTERIOR = 4;

    /**
   * Indicates that two rings of a polygonal geometry intersect
   */
    public static final int SELF_INTERSECTION = 5;

    /**
   * Indicates that a ring self-intersects
   */
    public static final int RING_SELF_INTERSECTION = 6;

    /**
   * Indicates that a polygon component of a MultiPolygon lies inside another polygonal component
   */
    public static final int NESTED_SHELLS = 7;

    /**
   * Indicates that a polygonal geometry contains two rings which are identical
   */
    public static final int DUPLICATE_RINGS = 8;

    /**
   * Indicates that either
   * <ul>
   * <li>a LineString contains a single point
   * <li>a LinearRing contains 2 or 3 points
   * </ul>
   */
    public static final int TOO_FEW_POINTS = 9;

    /**
   * Indicates that the <code>X</code> or <code>Y</code> ordinate of
   * a Coordinate is not a valid numeric value (e.g. {@link Double#NaN} )
   */
    public static final int INVALID_COORDINATE = 10;

    /**
   * Indicates that a ring is not correctly closed
   * (the first and the last coordinate are different)
   */
    public static final int RING_NOT_CLOSED = 11;

    private static String[] errMsg = { "Topology Validation Error", "Repeated Point", "Hole lies outside shell", "Holes are nested", "Interior is disconnected", "Self-intersection", "Ring Self-intersection", "Nested shells", "Duplicate Rings", "Too few points in geometry component", "Invalid Coordinate", "Ring is not closed" };

    private int errorType;

    private Coordinate pt;

    /**
   * Creates a validation error with the given type and location
   *
   * @param errorType the type of the error
   * @param pt the location of the error
   */
    public TopologyValidationError(int errorType, Coordinate pt) {
        this.errorType = errorType;
        if (pt != null) this.pt = (Coordinate) pt.clone();
    }

    /**
   * Creates a validation error of the given type with a null location
   *
   * @param errorType the type of the error
   *
   */
    public TopologyValidationError(int errorType) {
        this(errorType, null);
    }

    /**
   * Returns the location of this error (on the {@link Geometry} containing the error).
   *
   * @return a {@link Coordinate} on the input geometry
   */
    public Coordinate getCoordinate() {
        return pt;
    }

    /**
   * Gets the type of this error.
   *
   * @return the error type
   */
    public int getErrorType() {
        return errorType;
    }

    /**
   * Gets an error message describing this error.
   * The error message does not describe the location of the error.
   *
   * @return
   */
    public String getMessage() {
        return errMsg[errorType];
    }

    /**
   * Gets a message describing the type and location of this error.
   * @return
   */
    public String toString() {
        String locStr = "";
        if (pt != null) locStr = " at or near point " + pt;
        return getMessage() + locStr;
    }
}
