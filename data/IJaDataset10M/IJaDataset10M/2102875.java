package playground.mrieser.pt.fares;

import java.util.Map;
import org.matsim.core.facilities.ActivityFacilityImpl;
import org.matsim.core.utils.collections.Tuple;
import playground.mrieser.pt.fares.api.TransitFares;

public class TableLookupFares implements TransitFares {

    private final Map<Tuple<ActivityFacilityImpl, ActivityFacilityImpl>, Double> costs;

    public TableLookupFares(final Map<Tuple<ActivityFacilityImpl, ActivityFacilityImpl>, Double> costs) {
        this.costs = costs;
    }

    public double getSingleTripCost(final ActivityFacilityImpl fromStop, final ActivityFacilityImpl toStop) {
        Double cost = this.costs.get(new Tuple<ActivityFacilityImpl, ActivityFacilityImpl>(fromStop, toStop));
        if (cost == null) {
            cost = this.costs.get(new Tuple<ActivityFacilityImpl, ActivityFacilityImpl>(toStop, fromStop));
        }
        if (cost == null) {
            if (fromStop == toStop) {
                return 0.0;
            }
            return Double.NaN;
        }
        return cost.doubleValue();
    }
}
