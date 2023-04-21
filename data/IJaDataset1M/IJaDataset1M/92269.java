package org.matsim.contrib.cadyts.pt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.core.events.PersonEntersVehicleEvent;
import org.matsim.core.events.PersonLeavesVehicleEvent;
import org.matsim.core.events.TransitDriverStartsEvent;
import org.matsim.core.events.VehicleDepartsAtFacilityEvent;
import org.matsim.core.events.handler.PersonEntersVehicleEventHandler;
import org.matsim.core.events.handler.PersonLeavesVehicleEventHandler;
import org.matsim.core.events.handler.TransitDriverStartsEventHandler;
import org.matsim.core.events.handler.VehicleDepartsAtFacilityEventHandler;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.pt.transitSchedule.api.TransitSchedule;
import org.matsim.pt.transitSchedule.api.TransitStopFacility;
import cadyts.demand.PlanBuilder;

class PtPlanToPlanStepBasedOnEvents implements TransitDriverStartsEventHandler, PersonEntersVehicleEventHandler, PersonLeavesVehicleEventHandler, VehicleDepartsAtFacilityEventHandler {

    private static final Logger log = Logger.getLogger(PtPlanToPlanStepBasedOnEvents.class);

    private final Scenario sc;

    private final TransitSchedule schedule;

    private final Map<Id, Collection<Id>> personsFromVehId = new HashMap<Id, Collection<Id>>();

    private int iteration = -1;

    Set<Plan> plansEverSeen = new HashSet<Plan>();

    private static final String STR_PLANSTEPFACTORY = "planStepFactory";

    private static final String STR_ITERATION = "iteration";

    private final Set<Id> transitDrivers = new HashSet<Id>();

    private final Set<Id> transitVehicles = new HashSet<Id>();

    private final Set<Id> calibratedLines;

    PtPlanToPlanStepBasedOnEvents(final Scenario sc, final Set<Id> calibratedLines) {
        this.sc = sc;
        this.schedule = ((ScenarioImpl) sc).getTransitSchedule();
        this.calibratedLines = calibratedLines;
    }

    private long plansFound = 0;

    private long plansNotFound = 0;

    final cadyts.demand.Plan<TransitStopFacility> getPlanSteps(final Plan plan) {
        PlanBuilder<TransitStopFacility> planStepFactory = (PlanBuilder<TransitStopFacility>) plan.getCustomAttributes().get(STR_PLANSTEPFACTORY);
        if (planStepFactory == null) {
            this.plansNotFound++;
            return null;
        }
        this.plansFound++;
        final cadyts.demand.Plan<TransitStopFacility> planSteps = planStepFactory.getResult();
        return planSteps;
    }

    @Override
    public void reset(final int iteration) {
        this.iteration = iteration;
        log.warn("found " + this.plansFound + " out of " + (this.plansFound + this.plansNotFound) + " (" + (100. * this.plansFound / (this.plansFound + this.plansNotFound)) + "%)");
        log.warn("(above values may both be at zero for a couple of iterations if multiple plans per agent all have no score)");
        this.personsFromVehId.clear();
        this.transitDrivers.clear();
        this.transitVehicles.clear();
    }

    @Override
    public void handleEvent(final TransitDriverStartsEvent event) {
        if (this.calibratedLines.contains(event.getTransitLineId())) {
            this.transitDrivers.add(event.getDriverId());
            this.transitVehicles.add(event.getVehicleId());
        }
    }

    @Override
    public void handleEvent(final PersonEntersVehicleEvent event) {
        if (this.transitDrivers.contains(event.getPersonId()) || !this.transitVehicles.contains(event.getVehicleId())) {
            return;
        }
        addPersonToVehicleContainer(event.getPersonId(), event.getVehicleId());
    }

    @Override
    public void handleEvent(final PersonLeavesVehicleEvent event) {
        if (this.transitDrivers.contains(event.getPersonId()) || !this.transitVehicles.contains(event.getVehicleId())) {
            return;
        }
        removePersonFromVehicleContainer(event.getPersonId(), event.getVehicleId());
    }

    @Override
    public void handleEvent(final VehicleDepartsAtFacilityEvent event) {
        double time = event.getTime();
        Id vehId = event.getVehicleId();
        Id facId = event.getFacilityId();
        if (this.personsFromVehId.get(vehId) == null) {
            return;
        }
        TransitStopFacility fac = this.schedule.getFacilities().get(facId);
        for (Id personId : this.personsFromVehId.get(vehId)) {
            Person person = this.sc.getPopulation().getPersons().get(personId);
            Plan selectedPlan = person.getSelectedPlan();
            PlanBuilder<TransitStopFacility> tmpPlanStepFactory = getPlanStepFactoryForPlan(selectedPlan);
            if (tmpPlanStepFactory != null) {
                tmpPlanStepFactory.addTurn(fac, (int) time);
            }
        }
    }

    private void addPersonToVehicleContainer(final Id personId, final Id vehId) {
        Collection<Id> personsInVehicle = this.personsFromVehId.get(vehId);
        if (personsInVehicle == null) {
            personsInVehicle = new ArrayList<Id>();
            this.personsFromVehId.put(vehId, personsInVehicle);
        }
        personsInVehicle.add(personId);
    }

    private void removePersonFromVehicleContainer(final Id personId, final Id vehId) {
        Collection<Id> personsInVehicle = this.personsFromVehId.get(vehId);
        if (personsInVehicle == null) {
            Gbl.errorMsg("should not be possible: person should enter before leaving, and then construct the container");
        }
        personsInVehicle.remove(personId);
    }

    private PlanBuilder<TransitStopFacility> getPlanStepFactoryForPlan(final Plan selectedPlan) {
        PlanBuilder<TransitStopFacility> planStepFactory = null;
        planStepFactory = (PlanBuilder<TransitStopFacility>) selectedPlan.getCustomAttributes().get(STR_PLANSTEPFACTORY);
        Integer factoryIteration = (Integer) selectedPlan.getCustomAttributes().get(STR_ITERATION);
        if (planStepFactory == null || factoryIteration == null || factoryIteration != this.iteration) {
            selectedPlan.getCustomAttributes().put(STR_ITERATION, this.iteration);
            planStepFactory = new PlanBuilder<TransitStopFacility>();
            selectedPlan.getCustomAttributes().put(STR_PLANSTEPFACTORY, planStepFactory);
            this.plansEverSeen.add(selectedPlan);
        }
        return planStepFactory;
    }

    static void printCadytsPlan(final cadyts.demand.Plan<TransitStopFacility> cadytsPlan) {
        String sepCadStr = "==printing Cadyts Plan==";
        System.err.println(sepCadStr);
        if (cadytsPlan != null) {
            for (int ii = 0; ii < cadytsPlan.size(); ii++) {
                cadyts.demand.PlanStep<TransitStopFacility> cadytsPlanStep = cadytsPlan.getStep(ii);
                System.err.println("stopId" + cadytsPlanStep.getLink().getId() + " time: " + cadytsPlanStep.getEntryTime_s());
            }
        } else {
            System.err.println(" cadyts plan is null ");
        }
    }
}
