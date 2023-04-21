package edu.uci.ics.jung.algorithms.connectivity;

import java.util.Set;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.filters.Filter;
import edu.uci.ics.jung.graph.filters.impl.KNeighborhoodFilter;

/**
 * Extracts the subgraph (neighborhood) from a graph whose nodes are no more than distance k away from at
 * least one of the root nodes (starting vertices).
 * @author Scott White and Danyel Fisher
 */
public class KNeighborhoodExtractor {

    /**
	 * Extracts the subgraph comprised of all vertices within distance K (undirected) from any
	 * node in rootNodes.
	 * @param graph the graph whose subgraph is to be extracted
	 * @param rootNodes the set of root nodes (starting vertices) in the graph
	 * @param radiusK the radius of the subgraph to be extracted
	 */
    public static Graph extractNeighborhood(Graph graph, Set rootNodes, int radiusK) {
        return extract(graph, rootNodes, radiusK, KNeighborhoodFilter.IN_OUT);
    }

    /**
	 * Extracts the subgraph comprised of all vertices within distance K (out-directed) from any
	 * node in rootNodes.
	  *@param graph the graph whose subgraph is to be extracted
	 * @param rootNodes the set of root nodes (starting vertices) in the graph
	 * @param radiusK the radius of the subgraph to be extracted
	 */
    public static Graph extractOutDirectedNeighborhood(DirectedGraph graph, Set rootNodes, int radiusK) {
        return extract(graph, rootNodes, radiusK, KNeighborhoodFilter.OUT);
    }

    /**
	 * Extracts the subgraph comprised of all vertices within distance K (in-directed) from any
	 * node in rootNodes.
	  * @param graph the graph whose subgraph is to be extracted
	 * @param rootNodes the set of root nodes (starting vertices) in the graph
	 * @param radiusK the radius of the subgraph to be extracted
	 */
    public static Graph extractInDirectedNeighborhood(DirectedGraph graph, Set rootNodes, int radiusK) {
        return extract(graph, rootNodes, radiusK, KNeighborhoodFilter.IN);
    }

    private static Graph extract(Graph graph, Set rootNodes, int radiusK, int edgeType) {
        Filter nf = new KNeighborhoodFilter(rootNodes, radiusK, edgeType);
        return nf.filter(graph).assemble();
    }
}
