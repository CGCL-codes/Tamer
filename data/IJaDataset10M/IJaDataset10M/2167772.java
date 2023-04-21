package edu.uci.ics.jung.visualization.contrib;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.Set;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.utils.UserData;
import edu.uci.ics.jung.visualization.Coordinates;
import edu.uci.ics.jung.visualization.SpringLayout;

/**
 * @author John Yesberg
 * 
 * DAGLayout is a layout algorithm which is suitable for tree-like directed
 * acyclic graphs. Parts of it will probably not terminate if the graph is
 * cyclic! The layout will result in directed edges pointing generally upwards.
 * Any vertices with no successors are considered to be level 0, and tend
 * towards the top of the layout. Any vertex has a level one greater than the
 * maximum level of all its successors.
 * 
 * Note: had to make minor access changes to SpringLayout to make this work.
 * FORCE_CONSTANT, LengthFunction, SpringVertexData, and SpringEdgeData were
 * all made "protected".
 */
public class DAGLayout extends SpringLayout {

    protected static final String MINIMUMLEVELKEY = "DAGLayout.minimumLevel";

    static int graphHeight;

    static int numRoots;

    final double SPACEFACTOR = 1.3;

    final double LEVELATTRACTIONRATE = 0.8;

    final double MSV_THRESHOLD = 10.0;

    static double meanSquareVel;

    static boolean stoppingIncrements = false;

    static int incrementsLeft;

    final int COOL_DOWN_INCREMENTS = 200;

    public DAGLayout(Graph g) {
        super(g);
    }

    /**
   * setRoot calculates the level of each vertex in the graph. Level 0 is
   * allocated to any vertex with no successors. Level n+1 is allocated to
   * any vertex whose successors' maximum level is n.
   */
    public static void setRoot(Graph g) {
        numRoots = 0;
        Set verts = g.getVertices();
        Iterator iter = verts.iterator();
        Vertex v;
        Set successors;
        while (iter.hasNext()) {
            v = (Vertex) iter.next();
            successors = v.getSuccessors();
            if (successors.size() == 0) {
                setRoot(v);
                numRoots++;
            }
        }
    }

    /**
   * Set vertex v to be level 0.
   */
    public static void setRoot(Vertex v) {
        v.setUserDatum(MINIMUMLEVELKEY, new Integer(0), UserData.REMOVE);
        propagateMinimumLevel(v);
    }

    /**
   * A recursive method for allocating the level for each vertex. Ensures
   * that all predecessors of v have a level which is at least one greater
   * than the level of v.
   * 
   * @param v
   */
    public static void propagateMinimumLevel(Vertex v) {
        int level = ((Integer) v.getUserDatum(MINIMUMLEVELKEY)).intValue();
        Set predecessors = v.getPredecessors();
        Iterator iter = predecessors.iterator();
        Vertex child;
        while (iter.hasNext()) {
            child = (Vertex) iter.next();
            int oldLevel, newLevel;
            Object o = child.getUserDatum(MINIMUMLEVELKEY);
            if (o != null) oldLevel = ((Integer) o).intValue(); else oldLevel = 0;
            newLevel = Math.max(oldLevel, level + 1);
            child.setUserDatum(MINIMUMLEVELKEY, new Integer(newLevel), UserData.REMOVE);
            if (newLevel > graphHeight) graphHeight = newLevel;
            propagateMinimumLevel(child);
        }
    }

    /**
   * Sets random locations for a vertex within the dimensions of the space.
   * This overrides the method in AbstractLayout
   * 
   * @param coord
   * @param d
   */
    protected void initializeLocation(Vertex v, Coordinates coord, Dimension d) {
        int level = ((Integer) v.getUserDatum(MINIMUMLEVELKEY)).intValue();
        int minY = (int) (level * d.getHeight() / (graphHeight * SPACEFACTOR));
        double x = Math.random() * d.getWidth();
        double y = Math.random() * (d.getHeight() - minY) + minY;
        coord.setX(x);
        coord.setY(y);
    }

    /**
   * Had to override this one as well, to ensure that setRoot() is called.
   */
    protected void initialize_local() {
        for (Iterator iter = getGraph().getEdges().iterator(); iter.hasNext(); ) {
            Edge e = (Edge) iter.next();
            SpringEdgeData sed = getSpringData(e);
            if (sed == null) {
                sed = new SpringEdgeData(e);
                e.addUserDatum(getSpringKey(), sed, UserData.REMOVE);
            }
            calcEdgeLength(sed, lengthFunction);
        }
        setRoot(getGraph());
    }

    /**
   * Override the moveNodes() method from SpringLayout. The only change we
   * need to make is to make sure that nodes don't float higher than the minY
   * coordinate, as calculated by their minimumLevel.
   */
    protected void moveNodes() {
        double oldMSV = meanSquareVel;
        meanSquareVel = 0;
        synchronized (getCurrentSize()) {
            for (Iterator i = getVisibleVertices().iterator(); i.hasNext(); ) {
                Vertex v = (Vertex) i.next();
                if (dontMove(v)) continue;
                SpringLayout.SpringVertexData vd = getSpringData(v);
                Coordinates xyd = getCoordinates(v);
                int width = getCurrentSize().width;
                int height = getCurrentSize().height;
                int level = ((Integer) v.getUserDatum(MINIMUMLEVELKEY)).intValue();
                int minY = (int) (level * height / (graphHeight * SPACEFACTOR));
                int maxY = level == 0 ? (int) (height / (graphHeight * SPACEFACTOR * 2)) : height;
                vd.dx += 2 * vd.repulsiondx + vd.edgedx;
                vd.dy += vd.repulsiondy + vd.edgedy;
                double delta = xyd.getY() - minY;
                vd.dy -= delta * LEVELATTRACTIONRATE;
                if (level == 0) vd.dy -= delta * LEVELATTRACTIONRATE;
                meanSquareVel += (vd.dx * vd.dx + vd.dy * vd.dy);
                xyd.addX(Math.max(-5, Math.min(5, vd.dx)));
                xyd.addY(Math.max(-5, Math.min(5, vd.dy)));
                if (xyd.getX() < 0) {
                    xyd.setX(0);
                } else if (xyd.getX() > width) {
                    xyd.setX(width);
                }
                if (xyd.getY() < minY) {
                    xyd.setY(minY);
                } else if (xyd.getY() > maxY) {
                    xyd.setY(maxY);
                }
                if (numRoots == 1 && level == 0) {
                    xyd.setX(width / 2);
                }
            }
        }
        if (!stoppingIncrements && Math.abs(meanSquareVel - oldMSV) < MSV_THRESHOLD) {
            stoppingIncrements = true;
            incrementsLeft = COOL_DOWN_INCREMENTS;
        } else if (stoppingIncrements && Math.abs(meanSquareVel - oldMSV) <= MSV_THRESHOLD) {
            incrementsLeft--;
            if (incrementsLeft <= 0) incrementsLeft = 0;
        }
    }

    /**
   * Override incrementsAreDone so that we can eventually stop.
   */
    public boolean incrementsAreDone() {
        if (stoppingIncrements && incrementsLeft == 0) return true; else return false;
    }

    /**
   * Override forceMove so that if someone moves a node, we can re-layout
   * everything.
   */
    public void forceMove(Vertex picked, int x, int y) {
        Coordinates coord = getCoordinates(picked);
        coord.setX(x);
        coord.setY(y);
        stoppingIncrements = false;
    }

    /**
   * Overridden relaxEdges. This one reduces the effect of edges between
   * greatly different levels.
   *  
   */
    protected void relaxEdges() {
        for (Iterator i = getVisibleEdges().iterator(); i.hasNext(); ) {
            Edge e = (Edge) i.next();
            Vertex v1 = getAVertex(e);
            Vertex v2 = e.getOpposite(v1);
            double vx = getX(v1) - getX(v2);
            double vy = getY(v1) - getY(v2);
            double len = Math.sqrt(vx * vx + vy * vy);
            int level1 = ((Integer) v1.getUserDatum(MINIMUMLEVELKEY)).intValue();
            int level2 = ((Integer) v2.getUserDatum(MINIMUMLEVELKEY)).intValue();
            double desiredLen = getLength(e);
            len = (len == 0) ? .0001 : len;
            double f = (1.0 / 3.0) * (desiredLen - len) / len;
            f = f * Math.pow(stretch / 100.0, (v1.degree() + v2.degree() - 2));
            if (level1 != level2) f = f / Math.pow(Math.abs(level2 - level1), 1.5);
            double dx = f * vx;
            double dy = f * vy;
            SpringVertexData v1D, v2D;
            v1D = getSpringData(v1);
            v2D = getSpringData(v2);
            SpringEdgeData sed = getSpringData(e);
            sed.f = f;
            v1D.edgedx += dx;
            v1D.edgedy += dy;
            v2D.edgedx += -dx;
            v2D.edgedy += -dy;
        }
    }
}
