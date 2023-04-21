package pcgen.base.graph.core;

import java.util.List;

/**
 * @author Thomas Parker (thpr [at] yahoo.com)
 * 
 * A DirectionalEdge is a directional Edge in a Graph. A directional edge has
 * source and sink nodes. It is possible on a DirectionalEdge to have multiple
 * sources and/or multiple sinks.
 */
public interface DirectionalEdge<N> extends Edge<N> {

    /**
	 * The bitmask for identifying when a Node is unconnected to a given
	 * DirectionalEdge
	 */
    public static final int UNCONNECTED = 0;

    /**
	 * The bitmask for identifying when a Node is a source of a given
	 * DirectionalEdge.
	 */
    public static final int SOURCE = 1;

    /**
	 * The bitmask for identifying when a Node is a sink of a given
	 * DirectionalEdge.
	 */
    public static final int SINK = 2;

    /**
	 * Returns an identifier indicating the association between this
	 * DirectionalEdge and the given Node. Returns 0 (zero) if this
	 * DirectionalEdge is not attached to the given Node. If attached, the
	 * return value is a Bitmask of DirectionalEdge.SOURCE and
	 * DirectionalEdge.SINK to indicate whether the given Node was a Source, a
	 * Sink, or both.
	 * 
	 * @param node
	 *            The node for which the interface type should be returned
	 * @return A bitmask indicating the interface type between this
	 *         DirectionalEdge and the given Node
	 */
    public int getNodeInterfaceType(N node);

    /**
	 * Returns a List of the Source Nodes of this DirectionalEdge.
	 * 
	 * @return A List of the Source Nodes of this DirectionalEdge
	 */
    public List<N> getSourceNodes();

    /**
	 * Returns a List of the Sink Nodes of this DirectionalEdge.
	 * 
	 * @return A List of the Sink Nodes of this DirectionalEdge
	 */
    public List<N> getSinkNodes();
}
