package playground.johannes.socialnetworks.graph.spatial;

import java.util.HashSet;
import java.util.Set;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import playground.johannes.socialnetworks.spatial.Zone;

/**
 * @author illenberger
 *
 */
public class SpatialPartitions {

    public static <V extends SpatialSparseVertex> Set<V> createSpatialPartition(Set<V> vertices, Zone zone) {
        Set<V> partition = new HashSet<V>();
        GeometryFactory factory = new GeometryFactory();
        Geometry geometry = zone.getBorder();
        for (V v : vertices) {
            Coordinate coordinate = new Coordinate(v.getCoordinate().getX(), v.getCoordinate().getY());
            if (geometry.contains(factory.createPoint(coordinate))) {
                partition.add(v);
            }
        }
        return partition;
    }
}
