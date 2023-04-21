package jung;

import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import jung.EdgeOffsetFunctions.HasEdgeOffsetFunctions;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.EdgeIndexFunction;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.EdgeShape.IndexedRendering;
import edu.uci.ics.jung.visualization.transform.LensTransformer;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;

public class BasicEdgeRenderer<V, E> extends edu.uci.ics.jung.visualization.renderers.BasicEdgeRenderer<V, E> {

    public BasicEdgeRenderer() {
        super();
    }

    @Override
    protected void drawSimpleEdge(final RenderContext<V, E> rc, final Layout<V, E> layout, final E e) {
        final GraphicsDecorator g = rc.getGraphicsContext();
        final Graph<V, E> graph = layout.getGraph();
        final Pair<V> endpoints = graph.getEndpoints(e);
        final V v1 = endpoints.getFirst();
        final V v2 = endpoints.getSecond();
        Point2D p1 = layout.transform(v1);
        Point2D p2 = layout.transform(v2);
        p1 = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, p1);
        p2 = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, p2);
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();
        double dx = x2 - x1;
        double dy = y2 - y1;
        final double theta = Math.atan2(dy, dx);
        if (rc.getEdgeShapeTransformer() instanceof HasEdgeOffsetFunctions) {
            @SuppressWarnings("unchecked") final HasEdgeOffsetFunctions<V, E> offsetFunctionsGetter = (HasEdgeOffsetFunctions<V, E>) rc.getEdgeShapeTransformer();
            final EdgeOffsetFunctions<V, E> offsetFunctions = offsetFunctionsGetter.getEdgeOffsetFunctions();
            float sourceOffset = 0;
            if (offsetFunctions != null && offsetFunctions.getSourceOffsetFunction() != null) {
                sourceOffset = offsetFunctions.getSourceOffsetFunction().getOffset(graph, e);
            }
            float destOffset = 0;
            if (offsetFunctions != null && offsetFunctions.getDestOffsetFunction() != null) {
                destOffset = offsetFunctions.getDestOffsetFunction().getOffset(graph, e);
            }
            final double inc = Bracket.getXOffsetIncrement();
            double cx1 = 0;
            double cx2 = 0;
            double cy1 = 0;
            double cy2 = 0;
            if (dx != 0) {
                if (dy != 0) {
                    final double incx = Math.abs(Math.cos(theta) * inc);
                    cx1 = sourceOffset * incx;
                    cx2 = destOffset * incx;
                    final double ct = dx / dy;
                    cy1 = cx1 / ct;
                    cy2 = cx2 / ct;
                } else {
                    cx1 = sourceOffset * inc;
                    cx2 = destOffset * inc;
                    cy1 = 0;
                    cy2 = 0;
                }
            } else if (dy != 0) {
                cy1 = sourceOffset * inc;
                cy2 = destOffset * inc;
                cx1 = 0;
                cx2 = 0;
            }
            x1 += cx1;
            x2 += cx2;
            y1 += cy1;
            y2 += cy2;
            dx = x2 - x1;
            dy = y2 - y1;
        }
        Rectangle deviceRectangle = null;
        final JComponent vv = rc.getScreenDevice();
        if (vv != null) {
            final Dimension d = vv.getSize();
            deviceRectangle = new Rectangle(0, 0, d.width, d.height);
        }
        final AffineTransform xform = AffineTransform.getTranslateInstance(x1, y1);
        Shape edgeShape = null;
        final boolean isLoop = v1.equals(v2);
        if (isLoop) {
            edgeShape = rc.getEdgeShapeTransformer().transform(Context.<Graph<V, E>, E>getInstance(graph, e));
            final Shape vertexShape = rc.getVertexShapeTransformer().transform(v2);
            final Rectangle2D vertexBounds = vertexShape.getBounds2D();
            xform.scale(vertexBounds.getWidth(), vertexBounds.getHeight());
            xform.translate(0, -edgeShape.getBounds2D().getWidth() / 2);
        } else if (rc.getEdgeShapeTransformer() instanceof EdgeShape.Orthogonal) {
            int index = 0;
            if (rc.getEdgeShapeTransformer() instanceof IndexedRendering) {
                @SuppressWarnings("unchecked") final EdgeIndexFunction<V, E> peif = ((IndexedRendering<V, E>) rc.getEdgeShapeTransformer()).getEdgeIndexFunction();
                index = peif.getIndex(graph, e);
                index *= 20;
            }
            final GeneralPath gp = new GeneralPath();
            gp.moveTo(0, 0);
            if (x1 > x2) {
                if (y1 > y2) {
                    gp.lineTo(0, index);
                    gp.lineTo(dx - index, index);
                    gp.lineTo(dx - index, dy);
                    gp.lineTo(dx, dy);
                } else {
                    gp.lineTo(0, -index);
                    gp.lineTo(dx - index, -index);
                    gp.lineTo(dx - index, dy);
                    gp.lineTo(dx, dy);
                }
            } else {
                if (y1 > y2) {
                    gp.lineTo(0, index);
                    gp.lineTo(dx + index, index);
                    gp.lineTo(dx + index, dy);
                    gp.lineTo(dx, dy);
                } else {
                    gp.lineTo(0, -index);
                    gp.lineTo(dx + index, -index);
                    gp.lineTo(dx + index, dy);
                    gp.lineTo(dx, dy);
                }
            }
            edgeShape = gp;
        } else {
            edgeShape = rc.getEdgeShapeTransformer().transform(Context.<Graph<V, E>, E>getInstance(graph, e));
            final double dist = Math.sqrt(dx * dx + dy * dy);
            xform.rotate(theta);
            xform.scale(dist, 1.0);
        }
        edgeShape = xform.createTransformedShape(edgeShape);
        MutableTransformer vt = rc.getMultiLayerTransformer().getTransformer(Layer.VIEW);
        if (vt instanceof LensTransformer) {
            vt = ((LensTransformer) vt).getDelegate();
        }
        final boolean edgeHit = vt.transform(edgeShape).intersects(deviceRectangle);
        if (edgeHit) {
            final Paint oldPaint = g.getPaint();
            final Paint fill_paint = rc.getEdgeFillPaintTransformer().transform(e);
            if (fill_paint != null) {
                g.setPaint(fill_paint);
                g.fill(edgeShape);
            }
            final Paint draw_paint = rc.getEdgeDrawPaintTransformer().transform(e);
            if (draw_paint != null) {
                g.setPaint(draw_paint);
                g.draw(edgeShape);
            }
            final double scalex = g.getTransform().getScaleX();
            final double scaley = g.getTransform().getScaleY();
            if (scalex < .3 || scaley < .3) return;
            if (rc.getEdgeArrowPredicate().evaluate(Context.<Graph<V, E>, E>getInstance(graph, e))) {
                final Stroke old_stroke = g.getStroke();
                final Stroke new_stroke = rc.getEdgeArrowStrokeTransformer().transform(e);
                if (new_stroke != null) {
                    g.setStroke(new_stroke);
                }
                Shape destVertexShape = rc.getVertexShapeTransformer().transform(graph.getEndpoints(e).getSecond());
                AffineTransform xf = AffineTransform.getTranslateInstance(x2, y2);
                destVertexShape = xf.createTransformedShape(destVertexShape);
                boolean arrowHit = true;
                arrowHit = rc.getMultiLayerTransformer().getTransformer(Layer.VIEW).transform(destVertexShape).intersects(deviceRectangle);
                if (arrowHit) {
                    @SuppressWarnings("unchecked") final AffineTransform at = this.edgeArrowRenderingSupport.getArrowTransform(rc, edgeShape, destVertexShape);
                    if (at == null) return;
                    Shape arrow = rc.getEdgeArrowTransformer().transform(Context.<Graph<V, E>, E>getInstance(graph, e));
                    arrow = at.createTransformedShape(arrow);
                    g.setPaint(rc.getArrowFillPaintTransformer().transform(e));
                    g.fill(arrow);
                    g.setPaint(rc.getArrowDrawPaintTransformer().transform(e));
                    g.draw(arrow);
                }
                if (graph.getEdgeType(e) == EdgeType.UNDIRECTED) {
                    Shape vertexShape = rc.getVertexShapeTransformer().transform(graph.getEndpoints(e).getFirst());
                    xf = AffineTransform.getTranslateInstance(x1, y1);
                    vertexShape = xf.createTransformedShape(vertexShape);
                    arrowHit = rc.getMultiLayerTransformer().getTransformer(Layer.VIEW).transform(vertexShape).intersects(deviceRectangle);
                    if (arrowHit) {
                        @SuppressWarnings("unchecked") final AffineTransform at = this.edgeArrowRenderingSupport.getReverseArrowTransform(rc, edgeShape, vertexShape, !isLoop);
                        if (at == null) return;
                        Shape arrow = rc.getEdgeArrowTransformer().transform(Context.<Graph<V, E>, E>getInstance(graph, e));
                        arrow = at.createTransformedShape(arrow);
                        g.setPaint(rc.getArrowFillPaintTransformer().transform(e));
                        g.fill(arrow);
                        g.setPaint(rc.getArrowDrawPaintTransformer().transform(e));
                        g.draw(arrow);
                    }
                }
                if (new_stroke != null) {
                    g.setStroke(old_stroke);
                }
            }
            g.setPaint(oldPaint);
        }
    }
}
