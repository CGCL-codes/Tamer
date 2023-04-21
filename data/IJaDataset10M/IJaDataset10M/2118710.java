package polygonappend;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.openstreetmap.osmosis.core.domain.v0_6.CommonEntityData;
import org.openstreetmap.osmosis.core.domain.v0_6.OsmUser;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 */
public class PseudoWay {

    private List<PseudoNode> nodes = new ArrayList<PseudoNode>();

    private boolean idAssigned = false;

    private long id = 0;

    /**
	 * Create a new pseudo way from this linear ring.
	 * 
	 * @param line
	 *            the linear ring to create from.
	 * @return the pseudo way.
	 */
    public static PseudoWay create(LineString line) {
        PseudoWay way = new PseudoWay();
        for (int i = 0; i < line.getNumPoints() - 1; i++) {
            Coordinate c = line.getCoordinateN(i);
            way.nodes.add(new PseudoNode(c));
        }
        way.nodes.add(way.nodes.get(0));
        return way;
    }

    public List<PseudoNode> assignNodeIds(OsmIdFactory idFactory) {
        List<PseudoNode> newNodes = new ArrayList<PseudoNode>();
        for (PseudoNode pseudoNode : nodes) {
            if (!pseudoNode.hasAssignedId()) {
                pseudoNode.assignNodeId(idFactory);
                newNodes.add(pseudoNode);
            }
        }
        return newNodes;
    }

    public void assignWayId(OsmIdFactory idFactory) {
        id = idFactory.nextWayId();
        idAssigned = true;
    }

    public boolean hasAssignedId() {
        return idAssigned;
    }

    public long getId() {
        return id;
    }

    public Way createRealWay() {
        CommonEntityData common = new CommonEntityData(id, -1, new Date(), OsmUser.NONE, -1);
        List<WayNode> wayNodes = new ArrayList<WayNode>();
        for (PseudoNode node : nodes) {
            long nodeId = node.getId();
            wayNodes.add(new WayNode(nodeId));
        }
        Way way = new Way(common, wayNodes);
        return way;
    }
}
