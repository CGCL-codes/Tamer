package com.xith3d.spatial.bounds;

import javax.vecmath.*;
import com.xith3d.scenegraph.*;

/**
 * <p> </p>
 * <p> </p>
 * <p>Copyright (c) 2003 David Yazel Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author David Yazel
 * @version 1.0
 */
public abstract class Bounds {

    /**
     * sets the the value of this Bounds object to
     * enclode the specified bounding object
     */
    public abstract void set(Bounds boundsObject);

    /**
     * test for intersection with a ray
     */
    public abstract boolean intersect(Point3f origin, Vector3f direction);

    /**
     * test for intersection with a point
     */
    public abstract boolean intersect(Point3f point);

    /**
     * test for intersection with another Bounds object
     */
    public abstract boolean intersect(Bounds bo);

    /**
     * test for intersection with an array of Bounds objects
     */
    public abstract boolean intersect(Bounds[] bos);

    /**
     * combine this object with a bounding object
     */
    public abstract void combine(Bounds bo);

    /**
     * combine this object with an array of bounding objects
     */
    public abstract void combine(Bounds[] bos);

    /**
     * combine this object with a point
     */
    public abstract void combine(Point3f point);

    /**
     * combine this object with an array of points
     */
    public abstract void combine(Point3f[] points);

    /**
     * Transforms a Bounds object so that it bounds a vloume that
     * is the result of transforming the given bounding object by
     * the given transform.
     */
    public abstract void transform(Bounds bounds, Transform3D trans);

    /**
     * Transforms the Bounds object by the given transform.
     */
    public abstract void transform(Transform3D trans);
}
