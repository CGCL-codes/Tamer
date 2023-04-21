package org.matsim.router.util;

import org.matsim.interfaces.core.v01.Link;

/**
 * @author dgrether
 *
 */
public interface TurningMoveTravelTime extends TravelTime {

    /**
	 * Returns the travel time for the given fromLink inclusive the 
	 * turning move time to enter the given toLink at the specified time.
	 *
	 * @param fromLink The link for which the travel time is calculated.
	 * @param toLink The link that is entered from the fromLink the turning
	 * move time is calculated for the fromLink toLink relationship.
	 * @param time The departure time (in seconds since 00:00) at the beginning
	 * 		of the fromLink for which the travel time is calculated.
	 * @return The time (in seconds) needed to travel over the fromLink
	 * 		<code>fromLink</code> and enter the toLink <code>toLink</code>, 
	 * departing at time <code>time</code>.
	 */
    public double getTurningMoveTravelTime(Link fromLink, Link toLink, double time);
}
