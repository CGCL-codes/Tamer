package uk.ac.rdg.resc.edal.geometry.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import uk.ac.rdg.resc.edal.geometry.HorizontalPosition;
import uk.ac.rdg.resc.ncwms.exceptions.InvalidCrsException;
import uk.ac.rdg.resc.ncwms.util.WmsUtils;

/**
 * Represents a path through a coordinate system.  The path consists of a set
 * of linear path elements, drawn between <i>control points</i>.
 * @todo This shares things in common with GeoAPI's {@link LineString} and
 * GeoTools' LineStringImpl - can we reuse these classes?  The problem
 * is that the Geo* classes are based upon different components, e.g. DirectPositions,
 * and seem to lack a method for finding coordinates along the path.
 * @author Jon
 */
public final class LineString {

    private final List<HorizontalPosition> controlPoints;

    private final double[] controlPointDistances;

    private double pathLength;

    private final CoordinateReferenceSystem crs;

    /**
     * Constructs a {@link LineString} from a line string in the form.
     * @param lineStringSpec the line string as specified in the form
     * "x1 y1, x2 y2, x3 y3".
     * @param crs The coordinate reference system for the line string's
     * coordinates
     * @throws InvalidLineStringException if the line string is not correctly
     * specified.
     * @throws InvalidCrsException 
     * @throws NullPointerException if crsHelper == null
     */
    public LineString(String lineStringSpec, String crsCode, String wmsVersion) throws InvalidLineStringException, InvalidCrsException {
        String[] pointsStr = lineStringSpec.split(",");
        if (pointsStr.length < 2) {
            throw new InvalidLineStringException("At least two points are required to generate a transect");
        }
        if (crsCode == null) {
            throw new NullPointerException("CRS cannot be null");
        }
        this.crs = WmsUtils.getCrs(crsCode);
        int lonIndex = 0;
        int latIndex = 1;
        if (crsCode != null && crsCode.equalsIgnoreCase("EPSG:4326") && wmsVersion != null && wmsVersion.equalsIgnoreCase("1.3.0")) {
            latIndex = 0;
            lonIndex = 1;
        }
        final List<HorizontalPosition> ctlPoints = new ArrayList<HorizontalPosition>();
        for (String s : pointsStr) {
            String[] coords = s.trim().split(" +");
            if (coords.length != 2) {
                throw new InvalidLineStringException("Coordinates format error");
            }
            try {
                ctlPoints.add(new HorizontalPositionImpl(Double.parseDouble(coords[lonIndex].trim()), Double.parseDouble(coords[latIndex].trim()), crs));
            } catch (NumberFormatException nfe) {
                throw new InvalidLineStringException("Coordinates format error");
            }
        }
        this.controlPoints = Collections.unmodifiableList(ctlPoints);
        this.controlPointDistances = new double[this.controlPoints.size()];
        this.pathLength = 0.0;
        this.controlPointDistances[0] = this.pathLength;
        for (int i = 1; i < this.controlPoints.size(); i++) {
            HorizontalPosition p1 = this.controlPoints.get(i - 1);
            HorizontalPosition p2 = this.controlPoints.get(i);
            double dx = p2.getX() - p1.getX();
            double dy = p2.getY() - p1.getY();
            this.pathLength += Math.sqrt(dx * dx + dy * dy);
            this.controlPointDistances[i] = this.pathLength;
        }
    }

    /**
     * Returns the list of control points along this line string.
     * @return an unmodifiable list of control points.
     */
    public List<HorizontalPosition> getControlPoints() {
        return this.controlPoints;
    }

    /**
     * Returns the fractional distance along the line string to the control
     * point with the given index.
     * @index The index of the control point.  An index of zero represents the
     * start of the line string.
     * @return the fractional distance along the whole line string to the
     * control point
     * @throws IndexOutOfBoundsException if {@code index < 0 || index >= number of control points}
     */
    public double getFractionalControlPointDistance(int index) {
        if (index < 0 || index >= this.controlPointDistances.length) {
            throw new IndexOutOfBoundsException();
        }
        return this.controlPointDistances[index] / this.pathLength;
    }

    /**
     * Returns a list of <i>n</i> points along the path defined by this line
     * string.  The first point will be the first control point; the last point
     * will be the last control point.  Intermediate points are linearly
     * interpolated between the control points.  Note that it is not guaranteed
     * that the intermediate control points will be contained in this list.
     * @param numPoints The number of points to return
     * @return an unmodifiable list of {@code n} points that lie on this path.
     * @throws IllegalArgumentException if {@code numPoints < 2}
     * @todo Add the control points to this list, in the correct location.
     */
    public List<HorizontalPosition> getPointsOnPath(int n) {
        if (n < 2) {
            throw new IllegalArgumentException("Must request at least 2 points");
        }
        final List<HorizontalPosition> points = new ArrayList<HorizontalPosition>(n);
        points.add(this.controlPoints.get(0));
        for (int i = 1; i < n - 1; i++) {
            double s = this.pathLength * i / (n - 1);
            points.add(this.interpolatePoint(s));
        }
        points.add(this.controlPoints.get(this.controlPoints.size() - 1));
        return Collections.unmodifiableList(points);
    }

    /**
     * Given a length <i>s</i> along the path defined by this line string, this
     * method returns a {@link HorizontalPosition} that represents this point on the
     * path.
     * @param s the distance along the path
     * @return a HorizontalPosition representing this point on the path.
     * @throws IllegalArgumentException if s < 0 or s > pathLength
     */
    private HorizontalPosition interpolatePoint(double s) {
        if (s < 0.0 || s > this.pathLength) {
            throw new IllegalArgumentException("s does not lie on the path");
        }
        int i = this.getPreviousControlPointIndex(s);
        double dlast = s - this.controlPointDistances[i];
        double dnext = this.controlPointDistances[i + 1] - s;
        double dfrac = dlast / (dlast + dnext);
        HorizontalPosition cplast = this.controlPoints.get(i);
        HorizontalPosition cpnext = this.controlPoints.get(i + 1);
        double x = (1.0 - dfrac) * cplast.getX() + dfrac * cpnext.getX();
        double y = (1.0 - dfrac) * cplast.getY() + dfrac * cpnext.getY();
        return new HorizontalPositionImpl(x, y, this.crs);
    }

    /**
     * If we walk a distance <i>s</i> along the path defined by this line string,
     * this method returns the index <i>i</i> of the last control point we passed.
     * I.e. the point at distance <i>s</i> lies between control point <i>i</i>
     * and control point <i>i</i> + 1.
     * @param s The distance along the path, which must lie between 0 and
     * pathLength (checked before this method is called).
     * @return the index of the previous control point, where 0 <= i < numControlPoints - 1.
     */
    private int getPreviousControlPointIndex(double s) {
        for (int i = 1; i < this.controlPointDistances.length; i++) {
            if (this.controlPointDistances[i] > s) return i - 1;
        }
        throw new AssertionError();
    }

    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return this.crs;
    }
}
