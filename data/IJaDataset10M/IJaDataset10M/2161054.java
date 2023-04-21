package playground.balmermi.world;

import java.util.Map;
import org.matsim.api.core.v01.BasicLocation;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.utils.geometry.CoordImpl;

/**
 * The collection of zone objects in MATSim.
 * @see LayerImpl
 * @author Michael Balmer
 */
public class ZoneLayer extends LayerImpl {

    public final Zone createZone(final Id id, final String center_x, final String center_y, final String min_x, final String min_y, final String max_x, final String max_y) {
        Id i = id;
        if (this.getLocations().containsKey(i)) {
            Gbl.errorMsg(this.toString() + "[zone id=" + id + " already exists]");
        }
        Coord center = null;
        Coord min = null;
        Coord max = null;
        if ((center_x != null) && (center_y != null)) {
            center = new CoordImpl(center_x, center_y);
        }
        if ((min_x != null) && (min_y != null)) {
            min = new CoordImpl(min_x, min_y);
        }
        if ((max_x != null) && (max_y != null)) {
            max = new CoordImpl(max_x, max_y);
        }
        Zone z = new Zone(id, center, min, max);
        Map<Id, BasicLocation> locations = this.getLocations();
        locations.put(i, z);
        return z;
    }
}
