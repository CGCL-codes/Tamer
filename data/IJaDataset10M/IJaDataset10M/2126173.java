package gj.layout.tree;

import gj.awt.geom.Geometry;
import gj.awt.geom.Path;
import gj.model.Arc;
import gj.model.Node;
import gj.util.ArcHelper;
import gj.util.ArcIterator;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

class Algorithm {

    /** a stack of orientations to return to */
    private Stack oldos = new Stack();

    /** the orientation in use */
    private Orientation orientn;

    /** the node options in use */
    private NodeOptions nodeop;

    /** the arc options in use */
    private ArcOptions arcop;

    /** latitude alignment is enabled (0-1) */
    private double latalign;

    /** whether we align children */
    private boolean balance;

    /** whether we bend arcs */
    private boolean bendarcs;

    Algorithm(Orientation orientation, NodeOptions nodeOptions, ArcOptions arcOptions, double latAlignment, boolean isBalanceChildrenEnable, boolean isBendedArcs) {
        orientn = orientation;
        nodeop = nodeOptions;
        arcop = arcOptions;
        latalign = latAlignment;
        balance = isBalanceChildrenEnable;
        bendarcs = isBendedArcs;
    }

    Rectangle layout(Tree tree, Collection debugShapes) {
        Node root = tree.getRoot();
        int rootLat = orientn.getLatitude(root.getPosition()), rootLon = orientn.getLongitude(root.getPosition());
        Branch branch = layout(root, null, tree, 0);
        int dlat = rootLat - orientn.getLatitude(root.getPosition()), dlon = rootLon - orientn.getLongitude(root.getPosition());
        Contour result = branch.finalize(dlat, dlon, orientn);
        if (debugShapes != null) debugShapes.add(new DebugShape(result));
        return orientn.getBounds(result);
    }

    /**
   * Layout a node and all its descendants
   * <il>
   *  <li>all children of node (without backtracking to parent) are layouted
   *      recursively and placed beside each other (west to east)
   *  <li>node is placed as atop of children (north of)
   *  <li>arcs are layouted between node and its children
   *  <li>arcs/nodes to children are placed relative to root  
   *  <li>a merged contour for node and its children is calculated
   * </il>
   */
    private Branch layout(Node node, Node parent, Tree tree, int generation) {
        Branch[] children = calcChildren(node, parent, tree, generation);
        Contour contour = calcParentPosition(node, children, tree, generation);
        calcArcs(node, contour);
        contour = Contour.merge(Branch.getCountoursForMerge(contour, children));
        return new Branch(node, parent, contour);
    }

    /**
   * Calculate branches for children side-by-side
   */
    private Branch[] calcChildren(Node root, Node parent, Tree tree, int generation) {
        List children = new ArrayList(root.getArcs().size());
        ArcIterator it = new ArcIterator(root);
        while (it.next()) {
            if (!it.isFirst || it.isLoop || it.dest == parent) continue;
            layout(it.dest, root, tree, generation + 1).insertEastOf(children, orientn);
        }
        Branch[] result = (Branch[]) children.toArray(new Branch[children.size()]);
        if (balance) calcBalance(result, 0, result.length - 1, true);
        return result;
    }

    /**
   * Calculate parent's position and contour
   */
    private Contour calcParentPosition(Node root, Branch[] children, Tree tree, int generation) {
        Shape shape = root.getShape();
        Contour result = orientn.getContour(shape != null ? shape.getBounds2D() : new Rectangle2D.Double());
        result.pad(nodeop.getPadding(root, orientn));
        int lat = 0, lon = 0;
        if (children.length > 0) {
            lon = nodeop.getLongitude(root, children, orientn);
            lat = children[0].getLatitude() - result.south;
        }
        if (latalign >= 0 && latalign <= 1) {
            lat = tree.getLatitude(generation);
            int min = lat - result.north, max = lat + tree.getHeight(generation) - result.south;
            lat = (int) (min + (max - min) * Math.min(1D, Math.max(0D, latalign)));
        }
        root.getPosition().setLocation(orientn.getPoint(lat, lon));
        result.translate(lat, lon);
        if (latalign >= 0 && latalign <= 1) {
            result.north = tree.getLatitude(generation);
        }
        return result;
    }

    /**
   * Calculate the arcs to children
   */
    private void calcArcs(Node node, Contour parent) {
        ArcIterator it = new ArcIterator(node);
        while (it.next()) {
            if (it.arc.getPath() == null) continue;
            if (it.isLoop) ArcHelper.update(it.arc); else {
                if (bendarcs) calcBendedArc(it.arc, parent); else calcStraightArc(it.arc);
            }
        }
    }

    /**
   * make a straight arc
   */
    private void calcStraightArc(Arc arc) {
        Node n1 = arc.getStart(), n2 = arc.getEnd();
        Point2D p1 = arcop.getPort(arc, n1, orientn), p2 = arcop.getPort(arc, n2, orientn);
        Shape s1 = n1.getShape(), s2 = n2.getShape();
        p1 = Geometry.getIntersection(p1, orientn.getPoint(orientn.getLatitude(p2), orientn.getLongitude(p1)), p1, s1);
        p2 = Geometry.getIntersection(p2, orientn.getPoint(orientn.getLatitude(p1), orientn.getLongitude(p2)), p2, s2);
        Path path = arc.getPath();
        path.reset();
        path.moveTo(p1);
        path.lineTo(p2);
    }

    /**
   * make a bended arc
   */
    private void calcBendedArc(Arc arc, Contour parent) {
        Node n1 = arc.getStart(), n2 = arc.getEnd();
        Point2D p1 = arcop.getPort(arc, n1, orientn), p2 = new Point2D.Double(), p3 = new Point2D.Double(), p4 = arcop.getPort(arc, n2, orientn);
        p2.setLocation(orientn.getPoint(parent.south, orientn.getLongitude(p1)));
        p3.setLocation(orientn.getPoint(parent.south, orientn.getLongitude(p4)));
        ArcHelper.update(arc.getPath(), new Point2D[] { p1, p2, p3, p4 }, n1.getShape(), n2.getShape());
    }

    /**
   * Calculate the balance of branches that are placed from west to east 
   */
    private void calcBalance(Branch[] branches, int start, int end, boolean right) {
    }

    /**
   * A shape that can be used for debugging a contour
   */
    private class DebugShape extends Path {

        /**
     * Constructor
     */
        private DebugShape(Contour contour) {
            Contour.Iterator it = contour.getIterator(Contour.WEST);
            Point2D a = orientn.getPoint(it.north, it.longitude);
            moveTo(a);
            do {
                lineTo(orientn.getPoint(it.north, it.longitude));
                lineTo(orientn.getPoint(it.south, it.longitude));
            } while (it.next());
            Point2D b = getLastPoint();
            moveTo(a);
            it = contour.getIterator(Contour.EAST);
            do {
                lineTo(orientn.getPoint(it.north, it.longitude));
                lineTo(orientn.getPoint(it.south, it.longitude));
            } while (it.next());
            lineTo(b);
        }
    }
}
