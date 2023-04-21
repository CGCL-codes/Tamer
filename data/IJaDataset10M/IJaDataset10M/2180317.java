package org.semanticweb.mmm.mr3.jgraph;

import java.awt.geom.*;
import java.util.*;
import org.jgraph.graph.*;

/**
 * Algorithm which create intermediates points for parallel edges
 * 
 * @author fgendre
 */
public class JGParallelEdgeRouter implements Edge.Routing {

    /**
     * Singleton to reach parallel edge router
     */
    private static final JGParallelEdgeRouter sharedInstance = new JGParallelEdgeRouter();

    /**
     * Default model
     */
    private static final GraphModel emptyModel = new DefaultGraphModel();

    /**
     * Distance between each parallel edge
     */
    private static double edgeSeparation = 10.;

    /**
     * Distance between intermediate and source/target points
     */
    private static double edgeDeparture = 10.;

    /**
     * Getter for singleton managing parallel edges
     * 
     * @return JGraphParallelEdgeRouter Routeur for parallel edges
     */
    public static JGParallelEdgeRouter getSharedInstance() {
        return JGParallelEdgeRouter.sharedInstance;
    }

    /**
     * Calc of intermediates points
     * 
     * @param edge
     *            Edge for which routing is demanding
     * @param points
     *            List of points for this edge
     */
    public void route(EdgeView edge, java.util.List points) {
        if ((null == edge.getSource()) || (null == edge.getTarget()) || (null == edge.getSource().getParentView()) || (null == edge.getTarget().getParentView())) {
            return;
        }
        Object[] edges = getParallelEdges(edge);
        if (edges == null) {
            return;
        }
        if (edges.length < 2) {
            if (points.size() > 3) {
                points.remove(1);
                points.remove(2);
            } else if (points.size() > 2) {
                points.remove(1);
            }
            return;
        }
        int numEdges = edges.length;
        int position = 0;
        for (int i = 0; i < edges.length; i++) {
            Object e = edges[i];
            if (e == edge.getCell()) {
                position = i + 1;
            }
        }
        VertexView nodeFrom = (VertexView) edge.getSource().getParentView();
        VertexView nodeTo = (VertexView) edge.getTarget().getParentView();
        Point2D from = nodeFrom.getCenterPoint();
        Point2D to = nodeTo.getCenterPoint();
        if (from != null && to != null) {
            double dy = from.getY() - to.getY();
            double dx = from.getX() - to.getX();
            double m = dy / dx;
            double theta = Math.atan(-1 / m);
            double rx = dx / Math.sqrt(dx * dx + dy * dy);
            double ry = dy / Math.sqrt(dx * dx + dy * dy);
            double sizeFrom = Math.max(nodeFrom.getBounds().getWidth(), nodeFrom.getBounds().getHeight()) / 2.;
            double sizeTo = Math.max(nodeTo.getBounds().getWidth(), nodeTo.getBounds().getHeight()) / 2.;
            double edgeMiddleDeparture = (Math.sqrt(dx * dx + dy * dy) - sizeFrom - sizeTo) / 2 + sizeFrom;
            double edgeFromDeparture = edgeDeparture + sizeFrom;
            double edgeToDeparture = edgeDeparture + sizeTo;
            double r = edgeSeparation * Math.floor(position / 2);
            if (0 == (position % 2)) {
                r = -r;
            }
            double ex = r * Math.cos(theta);
            double ey = r * Math.sin(theta);
            if (edgeMiddleDeparture <= edgeFromDeparture) {
                double midX = from.getX() - rx * edgeMiddleDeparture;
                double midY = from.getY() - ry * edgeMiddleDeparture;
                Point2D controlPoint = new Point2D.Double(ex + midX, ey + midY);
                if (points.size() == 2) {
                    points.add(1, controlPoint);
                } else if (points.size() == 3) {
                    points.set(1, controlPoint);
                } else {
                    points.set(1, controlPoint);
                    points.remove(2);
                }
            } else {
                double midXFrom = from.getX() - rx * edgeFromDeparture;
                double midYFrom = from.getY() - ry * edgeFromDeparture;
                double midXTo = to.getX() + rx * edgeToDeparture;
                double midYTo = to.getY() + ry * edgeToDeparture;
                Point2D controlPointFrom = new Point2D.Double(ex + midXFrom, ey + midYFrom);
                Point2D controlPointTo = new Point2D.Double(ex + midXTo, ey + midYTo);
                if (points.size() == 2) {
                    points.add(1, controlPointFrom);
                    points.add(2, controlPointTo);
                } else if (points.size() == 3) {
                    points.set(1, controlPointFrom);
                    points.add(2, controlPointTo);
                } else {
                    points.set(1, controlPointFrom);
                    points.set(2, controlPointTo);
                }
            }
        }
    }

    /**
     * Getter to obtain the distance between each parallel edge
     * 
     * @return Distance
     */
    public static double getEdgeSeparation() {
        return JGParallelEdgeRouter.edgeSeparation;
    }

    /**
     * Setter to define distance between each parallel edge
     * 
     * @param edgeSeparation
     *            New distance
     */
    public static void setEdgeSeparation(double edgeSeparation) {
        JGParallelEdgeRouter.edgeSeparation = edgeSeparation;
    }

    /**
     * Getter to obtain the distance between intermediate and source/target
     * points
     * 
     * @return Distance
     */
    public static double getEdgeDeparture() {
        return JGParallelEdgeRouter.edgeDeparture;
    }

    /**
     * Setter to define distance between intermediate and source/target points
     * 
     * @param edgeDeparture
     *            New distance
     */
    public static void setEdgeDeparture(double edgeDeparture) {
        JGParallelEdgeRouter.edgeDeparture = edgeDeparture;
    }

    /**
     * Getter to obtain the list of parallel edges
     * 
     * @param edge
     *            Edge on which one wants to know parallel edges
     * @return Object[] Array of parallel edges (include edge passed on
     *         argument)
     */
    private Object[] getParallelEdges(EdgeView edge) {
        return DefaultGraphModel.getEdgesBetween(emptyModel, edge.getSource().getParentView().getCell(), edge.getTarget().getParentView().getCell(), false);
    }

    public int getPreferredLineStyle(EdgeView arg0) {
        return 0;
    }

    public List route(EdgeView edge) {
        List points = new ArrayList();
        if ((null == edge.getSource()) || (null == edge.getTarget()) || (null == edge.getSource().getParentView()) || (null == edge.getTarget().getParentView())) {
            return points;
        }
        Object[] edges = getParallelEdges(edge);
        if (edges == null) {
            return points;
        }
        if (edges.length < 2) {
            if (points.size() > 3) {
                points.remove(1);
                points.remove(2);
            } else if (points.size() > 2) {
                points.remove(1);
            }
            return points;
        }
        int numEdges = edges.length;
        int position = 0;
        for (int i = 0; i < edges.length; i++) {
            Object e = edges[i];
            if (e == edge.getCell()) {
                position = i + 1;
            }
        }
        VertexView nodeFrom = (VertexView) edge.getSource().getParentView();
        VertexView nodeTo = (VertexView) edge.getTarget().getParentView();
        Point2D from = nodeFrom.getCenterPoint();
        Point2D to = nodeTo.getCenterPoint();
        if (from != null && to != null) {
            double dy = from.getY() - to.getY();
            double dx = from.getX() - to.getX();
            double m = dy / dx;
            double theta = Math.atan(-1 / m);
            double rx = dx / Math.sqrt(dx * dx + dy * dy);
            double ry = dy / Math.sqrt(dx * dx + dy * dy);
            double sizeFrom = Math.max(nodeFrom.getBounds().getWidth(), nodeFrom.getBounds().getHeight()) / 2.;
            double sizeTo = Math.max(nodeTo.getBounds().getWidth(), nodeTo.getBounds().getHeight()) / 2.;
            double edgeMiddleDeparture = (Math.sqrt(dx * dx + dy * dy) - sizeFrom - sizeTo) / 2 + sizeFrom;
            double edgeFromDeparture = edgeDeparture + sizeFrom;
            double edgeToDeparture = edgeDeparture + sizeTo;
            double r = edgeSeparation * Math.floor(position / 2);
            if (0 == (position % 2)) {
                r = -r;
            }
            double ex = r * Math.cos(theta);
            double ey = r * Math.sin(theta);
            if (edgeMiddleDeparture <= edgeFromDeparture) {
                double midX = from.getX() - rx * edgeMiddleDeparture;
                double midY = from.getY() - ry * edgeMiddleDeparture;
                Point2D controlPoint = new Point2D.Double(ex + midX, ey + midY);
                if (points.size() == 2) {
                    points.add(1, controlPoint);
                } else if (points.size() == 3) {
                    points.set(1, controlPoint);
                } else {
                    points.set(1, controlPoint);
                    points.remove(2);
                }
            } else {
                double midXFrom = from.getX() - rx * edgeFromDeparture;
                double midYFrom = from.getY() - ry * edgeFromDeparture;
                double midXTo = to.getX() + rx * edgeToDeparture;
                double midYTo = to.getY() + ry * edgeToDeparture;
                Point2D controlPointFrom = new Point2D.Double(ex + midXFrom, ey + midYFrom);
                Point2D controlPointTo = new Point2D.Double(ex + midXTo, ey + midYTo);
                if (points.size() == 2) {
                    points.add(1, controlPointFrom);
                    points.add(2, controlPointTo);
                } else if (points.size() == 3) {
                    points.set(1, controlPointFrom);
                    points.add(2, controlPointTo);
                } else {
                    points.set(1, controlPointFrom);
                    points.set(2, controlPointTo);
                }
            }
        }
        return points;
    }

    @Override
    public List route(GraphLayoutCache arg0, EdgeView arg1) {
        return null;
    }
}
