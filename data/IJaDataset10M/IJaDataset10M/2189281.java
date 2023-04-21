package playground.johannes.sna.graph;

/**
 * A graph factory is responsible for instantiating new graphs, vertices and
 * edges. It does not handle the connectivity of vertices and edges.
 * 
 * @author illenberger
 * 
 */
public interface GraphFactory<G extends Graph, V extends Vertex, E extends Edge> {

    /**
	 * Creates and returns an empty graph.
	 * 
	 * @return an empty graph.
	 */
    public G createGraph();

    /**
	 * Creates and returns an isolated vertex.
	 * 
	 * @return an isolated vertex.
	 */
    public V createVertex();

    /**
	 * Creates and returns an orphaned edge.
	 * 
	 * @return an orphaned edge.
	 */
    public E createEdge();

    public G copyGraph(G graph);

    public V copyVertex(V vertex);

    public E copyEdge(E edge);
}
