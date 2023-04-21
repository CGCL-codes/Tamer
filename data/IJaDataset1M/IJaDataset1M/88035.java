package playground.mmoyo.zz_archive.PTRouter;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.network.NetworkImpl;
import org.matsim.pt.transitSchedule.api.Departure;
import org.matsim.pt.transitSchedule.api.TransitLine;
import org.matsim.pt.transitSchedule.api.TransitRoute;
import org.matsim.pt.transitSchedule.api.TransitRouteStop;
import org.matsim.pt.transitSchedule.api.TransitSchedule;
import org.matsim.pt.transitSchedule.api.TransitStopFacility;

/**
 * Calculates and stores travel time according to the logicTransitSchedule object
 **/
public class TransitTravelTimeCalculator {

    private Map<Id, Double> linkTravelTimeMap = new TreeMap<Id, Double>();

    public Map<Id, double[]> nodeDeparturesMap = new TreeMap<Id, double[]>();

    public TransitTravelTimeCalculator(final TransitSchedule logicTransitSchedule, final NetworkImpl logicNetwork) {
        calculateTravelTimes(logicTransitSchedule, logicNetwork);
    }

    /**fills  a map of travelTime for links and  a map of departures for each node to create a TransitTimeTable*/
    public void calculateTravelTimes(TransitSchedule logicTransitSchedule, NetworkImpl logicNetwork) {
        for (TransitLine transitLine : logicTransitSchedule.getTransitLines().values()) {
            for (TransitRoute transitRoute : transitLine.getRoutes().values()) {
                Node lastNode = null;
                boolean first = true;
                double departureDelay = 0;
                double lastDepartureDelay = 0;
                double linkTravelTime = 0;
                int numDepartures = transitRoute.getDepartures().size();
                double[] departuresArray = new double[numDepartures];
                int i = 0;
                for (Departure departure : transitRoute.getDepartures().values()) {
                    departuresArray[i] = departure.getDepartureTime();
                    i++;
                }
                for (TransitRouteStop transitRouteStop : transitRoute.getStops()) {
                    TransitStopFacility transitStopFacility = transitRouteStop.getStopFacility();
                    Node node = logicNetwork.getNodes().get(transitStopFacility.getId());
                    double[] nodeDeparturesArray = new double[numDepartures];
                    for (int j = 0; j < numDepartures; j++) {
                        double departureTime = departuresArray[j] + transitRouteStop.getDepartureOffset();
                        if (departureTime > 86400) departureTime = departureTime - 86400;
                        nodeDeparturesArray[j] = departureTime;
                    }
                    Arrays.sort(nodeDeparturesArray);
                    nodeDeparturesMap.put(transitStopFacility.getId(), nodeDeparturesArray);
                    if (!first) {
                        for (Link lastLink : node.getInLinks().values()) {
                            if (lastLink.getFromNode().equals(lastNode)) {
                                departureDelay = transitRouteStop.getDepartureOffset();
                                linkTravelTime = (departureDelay - lastDepartureDelay) / 60;
                                linkTravelTimeMap.put(lastLink.getId(), linkTravelTime);
                            }
                        }
                    } else {
                        first = false;
                    }
                    lastNode = node;
                    lastDepartureDelay = departureDelay;
                }
            }
        }
    }
}
