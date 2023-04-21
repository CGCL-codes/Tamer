package org.matsim.pt.qsim;

import java.util.List;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;
import org.matsim.pt.routes.ExperimentalTransitRoute;
import org.matsim.ptproject.qsim.helpers.DefaultPersonDriverAgent;
import org.matsim.ptproject.qsim.interfaces.QSimI;
import org.matsim.transitSchedule.api.TransitLine;
import org.matsim.transitSchedule.api.TransitRoute;
import org.matsim.transitSchedule.api.TransitRouteStop;
import org.matsim.transitSchedule.api.TransitStopFacility;

/**
 * @author mrieser
 */
public class TransitAgent extends DefaultPersonDriverAgent implements PassengerAgent {

    public TransitAgent(final Person p, final QSimI simulation) {
        super(p, simulation);
    }

    public boolean getExitAtStop(final TransitStopFacility stop) {
        ExperimentalTransitRoute route = (ExperimentalTransitRoute) getCurrentLeg().getRoute();
        return route.getEgressStopId().equals(stop.getId());
    }

    public boolean getEnterTransitRoute(final TransitLine line, final TransitRoute transitRoute, final List<TransitRouteStop> stopsToCome) {
        ExperimentalTransitRoute route = (ExperimentalTransitRoute) getCurrentLeg().getRoute();
        if (line.getId().equals(route.getLineId())) {
            return containsId(stopsToCome, route.getEgressStopId());
        } else {
            return false;
        }
    }

    private boolean containsId(List<TransitRouteStop> stopsToCome, Id egressStopId) {
        for (TransitRouteStop stop : stopsToCome) {
            if (egressStopId.equals(stop.getStopFacility().getId())) {
                return true;
            }
        }
        return false;
    }
}
