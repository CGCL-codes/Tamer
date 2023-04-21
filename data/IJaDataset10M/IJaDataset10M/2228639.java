package com.vividsolutions.jump.workbench.ui.renderer.java2D;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Converts JTS Geometry objects into Java 2D Shape objects
 */
public class Java2DConverter {

    private static double POINT_MARKER_SIZE = 3.0;

    private PointConverter pointConverter;

    public Java2DConverter(PointConverter pointConverter) {
        this.pointConverter = pointConverter;
    }

    private Shape toShape(Polygon p) throws NoninvertibleTransformException {
        ArrayList holeVertexCollection = new ArrayList();
        for (int j = 0; j < p.getNumInteriorRing(); j++) {
            holeVertexCollection.add(toViewCoordinates(p.getInteriorRingN(j).getCoordinates()));
        }
        return new PolygonShape(toViewCoordinates(p.getExteriorRing().getCoordinates()), holeVertexCollection);
    }

    public Coordinate[] toViewCoordinates(Coordinate[] modelCoordinates) throws NoninvertibleTransformException {
        Coordinate[] viewCoordinates = new Coordinate[modelCoordinates.length];
        double ps = 1d / (2d * pointConverter.getScale());
        Coordinate p0 = modelCoordinates[0];
        int npts = 0;
        int mpts = modelCoordinates.length;
        for (int i = 0; i < mpts; i++) {
            Coordinate pi = modelCoordinates[i];
            double xd = Math.abs(p0.x - pi.x);
            double yd = Math.abs(p0.y - pi.y);
            if ((xd >= ps) || (yd >= ps) || (npts < 4) || (i == mpts - 1)) {
                Point2D point2D = pointConverter.toViewPoint(pi);
                viewCoordinates[npts++] = new Coordinate(point2D.getX(), point2D.getY());
                p0 = pi;
            }
        }
        if (npts != mpts) {
            Coordinate[] viewCoordinates2 = new Coordinate[npts];
            for (int i = 0; i < npts; i++) {
                viewCoordinates2[i] = viewCoordinates[i];
            }
            return viewCoordinates2;
        } else return viewCoordinates;
    }

    private Shape toShape(GeometryCollection gc) throws NoninvertibleTransformException {
        GeometryCollectionShape shape = new GeometryCollectionShape();
        for (int i = 0; i < gc.getNumGeometries(); i++) {
            Geometry g = (Geometry) gc.getGeometryN(i);
            shape.add(toShape(g));
        }
        return shape;
    }

    private Path2D.Double toShape(MultiLineString mls) throws NoninvertibleTransformException {
        Path2D.Double path = new Path2D.Double();
        for (int i = 0; i < mls.getNumGeometries(); i++) {
            LineString lineString = (LineString) mls.getGeometryN(i);
            path.append(toShape(lineString), false);
        }
        return path;
    }

    class LineStringPath implements PathIterator {

        private int iterate;

        private int numPoints;

        private Coordinate[] points;

        private boolean closed;

        public LineStringPath(LineString linestring, Java2DConverter j2D) {
            try {
                points = j2D.toViewCoordinates(linestring.getCoordinates());
            } catch (NoninvertibleTransformException ex) {
            }
            this.numPoints = points.length;
            iterate = 0;
            closed = (numPoints > 1) && (points[0].equals2D(points[numPoints - 1]));
        }

        private int getSegType() {
            if (closed && (iterate == numPoints - 1)) return PathIterator.SEG_CLOSE;
            return (iterate == 0) ? PathIterator.SEG_MOVETO : PathIterator.SEG_LINETO;
        }

        public int currentSegment(double[] coords) {
            coords[0] = points[iterate].x;
            coords[1] = points[iterate].y;
            return getSegType();
        }

        public int currentSegment(float[] coords) {
            coords[0] = (float) points[iterate].x;
            coords[1] = (float) points[iterate].y;
            return getSegType();
        }

        public int getWindingRule() {
            return GeneralPath.WIND_NON_ZERO;
        }

        public boolean isDone() {
            return !(iterate < numPoints);
        }

        public void next() {
            iterate++;
        }
    }

    private Path2D.Double toShape(LineString lineString) throws NoninvertibleTransformException {
        int numPoints = lineString.getNumPoints();
        Path2D.Double shape = new Path2D.Double(GeneralPath.WIND_NON_ZERO, numPoints);
        PathIterator pi = new LineStringPath(lineString, this);
        shape.append(pi, false);
        return shape;
    }

    private Shape toShape(Point point) throws NoninvertibleTransformException {
        Rectangle2D.Double pointMarker = new Rectangle2D.Double(0.0, 0.0, POINT_MARKER_SIZE, POINT_MARKER_SIZE);
        Point2D viewPoint = toViewPoint(point.getCoordinate());
        pointMarker.x = (double) (viewPoint.getX() - (POINT_MARKER_SIZE / 2));
        pointMarker.y = (double) (viewPoint.getY() - (POINT_MARKER_SIZE / 2));
        return pointMarker;
    }

    private Point2D toViewPoint(Coordinate modelCoordinate) throws NoninvertibleTransformException {
        Point2D viewPoint = pointConverter.toViewPoint(modelCoordinate);
        return viewPoint;
    }

    public static interface PointConverter {

        public Point2D toViewPoint(Coordinate modelCoordinate) throws NoninvertibleTransformException;

        public double getScale();
    }

    /**
	 * If you pass in a general GeometryCollection, note that a Shape cannot
	 * preserve information about which elements are 1D and which are 2D.
	 * For example, if you pass in a GeometryCollection containing a ring and a
	 * disk, you cannot render them as such: if you use Graphics.fill, you'll get
	 * two disks, and if you use Graphics.draw, you'll get two rings. Solution:
	 * create Shapes for each element.
	 */
    public Shape toShape(Geometry geometry) throws NoninvertibleTransformException {
        if (geometry.isEmpty()) {
            return new GeneralPath();
        }
        if (geometry instanceof Polygon) {
            return toShape((Polygon) geometry);
        }
        if (geometry instanceof MultiPolygon) {
            return toShape((MultiPolygon) geometry);
        }
        if (geometry instanceof LineString) {
            return toShape((LineString) geometry);
        }
        if (geometry instanceof MultiLineString) {
            return toShape((MultiLineString) geometry);
        }
        if (geometry instanceof Point) {
            return toShape((Point) geometry);
        }
        if (geometry instanceof GeometryCollection) {
            return toShape((GeometryCollection) geometry);
        }
        throw new IllegalArgumentException("Unrecognized Geometry class: " + geometry.getClass());
    }
}
