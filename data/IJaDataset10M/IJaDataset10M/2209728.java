package com.vividsolutions.jts.operation.valid;

import com.vividsolutions.jts.geom.*;

/**
 * Implements the appropriate checks for repeated points
 * (consecutive identical coordinates) as defined in the
 * JTS spec.
 *
 * @version 1.7
 */
public class RepeatedPointTester {

    private Coordinate repeatedCoord;

    public RepeatedPointTester() {
    }

    public Coordinate getCoordinate() {
        return repeatedCoord;
    }

    public boolean hasRepeatedPoint(Geometry g) {
        if (g.isEmpty()) return false;
        if (g instanceof Point) return false; else if (g instanceof MultiPoint) return false; else if (g instanceof LineString) return hasRepeatedPoint(((LineString) g).getCoordinates()); else if (g instanceof Polygon) return hasRepeatedPoint((Polygon) g); else if (g instanceof GeometryCollection) return hasRepeatedPoint((GeometryCollection) g); else throw new UnsupportedOperationException(g.getClass().getName());
    }

    public boolean hasRepeatedPoint(Coordinate[] coord) {
        for (int i = 1; i < coord.length; i++) {
            if (coord[i - 1].equals(coord[i])) {
                repeatedCoord = coord[i];
                return true;
            }
        }
        return false;
    }

    private boolean hasRepeatedPoint(Polygon p) {
        if (hasRepeatedPoint(p.getExteriorRing().getCoordinates())) return true;
        for (int i = 0; i < p.getNumInteriorRing(); i++) {
            if (hasRepeatedPoint(p.getInteriorRingN(i).getCoordinates())) return true;
        }
        return false;
    }

    private boolean hasRepeatedPoint(GeometryCollection gc) {
        for (int i = 0; i < gc.getNumGeometries(); i++) {
            Geometry g = gc.getGeometryN(i);
            if (hasRepeatedPoint(g)) return true;
        }
        return false;
    }
}
