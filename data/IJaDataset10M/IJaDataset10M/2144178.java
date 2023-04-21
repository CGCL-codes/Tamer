package graphlab.gui.core.graph;

import graphlab.gui.core.graph.edge.Edge;
import graphlab.gui.core.graph.edge.EdgeModel;
import graphlab.gui.core.graph.edge.EdgeView;
import graphlab.gui.core.graph.graph.Graph;
import graphlab.gui.core.graph.vertex.VertexView;
import graphlab.main.attribute.AttributeListener;
import graphlab.main.lang.ArrayX;
import java.awt.*;
import java.util.Iterator;
import java.util.Vector;

public class ArrowHandler implements PaintHandler<EdgeView>, AttributeListener {

    Edge e;

    Arrow targetArrow;

    Arrow sourceArrow;

    public ArrowHandler(EdgeView edge) {
        this.e = edge.edge;
        e.model.addAttributeListener(this, new String[] { EdgeModel.DIRECTED, Arrow.ARROW });
        edge.setArrow(defaultArrow);
    }

    int x1, x2, y1, y2;

    int dist = 20;

    public void updateBounds(int x1, int x2, int y1, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    int size = 10;

    public void paint(Graphics _g, EdgeView e) {
        targetArrow = e.getArrow();
        Graphics2D g = (Graphics2D) _g;
        double angle = e.findAngle();
        VertexView v2 = e.edge.v2.view;
        int h = v2.getShapeHeight();
        int w = v2.getShapeWidth();
        double t = Math.atan2(w * Math.sin(angle), h * Math.cos(angle));
        int x2 = (int) (this.x2 + (w / 2) * Math.cos(t));
        int y2 = (int) (this.y2 + (h / 2) * Math.sin(t));
        g.translate(x2 + 1, y2 + 1);
        g.rotate(angle + Math.PI);
        targetArrow.paintArrow(g, 20, 20);
        g.rotate(-angle - Math.PI);
        g.translate(-x2 - 1, -y2 - 1);
    }

    private void enable() {
        e.view.addPrePaintHandler(this);
        e.view.repaint();
    }

    private void disable() {
        e.view.removePrePaintHandler(this);
        e.view.repaint();
    }

    public void attributeUpdated(String name, Object oldVal, Object newVal) {
        if (name.equals(EdgeModel.DIRECTED)) {
            if (newVal.equals(true)) enable(); else disable();
        }
        if (name.equals(Arrow.ARROW)) if (e.view != null) e.view.repaint();
    }

    public static void HandleGraph(final Graph g) {
        g.model.addAttributeListener(new AttributeListener() {

            public void attributeUpdated(String name, Object oldVal, Object newVal) {
                if (name.equals(Graph.EDGEDEFAULT)) {
                    ArrayX x = (ArrayX) newVal;
                    if (x.getValue().equals(Graph.EDGEDEFAULT_DIRECTED)) {
                        g.model.setDirected(true);
                        Iterator<Edge> eit = g.edgeIterator();
                        while (eit.hasNext()) {
                            Edge edge = eit.next();
                            edge.putAtr(EdgeModel.DIRECTED, true);
                        }
                    }
                    if (x.getValue().equals(Graph.EDGEDEFAULT_UNDIRECTED)) {
                        g.model.setDirected(false);
                        Iterator<Edge> eit = g.edgeIterator();
                        while (eit.hasNext()) eit.next().putAtr(EdgeModel.DIRECTED, false);
                    }
                    g.view.repaint();
                }
            }
        }, new String[] { Graph.EDGEDEFAULT });
    }

    public static PolygonArrow defaultArrow;

    static {
        int xPoints[] = { 0, -15, -15 };
        int yPoints[] = { 0, -15 / 2, 15 / 2 };
        defaultArrow = new PolygonArrow(new Polygon(xPoints, yPoints, 3), "Default");
        PolygonArrow ar1 = new PolygonArrow(new Polygon(new int[] { 0, -15, -10, -15 }, new int[] { 0, -7, 0, 7 }, 4), "Narrow");
        PolygonArrow ar2 = new PolygonArrow(new Polygon(new int[] { 0, -15, -5, -15 }, new int[] { 0, -7, 0, 7 }, 4), "Very Narrow");
        PolygonArrow ar3 = new PolygonArrow(new Polygon(new int[] { 0, -15, -30, -15 }, new int[] { 0, -7, 0, 7 }, 4), "Box");
        knownArrows = new Vector<Arrow>();
        registerArrow(defaultArrow);
        registerArrow(ar1);
        registerArrow(ar2);
        registerArrow(ar3);
    }

    public static Vector<Arrow> knownArrows;

    public static void registerArrow(Arrow arrow) {
        knownArrows.add(arrow);
    }
}
