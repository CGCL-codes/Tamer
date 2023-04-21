package org.matsim.locationchoice;

import java.util.List;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.matsim.core.api.facilities.ActivityFacility;
import org.matsim.core.api.population.Activity;
import org.matsim.core.api.population.Plan;
import org.matsim.core.controler.Controler;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.network.NetworkLayer;
import org.matsim.knowledges.Knowledges;
import org.matsim.locationchoice.utils.QuadTreeRing;

public class RandomLocationMutator extends LocationMutator {

    private static final Logger log = Logger.getLogger(RandomLocationMutator.class);

    public RandomLocationMutator(final NetworkLayer network, Controler controler, Knowledges kn) {
        super(network, controler, kn);
    }

    public RandomLocationMutator(final NetworkLayer network, Controler controler, Knowledges kn, TreeMap<String, QuadTreeRing<ActivityFacility>> quad_trees, TreeMap<String, ActivityFacility[]> facilities_of_type) {
        super(network, controler, kn, quad_trees, facilities_of_type);
    }

    @Override
    public void handlePlan(final Plan plan) {
        if (super.locationChoiceBasedOnKnowledge) {
            this.handlePlanBasedOnKnowldge(plan);
        } else {
            this.handlePlanForPreDefinedFlexibleTypes(plan);
        }
        super.resetRoutes(plan);
    }

    private void handlePlanBasedOnKnowldge(final Plan plan) {
        List<Activity> movablePrimaryActivities = defineMovablePrimaryActivities(plan);
        final List<?> actslegs = plan.getPlanElements();
        for (int j = 0; j < actslegs.size(); j = j + 2) {
            final Activity act = (Activity) actslegs.get(j);
            boolean isPrimary = this.knowledges.getKnowledgesByPersonId().get(plan.getPerson().getId()).isPrimary(act.getType(), act.getFacilityId());
            boolean movable = movablePrimaryActivities.contains(act);
            if ((!isPrimary || movable) && !(act.getType().startsWith("h") || act.getType().startsWith("tta"))) {
                int length = this.facilitiesOfType.get(act.getType()).length;
                if (length > 1) {
                    this.setNewLocationForAct(act, length);
                }
            }
        }
    }

    private void handlePlanForPreDefinedFlexibleTypes(final Plan plan) {
        final List<?> actslegs = plan.getPlanElements();
        for (int j = 0; j < actslegs.size(); j = j + 2) {
            final Activity act = (Activity) actslegs.get(j);
            if (super.defineFlexibleActivities.getFlexibleTypes().contains(act.getType())) {
                int length = this.facilitiesOfType.get(act.getType()).length;
                if (length > 1) {
                    this.setNewLocationForAct(act, length);
                }
            }
        }
    }

    private void setNewLocationForAct(Activity act, int length) {
        ActivityFacility facility = this.facilitiesOfType.get(act.getType())[MatsimRandom.getRandom().nextInt(length)];
        act.setFacility(facility);
        act.setLink(this.network.getNearestLink(facility.getCoord()));
        act.setCoord(facility.getCoord());
    }
}
