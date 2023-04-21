package org.matsim.locationchoice.random;

import java.util.Random;
import java.util.TreeMap;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.api.experimental.facilities.ActivityFacility;
import org.matsim.core.controler.Controler;
import org.matsim.core.facilities.ActivityFacilityImpl;
import org.matsim.core.network.NetworkImpl;
import org.matsim.core.population.ActivityImpl;
import org.matsim.locationchoice.LocationMutator;
import org.matsim.locationchoice.utils.QuadTreeRing;

/**
 * @author anhorni
 */
public class RandomLocationMutator extends LocationMutator {

    public RandomLocationMutator(final Network network, Controler controler, Random random) {
        super(network, controler, random);
    }

    public RandomLocationMutator(final Network network, Controler controler, TreeMap<String, QuadTreeRing<ActivityFacility>> quad_trees, TreeMap<String, ActivityFacilityImpl[]> facilities_of_type, Random random) {
        super(network, controler, quad_trees, facilities_of_type, random);
    }

    @Override
    public void handlePlan(final Plan plan) {
        this.handlePlanForPreDefinedFlexibleTypes(plan);
        super.resetRoutes(plan);
    }

    private void handlePlanForPreDefinedFlexibleTypes(final Plan plan) {
        for (PlanElement pe : plan.getPlanElements()) {
            if (pe instanceof Activity) {
                final Activity act = (Activity) pe;
                if (super.defineFlexibleActivities.getFlexibleTypes().contains(act.getType())) {
                    int length = this.facilitiesOfType.get(act.getType()).length;
                    if (length > 1) {
                        this.setNewLocationForAct((ActivityImpl) act, length);
                    }
                }
            }
        }
    }

    private void setNewLocationForAct(ActivityImpl act, int length) {
        ActivityFacilityImpl facility = this.facilitiesOfType.get(act.getType())[super.random.nextInt(length)];
        act.setFacilityId(facility.getId());
        act.setLinkId(((NetworkImpl) this.network).getNearestLink(facility.getCoord()).getId());
        act.setCoord(facility.getCoord());
    }
}
