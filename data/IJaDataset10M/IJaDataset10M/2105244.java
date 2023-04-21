package playground.ciarif.retailers.data;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.core.facilities.ActivityFacilityImpl;

public class FacilityRetailersImpl extends ActivityFacilityImpl {

    private static final long serialVersionUID = 1L;

    protected FacilityRetailersImpl(Id id, Coord center) {
        super(id, center);
    }
}
