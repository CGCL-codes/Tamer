package playground.christoph.evacuation.withinday.replanning;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.ScenarioImpl;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PopulationFactory;
import org.matsim.core.mobsim.framework.PersonAgent;
import org.matsim.core.population.ActivityImpl;
import org.matsim.core.population.PersonImpl;
import org.matsim.core.population.PlanImpl;
import org.matsim.core.utils.misc.Time;
import playground.christoph.withinday.mobsim.WithinDayPersonAgent;
import playground.christoph.withinday.replanning.WithinDayDuringActivityReplanner;
import playground.christoph.withinday.utils.EditRoutes;

public class EndActivityAndEvacuateReplanner extends WithinDayDuringActivityReplanner {

    public EndActivityAndEvacuateReplanner(Id id, Scenario scenario) {
        super(id, scenario);
    }

    @Override
    public boolean doReplanning(PersonAgent personAgent) {
        if (personAgent == null) return false;
        WithinDayPersonAgent withinDayPersonAgent = null;
        if (!(personAgent instanceof WithinDayPersonAgent)) return false; else {
            withinDayPersonAgent = (WithinDayPersonAgent) personAgent;
        }
        PersonImpl person = (PersonImpl) withinDayPersonAgent.getPerson();
        PlanImpl selectedPlan = (PlanImpl) person.getSelectedPlan();
        if (selectedPlan == null) return false;
        Activity currentActivity = withinDayPersonAgent.getCurrentActivity();
        if (currentActivity == null) return false;
        if (currentActivity.getEndTime() == Time.UNDEFINED_TIME) this.agentCounter.incLiving();
        currentActivity.setEndTime(this.time);
        int currentActivityIndex = selectedPlan.getActLegIndex(currentActivity);
        String transportMode = identifyTransportMode(currentActivityIndex, selectedPlan);
        while (selectedPlan.getPlanElements().size() - 1 > currentActivityIndex) {
            selectedPlan.removeActivity(selectedPlan.getPlanElements().size() - 1);
        }
        PopulationFactory factory = scenario.getPopulation().getFactory();
        Activity rescueActivity = factory.createActivityFromLinkId("rescue", scenario.createId("rescueLink"));
        ((ActivityImpl) rescueActivity).setFacilityId(scenario.createId("rescueFacility"));
        Coord rescueCoord = ((ScenarioImpl) scenario).getActivityFacilities().getFacilities().get(scenario.createId("rescueFacility")).getCoord();
        ((ActivityImpl) rescueActivity).setCoord(rescueCoord);
        Leg legToRescue = factory.createLeg(transportMode);
        selectedPlan.insertLegAct(selectedPlan.getActLegIndex(currentActivity) + 1, legToRescue, rescueActivity);
        new EditRoutes().replanFutureLegRoute(selectedPlan, legToRescue, planAlgorithm);
        withinDayPersonAgent.rescheduleCurrentActivity(time);
        return true;
    }

    private String identifyTransportMode(int currentActivityIndex, Plan selectedPlan) {
        boolean hasCar = false;
        boolean hasBike = false;
        boolean hasPt = false;
        boolean hasRide = false;
        if (currentActivityIndex > 0) {
            Leg previousLeg = (Leg) selectedPlan.getPlanElements().get(currentActivityIndex - 1);
            String transportMode = previousLeg.getMode();
            if (transportMode.equals(TransportMode.car)) hasCar = true; else if (transportMode.equals(TransportMode.bike)) hasBike = true; else if (transportMode.equals(TransportMode.pt)) hasPt = true; else if (transportMode.equals(TransportMode.ride)) hasRide = true;
        }
        if (currentActivityIndex + 1 < selectedPlan.getPlanElements().size()) {
            Leg nextLeg = (Leg) selectedPlan.getPlanElements().get(currentActivityIndex + 1);
            String transportMode = nextLeg.getMode();
            if (transportMode.equals(TransportMode.car)) hasCar = true; else if (transportMode.equals(TransportMode.bike)) hasBike = true; else if (transportMode.equals(TransportMode.pt)) hasPt = true; else if (transportMode.equals(TransportMode.ride)) hasRide = true;
        }
        if (hasCar) return TransportMode.car; else if (hasRide) return TransportMode.ride; else if (hasPt) return TransportMode.pt; else if (hasBike) return TransportMode.bike; else return TransportMode.walk;
    }

    @Override
    public EndActivityAndEvacuateReplanner clone() {
        EndActivityAndEvacuateReplanner clone = new EndActivityAndEvacuateReplanner(this.id, this.scenario);
        super.cloneBasicData(clone);
        return clone;
    }
}
