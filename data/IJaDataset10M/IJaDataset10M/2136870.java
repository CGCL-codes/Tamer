package osmosis.task;

import geometry.serialization.jsg.PolygonSerializer;
import java.io.IOException;
import java.util.Map;
import model.osmosis.OsmosisDataSet;
import org.openstreetmap.osmosis.core.container.v0_6.BoundContainer;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.EntityProcessor;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Relation;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import com.vividsolutions.jts.geom.Geometry;

class RegionExtractor implements Sink, EntityProcessor {

    private OsmosisDataSet data = new OsmosisDataSet();

    private String folderName;

    public RegionExtractor(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public void process(EntityContainer ec) {
        ec.process(this);
    }

    private String getFilename(Relation relation) {
        return folderName + System.getProperty("file.separator") + relation.getId() + ".ser";
    }

    @Override
    public void complete() {
        Map<Relation, Geometry> regions = data.getRegions();
        for (Relation relation : regions.keySet()) {
            String filename = getFilename(relation);
            System.out.println("writing to: " + filename);
            Geometry geometry = regions.get(relation);
            try {
                if (!geometry.isValid()) {
                    System.out.println("geometry invalid.");
                    continue;
                }
            } catch (Exception e) {
                System.out.println("runtime exception...");
                continue;
            }
            try {
                PolygonSerializer.write(filename, geometry);
            } catch (IOException e) {
                System.out.println("experienced failure during write of geometry:");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void release() {
    }

    @Override
    public void process(BoundContainer arg0) {
    }

    @Override
    public void process(NodeContainer nc) {
        Node node = nc.getEntity();
        data.getNodes().put(node.getId(), node);
    }

    @Override
    public void process(WayContainer wc) {
        Way way = wc.getEntity();
        data.getWays().put(way.getId(), way);
    }

    @Override
    public void process(RelationContainer rc) {
        Relation relation = rc.getEntity();
        data.getRelations().put(relation.getId(), relation);
    }
}
