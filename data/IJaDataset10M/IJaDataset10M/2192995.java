package org.matsim.core.mobsim.qsim.multimodalsimengine.router.util;

import org.matsim.api.core.v01.network.Link;
import org.matsim.core.router.util.PersonalizableTravelTime;

public interface MultiModalTravelTime extends PersonalizableTravelTime {

    public double getModalLinkTravelTime(Link link, double time, String transportMode);

    /**
	 * Define the transport mode that should be used if getLinkTravelTime(...) is called.
	 * Useful for backwards compatibility with code that is not designed to be multi-modal,
	 * e.g. a LeastCostPathCalculator. Before calculating the path, the transport mode
	 * can be set. As a result, the calculator will use the correct travel times for
	 * the given mode.
	 */
    public void setTransportMode(String transportMode);
}
