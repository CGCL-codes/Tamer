package playground.balmermi.census2000v2.modules;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.matsim.facilities.Facilities;
import org.matsim.facilities.Facility;
import org.matsim.gbl.Gbl;
import org.matsim.world.Location;
import org.matsim.world.World;
import org.matsim.world.Zone;
import org.matsim.world.ZoneLayer;
import playground.balmermi.census2000v2.data.CAtts;
import playground.balmermi.census2000v2.data.Household;
import playground.balmermi.census2000v2.data.Households;

public class WorldFacilityZoneMapping {

    private static final Logger log = Logger.getLogger(WorldFacilityZoneMapping.class);

    private final Households households;

    public WorldFacilityZoneMapping(Households households) {
        super();
        log.info("    init " + this.getClass().getName() + " module...");
        this.households = households;
        log.info("    done.");
    }

    public void run(World world) {
        log.info("    running " + this.getClass().getName() + " module...");
        world.complete();
        if (world.getLayers().size() != 2) {
            Gbl.errorMsg("World must contian 2 layers!");
        }
        Facilities fs = (Facilities) world.getBottomLayer();
        ZoneLayer ms = (ZoneLayer) world.getTopLayer();
        if (!fs.getUpRule().getUpLayer().equals(ms)) {
            Gbl.errorMsg("Incorrect mapping!");
        }
        for (Household h : this.households.getHouseholds().values()) {
            Zone z = h.getMunicipality().getZone();
            Facility f = h.getFacility();
            world.addMapping(z, f);
            if (!z.contains(f.getCenter())) {
                log.warn("      mapping via census info produces dist(f[" + f.getId() + "]->z[" + z.getId() + "])=" + z.calcDistance(f.getCenter()));
            }
        }
        for (Facility f : fs.getFacilities().values()) {
            if (f.getUpMapping().size() == 0) {
                if (f.getActivity(CAtts.ACT_HOME) != null) {
                    Gbl.errorMsg("That should not happen!");
                }
                ArrayList<Location> locs = new ArrayList<Location>();
                Location nearest_loc = null;
                double min_dist = Double.MAX_VALUE;
                for (Location z : ms.getLocations().values()) {
                    double dist = z.calcDistance(f.getCenter());
                    if (dist == 0.0) {
                        locs.add(z);
                    }
                    if (min_dist > dist) {
                        min_dist = dist;
                        nearest_loc = z;
                    }
                }
                if (locs.isEmpty()) {
                    world.addMapping(nearest_loc, f);
                    log.warn("      no zone for f_id=" + f.getId() + ". assigning nearest zone_id=" + nearest_loc.getId() + " with dist(f->z)=" + min_dist);
                } else {
                    Location z = locs.get(Gbl.random.nextInt(locs.size()));
                    world.addMapping(z, f);
                }
            }
        }
        log.info("    done.");
    }
}
