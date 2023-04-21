package net.sf.pged.gui.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import net.sf.pged.gui.EditorOptions;
import net.sf.pged.pmanager.algo.AbstractAlgoPlugin;

/**
 * This class provides functions for paints graph and its edges and vertexes.
 * 
 * @author dude03
 *
 */
public class PaintFunctions {

    public static void paintGraph(Graphics2D g2d, VisualGraph graph) {
        Iterator<VisualVertex> vertexIter = graph.vertexIterator();
        while (vertexIter.hasNext()) {
            VisualVertex vertex = vertexIter.next();
            paintVertex(g2d, graph, vertex);
            if (graph.getAttr(vertex, VisualGraphAttrs.SELECTED_ATTR) != null) PaintFunctions.paintSelectedVertexBorder(g2d, vertex);
        }
        Iterator<VisualEdge> edgeIter = graph.edgeIterator();
        if (graph.isOrient()) {
            while (edgeIter.hasNext()) {
                VisualEdge edge = edgeIter.next();
                g2d.setStroke(new BasicStroke(EditorOptions.EDGE_VISUAL_WEIGHT));
                Color algoColor = (Color) graph.getAttr(edge, VisualGraphAttrs.ALGO_RESULT_ATTR);
                if (algoColor != null) g2d.setColor(algoColor); else g2d.setColor(edge.getColor());
                paintOrientEdge(g2d, edge);
                if (graph.getAttr(edge, VisualGraphAttrs.SELECTED_ATTR) != null) paintSelectedEdgeBorder(g2d, edge);
            }
        } else {
            while (edgeIter.hasNext()) {
                VisualEdge edge = edgeIter.next();
                g2d.setStroke(new BasicStroke(EditorOptions.EDGE_VISUAL_WEIGHT));
                Color algoColor = (Color) graph.getAttr(edge, VisualGraphAttrs.ALGO_RESULT_ATTR);
                if (algoColor != null) g2d.setColor(algoColor); else g2d.setColor(edge.getColor());
                paintUnorientEdge(g2d, edge);
                if (graph.getAttr(edge, VisualGraphAttrs.SELECTED_ATTR) != null) paintSelectedEdgeBorder(g2d, edge);
            }
        }
    }

    public static void paintEdge(Graphics2D g2d, VisualGraph graph, VisualVertex vertex, Point2D endPoint) {
        g2d.setStroke(new BasicStroke(EditorOptions.EDGE_VISUAL_WEIGHT));
        g2d.setColor(EditorOptions.newDefEdgeColor());
        if (graph.isOrient()) {
            paintOrientEdge(g2d, vertex, endPoint);
        } else {
            paintUnorientEdge(g2d, vertex, endPoint);
        }
    }

    /**
	 * Paints vertex from graph on graphics context.
	 * 
	 * @param g2d the Graphics2D context.
	 * @param g the current graph.
	 * @param v the vertex for paint.
	 */
    public static void paintVertex(Graphics2D g2d, VisualGraph g, VisualVertex v) {
        Shape vertexShape = v.getShape();
        Color algoAttr = (Color) g.getAttr(v, VisualGraphAttrs.ALGO_RESULT_ATTR);
        if (algoAttr != null) g2d.setColor(algoAttr); else g2d.setColor(v.getColor());
        g2d.fill(vertexShape);
    }

    public static void paintSelectedVertexBorder(Graphics2D g2d, VisualVertex vertex) {
        final float[] dashPatter = new float[] { 3 };
        Shape vertexShape = vertex.getShape();
        Rectangle2D bounds = vertexShape.getBounds2D();
        bounds.setFrame(bounds.getX() - 1, bounds.getY() - 1, bounds.getWidth() + 1, bounds.getHeight() + 1);
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0F, dashPatter, 0));
        g2d.draw(bounds);
    }

    public static void paintSelectedEdgeBorder(Graphics2D g2d, VisualEdge edge) {
        final double SELECTED_BORDER_SIZE = 5;
        Line2D edgeLine = VisualGraphFuctions.makeEdgeLine(edge);
        Ellipse2D ellipse = new Ellipse2D.Double();
        g2d.setColor(Color.CYAN);
        ellipse.setFrameFromCenter(edgeLine.getX1(), edgeLine.getY1(), edgeLine.getX1() - SELECTED_BORDER_SIZE, edgeLine.getY1() - SELECTED_BORDER_SIZE);
        g2d.fill(ellipse);
        ellipse.setFrameFromCenter(edgeLine.getX2(), edgeLine.getY2(), edgeLine.getX2() - SELECTED_BORDER_SIZE, edgeLine.getY2() - SELECTED_BORDER_SIZE);
        g2d.fill(ellipse);
    }

    public static void paintUnorientEdge(Graphics2D g2d, VisualEdge edge) {
        g2d.draw(VisualGraphFuctions.makeEdgeLine(edge));
    }

    public static void paintUnorientEdge(Graphics2D g2d, VisualVertex vertex, Point2D endPoint) {
        g2d.draw(VisualGraphFuctions.makeEdgeLine(vertex, endPoint));
    }

    public static void paintOrientEdge(Graphics2D g2d, VisualEdge edge) {
        Line2D edgeLine = VisualGraphFuctions.makeEdgeLine(edge);
        g2d.draw(edgeLine);
        paintEdgeArrowHead(g2d, edgeLine);
    }

    public static void paintOrientEdge(Graphics2D g2d, VisualVertex vertex, Point2D endPoint) {
        Line2D edgeLine = VisualGraphFuctions.makeEdgeLine(vertex, endPoint);
        g2d.draw(edgeLine);
        paintEdgeArrowHead(g2d, edgeLine);
    }

    public static void paintEdgeArrowHead(Graphics2D g2d, Line2D edgeLine) {
        double x1, y1, x2, y2;
        x1 = edgeLine.getX1();
        y1 = edgeLine.getY1();
        x2 = edgeLine.getX2();
        y2 = edgeLine.getY2();
        double dx = x2 - x1;
        double dy = y2 - y1;
        double lenOfHeadProjOnEdge = EditorOptions.ORIENT_EDGE_HEAD_SIZE / Math.cos(Math.PI / 6);
        double k = lenOfHeadProjOnEdge * Math.sin(Math.PI / 6);
        if (dx == 0) {
            y2 -= (dy < 0) ? (-lenOfHeadProjOnEdge) : (lenOfHeadProjOnEdge);
        } else if (dy == 0) {
            x2 -= (dx < 0) ? (-lenOfHeadProjOnEdge) : (lenOfHeadProjOnEdge);
        } else {
            double lineLen = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
            x2 -= (dx / lineLen) * lenOfHeadProjOnEdge;
            y2 -= (dy / lineLen) * lenOfHeadProjOnEdge;
        }
        if (Math.abs(dx) > Math.abs(dy)) {
            g2d.draw(new Line2D.Double(edgeLine.getX2(), edgeLine.getY2(), x2, y2 + k));
            g2d.draw(new Line2D.Double(edgeLine.getX2(), edgeLine.getY2(), x2, y2 - k));
        } else {
            g2d.draw(new Line2D.Double(edgeLine.getX2(), edgeLine.getY2(), x2 + k, y2));
            g2d.draw(new Line2D.Double(edgeLine.getX2(), edgeLine.getY2(), x2 - k, y2));
        }
    }

    public static void paintEdgeShadow(Graphics2D g2d, Line2D edgeLine) {
    }
}
