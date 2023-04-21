package playground.johannes.socialnetworks.graph.spatial;

import java.util.Set;
import playground.johannes.socialnetworks.graph.SparseEdge;
import playground.johannes.socialnetworks.graph.SparseGraph;
import playground.johannes.socialnetworks.graph.SparseVertex;

/**
 * @author illenberger
 *
 */
public class SpatialGraph extends SparseGraph {

    @SuppressWarnings("unchecked")
    @Override
    public Set<? extends SpatialEdge> getEdges() {
        return (Set<? extends SpatialEdge>) super.getEdges();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<? extends SpatialVertex> getVertices() {
        return (Set<? extends SpatialVertex>) super.getVertices();
    }

    @Override
    protected boolean insertEdge(SparseEdge e, SparseVertex v1, SparseVertex v2) {
        return super.insertEdge(e, v1, v2);
    }

    @Override
    protected boolean insertVertex(SparseVertex v) {
        return super.insertVertex(v);
    }
}
