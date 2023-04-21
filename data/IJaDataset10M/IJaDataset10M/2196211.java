package org.zkforge.canvas;

import java.awt.Rectangle;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.json.*;

/**
 * @author simon
 *
 */
public class Path extends Shape {

    /**
	 * Creates an empty Path.
	 */
    public Path() {
        super();
        _internalShape = new Path2D.Double();
    }

    /**
	 * Creates a Path based on a java.awt.geom.Path2D.Double object. 
	 */
    public Path(Path2D.Double path) {
        super();
        _internalShape = new Path2D.Double(path);
    }

    /**
	 * Create a Path by cloning another Path.
	 */
    public Path(Path path) {
        this((Path2D.Double) path._internalShape);
    }

    @Override
    public String getType() {
        return "path";
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONAware getShapeJSONObject() {
        PathIterator pi = _internalShape.getPathIterator(null);
        float[] coords = new float[6];
        List<PathSegment> segments = new ArrayList<PathSegment>();
        while (!pi.isDone()) {
            int type = pi.currentSegment(coords);
            segments.add(new PathSegment(type, coords));
            pi.next();
        }
        JSONObject result = new JSONObject();
        result.put("sg", segments);
        return result;
    }

    @Override
    public Object clone() {
        return new Path(this);
    }

    private class PathSegment implements JSONAware {

        private static final String MOVE_TO = "mv";

        private static final String LINE_TO = "ln";

        private static final String QUAD_TO = "qd";

        private static final String BEZIER_TO = "bz";

        private static final String CLOSE = "cl";

        private static final String TYPE_KEY = "tp";

        private static final String DATA_KEY = "dt";

        private String _type;

        private float[] _coords;

        private PathSegment(int type, float[] coords) {
            int dataSize = 0;
            switch(type) {
                case PathIterator.SEG_MOVETO:
                    _type = MOVE_TO;
                    dataSize = 2;
                    break;
                case PathIterator.SEG_LINETO:
                    _type = LINE_TO;
                    dataSize = 2;
                    break;
                case PathIterator.SEG_QUADTO:
                    _type = QUAD_TO;
                    dataSize = 4;
                    break;
                case PathIterator.SEG_CUBICTO:
                    _type = BEZIER_TO;
                    dataSize = 6;
                    break;
                case PathIterator.SEG_CLOSE:
                    _type = CLOSE;
                    dataSize = 0;
                    break;
            }
            _coords = new float[dataSize];
            for (int i = 0; i < dataSize; i++) _coords[i] = coords[i];
        }

        public String toJSONString() {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put(TYPE_KEY, _type);
            m.put(DATA_KEY, _coords);
            return JSONObject.toJSONString(m);
        }
    }

    public final Path lineTo(double x, double y) {
        ((Path2D.Double) _internalShape).lineTo(x, y);
        return this;
    }

    public final Path moveTo(double x, double y) {
        ((Path2D.Double) _internalShape).moveTo(x, y);
        return this;
    }

    public final Path quadTo(double x1, double y1, double x2, double y2) {
        ((Path2D.Double) _internalShape).quadTo(x1, y1, x2, y2);
        return this;
    }

    public final Path curveTo(double x1, double y1, double x2, double y2, double x3, double y3) {
        ((Path2D.Double) _internalShape).curveTo(x1, y1, x2, y2, x3, y3);
        return this;
    }

    public final void reset() {
        ((Path2D.Double) _internalShape).reset();
    }

    public final Path append(PathIterator pi, boolean connect) {
        ((Path2D.Double) _internalShape).append(pi, connect);
        return this;
    }

    public final Path append(java.awt.Shape s, boolean connect) {
        ((Path2D.Double) _internalShape).append(s, connect);
        return this;
    }

    public final Path closePath() {
        ((Path2D.Double) _internalShape).closePath();
        return this;
    }

    public final void transform(AffineTransform at) {
        ((Path2D.Double) _internalShape).transform(at);
    }

    public final boolean contains(double x, double y, double w, double h) {
        return ((Path2D.Double) _internalShape).contains(x, y, w, h);
    }

    public final boolean contains(double x, double y) {
        return ((Path2D.Double) _internalShape).contains(x, y);
    }

    public final boolean contains(Point2D p) {
        return ((Path2D.Double) _internalShape).contains(p);
    }

    public final boolean contains(Rectangle2D r) {
        return ((Path2D.Double) _internalShape).contains(r);
    }

    public final boolean intersects(double x, double y, double w, double h) {
        return ((Path2D.Double) _internalShape).intersects(x, y, w, h);
    }

    public final boolean intersects(Rectangle2D r) {
        return ((Path2D.Double) _internalShape).intersects(r);
    }

    public final java.awt.Shape createTransformedShape(AffineTransform at) {
        return ((Path2D.Double) _internalShape).createTransformedShape(at);
    }

    public final Rectangle getBounds() {
        return ((Path2D.Double) _internalShape).getBounds();
    }

    public final Rectangle2D getBounds2D() {
        return ((Path2D.Double) _internalShape).getBounds2D();
    }

    public final Point2D getCurrentPoint() {
        return ((Path2D.Double) _internalShape).getCurrentPoint();
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return ((Path2D.Double) _internalShape).getPathIterator(at, flatness);
    }

    public PathIterator getPathIterator(AffineTransform at) {
        return ((Path2D.Double) _internalShape).getPathIterator(at);
    }

    public final int getWindingRule() {
        return ((Path2D.Double) _internalShape).getWindingRule();
    }

    public final void setWindingRule(int rule) {
        ((Path2D.Double) _internalShape).setWindingRule(rule);
    }

    public int hashCode() {
        return ((Path2D.Double) _internalShape).hashCode();
    }

    public boolean equals(Object obj) {
        return ((Path2D.Double) _internalShape).equals(obj);
    }

    public String toString() {
        return ((Path2D.Double) _internalShape).toString();
    }
}
