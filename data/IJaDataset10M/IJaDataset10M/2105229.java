package org.ascape.model.rule;

import org.ascape.model.Agent;
import org.ascape.model.Scape;
import org.ascape.util.data.StatCollector;

/**
 * A rule for gathering values (typically aggegate statistics) from the entire
 * population of agents.
 * 
 * @author Miles Parker
 * @version 1.0.1
 * @since 1.0
 */
public class CollectStats extends Rule {

    /**
     * The stats to be collected.
     */
    private StatCollector[] stats = new StatCollector[0];

    /**
     * The stats to be collected automatically.
     */
    private StatCollector[] autoSeriesStatCollectors = new StatCollector[0];

    /**
     * The stats to be collected automatically.
     */
    private StatCollector[] autoSeriesStatCollectors2 = new StatCollector[0];

    /**
     * The phase.
     */
    private int phase = 1;

    /**
     * Constructs a new stat collecting rule.
     */
    public CollectStats() {
        super("Collect Statistics");
    }

    /**
     * Constructs a new stat collecting rule. This constructor is prvided to
     * make it easier to build static stat collectors.
     * 
     * @param statCollectors
     *            the stats to use
     */
    public CollectStats(StatCollector[] statCollectors) {
        super("Collect Statistics");
        addStatCollectors(statCollectors);
    }

    /**
     * Returns the stats used to collect values from the scape.
     * 
     * @return the stat collectors
     */
    public StatCollector[] getStatCollectors() {
        return stats;
    }

    /**
     * Adds value stats to the collection rule. Typically not called directly,
     * but through Scape. Only adds stats that have autoCollect true.
     * 
     * @param addDataPoints
     *            the stats to add
     * @see Scape#addStatCollectors
     * @see org.ascape.util.data.StatCollector#isAutoCollect
     */
    public void addStatCollectors(StatCollector[] addDataPoints) {
        int newSize = autoSeriesStatCollectors.length;
        int newSize2 = autoSeriesStatCollectors2.length;
        for (int i = 0; i < addDataPoints.length; i++) {
            if (addDataPoints[i].isAutoCollect() && (!addDataPoints[i].isPhase2())) {
                newSize++;
            }
            if (addDataPoints[i].isAutoCollect() && (addDataPoints[i].isPhase2())) {
                newSize2++;
            }
        }
        StatCollector[] newDataPoints = new StatCollector[stats.length + addDataPoints.length];
        int i = 0;
        for (; i < stats.length; i++) {
            newDataPoints[i] = stats[i];
        }
        for (; i < newDataPoints.length; i++) {
            newDataPoints[i] = addDataPoints[i - stats.length];
        }
        stats = newDataPoints;
        StatCollector[] newAutoDataPoints = new StatCollector[newSize];
        StatCollector[] newAutoDataPoints2 = new StatCollector[newSize2];
        for (i = 0; i < autoSeriesStatCollectors.length; i++) {
            newAutoDataPoints[i] = autoSeriesStatCollectors[i];
        }
        int addIndex = i;
        for (i = 0; i < addDataPoints.length; i++) {
            if (addDataPoints[i].isAutoCollect() && (!(addDataPoints[i].isPhase2()))) {
                newAutoDataPoints[addIndex] = addDataPoints[i];
                addIndex++;
            }
        }
        autoSeriesStatCollectors = newAutoDataPoints;
        System.arraycopy(autoSeriesStatCollectors2, 0, newAutoDataPoints2, 0, autoSeriesStatCollectors2.length);
        addIndex = autoSeriesStatCollectors2.length;
        for (i = 0; i < addDataPoints.length; i++) {
            if (addDataPoints[i].isAutoCollect() && (addDataPoints[i].isPhase2())) {
                newAutoDataPoints2[addIndex] = addDataPoints[i];
                addIndex++;
            }
        }
        autoSeriesStatCollectors2 = newAutoDataPoints2;
    }

    /**
     * Adds value stats to the collection rule. Typically not called directly.
     * 
     * @param addDataPoint
     *            the add data point
     */
    public void addStatCollector(StatCollector addDataPoint) {
        StatCollector[] tempStats = new StatCollector[1];
        tempStats[0] = addDataPoint;
        addStatCollectors(tempStats);
    }

    /**
     * Removes any and all existing stat collectors from the rule. Note that
     * this should be called when used for example with a static rule to ensure
     * that a stat isn't added to that rule twice. This code will be reworked,
     * probably to prevent the insertion of non-unique stat collectors.
     */
    public void removeAllStatCollectors() {
        stats = new StatCollector[0];
        autoSeriesStatCollectors = new StatCollector[0];
        autoSeriesStatCollectors2 = new StatCollector[0];
    }

    /**
     * Removes the stat collector.
     * 
     * @param toBeRemoved
     *            the to be removed
     * @return true, if successful
     */
    public boolean removeStatCollector(StatCollector toBeRemoved) {
        StatCollector[] temp = new StatCollector[stats.length - 1];
        boolean found = false;
        for (int i = 0, j = 0; i < stats.length; i++) {
            StatCollector statCollector = stats[i];
            if (statCollector.equals(toBeRemoved)) {
                found = true;
            } else {
                temp[j++] = stats[i];
            }
        }
        stats = temp;
        StatCollector[] tempAuto;
        StatCollector[] removeFrom = null;
        boolean foundAuto = true;
        if (toBeRemoved.isAutoCollect()) {
            foundAuto = false;
            if (toBeRemoved.isPhase2()) {
                tempAuto = new StatCollector[autoSeriesStatCollectors2.length - 1];
                removeFrom = autoSeriesStatCollectors2;
            } else {
                tempAuto = new StatCollector[autoSeriesStatCollectors.length - 1];
                removeFrom = autoSeriesStatCollectors;
            }
            for (int i = 0, j = 0; i < removeFrom.length; i++) {
                StatCollector statCollector = removeFrom[i];
                if (statCollector.equals(toBeRemoved)) {
                    foundAuto = true;
                } else {
                    tempAuto[j++] = removeFrom[i];
                }
            }
            if (toBeRemoved.isPhase2()) {
                autoSeriesStatCollectors2 = tempAuto;
            } else {
                autoSeriesStatCollectors = tempAuto;
            }
        }
        if (found == false) {
            throw new RuntimeException("Tried to remove a non-existant StatCollector: " + toBeRemoved);
        }
        if (foundAuto == false) {
            throw new RuntimeException("Tried to remove a non-existant StatCollector from autoCollect: " + toBeRemoved);
        }
        return found;
    }

    /**
     * Clears the values so that they can be collected for the next iteration.
     */
    public void clear() {
        for (int i = 0; i < stats.length; i++) {
            stats[i].clear();
        }
    }

    /**
     * Clears the values so that they can be collected for the next iteration.
     */
    public void calculateValues() {
        for (int i = 0; i < stats.length; i++) {
            if (stats[i].isCalculated()) {
                stats[i].addValue(stats[i].calculateValue());
            }
        }
    }

    /**
     * Collects all values for the agent.
     * 
     * @param agent
     *            the target agent.
     */
    public void execute(Agent agent) {
        if (phase == 1) {
            for (int i = 0; i < autoSeriesStatCollectors.length; i++) {
                autoSeriesStatCollectors[i].addValueFor(agent);
            }
        } else if (phase == 2) {
            for (int i = 0; i < autoSeriesStatCollectors2.length; i++) {
                autoSeriesStatCollectors2[i].addValueFor(agent);
            }
        }
    }

    /**
     * Sets the phase.
     * 
     * @param phase
     *            the new phase
     */
    public void setPhase(int phase) {
        this.phase = phase;
    }

    /**
     * Returns false; it doesn't matter what order we collect statistics in.
     * 
     * @return true, if is random execution
     */
    public boolean isRandomExecution() {
        return false;
    }

    /**
     * Returns true.
     * 
     * @return true, if is iterate all
     */
    public boolean isIterateAll() {
        return true;
    }

    public boolean isCauseRemoval() {
        return false;
    }
}
