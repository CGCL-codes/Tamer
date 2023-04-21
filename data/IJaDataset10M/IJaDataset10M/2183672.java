package org.vizzini.ui.graphics.shape;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vizzini.math.DynamicState;
import org.vizzini.math.IState;
import org.vizzini.math.IStateful;
import org.vizzini.math.Quaternion;
import org.vizzini.math.State;
import org.vizzini.math.StatefulSupport;
import org.vizzini.math.Vector;
import org.vizzini.ui.ColorCache;
import org.vizzini.ui.graphics.IShape;
import org.vizzini.ui.graphics.ShapeGroup;
import org.vizzini.util.EmptyUtilities;

/**
 * Provides base functionality for 3D shapes. The coordinate system is screen
 * based (+Z into the screen).
 *
 * <pre>
         ^ +Z
        /
       /
      +-----> +X
      |
      |
      V +Y
 * </pre>
 *
 * Other shapes can be placed "on" or relative to this one with <code>
 * add</code>. Specify their center with <code>setCenter</code> relative to
 * this.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.2
 */
public abstract class AbstractShape implements IShape {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(AbstractShape.class.getName());

    /** Color cache. */
    private static ColorCache _colorCache = new ColorCache();

    /** Flag indicating whether to draw this as a wireframe or solid. */
    protected boolean _isWireframe = false;

    /** The absolute point relative to the center. */
    protected Vector[] _points;

    /** The center in screen coordinates. */
    protected Vector _screenCenter = Vector.ZERO;

    /** The shape's points in screen coordinates. */
    protected Vector[] _screenPoints;

    /** Collection of child shapes to this shape. */
    protected ShapeGroup _shapeGroup;

    /** The z normal value. */
    protected double _zNormal = Double.MAX_VALUE;

    /** This shape's color. */
    private Color _color = Color.white;

    /** Extent. */
    private Vector _dimensions;

    /** Empty utilities object. */
    private EmptyUtilities _emptyUtilities;

    /** Flag indicating whether this is a light source. */
    private boolean _isLightSource;

    /** Flag indicating if this is visible. */
    private boolean _isVisible = true;

    /** Name. */
    private String _name;

    /** Stateful delegate. */
    private StatefulSupport _statefulSupport = createStatefulSupport();

    /** Texture filepath. */
    private String _textureFilepath;

    /**
     * Construct this object with the given parameters.
     *
     * @param  width   Width.
     * @param  height  Height.
     * @param  depth   Depth.
     *
     * @since  v0.2
     */
    public AbstractShape(double width, double height, double depth) {
        _dimensions = new Vector(width, height, depth);
    }

    /**
     * Add the given shape as a child of this.
     *
     * @param  shape  The new child shape.
     *
     * @since  v0.2
     */
    public void add(IShape shape) {
        getShapeGroup().add(shape);
    }

    /**
     * Clear all child shapes.
     *
     * @since  v0.2
     */
    public void clear() {
        if (_shapeGroup != null) {
            _shapeGroup.clear();
        }
    }

    /**
     * Compare this shape to the given object.
     *
     * @param   object  Another shape.
     *
     * @return  an integer indicating order.
     *
     * @since   v0.2
     */
    public int compareTo(IShape object) {
        if (this == object) {
            return 0;
        }
        AbstractShape another = (AbstractShape) object;
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.logp(Level.FINEST, getClass().getName(), "compareTo", "_screenCenter = " + _screenCenter);
            LOGGER.logp(Level.FINEST, getClass().getName(), "compareTo", "another._screenCenter = " + another._screenCenter);
        }
        double diff = (another._screenCenter.getZ() - _screenCenter.getZ());
        int answer = 0;
        if (diff != 0.0) {
            return (int) (diff / Math.abs(diff));
        }
        return answer;
    }

    /**
     * @param   i1  First index.
     * @param   i2  Second index.
     * @param   i3  Third index.
     *
     * @return  the normal z component of the plane define by the points with
     *          the given indices in screen coordinates.
     *
     * @since   v0.2
     */
    public final double computeNormalZComponent(final int i1, final int i2, final int i3) {
        if (_zNormal == Double.MAX_VALUE) {
            Vector v1 = _screenPoints[i2].subtract(_screenPoints[i1]);
            Vector v2 = _screenPoints[i3].subtract(_screenPoints[i2]);
            Vector normal = v1.cross(v2).unit();
            _zNormal = normal.getZ();
        }
        return _zNormal;
    }

    /**
     * Compute the points of this shape in screen coordinates.
     *
     * @param  q        Rotation of the parent to world coordinates.
     * @param  v        The parent position in world coordinates..
     * @param  magnify  The magnification.
     * @param  offsetX  The offset X coordinate.
     * @param  offsetY  The offset Y coordinate.
     * @param  d        Perspective constant.
     *
     * @since  v0.2
     */
    public void computeScreenPoints(Quaternion q, Vector v, double magnify, int offsetX, int offsetY, double d) {
        Quaternion q2 = _statefulSupport.getState().getOrientation().multiply(q);
        for (int i = 0; i < _points.length; i++) {
            _screenPoints[i] = q2.preMultiply(_points[i]);
        }
        for (int i = 0; i < _points.length; i++) {
            double factor = d / (d + _screenPoints[i].getZ());
            _screenPoints[i] = _screenPoints[i].multiply(factor / magnify);
        }
        Vector center = computeScreenCenter(q, v, magnify, offsetX, offsetY);
        computeCenteredScreenPoints();
        if (_shapeGroup != null) {
            _shapeGroup.computeScreenPoints(q2, center, magnify, offsetX, offsetY, d);
        }
        _zNormal = Double.MAX_VALUE;
    }

    /**
     * @param   point  Point.
     *
     * @return  true if the given point is within this shape.
     *
     * @since   v0.2
     */
    public boolean contains(Point point) {
        boolean answer = false;
        Polygon polygon = new Polygon();
        for (int i = 0; i < _screenPoints.length; i++) {
            polygon.addPoint((int) _screenPoints[i].getX(), (int) _screenPoints[i].getY());
        }
        answer = polygon.contains(point);
        return answer;
    }

    /**
     * Draw this shape on the given graphics with the given magnification.
     *
     * @param  g        The graphics.
     * @param  magnify  The magnification.
     *
     * @since  v0.2
     */
    public void drawOn(Graphics g, double magnify) {
        drawOn(g, magnify, _isWireframe);
        if (_shapeGroup != null) {
            _shapeGroup.setSorted(false);
            _shapeGroup.drawOn(g, magnify);
        }
    }

    /**
     * @return  the absolute state.
     *
     * @since   v0.2
     */
    public IState getAbsoluteState() {
        return _statefulSupport.getAbsoluteState();
    }

    /**
     * @return  the bounding box of the screen coordinates.
     *
     * @since   v0.2
     */
    public Rectangle getBounds() {
        Rectangle answer = null;
        if (_screenPoints.length > 0) {
            double xMin = _screenPoints[0].getX();
            double xMax = _screenPoints[0].getX();
            double yMin = _screenPoints[0].getY();
            double yMax = _screenPoints[0].getY();
            double x;
            double y;
            for (int i = 1; i < _screenPoints.length; i++) {
                x = _screenPoints[i].getX();
                y = _screenPoints[i].getY();
                xMin = Math.min(xMin, x);
                xMax = Math.max(xMax, x);
                yMin = Math.min(yMin, y);
                yMax = Math.max(yMax, y);
            }
            answer = new Rectangle((int) xMin, (int) yMin, (int) (xMax - xMin), (int) (yMax - yMin));
        }
        return answer;
    }

    /**
     * @return  the child shapes.
     *
     * @since   v0.2
     */
    public ShapeGroup getChildren() {
        return getShapeGroup();
    }

    /**
     * @return  the color of this shape.
     *
     * @since   v0.2
     */
    public Color getColor() {
        return _color;
    }

    /**
     * @return  depth.
     *
     * @since   v0.3
     */
    public double getDepth() {
        return _dimensions.getZ();
    }

    /**
     * @return  the dimensions in world coordinates.
     *
     * @since   v0.3
     */
    public Vector getDimensions() {
        return _dimensions;
    }

    /**
     * @return  height.
     *
     * @since   v0.3
     */
    public double getHeight() {
        return _dimensions.getY();
    }

    /**
     * @return  the minimum dimension.
     *
     * @since   v0.3
     */
    public double getMinDimension() {
        double answer = Math.min(_dimensions.getX(), _dimensions.getY());
        answer = Math.min(answer, _dimensions.getZ());
        return answer;
    }

    /**
     * @return  name.
     *
     * @since   v0.3
     */
    public String getName() {
        if (_name == null) {
            return getClass().getName();
        }
        return _name;
    }

    /**
     * @return  this shape's center in screen coordinates.
     *
     * @since   v0.2
     */
    public Vector getScreenCenter() {
        return _screenCenter;
    }

    /**
     * @return  the shape group.
     *
     * @since   v0.2
     */
    public ShapeGroup getShapeGroup() {
        if (_shapeGroup == null) {
            _shapeGroup = new ShapeGroup();
        }
        return _shapeGroup;
    }

    /**
     * @return  the state.
     *
     * @since   v0.2
     */
    public IState getState() {
        return _statefulSupport.getState();
    }

    /**
     * @return  the parent.
     *
     * @since   v0.2
     */
    public IStateful getStatefulParent() {
        return _statefulSupport.getStatefulParent();
    }

    /**
     * @return  textureFilepath.
     *
     * @since   v0.3
     */
    public String getTextureFilepath() {
        return _textureFilepath;
    }

    /**
     * @return  width.
     *
     * @since   v0.3
     */
    public double getWidth() {
        return _dimensions.getX();
    }

    /**
     * @return  isLightSource.
     *
     * @since   v0.3
     */
    public boolean isLightSource() {
        return _isLightSource;
    }

    /**
     * @return  true if a texture filepath is set.
     *
     * @since   v0.3
     */
    public boolean isTextureFilepathSet() {
        return (!getEmptyUtilities().isNullOrEmpty(getTextureFilepath()));
    }

    /**
     * @return  true if this is visible.
     *
     * @since   v0.2
     */
    public boolean isVisible() {
        return _isVisible;
    }

    /**
     * @return  true if this is wireframe.
     *
     * @since   v0.2
     */
    public boolean isWireframe() {
        return _isWireframe;
    }

    /**
     * Remove the given child shape.
     *
     * @param  shape  Shape.
     *
     * @since  v0.2
     */
    public void remove(IShape shape) {
        getShapeGroup().remove(shape);
    }

    /**
     * Set the color.
     *
     * @param  color  Color.
     *
     * @since  v0.2
     */
    public void setColor(Color color) {
        _color = color;
    }

    /**
     * @param  isLightSource  the isLightSource to set
     *
     * @since  v0.3
     */
    public void setLightSource(boolean isLightSource) {
        _isLightSource = isLightSource;
    }

    /**
     * @param  name  the name to set
     *
     * @since  v0.3
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Set the parent.
     *
     * @param  parent  Parent.
     *
     * @since  v0.2
     */
    public void setStatefulParent(IStateful parent) {
        _statefulSupport.setStatefulParent(parent);
    }

    /**
     * @param  textureFilepath  the textureFilepath to set
     *
     * @since  v0.3
     */
    public void setTextureFilepath(String textureFilepath) {
        _textureFilepath = textureFilepath;
    }

    /**
     * Set the visibility flag. If false, neither this shape nor its children
     * are drawn.
     *
     * @param  isVisible  Flag indicating whether this is visible.
     *
     * @since  v0.2
     */
    public void setVisible(boolean isVisible) {
        _isVisible = isVisible;
    }

    /**
     * Set the wireframe flag.
     *
     * @param  isWireframe  Flag indicating whether this is drawn as wireframe.
     *
     * @since  v0.2
     */
    public void setWireframe(boolean isWireframe) {
        _isWireframe = isWireframe;
    }

    /**
     * @see  java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(" [");
        sb.append("_screenCenter=").append(_screenCenter);
        sb.append(",center=").append(_statefulSupport.getState().getPosition());
        sb.append("]");
        return sb.toString();
    }

    /**
     * Translate the screen points by the screen center.
     *
     * @since  v0.2
     */
    protected void computeCenteredScreenPoints() {
        for (int i = 0; i < _screenPoints.length; i++) {
            _screenPoints[i] = _screenPoints[i].add(_screenCenter);
        }
    }

    /**
     * @param   q        The parent quaternion.
     * @param   v        The parent center.
     * @param   magnify  The magnification.
     * @param   offsetX  The offset X coordinate.
     * @param   offsetY  The offset Y coordinate.
     *
     * @return  the center in screen coordinates.
     *
     * @since   v0.2
     */
    protected Vector computeScreenCenter(Quaternion q, Vector v, double magnify, int offsetX, int offsetY) {
        Vector center = _statefulSupport.getState().getPosition().divide(magnify);
        Vector answer = q.preMultiply(center);
        _screenCenter = new Vector(v.getX() + offsetX + answer.getX(), v.getY() + offsetY + answer.getY(), v.getZ() + answer.getZ());
        return answer;
    }

    /**
     * @return  a new2 state.
     *
     * @since   v0.2
     */
    protected IState createState() {
        return new State();
    }

    /**
     * @return  a stateful support object.
     *
     * @since   v0.2
     */
    protected StatefulSupport createStatefulSupport() {
        return new StatefulSupport() {

            /** Serial version UID. */
            private static final long serialVersionUID = 1L;

            @Override
            protected IState createState() {
                return new DynamicState();
            }
        };
    }

    /**
     * Draw this shape on the given graphics with the given magnification.
     * Subclasses should override this method if their solid and wireframe
     * representations are the same.
     *
     * @param  g            The graphics.
     * @param  magnify      The magnification.
     * @param  isWireframe  Flag indicating if this shape is wireframe or solid.
     *
     * @since  v0.2
     */
    protected void drawOn(Graphics g, double magnify, boolean isWireframe) {
        if (_isVisible) {
            if (isWireframe) {
                drawOnWireframe(g, magnify);
            } else {
                drawOnSolid(g, magnify);
            }
        }
    }

    /**
     * Draw a solid representation of this shape on the given graphics with the
     * given magnification.
     *
     * @param  g        The graphics.
     * @param  magnify  The magnification.
     *
     * @since  v0.2
     */
    protected void drawOnSolid(Graphics g, double magnify) {
        throw new RuntimeException("Shape.drawOnSolid() is not implemented for " + getClass().getName());
    }

    /**
     * Draw a wireframe representation of this shape on the given graphics with
     * the given magnification.
     *
     * @param  g        The graphics.
     * @param  magnify  The magnification.
     *
     * @since  v0.2
     */
    protected void drawOnWireframe(Graphics g, double magnify) {
        throw new RuntimeException("Shape.drawOnWireframe() is not implemented for " + getClass().getName());
    }

    /**
     * @param   intensity  The desired color intensity between 0.0 and 1.0.
     *
     * @return  this shape's color scaled by the given intensity.
     *
     * @since   v0.2
     */
    protected Color getColorForIntensity(final double intensity) {
        if ((intensity < 0.0) || (1.0 < intensity)) {
            throw new IllegalArgumentException("intensity out of range [0.0,1.0] " + intensity);
        }
        int r = (int) (_color.getRed() * intensity);
        int g = (int) (_color.getGreen() * intensity);
        int b = (int) (_color.getBlue() * intensity);
        return _colorCache.get(r, g, b, _color.getAlpha());
    }

    /**
     * @return  the emptyUtilities
     */
    protected EmptyUtilities getEmptyUtilities() {
        if (_emptyUtilities == null) {
            _emptyUtilities = new EmptyUtilities();
        }
        return _emptyUtilities;
    }

    /**
     * @param   s  Shape to compare.
     *
     * @return  true if the given shape is in front of this.
     *
     * @since   v0.2
     */
    protected boolean isInFrontOf(IShape s) {
        AbstractShape shape = (AbstractShape) s;
        return (_screenCenter.getZ() < shape._screenCenter.getZ());
    }
}
