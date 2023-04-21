package org.matsim.world;

import org.matsim.basic.v01.Id;
import org.matsim.basic.v01.IdImpl;
import org.matsim.facilities.Facilities;
import org.matsim.gbl.Gbl;
import org.matsim.network.NetworkLayer;
import org.matsim.utils.geometry.CoordI;
import org.matsim.utils.geometry.shared.Coord;

/**
 * The collection of zone objects in MATSim.
 * @see Layer
 * @see NetworkLayer
 * @see Facilities
 * @author Michael Balmer
 */
public class ZoneLayer extends Layer {

    protected ZoneLayer(final String type, final String name) {
        super(type, name);
    }

    protected ZoneLayer(final Id type, final String name) {
        super(type, name);
    }

    public final Zone createZone(final String id, final String center_x, final String center_y, final String min_x, final String min_y, final String max_x, final String max_y, final String area, final String name) {
        Id i = new IdImpl(id);
        if (this.locations.containsKey(i)) {
            Gbl.errorMsg(this.toString() + "[zone id=" + id + " already exists]");
        }
        CoordI center = null;
        CoordI min = null;
        CoordI max = null;
        if ((center_x != null) && (center_y != null)) {
            center = new Coord(center_x, center_y);
        }
        if ((min_x != null) && (min_y != null)) {
            min = new Coord(min_x, min_y);
        }
        if ((max_x != null) && (max_y != null)) {
            max = new Coord(max_x, max_y);
        }
        Zone z = new Zone(this, id, center, min, max, area, name);
        this.locations.put(i, z);
        return z;
    }

    @Override
    public final String toString() {
        return super.toString();
    }
}
