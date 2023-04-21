package model.style;

import math.geom2d.curve.Curve2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.Domain2D;

/**
 * @author dlegland
 *
 */
public class DefaultMarker implements Marker {

    Curve2D shape;

    Boundary2D boundary = null;

    boolean fillable = false;

    public DefaultMarker(Curve2D shape) {
        this.shape = shape;
    }

    public DefaultMarker(Curve2D shape, Boundary2D boundary) {
        this.shape = shape;
        this.boundary = boundary;
        this.fillable = true;
    }

    public DefaultMarker(Domain2D domain) {
        this.boundary = domain.getBoundary();
        this.shape = this.boundary;
        this.fillable = true;
    }

    public Curve2D getShape() {
        return shape;
    }

    public Boundary2D getFillBoundary() {
        return boundary;
    }

    public boolean isFillable() {
        return fillable;
    }
}
