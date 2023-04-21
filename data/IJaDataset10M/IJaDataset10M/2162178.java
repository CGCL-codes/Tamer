package com.vividsolutions.jump.geom;

import com.vividsolutions.jts.geom.*;

/**
 * An AffineTransform applies an affine transforms to a JTS Geometry.
 * The transform is done in-place. If the object must not be changed,
 * it should be cloned and the transform applied to the clone.
 *
 * <b>NOTE: INCOMPLETE IMPLEMENTATION</b>
 */
public class AffineTransform implements CoordinateFilter {

    private Coordinate transPt = null;

    public AffineTransform() {
    }

    /**
     * Append a translation to the transform.
     *
     * @param transPt the vector to translate by
     */
    public void translate(Coordinate p) {
        if (transPt == null) {
            transPt = new Coordinate(p);
        } else {
            transPt.x += p.x;
            transPt.y += p.y;
            transPt.z += p.z;
        }
    }

    public void apply(Geometry g) {
        g.apply(this);
    }

    public void filter(Coordinate coord) {
        coord.x += transPt.x;
        coord.y += transPt.y;
        coord.z += transPt.z;
    }
}
