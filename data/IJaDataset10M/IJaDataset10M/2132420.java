package playground.balmermi.modules;

import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.api.experimental.facilities.ActivityFacilities;
import org.matsim.core.facilities.ActivityFacilityImpl;
import org.matsim.core.population.ActivityImpl;
import org.matsim.core.population.LegImpl;
import org.matsim.population.algorithms.AbstractPersonAlgorithm;
import org.matsim.population.algorithms.PlanAlgorithm;

public class PersonResetCoordAndLink extends AbstractPersonAlgorithm implements PlanAlgorithm {

    private final ActivityFacilities facilities;

    public PersonResetCoordAndLink(final ActivityFacilities facilities) {
        this.facilities = facilities;
    }

    @Override
    public void run(final Person person) {
        for (Plan plan : person.getPlans()) {
            run(plan);
        }
    }

    @Override
    public void run(final Plan plan) {
        for (PlanElement pe : plan.getPlanElements()) {
            if (pe instanceof ActivityImpl) {
                ActivityImpl a = (ActivityImpl) pe;
                a.setCoord(this.facilities.getFacilities().get(a.getFacilityId()).getCoord());
                a.setLinkId(((ActivityFacilityImpl) this.facilities.getFacilities().get(a.getFacilityId())).getLinkId());
            } else if (pe instanceof LegImpl) {
                LegImpl l = (LegImpl) pe;
                l.setArrivalTime(l.getDepartureTime());
                l.setTravelTime(0.0);
                l.setRoute(null);
            }
        }
    }
}
