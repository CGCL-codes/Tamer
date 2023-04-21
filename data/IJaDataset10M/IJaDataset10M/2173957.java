package ru.cos.sim.ras.duo.dijkstra;

import ru.cos.sim.ras.duo.digraph.Digraph;
import ru.cos.sim.ras.duo.digraph.Edge;
import ru.cos.sim.ras.duo.digraph.Vertex;
import ru.cos.sim.ras.duo.utils.ExtensionCollection;
import ru.cos.sim.ras.duo.utils.SimpleFactory;
import ru.cos.sim.utils.AdaptIterator;
import ru.cos.sim.utils.Adapter;

public class DijkstraPersistenceProvider {

    public DijkstraPersistenceProvider(Digraph graph) {
        this(graph, ExtensionCollection.getFreeExtensionNumber());
    }

    private DijkstraPersistenceProvider(Digraph graph, int extensionNumber) {
        this.extensionNumber = extensionNumber;
        this.graph = graph;
    }

    private int extensionNumber;

    private Digraph graph;

    public void dispose() {
        for (Edge e : graph.getEdges()) e.getExtensions().free(extensionNumber);
        for (Vertex v : graph.getVertexes()) v.getExtensions().free(extensionNumber);
    }

    public DijkstraEdgeInfo getInfo(final Edge edge) {
        return edge.getExtensions().get(extensionNumber, DijkstraEdgeInfo.class, new SimpleFactory<DijkstraEdgeInfo>() {

            @Override
            public DijkstraEdgeInfo createNew() {
                return new DijkstraEdgeInfo();
            }
        });
    }

    public DijkstraVertexInfo getInfo(final Vertex vertex) {
        return vertex.getExtensions().get(extensionNumber, DijkstraVertexInfo.class, new SimpleFactory<DijkstraVertexInfo>() {

            @Override
            public DijkstraVertexInfo createNew() {
                return new DijkstraVertexInfo();
            }
        });
    }

    public Iterable<DijkstraVertexInfo> getAllVertexInfo() {
        return new AdaptIterator<Vertex, DijkstraVertexInfo>(graph.getVertexes(), new Adapter<Vertex, DijkstraVertexInfo>() {

            @Override
            public DijkstraVertexInfo adapt(Vertex source) {
                return getInfo(source);
            }
        }).asIterable();
    }

    public Iterable<DijkstraEdgeInfo> getAllEdgeInfo() {
        return new AdaptIterator<Edge, DijkstraEdgeInfo>(graph.getEdges(), new Adapter<Edge, DijkstraEdgeInfo>() {

            @Override
            public DijkstraEdgeInfo adapt(Edge source) {
                return getInfo(source);
            }
        }).asIterable();
    }
}
