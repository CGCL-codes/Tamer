package org.unitmetrics.java.ui.views.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.ToolTipManager;
import org.unitmetrics.java.ui.views.graph.cycle.IStrongComponent;
import org.unitmetrics.java.ui.views.graph.cycle.IStrongComponents;
import org.unitmetrics.util.UnsafeCast;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.ToolTipFunction;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.VisualizationModel;

/**
 * <p>Note: Due restrictions of the orginal implementation, the calculation of 
 *    the fps feature does not work anymore.</p>
 * @author Martin Kersten
 */
@SuppressWarnings("serial")
public class VisualizationViewer extends edu.uci.ics.jung.visualization.VisualizationViewer {

    private static final int MAX_ANTIALIZED_EDGE_COUNT = 50;

    private static final int CONVEX_HULL_VERTEX_RADIUS = 30;

    private final IVisualizationStatus visualizationStatus;

    public VisualizationViewer(VisualizationModel visualizationModel, IVisualizationStatus visualizationStatus, DependencyRenderer renderer) {
        super(visualizationModel, renderer);
        this.visualizationStatus = visualizationStatus;
    }

    public void setToolTipFunction(ToolTipFunction toolTipFunction) {
        super.setToolTipFunction(toolTipFunction);
        ToolTipManager.sharedInstance().setDismissDelay(60 * 1000);
    }

    protected void renderGraph(Graphics2D g2d) {
        Layout layout = model.getGraphLayout();
        g2d.setRenderingHints(renderingHints);
        Dimension d = getSize();
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, d.width, d.height);
        AffineTransform oldXform = g2d.getTransform();
        AffineTransform newXform = new AffineTransform(oldXform);
        newXform.concatenate(viewTransformer.getTransform());
        g2d.setTransform(newXform);
        for (Iterator iterator = preRenderers.iterator(); iterator.hasNext(); ) {
            Paintable paintable = (Paintable) iterator.next();
            if (paintable.useTransform()) {
                paintable.paint(g2d);
            } else {
                g2d.setTransform(oldXform);
                paintable.paint(g2d);
                g2d.setTransform(newXform);
            }
        }
        locationMap.clear();
        renderStrongComponents(g2d, layout);
        renderEdges(g2d, layout);
        renderVertices(g2d, layout);
        for (Iterator iterator = postRenderers.iterator(); iterator.hasNext(); ) {
            Paintable paintable = (Paintable) iterator.next();
            if (paintable.useTransform()) {
                paintable.paint(g2d);
            } else {
                g2d.setTransform(oldXform);
                paintable.paint(g2d);
                g2d.setTransform(newXform);
            }
        }
        g2d.setTransform(oldXform);
    }

    /** Renders strong components by drawing and filling the convex hulls of its members. 
	 * The hulls are filled all completly first to mark all existing components. */
    private void renderStrongComponents(Graphics2D g2d, Layout layout) {
        IStrongComponents<GraphNode> strongComponents = visualizationStatus.getStrongComponents();
        for (IStrongComponent<GraphNode> strongComponent : strongComponents) {
            if (strongComponent.size() > 1) {
                Set<Point2D> points = constructPoints(layout, strongComponent);
                ((DependencyRenderer) renderer).fillHull(g2d, points, Color.BLACK, Color.CYAN);
            }
        }
        for (IStrongComponent<GraphNode> strongComponent : strongComponents) {
            if (strongComponent.size() > 1) {
                Set<Point2D> points = constructPoints(layout, strongComponent);
                ((DependencyRenderer) renderer).drawHull(g2d, points, Color.BLACK, Color.CYAN);
            }
        }
    }

    private Set<Point2D> constructPoints(Layout layout, IStrongComponent<GraphNode> strongComponent) {
        Set<Point2D> points = new HashSet<Point2D>();
        for (GraphNode node : strongComponent) {
            Point2D point = layout.getLocation(node);
            if (point != null && isDisplayNode(node)) {
                point = layoutTransformer.transform(point);
                points.add(new Point2D.Double(point.getX() - CONVEX_HULL_VERTEX_RADIUS, point.getY() - CONVEX_HULL_VERTEX_RADIUS));
                points.add(new Point2D.Double(point.getX() + CONVEX_HULL_VERTEX_RADIUS, point.getY() - CONVEX_HULL_VERTEX_RADIUS));
                points.add(new Point2D.Double(point.getX() - CONVEX_HULL_VERTEX_RADIUS, point.getY() + CONVEX_HULL_VERTEX_RADIUS));
                points.add(new Point2D.Double(point.getX() + CONVEX_HULL_VERTEX_RADIUS, point.getY() + CONVEX_HULL_VERTEX_RADIUS));
            }
        }
        return points;
    }

    private boolean isDisplayNode(GraphNode node) {
        return !(visualizationStatus.isHidden(node) && !visualizationStatus.isDisplayHiddenNodes());
    }

    private void renderVertices(Graphics2D g2d, Layout layout) {
        Set<GraphNode> vertices = UnsafeCast.castSet(layout.getGraph().getVertices(), GraphNode.class);
        try {
            for (GraphNode vertex : vertices) {
                if (visualizationStatus.isHidden(vertex)) renderVertex(g2d, layout, vertex);
            }
            for (GraphNode vertex : vertices) {
                if (visualizationStatus.isShown(vertex)) renderVertex(g2d, layout, vertex);
            }
        } catch (ConcurrentModificationException cme) {
            repaint();
        }
    }

    private void renderEdges(Graphics2D g2d, Layout layout) {
        Set<GraphEdge> edges = UnsafeCast.castSet(layout.getGraph().getEdges(), GraphEdge.class);
        GraphicsControl control = new GraphicsControl(g2d);
        control.setAntialiasing(edges.size() <= MAX_ANTIALIZED_EDGE_COUNT);
        try {
            for (GraphEdge edge : edges) {
                if (visualizationStatus.isHidden(edge)) renderEdge(g2d, layout, edge);
            }
            for (GraphEdge edge : edges) {
                if (visualizationStatus.isShown(edge)) renderEdge(g2d, layout, edge);
            }
        } catch (ConcurrentModificationException cme) {
            repaint();
        }
        control.restore();
    }

    @SuppressWarnings("unchecked")
    private void renderVertex(Graphics2D g2d, Layout layout, Vertex vertex) {
        if (isDisplayNode((GraphNode) vertex)) {
            Point2D p = (Point2D) locationMap.get(vertex);
            if (p == null) {
                p = layout.getLocation(vertex);
                p = layoutTransformer.transform(p);
                locationMap.put(vertex, p);
            }
            if (p != null) {
                renderer.paintVertex(g2d, vertex, (int) p.getX(), (int) p.getY());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void renderEdge(Graphics2D g2d, Layout layout, GraphEdge e) {
        Vertex v1 = (Vertex) e.getEndpoints().getFirst();
        Vertex v2 = (Vertex) e.getEndpoints().getSecond();
        Point2D p = (Point2D) locationMap.get(v1);
        if (p == null) {
            p = layout.getLocation(v1);
            p = layoutTransformer.transform(p);
            locationMap.put(v1, p);
        }
        Point2D q = (Point2D) locationMap.get(v2);
        if (q == null) {
            q = layout.getLocation(v2);
            q = layoutTransformer.transform(q);
            locationMap.put(v2, q);
        }
        if (p != null && q != null && isDisplayNode((GraphNode) v1) && isDisplayNode((GraphNode) v2)) {
            renderer.paintEdge(g2d, e, (int) p.getX(), (int) p.getY(), (int) q.getX(), (int) q.getY());
        }
    }

    public Set<GraphNode> getNodes() {
        return UnsafeCast.castSet(getGraphLayout().getGraph().getVertices(), GraphNode.class);
    }

    public Set<GraphNode> getSelectedNodes() {
        return UnsafeCast.castSet(getPickedState().getPickedVertices(), GraphNode.class);
    }
}
