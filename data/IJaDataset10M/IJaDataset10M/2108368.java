package playground.christoph.withinday.utils;

import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Plan;

public class ReplacePlanElements {

    public boolean replaceActivity(Plan plan, Activity oldActivity, Activity newActivity) {
        if (oldActivity == null) return false;
        if (newActivity == null) return false;
        int index = plan.getPlanElements().indexOf(oldActivity);
        if (index == -1) return false;
        plan.getPlanElements().remove(index);
        plan.getPlanElements().add(index, newActivity);
        return true;
    }

    public boolean replaceLeg(Plan plan, Leg oldLeg, Leg newLeg) {
        if (oldLeg == null) return false;
        if (newLeg == null) return false;
        int index = plan.getPlanElements().indexOf(oldLeg);
        if (index == -1) return false;
        plan.getPlanElements().remove(index);
        plan.getPlanElements().add(index, newLeg);
        return true;
    }
}
