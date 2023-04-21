package com.dqgen.mapper.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.jgrapht.EdgeFactory;
import org.jgrapht.Graph;
import org.jgrapht.graph.Pseudograph;

/**
 * <p>Finds a minimal connected subgraph for a set of vertices in a graph</p>
 * 
 * <p>The subgraph is constructed by first getting the shortest path between all pairs of nodes in the graph and making use
 * of that information to construct the minimal subgraph</p> 
 * 
 * @author sganesh
 *
 * @param <V> the vertex
 * @param <E> the edge
 */
public class MinimalConnectedSubgraph<V, E> {

    private AllPairsShortestPath<V, E> paths;

    private Graph<V, E> graph;

    private Set<V> vertices;

    private static final Logger LOGGER = Logger.getLogger(MinimalConnectedSubgraph.class);

    /**
	 * <p>Default constructor</p>
	 *  
	 * @param graph the graph for which the subgraph has to be identified
	 * @param vertices the vertices to include in the subgraph
	 */
    public MinimalConnectedSubgraph(Graph<V, E> graph, Set<V> vertices) {
        this(new AllPairsShortestPath<V, E>(graph), graph, vertices);
    }

    /**
	 * <p>Constructor with all pairs shortest path provided</p>
	 * 
	 * @param paths all pairs shortest path in the graph
	 * @param graph the graph for which the subgraph has to be identified
	 * @param vertices the vertices to include in the subgraph
	 */
    public MinimalConnectedSubgraph(AllPairsShortestPath<V, E> paths, Graph<V, E> graph, Set<V> vertices) {
        this.graph = graph;
        this.vertices = vertices;
        this.paths = paths;
    }

    /**
	 * <p>Returns the minimally connected subgraph including the vertices from the subgraph</p>
	 * 
	 * @return the minimally connected subgraph
	 */
    public Graph<V, E> getSubGraph() {
        LOGGER.debug("Finding the minimally connected subgraph from the graph - " + this.graph);
        Set<V> subGraphVertices = new HashSet<V>();
        Set<E> subGraphEdges = new HashSet<E>();
        subGraphVertices.addAll(this.vertices);
        for (V vertex : this.vertices) {
            Double shortestDistance = Double.MAX_VALUE;
            List<E> shortestPath = null;
            for (V subGraphVertex : subGraphVertices) {
                if (vertex.equals(subGraphVertex)) {
                    continue;
                }
                Double tmp = this.paths.getShrotestDistance(vertex, subGraphVertex);
                if (tmp < shortestDistance) {
                    shortestDistance = tmp;
                    shortestPath = this.paths.getShortestPath(vertex, subGraphVertex);
                }
            }
            if (shortestPath != null) {
                subGraphEdges.addAll(shortestPath);
                for (E edge : shortestPath) {
                    subGraphVertices.add(this.graph.getEdgeSource(edge));
                    subGraphVertices.add(this.graph.getEdgeTarget(edge));
                }
            }
        }
        Graph<V, E> subGraph = new Pseudograph<V, E>(new EdgeFactory<V, E>() {

            @Override
            public E createEdge(V vertex1, V vertex2) {
                return null;
            }
        });
        for (E edge : subGraphEdges) {
            subGraph.addVertex(this.graph.getEdgeSource(edge));
            subGraph.addVertex(this.graph.getEdgeTarget(edge));
            subGraph.addEdge(this.graph.getEdgeSource(edge), this.graph.getEdgeTarget(edge), edge);
        }
        return subGraph;
    }
}
