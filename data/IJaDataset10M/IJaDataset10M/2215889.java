package playground.thibautd.jointtrips.replanning.modules.jointplanoptimizer.pipeddecoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jgap.IChromosome;
import org.jgap.impl.DoubleGene;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.config.groups.PlanomatConfigGroup;
import org.matsim.core.config.groups.PlanomatConfigGroup.SimLegInterpretation;
import org.matsim.core.population.PlanImpl;
import org.matsim.core.router.PlansCalcRoute;
import org.matsim.core.utils.misc.Time;
import org.matsim.planomat.costestimators.LegTravelTimeEstimator;
import playground.thibautd.jointtrips.config.JointReplanningConfigGroup;
import playground.thibautd.jointtrips.population.JointActingTypes;
import playground.thibautd.jointtrips.population.JointActivity;
import playground.thibautd.jointtrips.population.JointLeg;
import playground.thibautd.jointtrips.population.JointPlan;
import playground.thibautd.jointtrips.replanning.modules.jointplanoptimizer.costestimators.JointPlanOptimizerLegTravelTimeEstimatorFactory;

/**
 * Decodes plan durations, assuming that all other dimensions (toggle, mode) are
 * set correctly.
 *
 * @author thibautd
 */
public class DurationDecoder implements JointPlanOptimizerDimensionDecoder {

    private static final Logger log = Logger.getLogger(DurationDecoder.class);

    /**
	 * lists the indices of the genes in the chromosome relative to
	 * each individual.
	 */
    private final Map<Id, List<Integer>> genesIndices = new HashMap<Id, List<Integer>>();

    public static final double MIN_DURATION = 0d;

    public static final double PU_DURATION = 0d;

    public static final double DO_DURATION = 0d;

    private JointPlan plan;

    private final Map<Id, List<PlanElement>> individualPlanElements = new HashMap<Id, List<PlanElement>>();

    private final Map<Id, LegTravelTimeEstimator> legTTEstimators = new HashMap<Id, LegTravelTimeEstimator>();

    /**
	 * links shared legs with their soonest beginning time (understood as the
	 * arrival time of the agent to the pick-up).
	 */
    private final Map<JointLeg, Double> readyJointLegs = new HashMap<JointLeg, Double>();

    /**
	 * relates passenger shared rides to the related driver trip, if already
	 * scheduled.
	 */
    private final Map<JointLeg, JointLeg> driverLegs = new HashMap<JointLeg, JointLeg>();

    private final JointPlanOptimizerLegTravelTimeEstimatorFactory legTravelTimeEstimatorFactory;

    private final PlansCalcRoute routingAlgorithm;

    private final Network network;

    private final int nMembers;

    /**
	 * initializes a decoder, which can be used on any modification of the parameter plan.
	 * This constructor initializes the relation between activities and genes.
	 */
    public DurationDecoder(final JointPlan plan, final JointReplanningConfigGroup configGroup, final JointPlanOptimizerLegTravelTimeEstimatorFactory legTravelTimeEstimatorFactory, final PlansCalcRoute routingAlgorithm, final Network network, final int numJointEpisodes, final int numEpisodes, final int nMembers) {
        this.legTravelTimeEstimatorFactory = legTravelTimeEstimatorFactory;
        this.routingAlgorithm = routingAlgorithm;
        this.network = network;
        this.nMembers = nMembers;
        int currentDurationGene = numJointEpisodes;
        Integer indexToAdd;
        for (Id id : plan.getClique().getMembers().keySet()) {
            this.genesIndices.put(id, new ArrayList<Integer>());
        }
        Id currentId;
        List<PlanElement> planElements;
        for (Map.Entry<Id, Plan> planEntry : plan.getIndividualPlans().entrySet()) {
            currentId = planEntry.getKey();
            planElements = planEntry.getValue().getPlanElements();
            for (PlanElement pe : planElements.subList(0, planElements.size() - 1)) {
                if (pe instanceof JointLeg) {
                    continue;
                }
                if (!isPickUp(pe) && !isDropOff(pe)) {
                    indexToAdd = currentDurationGene;
                    currentDurationGene++;
                    this.genesIndices.get(currentId).add(indexToAdd);
                }
            }
            this.genesIndices.get(currentId).add(null);
        }
        this.initializeLegEstimators(plan, configGroup.getSimLegInterpretation());
    }

    private void initializeLegEstimators(final JointPlan plan, final SimLegInterpretation simLegInt) {
        LegTravelTimeEstimator currentLegTTEstimator;
        for (Id id : plan.getClique().getMembers().keySet()) {
            currentLegTTEstimator = legTravelTimeEstimatorFactory.getLegTravelTimeEstimator(plan.getIndividualPlan(id), simLegInt, PlanomatConfigGroup.RoutingCapability.fixedRoute, routingAlgorithm, network);
            this.legTTEstimators.put(id, currentLegTTEstimator);
            this.individualPlanElements.put(id, plan.getIndividualPlan(id).getPlanElements());
        }
    }

    /**
	 * Returns a plan corresponding to the chromosome.
	 */
    @Override
    public JointPlan decode(final IChromosome chromosome, final JointPlan inputPlan) {
        Map<Id, PlanImpl> constructedIndividualPlans = new HashMap<Id, PlanImpl>(nMembers);
        Map<Id, IndividualValuesWrapper> individualValuesMap = new HashMap<Id, IndividualValuesWrapper>(nMembers);
        List<Id> individualsToPlan = new ArrayList<Id>(nMembers);
        List<Id> toRemove = new ArrayList<Id>(nMembers);
        Id currentId = null;
        PlanImpl individualPlan = null;
        this.plan = inputPlan;
        resetInternalState();
        for (Map.Entry<Id, Plan> inputIndivPlan : plan.getIndividualPlans().entrySet()) {
            individualPlan = new PlanImpl(inputIndivPlan.getValue().getPerson());
            currentId = inputIndivPlan.getKey();
            constructedIndividualPlans.put(currentId, individualPlan);
            individualsToPlan.add(currentId);
            individualValuesMap.put(currentId, new IndividualValuesWrapper(inputIndivPlan.getValue().getPlanElements()));
        }
        do {
            for (Id id : individualsToPlan) {
                planNextActivity(chromosome, individualValuesMap, id, constructedIndividualPlans);
                if (individualValuesMap.get(id).getIndexInPlan() >= individualValuesMap.get(id).planElements.size()) {
                    toRemove.add(id);
                }
            }
            individualsToPlan.removeAll(toRemove);
            toRemove.clear();
        } while (!individualsToPlan.isEmpty());
        return new JointPlan(this.plan.getClique(), constructedIndividualPlans, false, false, this.plan.getScoresAggregatorFactory());
    }

    private void resetInternalState() {
        this.readyJointLegs.clear();
        this.driverLegs.clear();
    }

    private final void planNextActivity(final IChromosome chromosome, final Map<Id, IndividualValuesWrapper> individualValuesMap, final Id id, final Map<Id, PlanImpl> constructedIndividualPlans) {
        IndividualValuesWrapper individualValues = individualValuesMap.get(id);
        PlanImpl constructedPlan = constructedIndividualPlans.get(id);
        List<PlanElement> planElements = this.individualPlanElements.get(id);
        PlanElement currentElement = individualValues.planElements.get(individualValues.getIndexInPlan());
        LegTravelTimeEstimator currentLegTTEstimator = this.legTTEstimators.get(id);
        double currentDuration;
        Integer geneIndex;
        JointActivity origin;
        JointActivity destination;
        geneIndex = this.genesIndices.get(id).get(individualValues.getIndexInChromosome());
        if (individualValues.getIndexInPlan() == 0) {
            origin = new JointActivity((JointActivity) currentElement);
            origin.setStartTime(individualValues.getNow());
            currentDuration = ((DoubleGene) chromosome.getGene(geneIndex)).doubleValue();
            origin.setMaximumDuration(currentDuration);
            individualValues.addToNow(currentDuration);
            origin.setEndTime(individualValues.getNow());
            constructedPlan.addActivity(origin);
            individualValues.addToIndexInPlan(1);
            individualValues.addToIndexInChromosome(1);
        } else if (currentElement instanceof JointLeg) {
            origin = (JointActivity) individualValues.planElements.get(individualValues.getIndexInPlan() - 1);
            destination = new JointActivity((JointActivity) individualValues.planElements.get(individualValues.getIndexInPlan() + 1));
            if (isPickUp(destination)) {
                if (!this.readyJointLegs.containsKey(individualValues.planElements.get(individualValues.getIndexInPlan() + 2))) {
                    planPuAccessLeg(planElements, currentElement, constructedPlan, currentLegTTEstimator, individualValues, chromosome);
                } else {
                    throw new RuntimeException("passing twice the same access leg");
                }
            } else if (isDropOff(destination)) {
                throw new RuntimeException("index positionned on a shared leg");
            } else {
                planIndividualLegAct(planElements, currentElement, constructedPlan, currentLegTTEstimator, individualValues, geneIndex, chromosome);
            }
        } else if (isPickUp(currentElement) || isDropOff(currentElement)) {
            if (isReadyForPlanning(individualValues.planElements, individualValues.getIndexInPlan())) {
                planSharedLegAct(planElements, currentElement, constructedPlan, currentLegTTEstimator, individualValues, geneIndex, chromosome);
            }
        } else {
            log.error("unexpected index: trying to plan an element which is not" + " a JointLeg nor a Pick-Up activity");
        }
    }

    private void planPuAccessLeg(final List<PlanElement> planElements, final PlanElement currentElement, final Plan constructedPlan, final LegTravelTimeEstimator legTTEstimator, final IndividualValuesWrapper individualValues, final IChromosome chromosome) {
        JointLeg leg = ((JointLeg) currentElement);
        JointActivity origin = (JointActivity) individualValues.planElements.get(individualValues.getIndexInPlan() - 1);
        JointActivity destination = new JointActivity((JointActivity) individualValues.planElements.get(individualValues.getIndexInPlan() + 1));
        double currentTravelTime;
        leg = createLeg(legTTEstimator, origin, destination, planElements.indexOf(leg), individualValues, leg);
        constructedPlan.addLeg(leg);
        currentTravelTime = leg.getTravelTime();
        individualValues.addToNow(currentTravelTime);
        this.readyJointLegs.put((JointLeg) individualValues.planElements.get(individualValues.getIndexInPlan() + 2), individualValues.getNow());
        individualValues.addToIndexInPlan(1);
        individualValues.addToJointTravelTime(currentTravelTime);
    }

    private void planIndividualLegAct(final List<PlanElement> planElements, final PlanElement currentElement, final Plan constructedPlan, final LegTravelTimeEstimator legTTEstimator, final IndividualValuesWrapper individualValues, final Integer geneIndex, final IChromosome chromosome) {
        JointLeg leg = ((JointLeg) currentElement);
        JointActivity origin = (JointActivity) individualValues.planElements.get(individualValues.getIndexInPlan() - 1);
        JointActivity destination = new JointActivity((JointActivity) individualValues.planElements.get(individualValues.getIndexInPlan() + 1));
        double currentTravelTime;
        double currentDuration;
        leg = createLeg(legTTEstimator, origin, destination, planElements.indexOf(leg), individualValues, leg);
        constructedPlan.addLeg(leg);
        currentTravelTime = leg.getTravelTime();
        individualValues.addToNow(currentTravelTime);
        individualValues.addToIndexInPlan(2);
        individualValues.addToJointTravelTime(currentTravelTime);
        currentDuration = getDuration(chromosome, geneIndex, individualValues.getNow(), individualValues.getJointTravelTime());
        destination.setStartTime(individualValues.getNow());
        destination.setMaximumDuration(currentDuration);
        individualValues.addToNow(currentDuration);
        destination.setEndTime(individualValues.getNow());
        constructedPlan.addActivity(destination);
        individualValues.addToIndexInChromosome(1);
        individualValues.resetTravelTime();
    }

    private void planSharedLegAct(final List<PlanElement> planElements, final PlanElement currentElement, final Plan constructedPlan, final LegTravelTimeEstimator legTTEstimator, final IndividualValuesWrapper individualValues, final Integer geneIndex, final IChromosome chromosome) {
        JointActivity origin = ((JointActivity) currentElement);
        JointLeg leg = (JointLeg) individualValues.planElements.get(individualValues.getIndexInPlan() + 1);
        JointActivity destination = (JointActivity) individualValues.planElements.get(individualValues.getIndexInPlan() + 2);
        double currentTravelTime = 0d;
        double currentDuration;
        if (isPickUp(origin)) {
            currentDuration = getTimeToJointTrip(leg.getLinkedElements().values(), individualValues.getNow()) + PU_DURATION;
            constructedPlan.addActivity(createActivity(origin, individualValues.getNow(), currentDuration));
        } else if (isDropOff(origin)) {
            constructedPlan.addActivity(createActivity(origin, individualValues.getNow(), DO_DURATION));
            currentDuration = DO_DURATION;
        } else {
            throw new RuntimeException("planning of a shared ride launched " + "on a non-PU nor DO activity");
        }
        individualValues.addToNow(currentDuration);
        currentTravelTime += currentDuration;
        leg = (JointLeg) individualValues.planElements.get(individualValues.getIndexInPlan() + 1);
        if (leg.getIsDriver()) {
            JointLeg driverLeg = createLeg(legTTEstimator, origin, destination, planElements.indexOf(leg), individualValues, leg);
            updateDriverLegs(driverLeg, leg);
            leg = driverLeg;
        } else {
            leg = new JointLeg(this.driverLegs.get(leg), leg);
            leg.setMode(JointActingTypes.PASSENGER);
        }
        individualValues.addToNow(leg.getTravelTime());
        currentTravelTime += leg.getTravelTime();
        constructedPlan.addLeg(leg);
        if (isDropOff(destination)) {
            if (!((JointLeg) individualValues.planElements.get(individualValues.getIndexInPlan() + 3)).getJoint()) {
                destination = createActivity(destination, individualValues.getNow(), DO_DURATION);
                individualValues.addToNow(DO_DURATION);
                currentTravelTime += DO_DURATION;
                constructedPlan.addActivity(destination);
                individualValues.addToIndexInPlan(3);
            } else {
                individualValues.addToIndexInPlan(2);
            }
        } else if (isPickUp(destination)) {
            this.readyJointLegs.put((JointLeg) individualValues.planElements.get(individualValues.getIndexInPlan() + 3), individualValues.getNow());
            individualValues.addToIndexInPlan(2);
        } else {
            throw new RuntimeException("only PU and DO activity should occur " + "when planning a shared ride");
        }
        individualValues.addToJointTravelTime(currentTravelTime);
    }

    private void updateDriverLegs(final JointLeg driverLeg, final JointLeg legInPlan) {
        for (JointLeg leg : legInPlan.getLinkedElements().values()) {
            this.driverLegs.put(leg, driverLeg);
        }
    }

    /**
	 * Determines if a pick-up or a drop-off followed by a shared ride is ready
	 * for planning, that is:
	 * - all related access trips are planned;
	 * - the current individual is the driver, or the driver trip is planned
	 *
	 * returns true for all other activities
	 */
    private final boolean isReadyForPlanning(final List<PlanElement> planElementsToPlan, final int index) {
        JointLeg sharedRide = (JointLeg) planElementsToPlan.get(index + 1);
        boolean test = (isPickUp(planElementsToPlan.get(index)) ? true : (isDropOff(planElementsToPlan.get(index)) && sharedRide.getJoint()));
        if (test) {
            boolean allRelativesArePlanned = this.readyJointLegs.keySet().containsAll(sharedRide.getLinkedElements().values());
            boolean isDriver = sharedRide.getIsDriver();
            boolean driverIsPlanned = this.driverLegs.containsKey(sharedRide);
            return (allRelativesArePlanned && (isDriver || driverIsPlanned));
        }
        return true;
    }

    private final JointActivity createActivity(final JointActivity act, final double now, final double duration) {
        JointActivity newAct = new JointActivity(act);
        newAct.setStartTime(now);
        newAct.setMaximumDuration(duration);
        newAct.setEndTime(now + duration);
        return newAct;
    }

    private JointLeg createLeg(final LegTravelTimeEstimator legTTEstimator, final Activity origin, final Activity destination, final int indexInPlan, final IndividualValuesWrapper individualValues, final JointLeg leg) {
        JointLeg output = new JointLeg(legTTEstimator.getNewLeg(leg.getMode(), origin, destination, indexInPlan, individualValues.getNow()), leg);
        output.getRoute().setTravelTime(output.getTravelTime());
        output.setDepartureTime(individualValues.getNow());
        output.setArrivalTime(individualValues.getNow() + output.getTravelTime());
        return output;
    }

    private final double getTimeToJointTrip(final Collection<? extends JointLeg> linkedElements, final double now) {
        double max = now;
        double currentTime;
        for (JointLeg leg : linkedElements) {
            currentTime = this.readyJointLegs.get(leg);
            if (max < currentTime) {
                max = currentTime;
            }
        }
        return (max - now);
    }

    /**
	 * @param now time of day after the travel
	 */
    private final double getDuration(final IChromosome chromosome, final Integer geneIndex, final double now, final double travelTime) {
        double duration = 0d;
        if (geneIndex != null) {
            duration = ((DoubleGene) chromosome.getGene(geneIndex)).doubleValue() - travelTime;
            if (duration < MIN_DURATION) {
                duration = MIN_DURATION;
            }
        } else {
            duration = Time.UNDEFINED_TIME;
        }
        return duration;
    }

    private boolean isPickUp(final PlanElement pe) {
        return ((JointActivity) pe).getType().equals(JointActingTypes.PICK_UP);
    }

    private boolean isDropOff(final PlanElement pe) {
        return ((JointActivity) pe).getType().equals(JointActingTypes.DROP_OFF);
    }

    /**
	 * internal class, wrapping the int indices used to step through the plans.
	 */
    private static class IndividualValuesWrapper {

        private int indexInPlan = 0;

        private int indexInChromosome = 0;

        private double now = 0d;

        /**
		 * For tracking travel time in complicated joint trips
		 */
        private double jointTravelTime = 0d;

        public List<PlanElement> planElements;

        public IndividualValuesWrapper(final List<PlanElement> planElements) {
            this.planElements = planElements;
        }

        public void addToIndexInPlan(final int i) {
            indexInPlan += i;
        }

        public void addToIndexInChromosome(final int i) {
            indexInChromosome += i;
        }

        public void addToNow(final double d) {
            now += d;
        }

        public int getIndexInPlan() {
            return indexInPlan;
        }

        public int getIndexInChromosome() {
            return indexInChromosome;
        }

        public double getNow() {
            return now;
        }

        public double getJointTravelTime() {
            return this.jointTravelTime;
        }

        public void addToJointTravelTime(final double d) {
            this.jointTravelTime += d;
        }

        public void resetTravelTime() {
            this.jointTravelTime = 0d;
        }
    }
}
