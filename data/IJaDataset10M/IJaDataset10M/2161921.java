package playground.johannes.socialnetworks.snowball2.spatial.analysis;

import gnu.trove.TObjectDoubleHashMap;
import java.util.Set;
import playground.johannes.sna.graph.Vertex;
import playground.johannes.sna.graph.spatial.SpatialVertex;
import playground.johannes.socialnetworks.graph.spatial.analysis.EdgeLengthMedian;
import playground.johannes.socialnetworks.snowball2.spatial.SpatialSampledVertexDecorator;

/**
 * @author illenberger
 *
 */
public class ObservedEdgeLengthMedian extends EdgeLengthMedian {

    @Override
    public TObjectDoubleHashMap<Vertex> values(Set<? extends Vertex> vertices) {
        @SuppressWarnings("unchecked") Set<SpatialSampledVertexDecorator<SpatialVertex>> spatialVertices = (Set<SpatialSampledVertexDecorator<SpatialVertex>>) vertices;
        return super.values(spatialVertices);
    }
}
