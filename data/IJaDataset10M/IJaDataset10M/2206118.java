package playground.thibautd.parknride;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.core.api.experimental.facilities.Facility;

/**
 * Represents a park and ride facility, linking a network link to transit stops
 * @author thibautd
 */
public class ParkAndRideFacility implements Facility {

    private final Map<String, Object> customAttributes = new HashMap<String, Object>();

    private final Coord coord;

    private final Id linkId;

    private final Id id;

    private final List<Id> stopsIds;

    public ParkAndRideFacility(final Id id, final Coord coord, final Id linkId, final List<Id> stopsIds) {
        this.coord = coord;
        this.id = id;
        this.linkId = linkId;
        this.stopsIds = Collections.unmodifiableList(stopsIds);
    }

    @Override
    public Coord getCoord() {
        return coord;
    }

    @Override
    public Id getId() {
        return id;
    }

    @Override
    public Map<String, Object> getCustomAttributes() {
        return customAttributes;
    }

    @Override
    public Id getLinkId() {
        return linkId;
    }

    /**
	 * Gives access to the associated stops. The cost of walking from the parking to
	 * the stop is considered as being the same for all stops. Thus, they should correspond
	 * to the same "physical" station. It should even be wise to have only one stop
	 * associated.
	 * Possibility to walk to another stop is handled by the router itself, using the
	 * "transfer" links of the routing network.
	 *
	 * @return the list of associated stops Ids, as they are referenced in the schedule
	 * @deprecated this is not actually used to determine the pt stops asociated
	 * with a PNR facility! The PT router walk distance is used instead!
	 */
    @Deprecated
    public List<Id> getStopsFacilitiesIds() {
        return stopsIds;
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof ParkAndRideFacility) {
            ParkAndRideFacility facility = (ParkAndRideFacility) other;
            return id.equals(facility.id) && coord.equals(facility.coord) && linkId.equals(facility.linkId) && stopsIds.size() == facility.stopsIds.size() && stopsIds.containsAll(facility.stopsIds);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
