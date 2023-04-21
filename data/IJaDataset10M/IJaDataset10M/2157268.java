package de.grogra.imp2d.layout;

/**
 * An <code>Edge</code> represents an edge of a graph which is to be
 * layouted by a {@link de.grogra.imp2d.layout.Layout}. Such a graph
 * is constructed as an image of an actual source {@link de.grogra.graph.Graph}.
 * 
 * @see de.grogra.imp2d.layout.Node
 * @author Ole Kniemeyer
 */
public final class Edge {

    /**
	 * The original edge in the source <code>Graph</code>. 
	 */
    public final Object object;

    /**
	 * <code>true</code> iff this edge corresponds to a node
	 * in the source <code>Graph</code> which represents an edge.
	 */
    public final boolean edgeNode;

    /**
	 * The edge weight.
	 */
    public final float weight;

    /**
	 * The source node of this edge.
	 */
    public final Node source;

    /**
	 * The target node of this edge.
	 */
    public final Node target;

    /**
	 * The width of the 2D-visualization. May be 0.
	 */
    public float width;

    /**
	 * The height of the 2D-visualization. May be 0.
	 */
    public float height;

    public boolean isAccessed;

    private Edge sourceNext;

    private Edge targetNext;

    Edge(Node source, Node target, Object object, boolean edgeNode, float weight) {
        this.object = object;
        this.edgeNode = edgeNode;
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    /**
	 * Returns the next edge in <code>parent</code>'s list of edges
	 * 
	 * @param parent a node
	 * @return the next edge in <code>parent</code>'s list of edges
	 * @see Node
	 */
    public Edge getNext(Node parent) {
        return (parent == source) ? sourceNext : targetNext;
    }

    /**
	 * Returns the neighbor of <code>n</code>.
	 * 
	 * @param n one of the two incident nodes of this edge
	 * @return the other incident node
	 */
    public Node getNeighbor(Node n) {
        return (n == source) ? target : source;
    }

    void setNext(Edge next, Node parent) {
        if (parent == source) {
            this.sourceNext = next;
        } else {
            this.targetNext = next;
        }
    }
}
