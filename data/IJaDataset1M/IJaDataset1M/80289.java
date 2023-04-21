package de.grogra.graph;

/**
 * A <code>Path</code> consists of an alternating sequence of nodes and edges
 * (starting with a node) of a graph. An edge of a graph is incident with
 * its neighbouring nodes in the path.
 * <p>
 * The nodes and edges of this path are accessed using their index. Because
 * a path starts with a node, nodes have even indices, while edges haves odd
 * indices. If the <code>index</code> parameter to one of the methods of
 * <code>Path</code> is negative, it is interpreted as an index relative
 * to the end of the path, i.e., the actual index is
 * <code>path.getNodeAndEdgeCount () + index</code>.
 * 
 * @author Ole Kniemeyer
 */
public interface Path {

    /**
	 * A constant array of length 0.
	 */
    Path[] PATH_0 = new Path[0];

    /**
	 * Returns the graph which contains this path.
	 * 
	 * @return graph of path
	 */
    Graph getGraph();

    /**
	 * Returns the number of nodes and edges of this path.
	 * If this is even, the path ends in an edge; if this is odd,
	 * the path ends in a node.
	 * 
	 * @return number of nodes and edges
	 */
    int getNodeAndEdgeCount();

    /**
	 * Returns the edge bits (see {@link Graph#getEdgeBits(Object)})
	 * of the edge at <code>index</code>.
	 * If <code>index</code> is negative, it is relative to the end
	 * of the list, i.e., it is incremented by {@link #getNodeAndEdgeCount()}.
	 * The actual index has to be odd because edges have odd indices.
	 * 
	 * @param index edge index
	 * @return edge bits of edge
	 */
    int getEdgeBits(int index);

    /**
	 * Checks whether the path traverses the edge at <code>index</code>
	 * in edge direction or in reverse edge direction.
	 * If <code>index</code> is negative, it is relative to the end
	 * of the list, i.e., it is incremented by {@link #getNodeAndEdgeCount()}.
	 * The actual index has to be odd because edges have odd indices.
	 * 
	 * @param index edge index
	 * @return <code>true</code> iff path traverses edge in its direction
	 */
    boolean isInEdgeDirection(int index);

    /**
	 * Returns the node or edge at <code>index</code>. Because a path
	 * always starts with a node, even values of <code>index</code> address
	 * nodes, while odd values address edges. Edges may be returned
	 * as <code>null</code>, in this case only the information provided
	 * by {@link #getEdgeBits(int)} and {@link #isInEdgeDirection(int)}
	 * is available for the edge. 
	 * If <code>index</code> is negative, it is relative to the end
	 * of the list, i.e., it is incremented by {@link #getNodeAndEdgeCount()}.
	 * 
	 * @param index node or edge index
	 * @return node or edge at <code>index</code>
	 */
    Object getObject(int index);

    long getObjectId(int index);

    boolean isInstancingEdge(int index);
}
