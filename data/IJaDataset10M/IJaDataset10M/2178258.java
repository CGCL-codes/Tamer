package com.horstmann.violet.product.diagram.sequence;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import com.horstmann.violet.product.diagram.abstracts.edge.SegmentedLineEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.property.ArrowHead;

/**
 * An edge that joins two call nodes. Typically, call edges are used in sequence diagram to represent calls between entities (call
 * nodes).
 */
public class CallEdge extends SegmentedLineEdge {

    /**
     * Default constructor
     */
    public CallEdge() {
        setSignal(false);
    }

    /**
     * Gets the signal property.
     * 
     * @return true if this is an edge that represents an asynchronus signal
     */
    public boolean isSignal() {
        return signal;
    }

    /**
     * Sets the signal property.
     * 
     * @param newValue true if this is an edge that represents an asynchronus signal
     */
    public void setSignal(boolean newValue) {
        signal = newValue;
    }

    @Override
    public ArrowHead getEndArrowHead() {
        if (signal) {
            return ArrowHead.V;
        }
        return ArrowHead.BLACK_TRIANGLE;
    }

    @Override
    public Line2D getConnectionPoints() {
        ArrayList<Point2D> points = getPoints();
        Point2D p1 = points.get(0);
        Point2D p2 = points.get(points.size() - 1);
        return new Line2D.Double(p1, p2);
    }

    @Override
    public ArrayList<Point2D> getPoints() {
        INode endingNode = getEnd();
        INode startingNode = getStart();
        if (isActivationBarsOnSameLifeline(startingNode, endingNode)) {
            return getPointsForLoopOnActivationBarNode(startingNode, endingNode);
        }
        return getPointsForActivationBarsOnDifferentLifeLines(startingNode, endingNode);
    }

    private boolean isActivationBarsOnSameLifeline(INode startingNode, INode endingNode) {
        if (startingNode.getClass().isAssignableFrom(ActivationBarNode.class) && endingNode.getClass().isAssignableFrom(ActivationBarNode.class)) {
            ActivationBarNode startingActivationBarNode = (ActivationBarNode) startingNode;
            ActivationBarNode endingActivationBarNode = (ActivationBarNode) endingNode;
            if (startingActivationBarNode.getImplicitParameter() == endingActivationBarNode.getImplicitParameter()) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Point2D> getPointsForActivationBarsOnDifferentLifeLines(INode startingNode, INode endingNode) {
        Point2D startingPoint = startingNode.getConnectionPoint(this);
        Point2D endingPoint = endingNode.getConnectionPoint(this);
        ArrayList<Point2D> a = new ArrayList<Point2D>();
        a.add(startingPoint);
        a.add(endingPoint);
        return a;
    }

    private ArrayList<Point2D> getPointsForLoopOnActivationBarNode(INode startingNode, INode endingNode) {
        ArrayList<Point2D> a = new ArrayList<Point2D>();
        Point2D startingPoint = startingNode.getConnectionPoint(this);
        Point2D endingPoint = endingNode.getConnectionPoint(this);
        Point2D p = startingPoint;
        Point2D q = new Point2D.Double(endingPoint.getX() + LOOP_GAP, p.getY());
        Point2D r = new Point2D.Double(q.getX(), endingPoint.getY());
        Point2D s = endingPoint;
        a.add(p);
        a.add(q);
        a.add(r);
        a.add(s);
        return a;
    }

    /** Indicate if the node represents an asynchonus signal */
    private boolean signal;

    /** Horizintal gap used to connected two activation bars on the same lifeline */
    private static int LOOP_GAP = 15;
}
