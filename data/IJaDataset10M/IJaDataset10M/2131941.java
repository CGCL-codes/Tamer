package org.matsim.ptproject.qsim.multimodalsimengine.router.util;

import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.router.util.PersonalizableTravelTime;
import org.matsim.core.router.util.TravelTime;

/**
 * 
 * @author cdobler
 */
public class RideTravelTime implements PersonalizableTravelTime {

    private final TravelTime carTravelTime;

    private final PersonalizableTravelTime walkTravelTime;

    RideTravelTime(TravelTime carTravelTime, PersonalizableTravelTime walkTravelTime) {
        this.carTravelTime = carTravelTime;
        this.walkTravelTime = walkTravelTime;
    }

    @Override
    public double getLinkTravelTime(Link link, double time) {
        if (link.getAllowedModes().contains(TransportMode.car)) {
            if (carTravelTime instanceof BufferedTravelTime) return ((BufferedTravelTime) carTravelTime).getBufferedLinkTravelTime(link, time); else return carTravelTime.getLinkTravelTime(link, time);
        } else if (link.getAllowedModes().contains(TransportMode.bike) || link.getAllowedModes().contains(TransportMode.walk)) {
            return walkTravelTime.getLinkTravelTime(link, time);
        }
        return link.getLength() / 1.0;
    }

    @Override
    public void setPerson(Person person) {
        walkTravelTime.setPerson(person);
    }
}
