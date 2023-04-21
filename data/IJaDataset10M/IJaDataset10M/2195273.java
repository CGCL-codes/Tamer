package net.claribole.zvtm.layout.jung;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import com.xerox.VTM.engine.LongPoint;
import com.xerox.VTM.engine.AnimManager;
import com.xerox.VTM.glyphs.VPath;
import com.xerox.VTM.glyphs.VSegment;
import com.xerox.VTM.glyphs.VSegmentST;
import net.claribole.zvtm.glyphs.DPath;
import net.claribole.zvtm.glyphs.DPathST;
import net.claribole.zvtm.glyphs.VPathST;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.visualization.Coordinates;
import edu.uci.ics.jung.visualization.AbstractLayout;
import edu.uci.ics.jung.utils.Pair;
import edu.uci.ics.jung.graph.decorators.EdgeShape;
import edu.uci.ics.jung.graph.decorators.EdgeShapeFunction;
import edu.uci.ics.jung.graph.decorators.EllipseVertexShapeFunction;
import edu.uci.ics.jung.graph.decorators.VertexShapeFunction;
import edu.uci.ics.jung.graph.decorators.ConstantVertexAspectRatioFunction;
import edu.uci.ics.jung.graph.decorators.ConstantVertexSizeFunction;

public class EdgeTransformer {

    public static final short EDGE_LINE = 0;

    public static final short EDGE_QUAD_CURVE = 2;

    public static final short EDGE_CUBIC_CURVE = 3;

    protected static VertexShapeFunction vertexShapeFunction = new EllipseVertexShapeFunction(new ConstantVertexSizeFunction(20), new ConstantVertexAspectRatioFunction(1.0f));

    public static void setVertexShapeFunction(VertexShapeFunction vsf) {
        vertexShapeFunction = vsf;
    }

    public static VertexShapeFunction getVertexShapeFunction() {
        return vertexShapeFunction;
    }

    /** Get a VPath representing a given edge in the graph.
	 *@param e the Jung edge
	 *@param l the layout that produced the graph's geometry
	 *@param edgeShape the edge type requested, one of EdgeTransformer.EDGE_*
	 *@param c stroke color of VPath object
	 *@param translucence true if the returned VPath should be a VPathST (with a default alpha of 1.0)
	 *@return null if edgeShape does not correspond to a known edge shape
	 *@see #getDPath(Edge e, AbstractLayout l, short edgeShape, Color c)
	 */
    public static VPath getVPath(Edge e, AbstractLayout l, short edgeShape, Color c, boolean translucence) {
        switch(edgeShape) {
            case EDGE_LINE:
                {
                    return getLineAsVPath(e, l, c, translucence);
                }
            case EDGE_QUAD_CURVE:
                {
                    return getQuadCurveAsVPath(e, l, c, translucence);
                }
            case EDGE_CUBIC_CURVE:
                {
                    return getCubicCurveAsVPath(e, l, c, translucence);
                }
            default:
                {
                    return null;
                }
        }
    }

    /** Get a DPath representing a given edge in the graph.
	 *@param e the Jung edge
	 *@param l the layout that produced the graph's geometry
	 *@param edgeShape the edge type requested, one of EdgeTransformer.EDGE_*
	 *@param c stroke color of DPath object
	 *@param translucence true if the returned VPath should be a VPathST (with a default alpha of 1.0)
	 *@return null if edgeShape does not correspond to a known edge shape
	 *@see #getVPath(Edge e, AbstractLayout l, short edgeShape, Color c)
	 */
    public static DPath getDPath(Edge e, AbstractLayout l, short edgeShape, Color c, boolean translucence) {
        switch(edgeShape) {
            case EDGE_LINE:
                {
                    return getLineAsDPath(e, l, c, translucence);
                }
            case EDGE_QUAD_CURVE:
                {
                    return getQuadCurveAsDPath(e, l, c, translucence);
                }
            case EDGE_CUBIC_CURVE:
                {
                    return getCubicCurveAsDPath(e, l, c, translucence);
                }
            default:
                {
                    return null;
                }
        }
    }

    /** Get a straight VPath representing a given edge in the graph.
	 *@param e the Jung edge
	 *@param l the layout that produced the graph's geometry
	 *@param c stroke color of VPath object
	 *@param translucence true if the returned VPath should be a VPathST (with a default alpha of 1.0)
	 *@return null if edgeShape does not correspond to a known edge shape
	 *@see #getLineAsDPath(Edge e, AbstractLayout l, Color c)
	 *@see #getLineAsVSegment(Edge e, AbstractLayout l, Color c)
	 */
    public static VPath getLineAsVPath(Edge e, AbstractLayout l, Color c, boolean translucence) {
        Shape s = (new EdgeShape.Line()).getShape(e);
        if (s instanceof Ellipse2D) {
            return getShapeAsVPath(e, l, c, s, translucence);
        }
        Line2D curve = (Line2D) s;
        double[] src = { curve.getX1(), curve.getY1(), curve.getX2(), curve.getY2() };
        double[] tgt = new double[4];
        getTransform(e, l, curve).transform(src, 0, tgt, 0, 2);
        VPath p = (translucence) ? new VPathST(Math.round(tgt[0]), Math.round(tgt[1]), 0, c, 1.0f) : new VPath(Math.round(tgt[0]), Math.round(tgt[1]), 0, c);
        p.addSegment(Math.round(tgt[2]), Math.round(tgt[3]), true);
        return p;
    }

    /** Get a straight DPath representing a given edge in the graph.
	 *@param e the Jung edge
	 *@param l the layout that produced the graph's geometry
	 *@param c stroke color of DPath object
	 *@param translucence true if the returned VPath should be a VPathST (with a default alpha of 1.0)
	 *@return null if edgeShape does not correspond to a known edge shape
	 *@see #getLineAsVPath(Edge e, AbstractLayout l, Color c)
	 *@see #getLineAsVSegment(Edge e, AbstractLayout l, Color c)
	 */
    public static DPath getLineAsDPath(Edge e, AbstractLayout l, Color c, boolean translucence) {
        Shape s = (new EdgeShape.Line()).getShape(e);
        if (s instanceof Ellipse2D) {
            return getShapeAsDPath(e, l, c, s, translucence);
        }
        Line2D curve = (Line2D) s;
        double[] src = { curve.getX1(), curve.getY1(), curve.getX2(), curve.getY2() };
        double[] tgt = new double[4];
        getTransform(e, l, curve).transform(src, 0, tgt, 0, 2);
        DPath p = (translucence) ? new DPathST(Math.round(tgt[0]), Math.round(tgt[1]), 0, c, 1.0f) : new DPath(Math.round(tgt[0]), Math.round(tgt[1]), 0, c);
        p.addSegment(Math.round(tgt[2]), Math.round(tgt[3]), true);
        return p;
    }

    /** Get a VSegment representing a given edge in the graph.
	 *@param e the Jung edge
	 *@param l the layout that produced the graph's geometry
	 *@param c stroke color of VSegment object
	 *@param translucence true if the returned VPath should be a VPathST (with a default alpha of 1.0)
	 *@return null if edgeShape does not correspond to a known edge shape, or if the edge is a self-loop, in which case one should use getLineAsDPath or getLineAsVPath
	 *@see #getLineAsDPath(Edge e, AbstractLayout l, Color c)
	 *@see #getLineAsVPath(Edge e, AbstractLayout l, Color c)
	 */
    public static VSegment getLineAsVSegment(Edge e, AbstractLayout l, Color c, boolean translucence) {
        Shape shp = (new EdgeShape.Line()).getShape(e);
        if (shp instanceof Ellipse2D) {
            return null;
        }
        Line2D curve = (Line2D) shp;
        double[] src = { curve.getX1(), curve.getY1(), curve.getX2(), curve.getY2() };
        double[] tgt = new double[4];
        getTransform(e, l, curve).transform(src, 0, tgt, 0, 2);
        VSegment s = (translucence) ? new VSegmentST(Math.round(tgt[0]), Math.round(tgt[1]), 0, c, Math.round(tgt[2]), Math.round(tgt[3]), 1.0f) : new VSegment(Math.round(tgt[0]), Math.round(tgt[1]), 0, c, Math.round(tgt[2]), Math.round(tgt[3]));
        return s;
    }

    /** Update the position of an existing straight DPath representing a given edge in the graph.
	 *@param e the Jung edge
	 *@param l the layout that produced the graph's geometry
	 *@param p the DPath representing this edge
	 *@param animDuration the duration of the animation to transition from the old position to the new one. Put 0 if no animation should be run.
	 *@param animator the ZVTM AnimManager instantiated by VirtualSpaceManager. Usually VirtualSpaceManager.animator. Can be null if animDuration == 0.
	 *@see #updateLine(Edge e, AbstractLayout l, VSegment s)
	 */
    public static void updateLine(Edge e, AbstractLayout l, DPath p, int animDuration, AnimManager animator) {
        Shape s = (new EdgeShape.Line()).getShape(e);
        if (s instanceof Ellipse2D) {
            updateShape(e, l, p, animDuration, animator, s);
            return;
        }
        Line2D curve = (Line2D) s;
        double[] src = { curve.getX1(), curve.getY1(), curve.getX2(), curve.getY2() };
        double[] tgt = new double[4];
        getTransform(e, l, curve).transform(src, 0, tgt, 0, 2);
        LongPoint[] coords = { new LongPoint(Math.round(tgt[0]), Math.round(tgt[1])), new LongPoint(Math.round(tgt[2]), Math.round(tgt[3])) };
        if (animDuration > 0) {
            animator.createPathAnimation(animDuration, AnimManager.DP_TRANS_SIG_ABS, coords, p.getID(), null);
        } else {
            p.edit(coords, true);
        }
    }

    /** Update the position of an existing VSegment representing a given edge in the graph.
	 *@param e the Jung edge
	 *@param l the layout that produced the graph's geometry
	 *@param s the VSegment representing this edge
	 *@see #updateLine(Edge e, AbstractLayout l, DPath p, int animDuration, AnimManager animator)
	 */
    public static void updateLine(Edge e, AbstractLayout l, VSegment s) {
        Shape shp = (new EdgeShape.Line()).getShape(e);
        if (shp instanceof Ellipse2D) {
            return;
        }
        Line2D curve = (Line2D) shp;
        double[] src = { curve.getX1(), curve.getY1(), curve.getX2(), curve.getY2() };
        double[] tgt = new double[4];
        getTransform(e, l, curve).transform(src, 0, tgt, 0, 2);
        s.setEndPoints(Math.round(tgt[0]), Math.round(tgt[1]), Math.round(tgt[2]), Math.round(tgt[3]));
    }

    /** Get a VPath composed of a quadratic curve representing a given edge in the graph.
	 *@param e the Jung edge
	 *@param l the layout that produced the graph's geometry
	 *@param c stroke color of VPath object
	 *@param translucence true if the returned VPath should be a VPathST (with a default alpha of 1.0)
	 *@return null if edgeShape does not correspond to a known edge shape
	 *@see #getQuadCurveAsDPath(Edge e, AbstractLayout l, Color c)
	 */
    public static VPath getQuadCurveAsVPath(Edge e, AbstractLayout l, Color c, boolean translucence) {
        Shape s = (new EdgeShape.QuadCurve()).getShape(e);
        if (s instanceof Ellipse2D) {
            return getShapeAsVPath(e, l, c, s, translucence);
        }
        QuadCurve2D curve = (QuadCurve2D) s;
        double[] src = { curve.getX1(), curve.getY1(), curve.getX2(), curve.getY2(), curve.getCtrlX(), curve.getCtrlY() };
        double[] tgt = new double[6];
        getTransform(e, l, curve).transform(src, 0, tgt, 0, 3);
        VPath p = (translucence) ? new VPathST(Math.round(tgt[0]), Math.round(tgt[1]), 0, c, 1.0f) : new VPath(Math.round(tgt[0]), Math.round(tgt[1]), 0, c);
        p.addQdCurve(Math.round(tgt[2]), Math.round(tgt[3]), Math.round(tgt[4]), Math.round(tgt[5]), true);
        return p;
    }

    /** Get a DPath composed of a quadratic curve representing a given edge in the graph.
	 *@param e the Jung edge
	 *@param l the layout that produced the graph's geometry
	 *@param c stroke color of DPath object
	 *@param translucence true if the returned VPath should be a VPathST (with a default alpha of 1.0)
	 *@return null if edgeShape does not correspond to a known edge shape
	 *@see #getQuadCurveAsVPath(Edge e, AbstractLayout l, Color c)
	 */
    public static DPath getQuadCurveAsDPath(Edge e, AbstractLayout l, Color c, boolean translucence) {
        Shape s = (new EdgeShape.QuadCurve()).getShape(e);
        if (s instanceof Ellipse2D) {
            return getShapeAsDPath(e, l, c, s, translucence);
        }
        QuadCurve2D curve = (QuadCurve2D) s;
        double[] src = { curve.getX1(), curve.getY1(), curve.getX2(), curve.getY2(), curve.getCtrlX(), curve.getCtrlY() };
        double[] tgt = new double[6];
        getTransform(e, l, curve).transform(src, 0, tgt, 0, 3);
        DPath p = (translucence) ? new DPathST(Math.round(tgt[0]), Math.round(tgt[1]), 0, c, 1.0f) : new DPath(Math.round(tgt[0]), Math.round(tgt[1]), 0, c);
        p.addQdCurve(Math.round(tgt[2]), Math.round(tgt[3]), Math.round(tgt[4]), Math.round(tgt[5]), true);
        return p;
    }

    /** Update the position of an existing quadratic curve representing a given edge in the graph.
	 *@param e the Jung edge
	 *@param l the layout that produced the graph's geometry
	 *@param p the DPath representing this edge
	 *@param animDuration the duration of the animation to transition from the old position to the new one. Put 0 if no animation should be run.
	 *@param animator the ZVTM AnimManager instantiated by VirtualSpaceManager. Usually VirtualSpaceManager.animator. Can be null if animDuration == 0.
	 */
    public static void updateQuadCurve(Edge e, AbstractLayout l, DPath p, int animDuration, AnimManager animator) {
        Shape s = (new EdgeShape.QuadCurve()).getShape(e);
        if (s instanceof Ellipse2D) {
            updateShape(e, l, p, animDuration, animator, s);
            return;
        }
        QuadCurve2D curve = (QuadCurve2D) s;
        double[] src = { curve.getX1(), curve.getY1(), curve.getX2(), curve.getY2(), curve.getCtrlX(), curve.getCtrlY() };
        double[] tgt = new double[6];
        getTransform(e, l, curve).transform(src, 0, tgt, 0, 3);
        LongPoint[] coords = { new LongPoint(Math.round(tgt[0]), Math.round(tgt[1])), new LongPoint(Math.round(tgt[4]), Math.round(tgt[5])), new LongPoint(Math.round(tgt[2]), Math.round(tgt[3])) };
        if (animDuration > 0) {
            animator.createPathAnimation(animDuration, AnimManager.DP_TRANS_SIG_ABS, coords, p.getID(), null);
        } else {
            p.edit(coords, true);
        }
    }

    /** Get a VPath composed of a cubic curve representing a given edge in the graph.
	 *@param e the Jung edge
	 *@param l the layout that produced the graph's geometry
	 *@param c stroke color of VPath object
	 *@param translucence true if the returned VPath should be a VPathST (with a default alpha of 1.0)
	 *@return null if edgeShape does not correspond to a known edge shape
	 *@see #getCubicCurveAsDPath(Edge e, AbstractLayout l, Color c)
	 */
    public static VPath getCubicCurveAsVPath(Edge e, AbstractLayout l, Color c, boolean translucence) {
        Shape s = (new EdgeShape.CubicCurve()).getShape(e);
        if (s instanceof Ellipse2D) {
            return getShapeAsVPath(e, l, c, s, translucence);
        }
        CubicCurve2D curve = (CubicCurve2D) s;
        double[] src = { curve.getX1(), curve.getY1(), curve.getX2(), curve.getY2(), curve.getCtrlX1(), curve.getCtrlY1(), curve.getCtrlX2(), curve.getCtrlY2() };
        double[] tgt = new double[8];
        getTransform(e, l, curve).transform(src, 0, tgt, 0, 4);
        VPath p = (translucence) ? new VPathST(Math.round(tgt[0]), Math.round(tgt[1]), 0, c, 1.0f) : new VPath(Math.round(tgt[0]), Math.round(tgt[1]), 0, c);
        p.addCbCurve(Math.round(tgt[2]), Math.round(tgt[3]), Math.round(tgt[4]), Math.round(tgt[5]), Math.round(tgt[6]), Math.round(tgt[7]), true);
        return p;
    }

    /** Get a DPath composed of a cubic curve representing a given edge in the graph.
	 *@param e the Jung edge
	 *@param l the layout that produced the graph's geometry
	 *@param c stroke color of DPath object
	 *@param translucence true if the returned VPath should be a VPathST (with a default alpha of 1.0)
	 *@return null if edgeShape does not correspond to a known edge shape
	 *@see #getCubicCurveAsVPath(Edge e, AbstractLayout l, Color c)
	 */
    public static DPath getCubicCurveAsDPath(Edge e, AbstractLayout l, Color c, boolean translucence) {
        Shape s = (new EdgeShape.CubicCurve()).getShape(e);
        if (s instanceof Ellipse2D) {
            return getShapeAsDPath(e, l, c, s, translucence);
        }
        CubicCurve2D curve = (CubicCurve2D) s;
        double[] src = { curve.getX1(), curve.getY1(), curve.getX2(), curve.getY2(), curve.getCtrlX1(), curve.getCtrlY1(), curve.getCtrlX2(), curve.getCtrlY2() };
        double[] tgt = new double[8];
        getTransform(e, l, curve).transform(src, 0, tgt, 0, 4);
        DPath p = (translucence) ? new DPathST(Math.round(tgt[0]), Math.round(tgt[1]), 0, c, 1.0f) : new DPath(Math.round(tgt[0]), Math.round(tgt[1]), 0, c);
        p.addCbCurve(Math.round(tgt[2]), Math.round(tgt[3]), Math.round(tgt[4]), Math.round(tgt[5]), Math.round(tgt[6]), Math.round(tgt[7]), true);
        return p;
    }

    /** Update the position of an existing cubic curve representing a given edge in the graph.
	 *@param e the Jung edge
	 *@param l the layout that produced the graph's geometry
	 *@param p the DPath representing this edge
	 *@param animDuration the duration of the animation to transition from the old position to the new one. Put 0 if no animation should be run.
	 *@param animator the ZVTM AnimManager instantiated by VirtualSpaceManager. Usually VirtualSpaceManager.animator. Can be null if animDuration == 0.
	 */
    public static void updateCubicCurve(Edge e, AbstractLayout l, DPath p, int animDuration, AnimManager animator) {
        Shape s = (new EdgeShape.CubicCurve()).getShape(e);
        if (s instanceof Ellipse2D) {
            updateShape(e, l, p, animDuration, animator, s);
            return;
        }
        CubicCurve2D curve = (CubicCurve2D) s;
        double[] src = { curve.getX1(), curve.getY1(), curve.getX2(), curve.getY2(), curve.getCtrlX1(), curve.getCtrlY1(), curve.getCtrlX2(), curve.getCtrlY2() };
        double[] tgt = new double[8];
        getTransform(e, l, curve).transform(src, 0, tgt, 0, 4);
        LongPoint[] coords = { new LongPoint(Math.round(tgt[0]), Math.round(tgt[1])), new LongPoint(Math.round(tgt[4]), Math.round(tgt[5])), new LongPoint(Math.round(tgt[6]), Math.round(tgt[7])), new LongPoint(Math.round(tgt[2]), Math.round(tgt[3])) };
        if (animDuration > 0) {
            animator.createPathAnimation(animDuration, AnimManager.DP_TRANS_SIG_ABS, coords, p.getID(), null);
        } else {
            p.edit(coords, true);
        }
    }

    /** Get a DPath representing a given self-loop edge in the graph.
	 *@param e the Jung edge
	 *@param l the layout that produced the graph's geometry
	 *@param c stroke color of DPath object
	 *@param s the shape already extracted from the Edge object
	 *@param translucence true if the returned VPath should be a VPathST (with a default alpha of 1.0)
	 *@return null if edgeShape does not correspond to a known edge shape
	 */
    protected static VPath getShapeAsVPath(Edge e, AbstractLayout l, Color c, Shape s, boolean translucence) {
        PathIterator pi = s.getPathIterator(getTransform(e, l, s));
        return (translucence) ? new VPathST(pi, 0, c, 1.0f) : new VPath(pi, 0, c);
    }

    /** Get a DPath representing a given self-loop edge in the graph.
	 *@param e the Jung edge
	 *@param l the layout that produced the graph's geometry
	 *@param c stroke color of DPath object
	 *@param s the shape already extracted from the Edge object
	 *@param translucence true if the returned VPath should be a VPathST (with a default alpha of 1.0)
	 *@return null if edgeShape does not correspond to a known edge shape
	 */
    protected static DPath getShapeAsDPath(Edge e, AbstractLayout l, Color c, Shape s, boolean translucence) {
        PathIterator pi = s.getPathIterator(getTransform(e, l, s));
        return (translucence) ? new DPathST(pi, 0, c, 1.0f) : new DPath(pi, 0, c);
    }

    /** Update the position of an existing straight DPath representing a given edge in the graph.
	 *@param e the Jung edge
	 *@param l the layout that produced the graph's geometry
	 *@param p the DPath representing this edge
	 *@param animDuration the duration of the animation to transition from the old position to the new one. Put 0 if no animation should be run.
	 *@param animator the ZVTM AnimManager instantiated by VirtualSpaceManager. Usually VirtualSpaceManager.animator. Can be null if animDuration == 0.
	 *@param s the shape already extracted from the Edge object
	 */
    protected static void updateShape(Edge e, AbstractLayout l, DPath p, int animDuration, AnimManager animator, Shape s) {
        LongPoint[] coords = new LongPoint[0];
        PathIterator pi = s.getPathIterator(getTransform(e, l, s));
        int type;
        double[] picoords = new double[6];
        while (!pi.isDone()) {
            LongPoint[] ncoords = null;
            type = pi.currentSegment(picoords);
            switch(type) {
                case PathIterator.SEG_CUBICTO:
                    {
                        ncoords = new LongPoint[coords.length + 3];
                        System.arraycopy(coords, 0, ncoords, 0, coords.length);
                        ncoords[coords.length] = new LongPoint(picoords[0], picoords[1]);
                        ncoords[coords.length + 1] = new LongPoint(picoords[2], picoords[3]);
                        ncoords[coords.length + 2] = new LongPoint(picoords[4], picoords[5]);
                        break;
                    }
                case PathIterator.SEG_QUADTO:
                    {
                        ncoords = new LongPoint[coords.length + 2];
                        System.arraycopy(coords, 0, ncoords, 0, coords.length);
                        ncoords[coords.length] = new LongPoint(picoords[0], picoords[1]);
                        ncoords[coords.length + 1] = new LongPoint(picoords[2], picoords[3]);
                        break;
                    }
                case PathIterator.SEG_LINETO:
                    {
                        ncoords = new LongPoint[coords.length + 1];
                        System.arraycopy(coords, 0, ncoords, 0, coords.length);
                        ncoords[coords.length] = new LongPoint(picoords[0], picoords[1]);
                        break;
                    }
                case PathIterator.SEG_MOVETO:
                    {
                        ncoords = new LongPoint[coords.length + 1];
                        System.arraycopy(coords, 0, ncoords, 0, coords.length);
                        ncoords[coords.length] = new LongPoint(picoords[0], picoords[1]);
                        break;
                    }
                case PathIterator.SEG_CLOSE:
                    {
                        pi.next();
                        continue;
                    }
            }
            coords = ncoords;
            pi.next();
        }
        if (animDuration > 0) {
            animator.createPathAnimation(animDuration, AnimManager.DP_TRANS_SIG_ABS, coords, p.getID(), null);
        } else {
            p.edit(coords, true);
        }
    }

    private static AffineTransform getTransform(Edge e, AbstractLayout l, Shape edgeShape) {
        Pair ep = e.getEndpoints();
        Vertex v1 = (Vertex) ep.getFirst();
        Vertex v2 = (Vertex) ep.getSecond();
        Coordinates c1 = l.getCoordinates(v1);
        Coordinates c2 = l.getCoordinates(v2);
        double dx = c2.getX() - c1.getX();
        double dy = c2.getY() - c1.getY();
        AffineTransform xform = AffineTransform.getTranslateInstance(c1.getX(), c1.getY());
        if (v1.equals(v2)) {
            Shape s2 = vertexShapeFunction.getShape(v2);
            Rectangle2D s2Bounds = s2.getBounds2D();
            xform.scale(s2Bounds.getWidth(), s2Bounds.getHeight());
            xform.translate(0, -edgeShape.getBounds2D().getWidth() / 2);
        } else {
            xform.rotate((float) Math.atan2(dy, dx));
            xform.scale(Math.sqrt(dx * dx + dy * dy), 1.0);
        }
        return xform;
    }
}
