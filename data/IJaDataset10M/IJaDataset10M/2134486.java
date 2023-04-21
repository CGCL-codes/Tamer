package playground.anhorni.locationchoice.run.scoring;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.core.api.experimental.facilities.ActivityFacilities;
import org.matsim.core.facilities.ActivityFacilityImpl;
import org.matsim.core.facilities.ActivityOption;
import org.matsim.core.facilities.OpeningTime;
import org.matsim.core.facilities.OpeningTimeImpl;
import org.matsim.core.facilities.OpeningTime.DayType;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.population.PersonImpl;
import org.matsim.core.population.PlanImpl;
import org.matsim.core.scoring.CharyparNagelScoringParameters;

/**
 * This class implements the activity scoring as used in Year 3 of the KTI project.
 *
 * It has the following features:
 *
 * <ul>
 * <li>use opening times from facilities, not from config. scoring function can process multiple
 * 		opening time intervals per activity option</li>
 * <li>use typical durations from agents' desires, not from config</li>
 * <li>typical duration applies to the sum of all instances of an activity type,
 * 		not to single instances (so agent finds out itself how much time to spend in which instance)</li>
 * <li>no penalties for late arrival and early departure are computed</li>
 * </ul>
 *
 * @author meisterk
 *
 */
public class ActivityScoringFunction extends org.matsim.core.scoring.charyparNagel.ActivityScoringFunction {

    static final Logger log = Logger.getLogger(ActivityScoringFunction.class);

    public static final double MINIMUM_DURATION = 0.5 * 3600;

    private final HashMap<String, Double> accumulatedTimeSpentPerforming = new HashMap<String, Double>();

    private final HashMap<String, Double> zeroUtilityDurations = new HashMap<String, Double>();

    private double accumulatedTooShortDuration;

    private double timeSpentWaiting;

    private double accumulatedNegativeDuration;

    private static final DayType DEFAULT_DAY = DayType.wed;

    private static final SortedSet<OpeningTime> DEFAULT_OPENING_TIME = new TreeSet<OpeningTime>();

    static {
        OpeningTime defaultOpeningTime = new OpeningTimeImpl(ActivityScoringFunction.DEFAULT_DAY, Double.MIN_VALUE, Double.MAX_VALUE);
        ActivityScoringFunction.DEFAULT_OPENING_TIME.add(defaultOpeningTime);
    }

    private double sign = 1.0;

    private boolean sizeScore = false;

    private boolean densityScore = false;

    private boolean shoppingCentersScore = false;

    private ShoppingScoreAdditionals shoppingScoreAdditionals;

    private final ActivityFacilities facilities;

    private PlanImpl plan;

    private CharyparNagelScoringParameters params;

    public ActivityScoringFunction(PlanImpl plan, CharyparNagelScoringParameters params, final ActivityFacilities facilities) {
        super(params);
        this.params = params;
        this.plan = plan;
        this.facilities = facilities;
    }

    @Override
    protected double calcActScore(double arrivalTime, double departureTime, Activity act) {
        double fromArrivalToDeparture = departureTime - arrivalTime;
        if (fromArrivalToDeparture < 0.0) {
            this.accumulatedNegativeDuration += fromArrivalToDeparture;
            this.accumulatedTooShortDuration += (ActivityScoringFunction.MINIMUM_DURATION - fromArrivalToDeparture);
        } else {
            SortedSet<OpeningTime> openTimes = null;
            ActivityOption actOpt = null;
            if (act.getType().startsWith("leisure")) {
                actOpt = this.facilities.getFacilities().get(act.getFacilityId()).getActivityOptions().get("leisure");
            } else {
                ActivityFacilityImpl facility = (ActivityFacilityImpl) this.facilities.getFacilities().get(act.getFacilityId());
                if (facility != null) {
                    actOpt = facility.getActivityOptions().get(act.getType());
                }
            }
            if (actOpt != null) {
                openTimes = actOpt.getOpeningTimes(ActivityScoringFunction.DEFAULT_DAY);
            } else {
                if (!act.getType().equals("tta")) {
                    log.error("Agent wants to perform an activity whose type is not available in the planned facility.");
                    log.error("facility id: " + act.getFacilityId());
                    log.error("activity type: " + act.getType());
                    log.error("link id: " + act.getLinkId());
                    Gbl.errorMsg("Agent wants to perform an activity whose type is not available in the planned facility.");
                }
            }
            if (openTimes == null) {
                openTimes = ActivityScoringFunction.DEFAULT_OPENING_TIME;
            }
            double timeSpentPerforming = 0.0;
            double activityStart, activityEnd;
            double openingTime, closingTime;
            for (OpeningTime openTime : openTimes) {
                openingTime = openTime.getStartTime();
                closingTime = openTime.getEndTime();
                activityStart = Math.max(arrivalTime, openingTime);
                activityEnd = Math.min(departureTime, closingTime);
                if ((openingTime > departureTime) || (closingTime < arrivalTime)) {
                    activityStart = departureTime;
                    activityEnd = departureTime;
                }
                double duration = activityEnd - activityStart;
                timeSpentPerforming += duration;
            }
            this.timeSpentWaiting += (fromArrivalToDeparture - timeSpentPerforming);
            double accumulatedDuration = 0.0;
            if (this.accumulatedTimeSpentPerforming.containsKey(act.getType())) {
                accumulatedDuration = this.accumulatedTimeSpentPerforming.get(act.getType());
            }
            this.accumulatedTimeSpentPerforming.put(act.getType(), accumulatedDuration + timeSpentPerforming);
            if (timeSpentPerforming < ActivityScoringFunction.MINIMUM_DURATION) {
                this.accumulatedTooShortDuration += (ActivityScoringFunction.MINIMUM_DURATION - timeSpentPerforming);
            }
        }
        return 0.0;
    }

    @Override
    public void finish() {
        super.finish();
        this.score += this.getEarlyDepartureScore();
        this.score += this.getPerformanceScore();
        this.score += this.getLateArrivalScore();
        if (this.sizeScore) {
            this.score += this.sign * this.shoppingScoreAdditionals.getScoreElementForSize(this.plan);
        }
        if (this.densityScore) {
            this.score += this.sign * this.shoppingScoreAdditionals.getScoreElementForStoreDensity(this.plan);
        }
        if (this.shoppingCentersScore) {
            this.score += this.sign * this.shoppingScoreAdditionals.getScoreElementForShoppingCenters(this.plan);
        }
    }

    public double getEarlyDepartureScore() {
        return this.params.marginalUtilityOfEarlyDeparture_s * this.accumulatedTooShortDuration;
    }

    public double getWaitingTimeScore() {
        return this.params.marginalUtilityOfWaiting_s * this.timeSpentWaiting;
    }

    public double getPerformanceScore() {
        double performanceScore = 0.0;
        for (String actType : this.accumulatedTimeSpentPerforming.keySet()) {
            performanceScore += this.getPerformanceScore(actType, this.accumulatedTimeSpentPerforming.get(actType));
        }
        return performanceScore;
    }

    protected double getPerformanceScore(String actType, double duration) {
        double typicalDuration = ((PersonImpl) this.plan.getPerson()).getDesires().getActivityDuration(actType);
        double zeroUtilityDuration;
        if (this.zeroUtilityDurations.containsKey(actType)) {
            zeroUtilityDuration = this.zeroUtilityDurations.get(actType);
        } else {
            zeroUtilityDuration = (typicalDuration / 3600.0) * Math.exp(-10.0 / (typicalDuration / 3600.0));
            this.zeroUtilityDurations.put(actType, zeroUtilityDuration);
        }
        double tmpScore = 0.0;
        if (duration > 0.0) {
            double utilPerf = this.params.marginalUtilityOfPerforming_s * typicalDuration * Math.log((duration / 3600.0) / this.zeroUtilityDurations.get(actType));
            double utilWait = this.params.marginalUtilityOfWaiting_s * duration;
            tmpScore = Math.max(0, Math.max(utilPerf, utilWait));
        } else if (duration < 0.0) {
            log.error("Accumulated activity durations < 0.0 must not happen.");
        }
        return tmpScore;
    }

    @Override
    public void reset() {
        super.reset();
        if (this.accumulatedTimeSpentPerforming != null) {
            this.accumulatedTimeSpentPerforming.clear();
        }
        this.accumulatedTooShortDuration = 0.0;
        this.timeSpentWaiting = 0.0;
        this.accumulatedNegativeDuration = 0.0;
    }

    public double getLateArrivalScore() {
        return (2 * this.params.marginalUtilityOfLateArrival_s * Math.abs(this.accumulatedNegativeDuration));
    }

    public Map<String, Double> getAccumulatedDurations() {
        return Collections.unmodifiableMap(this.accumulatedTimeSpentPerforming);
    }

    public Map<String, Double> getZeroUtilityDurations() {
        return Collections.unmodifiableMap(this.zeroUtilityDurations);
    }

    public double getAccumulatedTooShortDuration() {
        return accumulatedTooShortDuration;
    }

    public double getTimeSpentWaiting() {
        return timeSpentWaiting;
    }

    public double getAccumulatedNegativeDuration() {
        return accumulatedNegativeDuration;
    }

    public double getSign() {
        return sign;
    }

    public void setSign(double sign) {
        this.sign = sign;
    }

    public boolean isSizeScore() {
        return sizeScore;
    }

    public void setSizeScore(boolean sizeScore) {
        this.sizeScore = sizeScore;
    }

    public boolean isDensityScore() {
        return densityScore;
    }

    public void setDensityScore(boolean densityScore) {
        this.densityScore = densityScore;
    }

    public boolean isShoppingCentersScore() {
        return shoppingCentersScore;
    }

    public void setShoppingCentersScore(boolean shoppingCentersScore) {
        this.shoppingCentersScore = shoppingCentersScore;
    }

    public ShoppingScoreAdditionals getShoppingScoreAdditionals() {
        return shoppingScoreAdditionals;
    }

    public void setShoppingScoreAdditionals(ShoppingScoreAdditionals shoppingScoreAdditionals) {
        this.shoppingScoreAdditionals = shoppingScoreAdditionals;
    }
}
