package org.matsim.transitSchedule.api;

import org.matsim.api.core.v01.network.Link;
import org.matsim.core.facilities.FacilityWRefs;

/**
 * A facility (infrastructure) describing a public transport stop.
 *
 * @author mrieser
 */
public interface TransitStopFacility extends FacilityWRefs {

    boolean getIsBlockingLane();

    public void setLink(final Link link);

    /**
	 * Sets a human name for the stop facility, e.g. to be displayed 
	 * on vehicles or at the stops' locations. The name can be 
	 * <code>null</code> to delete a previously assigned name.
	 * 
	 * @param name
	 */
    public void setName(final String name);

    /**
	 * @return name of the stop facility. Can be <code>null</code>.
	 */
    public String getName();
}
