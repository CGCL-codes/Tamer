package playground.mmoyo.cadyts_integration.ptBseAsPlanStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
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
import org.matsim.pt.routes.ExperimentalTransitRoute;
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

    private static final String STR_M44 = "M44";

    private final String STR_PLANSTEPFACTORY = "planStepFactory";

    private final String STR_ITERATION = "iteration";

    private final Map<Id, Id> vehToRouteId = new HashMap<Id, Id>();

    private final Set<Id> transitDrivers = new HashSet<Id>();

    private final Set<Id> transitVehicles = new HashSet<Id>();

    PtPlanToPlanStepBasedOnEvents(final Scenario sc) {
        this.sc = sc;
        this.schedule = ((ScenarioImpl) sc).getTransitSchedule();
    }

    private long plansFound = 0;

    private long plansNotFound = 0;

    final cadyts.demand.Plan<TransitStopFacility> getPlanSteps(final Plan plan) {
        PlanBuilder<TransitStopFacility> planStepFactory = (PlanBuilder<TransitStopFacility>) plan.getCustomAttributes().get(this.STR_PLANSTEPFACTORY);
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
        this.vehToRouteId.clear();
        plansEverSeen.clear();
        this.transitDrivers.clear();
        this.transitVehicles.clear();
    }

    @Override
    public void handleEvent(final TransitDriverStartsEvent event) {
        this.vehToRouteId.put(event.getVehicleId(), event.getTransitRouteId());
        this.transitDrivers.add(event.getDriverId());
        this.transitVehicles.add(event.getVehicleId());
    }

    @Override
    public void handleEvent(final PersonEntersVehicleEvent event) {
        if (this.transitDrivers.contains(event.getPersonId()) || !this.transitVehicles.contains(event.getVehicleId())) {
            return;
        }
        Id transitLineId = this.vehToRouteId.get(event.getVehicleId());
        if (!transitLineId.toString().contains(STR_M44)) {
            return;
        }
        addPersonToVehicleContainer(event.getPersonId(), event.getVehicleId());
    }

    @Override
    public void handleEvent(final PersonLeavesVehicleEvent event) {
        if (this.transitDrivers.contains(event.getPersonId()) || !this.transitVehicles.contains(event.getVehicleId())) {
            return;
        }
        Id transitLineId = this.vehToRouteId.get(event.getVehicleId());
        if (!transitLineId.toString().contains(STR_M44)) {
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
        planStepFactory = (PlanBuilder<TransitStopFacility>) selectedPlan.getCustomAttributes().get(this.STR_PLANSTEPFACTORY);
        Integer factoryIteration = (Integer) selectedPlan.getCustomAttributes().get(this.STR_ITERATION);
        if (planStepFactory == null || factoryIteration == null || factoryIteration != this.iteration) {
            selectedPlan.getCustomAttributes().put(this.STR_ITERATION, this.iteration);
            planStepFactory = new PlanBuilder<TransitStopFacility>();
            selectedPlan.getCustomAttributes().put(this.STR_PLANSTEPFACTORY, planStepFactory);
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

    static boolean printMatsimPlanPtLinks(final Plan matsimPlan) {
        final String sepMatStr = "==printing MATSim plan exp transit routes==";
        final String personIdStr = "person Id: ";
        boolean containsM44 = false;
        System.err.println(sepMatStr);
        System.err.println(personIdStr + matsimPlan.getPerson().getId());
        for (PlanElement planElement : matsimPlan.getPerson().getSelectedPlan().getPlanElements()) {
            if ((planElement instanceof Leg)) {
                Leg leg = (Leg) planElement;
                if (leg.getRoute() != null && (leg.getMode().equals("pt"))) {
                    ExperimentalTransitRoute exptr = (ExperimentalTransitRoute) leg.getRoute();
                    if (exptr.getRouteDescription().contains(STR_M44)) {
                        containsM44 = true;
                        System.err.print(exptr.getRouteDescription() + ": ");
                        System.err.println();
                    }
                }
            }
        }
        return containsM44;
    }
}
