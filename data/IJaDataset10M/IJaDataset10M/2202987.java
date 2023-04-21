package org.vizzini.ui.graphics.shape;

import org.vizzini.math.Quaternion;
import org.vizzini.math.Vector;
import org.vizzini.ui.graphics.IShape;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides a polygon shape for the 3D graphics system. For this object the
 * points and screenPoints are interchangeable.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.2
 */
public final class PolygonShape extends AbstractShape {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(PolygonShape.class.getName());

    /** Point array used in drawOn(). */
    private static int[] _polyX = new int[4];

    /** Point array used in drawOn(). */
    private static int[] _polyY = new int[4];

    /** Highlight color. */
    private Color _highlightColor = Color.BLACK;

    /** Highlight segment indices. */
    private int[] _highlightSegment = null;

    /**
     * Construct this object.
     *
     * @since  v0.2
     */
    public PolygonShape() {
        super(0.0, 0.0, 0.0);
    }

    /**
     * Construct this object with the given parameters.
     *
     * @param  color   Color.
     * @param  points  Points.
     *
     * @since  v0.2
     */
    public PolygonShape(final Color color, List<Vector> points) {
        this(color, points, false);
    }

    /**
     * Construct this object with the given parameters.
     *
     * @param  color        Color.
     * @param  points       Points.
     * @param  isWireframe  Flag indicating if the polygon is wireframe or
     *                      solid.
     *
     * @since  v0.2
     */
    public PolygonShape(final Color color, List<Vector> points, boolean isWireframe) {
        super(0.0, 0.0, 0.0);
        setColor(color);
        initPoints(points);
        _isWireframe = isWireframe;
        getState().setPosition(Vector.ZERO);
    }

    /**
     * @see  org.vizzini.ui.graphics.shape.AbstractShape#compareTo(org.vizzini.ui.graphics.IShape)
     */
    @Override
    public int compareTo(IShape object) {
        if (this == object) {
            return 0;
        }
        PolygonShape another = (PolygonShape) object;
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.logp(Level.FINEST, getClass().getName(), "compareTo", "_screenCenter = " + _screenCenter);
            LOGGER.logp(Level.FINEST, getClass().getName(), "compareTo", "another._screenCenter = " + another._screenCenter);
        }
        if (getScreenZ() < another.getScreenZ()) {
            return 1;
        }
        return -1;
    }

    /**
     * Compute the points of this shape in screen coordinates. Override the
     * super method since this object's points are already in screen
     * coordinates.
     *
     * @param  q        The parent quaternion.
     * @param  v        The parent position in world coordinates..
     * @param  magnify  The magnification.
     * @param  offsetX  The offset X coordinate.
     * @param  offsetY  The offset Y coordinate.
     * @param  d        Perspective constant.
     *
     * @since  v0.2
     */
    @Override
    public void computeScreenPoints(Quaternion q, Vector v, double magnify, int offsetX, int offsetY, double d) {
    }

    /**
     * @see  org.vizzini.ui.graphics.shape.AbstractShape#contains(java.awt.Point)
     */
    @Override
    public boolean contains(Point point) {
        return toPolygon().contains(point);
    }

    /**
     * @see  org.vizzini.ui.graphics.shape.AbstractShape#drawOn(java.awt.Graphics,
     *       double)
     */
    @Override
    public void drawOn(Graphics g, double magnify) {
        double xc = _screenCenter.getX();
        double yc = _screenCenter.getY();
        for (int i = 0; i < _points.length; i++) {
            _polyX[i] = (int) (_screenPoints[i].getX() + xc);
            _polyY[i] = (int) (_screenPoints[i].getY() + yc);
        }
        if (_isWireframe) {
            g.setColor(getColor());
            g.drawPolygon(_polyX, _polyY, _points.length);
        } else {
            double z = computeNormalZComponent(0, 1, 2);
            if (z < 0.0) {
                z *= -1.0;
            }
            g.setColor(getColorForIntensity(z));
            g.fillPolygon(_polyX, _polyY, _points.length);
        }
        if (_highlightSegment != null) {
            int x1 = _polyX[_highlightSegment[0]];
            int y1 = _polyY[_highlightSegment[0]];
            int x2 = _polyX[_highlightSegment[1]];
            int y2 = _polyY[_highlightSegment[1]];
            g.setColor(_highlightColor);
            g.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * Initialize this polygon with the given points.
     *
     * @param  points  Points.
     *
     * @since  v0.2
     */
    public void initPoints(List<Vector> points) {
        int numPoints = points.size();
        _points = new Vector[numPoints];
        _screenPoints = _points;
        if (numPoints > _polyX.length) {
            _polyX = new int[numPoints];
        }
        if (numPoints > _polyY.length) {
            _polyY = new int[numPoints];
        }
        for (int i = 0; i < numPoints; i++) {
            _points[i] = points.get(i);
        }
        _zNormal = Double.MAX_VALUE;
    }

    /**
     * Set the highlight color.
     *
     * @param  color  Color.
     *
     * @since  v0.2
     */
    public void setHighlightColor(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("color == null");
        }
        _highlightColor = color;
    }

    /**
     * Set a segment for highlighting.
     *
     * @param  node1  The index of the start point.
     * @param  node2  The index of the end point.
     *
     * @since  v0.2
     */
    public void setHighlightSegment(final int node1, final int node2) {
        _highlightSegment = new int[2];
        _highlightSegment[0] = node1;
        _highlightSegment[1] = node2;
    }

    /**
     * @return  a <code>java.awt.Polygon</code> instance of this.
     *
     * @since   v0.2
     */
    public Polygon toPolygon() {
        double xc = _screenCenter.getX();
        double yc = _screenCenter.getY();
        Polygon polygon = new Polygon();
        for (int i = 0; i < _screenPoints.length; i++) {
            polygon.addPoint((int) (_screenPoints[i].getX() + xc), (int) (_screenPoints[i].getY() + yc));
        }
        return polygon;
    }

    /**
     * @see  org.vizzini.ui.graphics.shape.AbstractShape#toString()
     */
    @Override
    public String toString() {
        return "Polygon: " + _points.length + " [" + getColor() + "]" + _screenPoints[0] + ", " + _screenPoints[1];
    }

    /**
     * @return  the Z coordinate in screen coordinates.
     *
     * @since   v0.2
     */
    protected double getScreenZ() {
        double z = 0.0;
        for (int i = 0; i < _screenPoints.length; i++) {
            Vector vec = _screenPoints[i];
            z += vec.getZ();
        }
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.logp(Level.FINEST, getClass().getName(), "computeScreenCenter", "x, y, z = " + _screenCenter);
        }
        z /= _screenPoints.length;
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.logp(Level.FINEST, getClass().getName(), "getScreenZ", "z = " + z);
        }
        return z;
    }
}
