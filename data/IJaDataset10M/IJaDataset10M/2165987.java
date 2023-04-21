package net.sourceforge.combean.test.helpers.factories;

import net.sourceforge.combean.adapters.drasys.graph.DRAGraphAsConstructableNumberedDirectedGraph;
import net.sourceforge.combean.graph.containers.MapAsEdgeMap;
import net.sourceforge.combean.graph.util.GraphBuilder;
import net.sourceforge.combean.interfaces.graph.Graph;
import net.sourceforge.combean.interfaces.graph.containers.EdgeMap;

/**
 * Helper class which generates graphs for testing purposes.
 * 
 * @author schickin
 *
 */
public class GraphFixtureFactory {

    /**
     * Return a implementation of a constructable numbered graph to be
     * used for testing purposes.
     * 
     * @return a constructable numbered graph
     */
    @SuppressWarnings("unchecked")
    public static Graph<Object, Object> createConstructableNumberedGraph() {
        return (Graph) new DRAGraphAsConstructableNumberedDirectedGraph();
    }

    @SuppressWarnings("unchecked")
    private static GraphBuilder<Object, Object> createGraphWithBuilder() {
        return new GraphBuilder(new DRAGraphAsConstructableNumberedDirectedGraph());
    }

    /**
     * Construct a directed path with a given number of nodes.
     * 
     * @param numNodes the number of nodes in the path
     * @return a path
     */
    public static Graph<Object, Object> createDirectedPath(int numNodes) {
        GraphBuilder<Object, Object> builder = GraphFixtureFactory.createGraphWithBuilder();
        builder.addNodes(numNodes);
        builder.addPathEdges(0, numNodes - 1);
        return builder.getGraph();
    }

    /**
     * Construct a directed cycle with a given number of nodes.
     * 
     * @param numNodes the number of nodes in the cycle.
     * @return a cycle
     */
    public static Graph<Object, Object> createDirectedCycle(int numNodes) {
        GraphBuilder<Object, Object> builder = GraphFixtureFactory.createGraphWithBuilder();
        builder.addNodes(numNodes);
        builder.addPathEdges(0, numNodes - 1);
        builder.addEdge(numNodes - 1, 0);
        return builder.getGraph();
    }

    /**
     * Construct a graph with a given number of nodes, based on a
     * two-dimensional integer array where each row corresponds to an edge
     * and contains at least two entries, namely
     * - first entry: the number of the first node of the edge
     * - second entry: the number of the second node of the edge
     * 
     * @param numNodes the number of nodes in the graph
     * @param edges the edges, coded as described
     * above
     * @return a graph with the given nodes and edges.
     */
    public static Graph<Object, Object> createGraph(int numNodes, int[][] edges) {
        GraphBuilder<Object, Object> builder = GraphFixtureFactory.createGraphWithBuilder();
        builder.addNodes(numNodes);
        builder.addEdges(edges);
        return builder.getGraph();
    }

    /**
     * Construct a graph with a given number of nodes, based on a
     * two-dimensional integer array where each row corresponds to an edge
     * and contains three entries, namely
     * - the number of the first node of the edge
     * - the number of the second node of the edge
     * - the weight of the edge
     * 
     * @param numNodes the number of nodes in the graph
     * @param edgesWithWeights the edges with their weights, coded as described
     * above
     * @param edgeMapToFill an EdgeMap which shall be filled with the edge
     * weights
     * @return a graph with the given nodes and edges.
     */
    public static Graph<Object, Object> createGraphWithEdgeWeights(int numNodes, int[][] edgesWithWeights, EdgeMap<Object, Double> edgeMapToFill) {
        GraphBuilder<Object, Object> builder = GraphFixtureFactory.createGraphWithBuilder();
        builder.setEdgeMap(edgeMapToFill);
        builder.addNodes(numNodes);
        builder.addEdgesWithWeights(edgesWithWeights);
        return builder.getGraph();
    }

    public static EdgeMap<Object, Double> createDoubleEdgeMap(Graph<Object, Object> g, double edgeWeights[]) {
        GraphBuilder<Object, Object> builder = new GraphBuilder<Object, Object>(g);
        EdgeMap<Object, Double> tmpEdgeMap = new MapAsEdgeMap<Object, Double>();
        builder.fillDoubleEdgeMap(tmpEdgeMap, edgeWeights);
        return tmpEdgeMap;
    }
}
