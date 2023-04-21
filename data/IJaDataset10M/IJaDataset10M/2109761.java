package at.ac.tuwien.ifs.alviz.smallworld.layout;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import at.ac.tuwien.ifs.alviz.smallworld.render.TubeRenderer;
import at.ac.tuwien.ifs.alviz.smallworld.types.Cluster;
import at.ac.tuwien.ifs.alviz.smallworld.types.VoroNode;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.assignment.Layout;
import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.Edge;
import at.ac.tuwien.ifs.alviz.smallworld.geom.Pnt;

/**
 * Combination filter/layout fliters top clusters according to
 * a 2-D DOA function and determins their size and position
 *
 * Unfortunately there isn't a clear-cut distinction between
 * filtering and layout in the case of Degree of Abstraction
 * so I have made the grotesque decision to simply copy code
 * from Filter into Layout. 
 * 
 * @author Stephen
 */
public class DOALayout extends Layout {

    private boolean CONST_fisheye = false;

    private double CONST_DOA = 0.25;

    public static double DOA_RADIUS = 50;

    public static double ZERO_RADIUS = 50;

    protected TubeRenderer m_tube = null;

    private Set m_classes;

    private boolean m_gc;

    private boolean m_edges = false;

    private static int nodeCounter = 0;

    protected TreeMap<Integer, VoroNode> nodes = new TreeMap<Integer, VoroNode>();

    public void setTube(TubeRenderer tube) {
        m_tube = tube;
    }

    public void setEdges(boolean edges) {
        m_edges = edges;
    }

    public void setConstantDOA(double DOA) {
        CONST_DOA = DOA;
    }

    public double getConstantDOA() {
        return CONST_DOA;
    }

    public void setFisheye(boolean fisheye) {
        CONST_fisheye = fisheye;
    }

    /**
     * Creates a new filter associated with the given item class that
     * optionally performs garbage collection.
     * @param itemClass the item class associated with this filter
     * @param gc indicates whether garbage collection should be performed
     */
    public DOALayout(String itemClass, boolean gc) {
        m_classes = new HashSet(3);
        if (itemClass != null) m_classes.add(itemClass);
        m_gc = gc;
    }

    /**
     * Indicates whether or not this filter performs garbage collection.
     * @return true if garbage collection enabled, false otherwise
     */
    public boolean isGarbageCollectEnabled() {
        return m_gc;
    }

    /**
     * Sets a flag determining if garbage collection is performed
     * @param s the flag indicating if garbage collected is performed
     */
    public void setGarbageCollect(boolean s) {
        m_gc = s;
    }

    /**
     * Gets the item classes associated with this Filter. Item classes are
     * repreesnted as String instances corresponding to entries in an 
     * ItemRegistry.
     * @return the item classes associated with this Filter
     */
    public String[] getItemClasses() {
        return (String[]) m_classes.toArray(new String[m_classes.size()]);
    }

    /**
     * Associate an item class with this filter
     * @param itemClass the itemClass to add
     */
    public void addItemClass(String itemClass) {
        m_classes.add(itemClass);
    }

    /**
     * Dissociate an item class with this filter
     * @param itemClass the itemClass to remove
     */
    public void removeItemClass(String itemClass) {
        m_classes.remove(itemClass);
    }

    protected String m_itemClass = ItemRegistry.DEFAULT_NODE_CLASS;

    protected Point2D m_mousePos = null;

    protected Cluster m_dendrogram_root = null;

    private ItemRegistry m_registry = null;

    public DOALayout() {
        this(ItemRegistry.DEFAULT_NODE_CLASS, true);
    }

    public DOALayout(String itemClass) {
        this(itemClass, true);
        m_itemClass = itemClass;
    }

    /**
     * Computes the distance between a point and a rectangle
     * 
     * @param point
     * @param rect
     * @return
     */
    protected double distPointRect(Point2D point, Rectangle2D rect) {
        if (rect.contains(point)) return 0.;
        double dx = Math.abs(point.getX() - rect.getCenterX());
        double dy = Math.abs(point.getY() - rect.getCenterY());
        double sqrDist = 0.;
        double sqdx = dx - rect.getWidth() / 2.;
        double sqdy = dy - rect.getHeight() / 2.;
        if (sqdx > 0) {
            sqrDist += sqdx * sqdx;
        }
        if (sqdy > 0) {
            sqrDist += sqdy * sqdy;
        }
        return Math.sqrt(sqrDist);
    }

    protected float doaFunc(Rectangle2D bounds) {
        if (CONST_fisheye && m_anchor != null) {
            double dist = distPointRect(m_anchor, bounds);
            if (dist < DOA_RADIUS + ZERO_RADIUS) {
                if (dist < ZERO_RADIUS) return 0.0f;
                return (float) (((dist - ZERO_RADIUS) / DOA_RADIUS) * CONST_DOA);
            }
        }
        return (float) CONST_DOA;
    }

    protected float doaFunc(Point2D point) {
        if (CONST_fisheye && m_anchor != null) {
            double dist = point.distance(m_anchor);
            if (dist < DOA_RADIUS + ZERO_RADIUS) {
                if (dist < ZERO_RADIUS) return 0.0f;
                return (float) (((dist - ZERO_RADIUS) / DOA_RADIUS) * CONST_DOA);
            }
        }
        return (float) CONST_DOA;
    }

    protected void selectClusters(Cluster node) {
        selectClusters(node, 0);
    }

    protected void selectClusters(Cluster node, int level) {
        if (node.getDistance() > 0 && node.getDistance() >= m_dendrogram_root.getDistance() * doaFunc(node.getBounds())) {
            Iterator iter = node.getChildren();
            while (iter.hasNext()) {
                selectClusters((Cluster) iter.next(), level + 1);
            }
        } else {
            VoroNode vnode = (VoroNode) m_registry.getItem(m_itemClass, node, true, true);
            if (node.getParent() != null) {
                float lambda = (Math.max(doaFunc(node.getParent().getBounds()) * m_dendrogram_root.getDistance(), node.getDistance()) - node.getDistance()) / (node.getParent().getDistance() - node.getDistance());
                double x = (float) node.getCenter().getX() + lambda * (node.getParent().getCenter().getX() - node.getCenter().getX());
                double y = (float) node.getCenter().getY() + lambda * (node.getParent().getCenter().getY() - node.getCenter().getY());
                int size = (int) (node.getRadius() + lambda * (node.getParent().getRadius() - node.getRadius()));
                if (CONST_fisheye) {
                    if (m_anchor != null && m_anchor.distance(x, y) < ZERO_RADIUS) {
                        x = fisheye(x, m_anchor.getX(), 2, x - ZERO_RADIUS / 1, x + ZERO_RADIUS / 1);
                        y = fisheye(y, m_anchor.getY(), 2, y - ZERO_RADIUS / 1, y + ZERO_RADIUS / 1);
                        if (m_anchor.distance(x, y) > ZERO_RADIUS) {
                            double dx = m_anchor.getX() - x;
                            double dy = m_anchor.getY() - y;
                            double norm = ZERO_RADIUS / Math.sqrt(dx * dx + dy * dy);
                            dx *= norm;
                            dy *= norm;
                            x = m_anchor.getX() - dx;
                            y = m_anchor.getY() - dy;
                        }
                    }
                }
                vnode.setPos(new Pnt(x, y, vnode));
                vnode.setSize(size);
                nodes.put(level * 1000000 + nodeCounter, vnode);
                ++nodeCounter;
            }
        }
    }

    /**
     * Shamelessly stolen from FisheyeDistortion
     * @return
     */
    protected double fisheye(double x, double a, double d, double min, double max) {
        if (d != 0) {
            boolean left = x < a;
            double v, m = (left ? a - min : max - a);
            if (m == 0) m = max - min;
            v = Math.abs(x - a) / m;
            v = (d + 1) / (d + (1 / v));
            return (left ? -1 : 1) * m * v + a;
        } else {
            return x;
        }
    }

    public void run(ItemRegistry registry, double arg1) {
        int childs = 0;
        m_registry = registry;
        m_registry.clear();
        Iterator iter_nodes = m_registry.getGraph().getNodes();
        while (iter_nodes.hasNext()) {
            m_dendrogram_root = (Cluster) iter_nodes.next();
            if (m_dendrogram_root.isRoot()) {
                break;
            }
        }
        nodeCounter = 0;
        nodes.clear();
        if (m_dendrogram_root != null) selectClusters(m_dendrogram_root);
        boolean exec = true;
        while (exec) {
            exec = false;
            for (VoroNode vnA : nodes.values()) {
                for (VoroNode vnB : nodes.values()) {
                    if (vnA.equals(vnB)) continue;
                    double dx = vnA.pos().coord(0) - vnB.pos().coord(0);
                    double dy = vnA.pos().coord(1) - vnB.pos().coord(1);
                    if (dx == 0 && dy == 0) {
                        dx = dy = 1;
                    }
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    double minDistance = (double) ((double) vnA.size() / 2 + (double) vnB.size() / 2) * (1.1 - 0.8 * CONST_DOA);
                    if (distance < minDistance) {
                        double norm = minDistance / distance;
                        vnA.setPos(new Pnt(vnA.pos().coord(0) + dx * norm / 2, vnA.pos().coord(1) + dy * norm / 2, vnA));
                        vnB.setPos(new Pnt(vnB.pos().coord(0) - dx * norm / 2, vnB.pos().coord(1) - dy * norm / 2, vnB));
                        exec = true;
                    }
                }
            }
        }
        for (VoroNode vn : nodes.values()) {
            vn.computeShape();
        }
        double minedgelength = Double.MAX_VALUE;
        double maxedgelength = Double.MIN_VALUE;
        double dx = 0.;
        double dy = 0.;
        double edgelength = 0.;
        if (m_edges) {
            Iterator iter_edges = m_registry.getGraph().getEdges();
            while (iter_edges.hasNext()) {
                Edge edge = (Edge) iter_edges.next();
                Cluster clust1 = (Cluster) edge.getFirstNode();
                Cluster clust2 = (Cluster) edge.getSecondNode();
                VoroNode node1 = null;
                while (node1 == null && clust1 != null) {
                    node1 = (VoroNode) m_registry.getItem(m_itemClass, clust1, false, false);
                    clust1 = node1 != null ? clust1 : clust1.getParent();
                }
                VoroNode node2 = null;
                while (node2 == null & clust2 != null) {
                    node2 = (VoroNode) m_registry.getItem(m_itemClass, clust2, false, false);
                    clust2 = node2 != null ? clust2 : clust2.getParent();
                }
                if (node1 != null && node2 != null && node2 != node1) {
                    m_registry.getEdgeItem(new DefaultEdge(clust1, clust2), true);
                    if (m_tube != null) {
                        dx = node1.pos().coord(0) - node2.pos().coord(0);
                        dy = node1.pos().coord(1) - node2.pos().coord(1);
                        edgelength = Math.sqrt(dx * dx + dy * dy);
                        minedgelength = Math.min(minedgelength, edgelength);
                        maxedgelength = Math.max(maxedgelength, edgelength);
                    }
                }
            }
            if (m_tube != null) {
                m_tube.setMaxDistance((float) maxedgelength);
                m_tube.setMinDistance((float) minedgelength);
            }
        }
        if (m_gc) {
            Iterator iter = m_classes.iterator();
            while (iter.hasNext()) {
                String iclass = (String) iter.next();
                registry.garbageCollect(iclass);
            }
        }
    }

    public static void main(String[] args) {
    }
}
