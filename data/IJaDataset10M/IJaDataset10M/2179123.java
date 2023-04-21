package playground.scnadine.choiceSetGeneration.algorithms;

import org.matsim.api.core.v01.network.Link;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;

public class TravelDistanceCostCalculator implements TravelDisutility, TravelTime {

    public TravelDistanceCostCalculator() {
    }

    public double getLinkTravelDisutility(Link link, double time) {
        return link.getLength();
    }

    public double getLinkTravelTime(Link link, double time) {
        return link.getLength();
    }
}
