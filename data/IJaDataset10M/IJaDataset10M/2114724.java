package org.unitmetrics.java.ui.views.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Set;
import javax.swing.Icon;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.UndirectedEdge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgeStrokeFunction;
import edu.uci.ics.jung.graph.decorators.VertexIconFunction;
import edu.uci.ics.jung.graph.decorators.VertexShapeFunction;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.utils.Pair;
import edu.uci.ics.jung.visualization.MultiPickedState;
import edu.uci.ics.jung.visualization.PickedState;
import edu.uci.ics.jung.visualization.PluggableRenderer;

/**
 * Renders custom package vertexes using an icon and text. Icon and text
 * will scale.
 * @author Martin Kersten
 */
public class DependencyRenderer extends PluggableRenderer {

    private final NodeIcon packageIcon, classIcon, interfaceIcon;

    private final Font labelFont;

    private final PickedState defaultPickedState = new MultiPickedState();

    private PickedState pickedState = defaultPickedState;

    private final IVisualizationStatus visualizationStatus;

    public DependencyRenderer(final IVisualizationStatus visualizationStatus) {
        this.visualizationStatus = visualizationStatus;
        labelFont = new Font("Tahoma", Font.PLAIN, 11);
        packageIcon = new NodeIcon("package_normal");
        classIcon = new NodeIcon("class_normal");
        interfaceIcon = new NodeIcon("interface_normal");
        setVertexIconFunction(new VertexIconFunction() {

            public Icon getIcon(ArchetypeVertex v) {
                if (v instanceof GraphNode) {
                    GraphNode vertex = (GraphNode) v;
                    NodeIcon icon = packageIcon;
                    if (vertex.isRepresentingACLASS()) icon = classIcon; else if (vertex.isRepresentingAnInterface()) icon = interfaceIcon;
                    boolean isSelected = pickedState.isPicked(v);
                    boolean isShown = DependencyRenderer.this.visualizationStatus.isShown(vertex);
                    if (isShown) return !isSelected ? icon.getNormalIcon() : icon.getSelectedIcon(); else return !isSelected ? icon.getGrayIcon() : icon.getGraySelectedIcon();
                } else return null;
            }
        });
        setVertexShapeFunction(new IconVertexShapeFunction(getVertexShapeFunction()));
        setVertexStringer(new VertexStringer() {

            public String getLabel(ArchetypeVertex vertex) {
                if (vertex instanceof GraphNode) return ((GraphNode) vertex).getName(); else return vertex.toString();
            }
        });
        setEdgeStrokeFunction(new MyEdgeStrokeFunction(getEdgeStrokeFunction()));
        setPickedState(null);
    }

    public void setPickedState(PickedState pickedState) {
        this.pickedState = pickedState != null ? pickedState : defaultPickedState;
    }

    protected void labelVertex(Graphics g, Vertex vertex, String label, int x, int y) {
        if (vertex instanceof GraphNode) {
            if (visualizationStatus.isShown((GraphNode) vertex)) {
                Graphics2D g2d = (Graphics2D) g;
                GraphicsControl control = new GraphicsControl(g2d);
                if (labelFont != null) control.setFont(labelFont);
                Shape shape = getVertexShapeFunction().getShape(vertex);
                Rectangle bounds = shape.getBounds();
                int firstLine = y + ((int) bounds.getHeight()) / 2 + 5;
                drawLabelLines(control, label, x, firstLine);
                control.restoreAll();
            }
        } else super.labelVertex(g, vertex, label, x, y);
    }

    protected void drawSimpleEdge(Graphics2D g2d, Edge e, int x1, int y1, int x2, int y2) {
        drawLoopCorrectedSimpleEdge(g2d, e, x1, y1, x2, y2);
    }

    private void drawLabelLines(GraphicsControl control, String label, int x, int y) {
        Graphics2D g2d = control.getGraphics();
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int stringWidth = fontMetrics.stringWidth(label);
        control.setColor(new Color(225, 225, 225, 200));
        int labelX = x - stringWidth / 2;
        Rectangle rectangle = new Rectangle(labelX - 2, y - 2, stringWidth + 4, fontMetrics.getHeight() + 4);
        g2d.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        control.setAntialiasing(g2d.getTransform().getScaleX() >= 1.0);
        control.setColor(Color.BLACK);
        g2d.drawString(label, labelX, y + fontMetrics.getAscent());
    }

    public void fillHull(Graphics2D g2d, Set<Point2D> points, Color drawColor, Color fillColor) {
        if (points.size() >= 3) {
            ConvexHull hull = ConvexHull.create(points);
            Polygon hullPolygon = new Polygon();
            for (Point2D point : hull.getPoints()) hullPolygon.addPoint((int) point.getX(), (int) point.getY());
            GraphicsControl control = new GraphicsControl(g2d);
            control.setAntialiasing(true);
            control.setColor(fillColor);
            g2d.fill(hullPolygon);
            control.restore();
        }
    }

    public void drawHull(Graphics2D g2d, Set<Point2D> points, Color drawColor, Color fillColor) {
        if (points.size() >= 3) {
            ConvexHull hull = ConvexHull.create(points);
            Polygon hullPolygon = new Polygon();
            for (Point2D point : hull.getPoints()) hullPolygon.addPoint((int) point.getX(), (int) point.getY());
            GraphicsControl control = new GraphicsControl(g2d);
            control.setAntialiasing(true);
            control.setColor(drawColor);
            control.setStroke(new BasicStroke(3));
            g2d.draw(hullPolygon);
            control.restore();
        }
    }

    protected void drawLoopCorrectedSimpleEdge(Graphics2D g, Edge e, int x1, int y1, int x2, int y2) {
        Pair endpoints = e.getEndpoints();
        Vertex v1 = (Vertex) endpoints.getFirst();
        Vertex v2 = (Vertex) endpoints.getSecond();
        boolean isLoop = v1.equals(v2);
        Shape s2 = vertexShapeFunction.getShape(v2);
        Shape edgeShape = edgeShapeFunction.getShape(e);
        boolean edgeHit = true;
        boolean arrowHit = true;
        Rectangle deviceRectangle = null;
        if (screenDevice != null) {
            Dimension d = screenDevice.getSize();
            if (d.width <= 0 || d.height <= 0) {
                d = screenDevice.getPreferredSize();
            }
            deviceRectangle = new Rectangle(0, 0, d.width, d.height);
        }
        AffineTransform xform = AffineTransform.getTranslateInstance(x1, y1);
        if (isLoop) {
            Rectangle2D s2Bounds = s2.getBounds2D();
            xform.scale(s2Bounds.getWidth(), s2Bounds.getHeight());
            xform.translate(edgeShape.getBounds2D().getWidth() / 2, -edgeShape.getBounds2D().getHeight() / 2);
        } else {
            float dx = x2 - x1;
            float dy = y2 - y1;
            float thetaRadians = (float) Math.atan2(dy, dx);
            xform.rotate(thetaRadians);
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            xform.scale(dist / edgeShape.getBounds().getWidth(), 1.0);
        }
        edgeShape = xform.createTransformedShape(edgeShape);
        edgeHit = viewTransformer.transform(edgeShape).intersects(deviceRectangle);
        if (edgeHit == true) {
            Paint oldPaint = g.getPaint();
            Paint fill_paint = edgePaintFunction.getFillPaint(e);
            if (fill_paint != null) {
                g.setPaint(fill_paint);
                g.fill(edgeShape);
            }
            Paint draw_paint = edgePaintFunction.getDrawPaint(e);
            if (draw_paint != null) {
                g.setPaint(draw_paint);
                g.draw(edgeShape);
            }
            float scalex = (float) g.getTransform().getScaleX();
            float scaley = (float) g.getTransform().getScaleY();
            if (scalex < .3 || scaley < .3) return;
            if (edgeArrowPredicate.evaluate(e)) {
                Shape destVertexShape = vertexShapeFunction.getShape((Vertex) e.getEndpoints().getSecond());
                AffineTransform xf = AffineTransform.getTranslateInstance(x2, y2);
                destVertexShape = xf.createTransformedShape(destVertexShape);
                arrowHit = viewTransformer.transform(destVertexShape).intersects(deviceRectangle);
                if (arrowHit) {
                    AffineTransform at = getArrowTransform((GeneralPath) edgeShape, destVertexShape);
                    if (at == null) return;
                    Shape arrow = edgeArrowFunction.getArrow(e);
                    arrow = at.createTransformedShape(arrow);
                    g.fill(arrow);
                }
                if (e instanceof UndirectedEdge) {
                    Shape vertexShape = vertexShapeFunction.getShape((Vertex) e.getEndpoints().getFirst());
                    xf = AffineTransform.getTranslateInstance(x1, y1);
                    vertexShape = xf.createTransformedShape(vertexShape);
                    arrowHit = viewTransformer.transform(vertexShape).intersects(deviceRectangle);
                    if (arrowHit) {
                        AffineTransform at = getReverseArrowTransform((GeneralPath) edgeShape, vertexShape, !isLoop);
                        if (at == null) return;
                        Shape arrow = edgeArrowFunction.getArrow(e);
                        arrow = at.createTransformedShape(arrow);
                        g.fill(arrow);
                    }
                }
            }
            if (draw_paint == null) g.setPaint(oldPaint);
            String label = edgeStringer.getLabel(e);
            if (label != null) {
                labelEdge(g, e, label, x1, x2, y1, y2);
            }
            g.setPaint(oldPaint);
        }
    }

    private class IconVertexShapeFunction implements VertexShapeFunction {

        private final VertexShapeFunction defaultFunction;

        public IconVertexShapeFunction(VertexShapeFunction defaultFunction) {
            this.defaultFunction = defaultFunction;
        }

        public Shape getShape(Vertex vertex) {
            if (vertex instanceof GraphNode) {
                return packageIcon.getShape();
            } else return defaultFunction.getShape(vertex);
        }
    }

    private class MyEdgeStrokeFunction implements EdgeStrokeFunction {

        private final EdgeStrokeFunction defaultFunction;

        public MyEdgeStrokeFunction(EdgeStrokeFunction defaultFunction) {
            this.defaultFunction = defaultFunction;
        }

        public Stroke getStroke(Edge edge) {
            if (edge instanceof GraphEdge) {
                if (!((GraphEdge) edge).isReferringClasses() && ((GraphEdge) edge).isReferringInterfaces()) {
                    return PluggableRenderer.DOTTED;
                }
            }
            return defaultFunction.getStroke(edge);
        }
    }
}
