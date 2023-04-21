package engine.distribution.master.statistics;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for holding statistics data about single iteration.
 * @author Karol Stosiek (karol.stosiek@gmail.com)
 * @author Michal Anglart (anglart.michal@gmail.com)
 */
public class IterationStatistics {

    /** Counter name. Holds this iteration number. */
    public static final String ITERATION_NUMBER = "iteration_number";

    /** Counter name. Holds size of population at iteration begin. */
    public static final String POPULATION_SIZE = "population_size";

    /** Counter name. Holds size of population (bytes) at iteration begin. */
    public static final String POPULATION_SIZE_IN_BYTES = "population_size_in_bytes";

    /** Counter name. Holds number of slaves in this iteration. */
    public static final String SLAVES_NUMBER = "slaves_number";

    /** Counter name. Holds value of best individual in population. */
    public static final String BEST_INDIVIDUAL_VALUE = "best_individual_value";

    /** Counter name. Holds value of worst individual in population. */
    public static final String WORST_INDIVIDUAL_VALUE = "worst_individual_value";

    /** Counter name. Holds value of average individual in population. */
    public static final String AVERAGE_INDIVIDUAL_VALUE = "average_individual_value";

    /** Counter name. Holds value of standard deviation amoung individuals. */
    public static final String STD_DEVIATION_INDIVIDUAL_VALUE = "std_deviation_individual_value";

    /** Counter name. Holds number of seconds spent in this iteration. */
    public static final String TIME_SPENT_IN_SECONDS = "time_spent_in_seconds";

    /** Exact time when iteration started. */
    private GregorianCalendar iterationStartTime;

    /** Exact time when iteration ended. */
    private GregorianCalendar iterationEndTime;

    /** Map which holds double counters. */
    private Map<String, Double> counters;

    /**
     * Constructor. Package-visible for testing purposes.
     * @param iterationStartTime Time when iteration started.
     * @param iterationEndTime Time when iteration ended.
     * @param counters Map with counters.
     */
    IterationStatistics(GregorianCalendar iterationStartTime, GregorianCalendar iterationEndTime, Map<String, Double> counters) {
        this.iterationStartTime = iterationStartTime;
        this.iterationEndTime = iterationEndTime;
        this.counters = counters;
    }

    /** Constructor. Sets default counters. */
    public IterationStatistics() {
        this(null, null, new HashMap<String, Double>());
        setupDefaultCounters();
    }

    /**
     * Getter for counters. 
     * @return Map with all counters.
     */
    public Map<String, Double> getCounters() {
        return Collections.unmodifiableMap(counters);
    }

    /** 
     * Sets iteration start time. This method should be 
     * called at start of the new iteration.
     */
    public void setIterationStartTime() {
        iterationStartTime = new GregorianCalendar();
    }

    /** 
     * Gets iteration start time.
     * @return Time when iteration started.
     */
    public GregorianCalendar getIterationStartTime() {
        return iterationStartTime;
    }

    /**
     * Sets iteration end time. This method should be
     * called at end of the iteration. Also updates 
     * value of TIME_SPENT_IN_SECONDS counter.
     */
    public void setIterationEndTime() {
        iterationEndTime = new GregorianCalendar();
    }

    /** 
     * Gets iteration end time.
     * @return Time when iteration ended.
     */
    public GregorianCalendar getIterationEndTime() {
        return iterationEndTime;
    }

    /**
     * Adds new counter. Nothing happens if counter of given
     * name was declared previously.
     * @param counterName Name of counter. 
     * @param value Start value of counter.
     */
    public void addCounterValue(String counterName, double value) {
        counters.put(counterName, value);
    }

    /**
     * Gets value of specified counter.
     * @param counterName Name of counter.
     * @return Counter's value.
     */
    public double getCounterValue(String counterName) {
        return counters.get(counterName);
    }

    /** Setups default counters. */
    private void setupDefaultCounters() {
        addCounterValue(ITERATION_NUMBER, 0.0);
        addCounterValue(POPULATION_SIZE, 0.0);
        addCounterValue(POPULATION_SIZE_IN_BYTES, 0.0);
        addCounterValue(SLAVES_NUMBER, 0.0);
        addCounterValue(BEST_INDIVIDUAL_VALUE, 0.0);
        addCounterValue(WORST_INDIVIDUAL_VALUE, 0.0);
        addCounterValue(AVERAGE_INDIVIDUAL_VALUE, 0.0);
        addCounterValue(STD_DEVIATION_INDIVIDUAL_VALUE, 0.0);
        addCounterValue(TIME_SPENT_IN_SECONDS, 0.0);
    }
}
