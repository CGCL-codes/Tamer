package playground.christoph.withinday;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.core.mobsim.qsim.agents.PlanBasedWithinDayAgent;
import org.matsim.core.population.ActivityImpl;
import org.matsim.core.population.PlanImpl;
import org.matsim.withinday.replanning.replanners.interfaces.WithinDayDuringLegReplanner;
import org.matsim.withinday.utils.EditRoutes;
import org.matsim.withinday.utils.ReplacePlanElements;

public class ReplannerYoungPeople extends WithinDayDuringLegReplanner {

    ReplannerYoungPeople(Id id, Scenario scenario) {
        super(id, scenario);
    }

    @Override
    public boolean doReplanning(PlanBasedWithinDayAgent withinDayAgent) {
        if (this.routeAlgo == null) return false;
        if (withinDayAgent == null) return false;
        PlanImpl executedPlan = (PlanImpl) withinDayAgent.getSelectedPlan();
        if (executedPlan == null) return false;
        Leg currentLeg = withinDayAgent.getCurrentLeg();
        int currentLegIndex = withinDayAgent.getCurrentPlanElementIndex();
        Activity nextActivity = executedPlan.getNextActivity(currentLeg);
        if (!currentLeg.getMode().equals(TransportMode.car)) return false;
        ActivityImpl newWorkAct = new ActivityImpl("w", this.scenario.createId("22"));
        newWorkAct.setMaximumDuration(3600);
        new ReplacePlanElements().replaceActivity(executedPlan, nextActivity, newWorkAct);
        int currentPlanElementIndex = withinDayAgent.getCurrentPlanElementIndex();
        new EditRoutes().replanCurrentLegRoute(executedPlan, currentLegIndex, currentPlanElementIndex, routeAlgo, time);
        Leg homeLeg = executedPlan.getNextLeg(newWorkAct);
        int homeLegIndex = executedPlan.getPlanElements().indexOf(homeLeg);
        new EditRoutes().replanFutureLegRoute(executedPlan, homeLegIndex, routeAlgo);
        withinDayAgent.resetCaches();
        return true;
    }
}
