package playground.johannes.socialnetworks.graph;

import org.matsim.contrib.sna.graph.Edge;
import org.matsim.contrib.sna.graph.Graph;
import org.matsim.contrib.sna.graph.Vertex;

/**
 * @author illenberger
 *
 */
public class SparseGraphProjectionFactory<G extends Graph, V extends Vertex, E extends Edge> implements GraphProjectionFactory<G, V, E, GraphProjection<G, V, E>, VertexDecorator<V>, EdgeDecorator<E>> {

    public EdgeDecorator<E> createEdge(E delegate) {
        return new EdgeDecorator<E>(delegate);
    }

    public GraphProjection<G, V, E> createGraph(G delegate) {
        return new GraphProjection<G, V, E>(delegate);
    }

    public VertexDecorator<V> createVertex(V delegate) {
        return new VertexDecorator<V>(delegate);
    }
}
