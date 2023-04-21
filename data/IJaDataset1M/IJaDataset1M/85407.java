package playground.christoph.router.costcalculators;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.mobsim.qsim.qnetsimengine.NetsimNetwork;
import org.matsim.core.router.util.PersonalizableTravelTime;
import playground.christoph.network.MyLinkImpl;
import playground.christoph.network.SubLink;

public class KnowledgeTravelTimeCalculator implements PersonalizableTravelTime {

    protected double tbuffer = 35.0;

    protected double vehicleLength = 7.5;

    protected boolean calcFreeSpeedTravelTimes = false;

    protected NetsimNetwork qNetwork;

    private static final Logger log = Logger.getLogger(KnowledgeTravelTimeCalculator.class);

    public KnowledgeTravelTimeCalculator(NetsimNetwork qNetwork) {
        if (qNetwork == null) log.warn("No QNetwork was commited - FreeSpeedTravelTimes will be calculated and returned!");
        this.qNetwork = qNetwork;
    }

    public double getLinkTravelTime(Link link, double time) {
        if (qNetwork == null) {
            log.warn("No QueueNetwork found - FreeSpeedTravelTime is calculated and returned!");
            return link.getLength() / link.getFreespeed(time);
        }
        double vehicles = getVehiclesOnLink(link);
        return getLinkTravelTime(link, time, vehicles);
    }

    public double getLinkTravelTime(Link link, double time, double vehicles) {
        if (vehicles == 0.0) {
            return link.getLength() / link.getFreespeed(time);
        }
        vehicles = vehicles / link.getNumberOfLanes(time);
        double length = link.getLength();
        if (length < vehicleLength) length = vehicleLength;
        double vmax = link.getFreespeed(time);
        double v = (length / vehicles - vehicleLength) / tbuffer;
        if (v < 0.0) v = 0.0;
        if (v == 0.0) v = 0.1;
        if (v > vmax) v = vmax;
        double travelTime;
        if (v > 0.0) travelTime = length / v; else travelTime = Double.MAX_VALUE;
        double freespeedTravelTime = link.getLength() / link.getFreespeed(time);
        if (travelTime < freespeedTravelTime) {
            log.warn("TravelTime is shorter than FreeSpeedTravelTime - looks like something is wrong here. Using FreeSpeedTravelTime instead!");
            return freespeedTravelTime;
        }
        return travelTime;
    }

    protected int getVehiclesOnLink(Link link) {
        int vehicles;
        if (link instanceof SubLink) {
            Link parentLink = ((SubLink) link).getParentLink();
            vehicles = ((MyLinkImpl) parentLink).getVehiclesCount();
        } else {
            vehicles = ((MyLinkImpl) link).getVehiclesCount();
        }
        return vehicles;
    }

    public void setCalcFreeSpeedTravelTimes(boolean value) {
        this.calcFreeSpeedTravelTimes = value;
    }

    public boolean getCalcFreeSpeedTravelTimes() {
        return this.calcFreeSpeedTravelTimes;
    }

    @Override
    public void setPerson(Person person) {
    }
}
