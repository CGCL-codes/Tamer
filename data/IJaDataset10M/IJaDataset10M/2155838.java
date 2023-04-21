package playground.balmermi.lsa;

import java.util.HashMap;
import org.matsim.gbl.Gbl;

public class Intersection implements Comparable<Intersection> {

    protected final Integer id;

    protected final String desc;

    protected final HashMap<Integer, LSA> lsas = new HashMap<Integer, LSA>();

    protected final HashMap<Integer, Lane> lanes = new HashMap<Integer, Lane>();

    public Intersection(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public final void addLSA(LSA lsa) {
        if (this.lsas.containsKey(lsa.nr)) {
            Gbl.errorMsg("Intersection_id=" + this.id + ": LSA_nr=" + lsa.nr + " already exists!");
        }
        this.lsas.put(lsa.nr, lsa);
    }

    public final void addLane(Lane lane) {
        if (this.lanes.containsKey(lane.nr)) {
            Gbl.errorMsg("Intersection_id=" + this.id + ": lane_nr=" + lane.nr + " already exists!");
        }
        this.lanes.put(lane.nr, lane);
    }

    public int compareTo(Intersection other) {
        if (this.id < other.id) {
            return -1;
        } else if (this.id > other.id) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public final String toString() {
        return "[id=" + this.id + "]" + "[desc=" + this.desc + "]" + "[nof_LSAs=" + this.lsas.size() + "]" + "[nof_lanes=" + this.lanes.size() + "]";
    }
}
