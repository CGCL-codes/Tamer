package org.progeeks.extract;

import java.util.*;

/**
 *  RuleListener implementation that collects statistics
 *  about rule execution.
 *
 *  @version   $Revision: 3860 $
 *  @author    Paul Speed
 */
public class StatisticsListener implements RuleListener {

    private Map<Rule, Statistics> statsMap = new HashMap<Rule, Statistics>();

    public StatisticsListener() {
    }

    public Map<Rule, Statistics> getStatsMap() {
        return statsMap;
    }

    protected Statistics createStats(Rule rule) {
        return new Statistics(rule);
    }

    protected Statistics getStats(Rule rule, boolean create) {
        Statistics stats = statsMap.get(rule);
        if (stats == null && create) {
            stats = createStats(rule);
            statsMap.put(rule, stats);
        }
        return stats;
    }

    public void ruleExecuted(Rule rule, DataElement target, Object input, Object output) {
        Statistics stats = getStats(rule, true);
        stats.addStats(target, input, output);
    }

    public static class Statistics {

        private Rule rule;

        private int execCount;

        private int inputCount;

        private int outputCount;

        public Statistics(Rule rule) {
            this.rule = rule;
        }

        public Rule getRule() {
            return rule;
        }

        public int getExecutionCount() {
            return execCount;
        }

        public int getInputCount() {
            return inputCount;
        }

        public int getOutputCount() {
            return outputCount;
        }

        protected int getSize(Object o) {
            if (o instanceof Collection) return ((Collection) o).size();
            return 1;
        }

        public void addStats(DataElement target, Object input, Object output) {
            execCount++;
            inputCount += getSize(input);
            outputCount += getSize(output);
        }

        public String toString() {
            return rule.getName() + " x " + execCount + ":" + inputCount + " -> " + outputCount;
        }
    }
}
