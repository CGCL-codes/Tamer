package org.gwtsvg.client.path;

import org.gwtsvg.client.Coordinate;

/**
 *
 */
public class PathQuadraticBezierStart extends Coordinate {

    protected PathQuadraticBezierStart(double x, double y, String unit) {
        super(x, y, unit);
    }

    protected PathQuadraticBezierStart(Coordinate coordinate) {
        super(coordinate);
    }

    public String getCoordinate() {
        return "T " + super.getCoordinate();
    }
}
