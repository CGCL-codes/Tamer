package org.matsim.core.api.population;

import org.matsim.api.basic.v01.population.BasicRoute;
import org.matsim.core.api.network.Link;

/**
 * Defines the minimum amount of information a route in MATSim must provide.
 *
 * @author mrieser
 */
public interface Route extends BasicRoute {

    public double getTravelTime();

    public void setTravelTime(final double travelTime);

    public Link getStartLink();

    public void setStartLink(final Link link);

    public Link getEndLink();

    public void setEndLink(final Link link);
}
