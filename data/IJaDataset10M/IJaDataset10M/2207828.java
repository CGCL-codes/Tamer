package ch.ethz.sg.cuttlefish.layout;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import ch.ethz.sg.cuttlefish.misc.Edge;
import ch.ethz.sg.cuttlefish.misc.Vertex;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.Graph;

/**
* @author markus michael geipel
* 
*
* An implementation of the ARF Layouter. 
* See http://www.sg.ethz.ch/research/ for details
*/
public class ARF3Layout<V, E> extends AbstractLayout<V, E> implements IterativeContext {

    /**
 * number of position updates before the graph is rendered 
 */
    private int updatesPerFrame = 1;

    /**
 * the parameter a controls the attraction between connected nodes. 
 */
    private double a = 3;

    /**
 * ??? is a scaling factor for the attractive term. Connected as well as unconnected nodes are affected.
 */
    private double attraction = 0.2;

    /**
 * b scales the repulsive force
 */
    private double b = 8;

    /**
 * deltaT controls the calculation precision: smaller deltaT results in higher precission
 */
    private double deltaT = 2;

    private boolean done = false;

    private int maxUpdates = 50;

    int countUpdates = 0;

    /**
 * A marker used to tag nodes that shall not be moved
 */
    public static final String FIXED = "_ch.ethz.sg.jung.visualisation.FIXED";

    /**
 * if the movement in the system is less than epsilon*|V|, the algorithm terminates
 */
    private double epsilon = .2;

    /**
 * If the layout is used in a non interactive way, this variable gives a maximum bound to the layout steps
 */
    private int maxRelayouts = 30;

    /**
 * the random number generator used
 */
    private Random rnd = new Random();

    /**
 * 
 */
    private boolean incremental = true;

    /**
 * a maximum force for a node
 */
    private double forceCutoff = 7;

    private boolean verbose = false;

    private Collection<Vertex> visualizedVertices = new HashSet<Vertex>();

    private Point2D centerOfMass;

    /**
 * Genrates a new Layout for graph g
 * @param g
 */
    public ARF3Layout(Graph<V, E> g) {
        super(g);
    }

    /**
 * Generates a new Layout for graph g. if incremental is false the layout will not be interactive.
 * @param g
 * @param incremental
 */
    public ARF3Layout(Graph<V, E> g, boolean incremental) {
        super(g);
        this.incremental = incremental;
        initialize();
        if (!incremental) {
            update();
        }
    }

    public void complete(int n) {
        done = false;
        for (int i = 1; i <= n; i++) {
            System.out.println("i: " + i);
            step();
        }
        done = true;
    }

    protected void initialize_local_vertex(Vertex v) {
    }

    @SuppressWarnings("unchecked")
    public void advancePositions() {
        try {
            for (int i = 0; i < updatesPerFrame; i++) {
                for (Object o : graph.getVertices()) {
                    Vertex v = (Vertex) o;
                    if (!isFixed(v)) {
                        Point2D c = transform((V) v);
                        if (c != null) {
                            Point2D f = getForceforNode(v);
                            double deltaIndividual = getGraph().degree((V) v) > 1 ? deltaT / Math.pow(getGraph().degree((V) v), 0.4) : deltaT;
                            f.setLocation(f.getX() * deltaIndividual, f.getY() * deltaIndividual);
                            c.setLocation(c.getX() + f.getX(), c.getY() + f.getY());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
        align(100, 100);
    }

    /**
 * aligns the graph to make (x0,y0) the left upper corner
 * @param x0 
 * @param y0 
 */
    @SuppressWarnings("unchecked")
    public void align(double x0, double y0) {
        double x = java.lang.Double.MAX_VALUE;
        double y = java.lang.Double.MAX_VALUE;
        for (Object o : graph.getVertices()) {
            Vertex v = (Vertex) o;
            Point2D c = transform((V) v);
            x = Math.min(x, c.getX());
            y = Math.min(y, c.getY());
        }
        for (Object o : graph.getVertices()) {
            Vertex v = (Vertex) o;
            Point2D c = transform((V) v);
            c.setLocation(c.getX() - x + x0, c.getY() - y + y0);
            locations.put((V) v, c);
        }
    }

    /**
 * Determines whether the position of v should not be changed by the layouter
 * @param v
 * @return if the FIXED marker is set
 */
    private boolean isFixed(Vertex v) {
        return v.isFixed();
    }

    /**
 * 
 * @return a random position in within the unit square
 */
    private Point2D getRandomPoint(int scale) {
        return new Point2D.Double(rnd.nextDouble() * scale, rnd.nextDouble() * scale);
    }

    /**
 * Computes the force experienced by node
 * @param node
 * @return force vector
 */
    @SuppressWarnings("unchecked")
    private Point2D getForceforNode(Vertex node) {
        double numNodes = graph.getVertices().size();
        Point2D mDot = new Point2D.Double();
        Point2D x = transform((V) node);
        Point2D origin = new Point2D.Double(0.0, 0.0);
        if (x.distance(origin) == 0.0) {
            return mDot;
        }
        for (Object o : getGraph().getNeighbors((V) node)) {
            Vertex otherNode = (Vertex) o;
            if (node != otherNode) {
                Point2D otherNodeX = transform((V) otherNode);
                if (otherNodeX == null || otherNodeX.distance(origin) == 0.0) {
                    continue;
                }
                Point2D temp = (Point2D) otherNodeX.clone();
                temp.setLocation(temp.getX() - x.getX(), temp.getY() - x.getY());
                double multiplier = a - 1;
                multiplier *= attraction / Math.sqrt(numNodes);
                Point2D addition = (Point2D) temp.clone();
                addition.setLocation(addition.getX() * multiplier, addition.getY() * multiplier);
                mDot.setLocation(mDot.getX() + addition.getX(), mDot.getY() + addition.getY());
            }
        }
        if (incremental && mDot.distance(origin) > forceCutoff) {
            double mult = forceCutoff / mDot.distance(origin);
            mDot.setLocation(mDot.getX() * mult, mDot.getY() * mult);
        }
        return mDot;
    }

    /**
 * Assign position p to the vertex
 * @param vertex
 * @param p
 * @return the newly assigned position
 */
    @SuppressWarnings("unchecked")
    public Point2D assignPositionToVertex(Vertex vertex, Point2D p) {
        Point2D c = transform((V) vertex);
        c.setLocation(p);
        locations.put((V) vertex, c);
        return p;
    }

    /**
 * Assigns a position to the vertex
 * @param vertex
 * @return the newly assigned position
 */
    @SuppressWarnings("unchecked")
    public Point2D assignPositionToVertex(Vertex vertex) {
        Set<Vertex> nvertices = new HashSet<Vertex>();
        for (Vertex vertex2 : (Collection<Vertex>) getGraph().getNeighbors((V) vertex)) {
            if (!visualizedVertices.contains(vertex2)) {
                nvertices.add(vertex2);
                visualizedVertices.add(vertex2);
            }
        }
        Point2D c = transform((V) vertex);
        if (c == null) {
            c = getRandomPoint(((int) Math.sqrt(graph.getVertices().size()) * 50) + 1);
        }
        if (nvertices.size() > 0) {
            c = getRandomPoint(((int) Math.sqrt(graph.getVertices().size()) * 50) + 1);
            for (Vertex vertex2 : nvertices) {
                Point2D c2 = transform((V) vertex2);
                c.setLocation(c.getX() + c2.getX(), c.getY() + c2.getY());
                locations.put((V) vertex2, c);
            }
            double mult = 1.0 / (double) nvertices.size();
            c.setLocation(c.getX() * mult, c.getY() * mult);
        }
        return c;
    }

    @SuppressWarnings("unchecked")
    public void updateVertices() {
        Set<Vertex> nvertices = new HashSet<Vertex>();
        for (Vertex vertex2 : (Collection<Vertex>) getGraph().getVertices()) {
            if (!visualizedVertices.contains(vertex2)) {
                nvertices.add(vertex2);
                visualizedVertices.add(vertex2);
            }
        }
        Point2D c;
        if (nvertices.size() > 0) {
            for (Vertex vertex2 : nvertices) {
                c = getRandomPoint(((int) Math.sqrt(graph.getVertices().size()) * 50) + 1);
                locations.put((V) vertex2, c);
            }
        }
    }

    /**
 * Checks for the existence of edges
 * @param node
 * @param node2
 * @return true if node and node2 are connected
 */
    @SuppressWarnings({ "unchecked", "unused" })
    private boolean isEdgeInGraph(Vertex node, Vertex node2) {
        Edge e = (Edge) getGraph().findEdge((V) node, (V) node2);
        if (e == null) {
            e = (Edge) getGraph().findEdge((V) node2, (V) node);
        }
        if (e != null) {
            return e.isExcluded();
        } else {
            return false;
        }
    }

    public boolean isIncremental() {
        return incremental;
    }

    public boolean incrementsAreDone() {
        return !incremental;
    }

    @SuppressWarnings("unchecked")
    public void update() {
        updateVertices();
        for (Vertex v : (Collection<Vertex>) getGraph().getVertices()) assignPositionToVertex(v);
        if (!incremental) {
            if (verbose) {
                System.out.println("starting non incremental layout");
            }
            layout();
        }
    }

    /**
 * produce a non interactive layout by calling advancePositions() several times.
 */
    private void layout() {
        double error = java.lang.Double.MAX_VALUE;
        double threshold = (double) graph.getVertices().size() * epsilon;
        int count = 0;
        while (error > threshold && count < maxRelayouts) {
            if (verbose || !verbose) {
                System.out.println("relayout: " + (maxRelayouts - count));
            }
            advancePositions();
            count++;
        }
    }

    public double getDeltaT() {
        return deltaT;
    }

    public void setDeltaT(double alpha) {
        this.deltaT = alpha;
    }

    public double getAttraction() {
        return attraction;
    }

    public void setAttraction(double attraction) {
        this.attraction = attraction;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getForceCutoff() {
        return forceCutoff;
    }

    public void setForceCutoff(double forceCutoff) {
        this.forceCutoff = forceCutoff;
    }

    public int getMaxRelayouts() {
        return maxRelayouts;
    }

    public void setMaxRelayouts(int maxRelayouts) {
        this.maxRelayouts = maxRelayouts;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public int getUpdatesPerFrame() {
        return updatesPerFrame;
    }

    public void setUpdatesPerFrame(int updatesPerFrame) {
        this.updatesPerFrame = updatesPerFrame;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void setMaxUpdates(int maxUpdates) {
        this.maxUpdates = maxUpdates;
    }

    public int getMaxUpdates() {
        return maxUpdates;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initialize() {
        Point2D randomPoint;
        for (Vertex v : (Collection<Vertex>) getGraph().getVertices()) {
            randomPoint = getRandomPoint(((int) Math.sqrt(graph.getVertices().size()) * 50) + 1);
            locations.put((V) v, randomPoint);
        }
        update();
        updateCenterOfMass();
    }

    @SuppressWarnings("unchecked")
    public void updateCenterOfMass() {
        centerOfMass = new Point2D.Double(0.0, 0.0);
        for (Vertex v : (Collection<Vertex>) getGraph().getVertices()) {
            Point2D location = transform((V) v);
            centerOfMass.setLocation(centerOfMass.getX() + location.getX(), centerOfMass.getY() + location.getY());
        }
        centerOfMass.setLocation(centerOfMass.getX() / getGraph().getVertexCount(), centerOfMass.getX() / getGraph().getVertexCount());
    }

    @Override
    public void reset() {
        done = false;
        visualizedVertices.clear();
        update();
    }

    @Override
    public boolean done() {
        return done;
    }

    @Override
    public void step() {
        countUpdates++;
        done = (countUpdates > maxUpdates);
        update();
        if (!done) advancePositions();
    }
}
