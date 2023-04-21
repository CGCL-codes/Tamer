package org.matsim.scoring;

import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.matsim.config.groups.CharyparNagelScoringConfigGroup;
import org.matsim.config.groups.CharyparNagelScoringConfigGroup.ActivityParams;
import org.matsim.gbl.Gbl;
import org.matsim.plans.Act;
import org.matsim.plans.ActUtilityParameters;
import org.matsim.plans.Leg;
import org.matsim.plans.Person;
import org.matsim.plans.Plan;
import org.matsim.plans.Route;
import org.matsim.utils.misc.Time;

/**
 * This is the default scoring function for MATSim, referring to:
 *
 * <blockquote>
 *  <p>Charypar, D. und K. Nagel (2005) <br>
 *  Generating complete all-day activity plans with genetic algorithms,<br>
 *  Transportation, 32 (4) 369–397.</p>
 * </blockquote>
 *
 * The scoring function takes
 * the following aspects into account when calculating a score:
 * <dl>
 * <dt>trip duration<dt>
 * <dd>The longer an agent is traveling, the lower its score will be usually.
 * The score will be reduced by an amount linear to the travel time.</dd>
 * <dt>activity duration</dt>
 * <dd>The time spent at an activity can be further distinguished into time the
 * agent spends <em>waiting</em> at the place because the facility is currently
 * closed, or time the agent spends <em>performing</em> the activity. The time
 * spent waiting will decrease the score by an amount linear to the time spent
 * waiting, while the time spent performing an activity increases the score
 * logarithmically.
 * </dd>
 * <dt>stuck penalty</dt>
 * <dd>If the agent is not able to move further on a link in the simulation,
 * the simulation may decide that the agent is stuck and remove it from the
 * simulation. In this case, the agent's score will be decrease with a
 * penalty.</dd>
 * <dt>distance</dt>
 * <dd>The score may decrease by an amount linear to the traveled distance.</dd>
 * </dl>
 *
 * The actual amounts for how much the score increases or decreases for the
 * different aspects are to be set in the configuration. Besides the penalty
 * for being stuck, the following penalties can also decrease the score:
 * <dl>
 * <dt>late arrival</dt>
 * <dd>If the agent arrives too late at an activity (as specified in the
 * configuration for each activity type), a penalty linear to the time being
 * late will be subtracted from the score.</dd>
 * <dt>early departure</dt>
 * <dd>If the agent leaves an activity too early (as specified in the
 * configuration for each activity type), a penalty linear to the time left
 * early will be subtracted from the score.</dd>
 * </dl>
 *
 * @author mrieser
 */
public class CharyparNagelScoringFunction implements ScoringFunction {

    protected final Person person;

    protected final Plan plan;

    protected double score;

    private double lastTime;

    private int index;

    private double firstActTime;

    private final int lastActIndex;

    private static final double INITIAL_LAST_TIME = 0.0;

    private static final int INITIAL_INDEX = 0;

    private static final double INITIAL_FIRST_ACT_TIME = Time.UNDEFINED_TIME;

    private static final double INITIAL_SCORE = 0.0;

    public static boolean initialized = false;

    /** True if one at least one of marginal utilities for performing, waiting, being late or leaving early is not equal to 0. */
    private static boolean scoreActs = true;

    private static final Logger log = Logger.getLogger(CharyparNagelScoringFunction.class);

    public CharyparNagelScoringFunction(final Plan plan) {
        init();
        this.reset();
        this.plan = plan;
        this.person = this.plan.getPerson();
        this.lastActIndex = this.plan.getActsLegs().size() - 1;
    }

    public void reset() {
        this.lastTime = INITIAL_LAST_TIME;
        this.index = INITIAL_INDEX;
        this.firstActTime = INITIAL_FIRST_ACT_TIME;
        this.score = INITIAL_SCORE;
    }

    public void startActivity(final double time, final Act act) {
    }

    public void endActivity(final double time) {
    }

    public void startLeg(final double time, final Leg leg) {
        if (this.index % 2 == 0) {
            handleAct(time);
        }
        this.lastTime = time;
    }

    public void endLeg(final double time) {
        handleLeg(time);
        this.lastTime = time;
    }

    public void agentStuck(final double time) {
        this.lastTime = time;
        this.score += getStuckPenalty();
    }

    public void finish() {
        if (this.index == this.lastActIndex) {
            handleAct(24 * 3600);
        }
    }

    public double getScore() {
        return this.score;
    }

    public static final String CONFIG_MODULE = "planCalcScore";

    public static final String CONFIG_WAITING = "waiting";

    public static final String CONFIG_LATE_ARRIVAL = "lateArrival";

    public static final String CONFIG_EARLY_DEPARTURE = "earlyDeparture";

    public static final String CONFIG_TRAVELING = "traveling";

    public static final String CONFIG_TRAVELING_PT = "travelingPT";

    public static final String CONFIG_PERFORMING = "performing";

    public static final String CONFIG_LEARNINGRATE = "learningRate";

    public static final String CONFIG_DISTANCE_COST = "distanceCost";

    protected static final TreeMap<String, ActUtilityParameters> utilParams = new TreeMap<String, ActUtilityParameters>();

    private static double marginalUtilityOfWaiting = Double.NaN;

    private static double marginalUtilityOfLateArrival = Double.NaN;

    private static double marginalUtilityOfEarlyDeparture = Double.NaN;

    protected static double marginalUtilityOfTraveling = Double.NaN;

    private static double marginalUtilityOfTravelingPT = Double.NaN;

    private static double marginalUtilityOfPerforming = Double.NaN;

    private static double distanceCost = Double.NaN;

    private static double abortedPlanScore = Double.NaN;

    private static void init() {
        if (initialized) return;
        CharyparNagelScoringConfigGroup params = Gbl.getConfig().charyparNagelScoring();
        marginalUtilityOfWaiting = params.getWaiting() / 3600.0;
        marginalUtilityOfLateArrival = params.getLateArrival() / 3600.0;
        marginalUtilityOfEarlyDeparture = params.getEarlyDeparture() / 3600.0;
        marginalUtilityOfTraveling = params.getTraveling() / 3600.0;
        marginalUtilityOfTravelingPT = params.getTravelingPt() / 3600.0;
        marginalUtilityOfPerforming = params.getPerforming() / 3600.0;
        distanceCost = params.getDistanceCost() / 1000.0;
        abortedPlanScore = Math.min(Math.min(marginalUtilityOfLateArrival, marginalUtilityOfEarlyDeparture), Math.min(marginalUtilityOfTraveling, marginalUtilityOfWaiting)) * 3600.0 * 24.0;
        readUtilityValues();
        scoreActs = (marginalUtilityOfPerforming != 0 || marginalUtilityOfWaiting != 0 || marginalUtilityOfLateArrival != 0 || marginalUtilityOfEarlyDeparture != 0);
        initialized = true;
    }

    private final double calcActScore(final double arrivalTime, final double departureTime, final Act act) {
        ActUtilityParameters params = utilParams.get(act.getType());
        if (params == null) {
            throw new IllegalArgumentException("acttype \"" + act.getType() + "\" is not known in utility parameters.");
        }
        double score = 0.0;
        double openingTime = params.getOpeningTime();
        double closingTime = params.getClosingTime();
        double activityStart = arrivalTime;
        double activityEnd = departureTime;
        if (openingTime >= 0 && arrivalTime < openingTime) {
            activityStart = openingTime;
        }
        if (closingTime >= 0 && closingTime < departureTime) {
            activityEnd = closingTime;
        }
        if (openingTime >= 0 && closingTime >= 0 && ((openingTime > departureTime) || (closingTime < arrivalTime))) {
            activityStart = departureTime;
            activityEnd = departureTime;
        }
        double duration = activityEnd - activityStart;
        if (arrivalTime < activityStart) {
            score += marginalUtilityOfWaiting * (activityStart - arrivalTime);
        }
        double latestStartTime = params.getLatestStartTime();
        if (latestStartTime >= 0 && activityStart > latestStartTime) {
            score += marginalUtilityOfLateArrival * (activityStart - latestStartTime);
        }
        double typicalDuration = params.getTypicalDuration();
        if (duration > 0) {
            double utilPerf = marginalUtilityOfPerforming * typicalDuration * Math.log((duration / 3600.0) / params.getZeroUtilityDuration());
            double utilWait = marginalUtilityOfWaiting * duration;
            score += Math.max(0, Math.max(utilPerf, utilWait));
        } else {
            score += 2 * marginalUtilityOfLateArrival * Math.abs(duration);
        }
        double earliestEndTime = params.getEarliestEndTime();
        if (earliestEndTime >= 0 && activityEnd < earliestEndTime) {
            score += marginalUtilityOfEarlyDeparture * (earliestEndTime - activityEnd);
        }
        if (activityEnd < departureTime) {
            score += marginalUtilityOfWaiting * (departureTime - activityEnd);
        }
        double minimalDuration = params.getMinimalDuration();
        if (minimalDuration >= 0 && duration < minimalDuration) {
            score += marginalUtilityOfEarlyDeparture * (minimalDuration - duration);
        }
        return score;
    }

    protected double calcLegScore(final double departureTime, final double arrivalTime, final Leg leg) {
        double score = 0.0;
        double travelTime = arrivalTime - departureTime;
        double dist = 0.0;
        if (distanceCost != 0.0) {
            Route route = leg.getRoute();
            dist = route.getDist();
        }
        if ("car".equals(leg.getMode())) {
            score += travelTime * marginalUtilityOfTraveling - distanceCost * dist;
        } else if ("pt".equals(leg.getMode())) {
            score += travelTime * marginalUtilityOfTravelingPT - distanceCost * dist;
        } else {
            score += travelTime * marginalUtilityOfTraveling - distanceCost * dist;
        }
        return score;
    }

    private static double getStuckPenalty() {
        return abortedPlanScore;
    }

    /**
	 * reads all activity utility values from the config-file
	 */
    private static final void readUtilityValues() {
        CharyparNagelScoringConfigGroup config = Gbl.getConfig().charyparNagelScoring();
        for (ActivityParams params : config.getActivityParams()) {
            String type = params.getType();
            double priority = params.getPriority();
            double typDurationSecs = params.getTypicalDuration();
            ActUtilityParameters actParams = new ActUtilityParameters(type, priority, typDurationSecs);
            if (params.getMinimalDuration() >= 0) {
                actParams.setMinimalDuration(params.getMinimalDuration());
            }
            if (params.getOpeningTime() >= 0) {
                actParams.setOpeningTime(params.getOpeningTime());
            }
            if (params.getLatestStartTime() >= 0) {
                actParams.setLatestStartTime(params.getLatestStartTime());
            }
            if (params.getEarliestEndTime() >= 0) {
                actParams.setEarliestEndTime(params.getEarliestEndTime());
            }
            if (params.getClosingTime() >= 0) {
                actParams.setClosingTime(params.getClosingTime());
            }
            utilParams.put(type, actParams);
        }
    }

    private void handleAct(final double time) {
        Act act = (Act) this.plan.getActsLegs().get(this.index);
        if (this.index == 0) {
            this.firstActTime = time;
        } else if (this.index == this.lastActIndex) {
            String lastActType = act.getType();
            if (lastActType.equals(((Act) this.plan.getActsLegs().get(0)).getType())) {
                this.score += calcActScore(this.lastTime, this.firstActTime + 24 * 3600, act);
            } else {
                if (scoreActs) {
                    log.warn("The first and the last activity do not have the same type. The correctness of the scoring function can thus not be guaranteed.");
                    Act firstAct = (Act) this.plan.getActsLegs().get(0);
                    this.score += calcActScore(0.0, this.firstActTime, firstAct);
                    this.score += calcActScore(this.lastTime, 24 * 3600, act);
                }
            }
        } else {
            this.score += calcActScore(this.lastTime, time, act);
        }
        this.index++;
    }

    private void handleLeg(final double time) {
        Leg leg = (Leg) this.plan.getActsLegs().get(this.index);
        this.score += calcLegScore(this.lastTime, time, leg);
        this.index++;
    }
}
