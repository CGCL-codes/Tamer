package net.sourceforge.ondex.ovtk2.annotator.timeseries;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import net.sourceforge.ondex.ovtk2.annotator.stats.StatsCalc;

/**
 * An index for a series of Conditions and timepoints
 * @author hindlem
 *
 */
public class TimeSeriesIndex {

    private HashSet<Integer> timepoints = new HashSet<Integer>();

    private HashSet<String> conditions = new HashSet<String>();

    private HashSet<String> targetSequences = new HashSet<String>();

    private Map<String, Map<String, Double[]>> conditionIndex;

    private Map<Integer, Map<String, Map<String, Double>>> timeIndex;

    private String experiment;

    private StatsCalc allValues = new StatsCalc();

    private Map<String, Map<String, StatsCalc>> statsOnTargetSeqCondition = new HashMap<String, Map<String, StatsCalc>>();

    /**
	 * 
	 * @param experement the experement name
	 * @param conditions optimization paramiter (usful for a large number of conditions)
	 */
    public TimeSeriesIndex(String experiment, int conditions) {
        this.experiment = experiment;
        conditionIndex = new HashMap<String, Map<String, Double[]>>(conditions);
        timeIndex = new HashMap<Integer, Map<String, Map<String, Double>>>();
    }

    /**
	 * The main construcor for this index
	 * @param experement
	 */
    public TimeSeriesIndex(String experement) {
        this(experement, 3);
    }

    /**
	 * 
	 * @param condition the condition e.g. species, cultivar, tissue
	 * @param sequenceName the target sequence or probe name
	 * @return the TimeSeries corresponing the to specifed paramiters, returns null if not existing
	 */
    public Double[] getTimeSeries(String condition, String sequenceName) {
        Map<String, Double[]> conditionMap = conditionIndex.get(condition);
        if (conditionMap != null) return conditionMap.get(sequenceName);
        return null;
    }

    /**
	 * 
	 * @param condition the condition e.g. species, cultivar, tissue
	 * @param sequenceName the target sequence or probe name
	 * @param timepoint the timepoint to get expression at
	 * @return the expression value
	 */
    public Double getExpression(String condition, String sequenceName, int timepoint) {
        Map<String, Double[]> conditionMap = conditionIndex.get(condition.toUpperCase());
        if (conditionMap != null) {
            Double[] series = conditionMap.get(sequenceName.toUpperCase());
            if (series != null && series.length > timepoint) {
                return series[timepoint];
            }
        }
        return null;
    }

    /**
	 * 
	 * @param condition the condition
	 * @param timepoint the timepoint to fetch
	 * @return sequence to expression value map
	 */
    public Map<String, Double> getTimepoint(String condition, int timepoint) {
        Map<String, Map<String, Double>> timeMap = timeIndex.get(Integer.valueOf(timepoint));
        if (timeMap != null) {
            if (timeMap.get(condition.toUpperCase()) == null) {
                System.err.println("unknown condition " + condition);
                Iterator<String> conditions = timeMap.keySet().iterator();
                while (conditions.hasNext()) {
                    System.err.println("c==" + conditions.next());
                }
            }
            return timeMap.get(condition.toUpperCase());
        } else {
            System.err.println("unknown timepoint " + timepoint);
        }
        return null;
    }

    /**
	 * Adds a new TimeSeries point to the index
	 * @param condition the condition e.g. species, cultivar, tissue
	 * @param sequenceName the target sequence or probe name
	 * @param ts The time series data point
	 * @param sequences optimization paramiter
	 */
    public void addTimeSeries(String condition, String sequenceName, Double[] ts) {
        addTimeSeries(condition.toUpperCase(), sequenceName.toUpperCase(), ts, 10000);
    }

    /**
	 * Adds a new TimeSeries point to the index (will ignore null ts values)
	 * @param condition the condition e.g. species, cultivar, tissue
	 * @param sequenceName the target sequence or probe name
	 * @param ts The time series data point
	 * @param sequences optimization paramiter
	 */
    public void addTimeSeries(String condition, String sequenceName, Double[] ts, int sequences) {
        targetSequences.add(sequenceName.toUpperCase());
        conditions.add(condition.toUpperCase());
        Map<String, Double[]> conditionMap = conditionIndex.get(condition.toUpperCase());
        if (conditionMap == null) {
            conditionMap = new HashMap<String, Double[]>(sequences);
            conditionIndex.put(condition.toUpperCase(), conditionMap);
        }
        conditionMap.put(sequenceName.toUpperCase(), ts);
        for (int i = 0; i < ts.length; i++) {
            Integer timepoint = Integer.valueOf(i);
            Map<String, Map<String, Double>> timeMap = timeIndex.get(timepoint);
            if (timeMap == null) {
                timeMap = new HashMap<String, Map<String, Double>>();
                timeIndex.put(timepoint, timeMap);
            }
            Map<String, Double> sequenceMap = timeMap.get(condition.toUpperCase());
            if (sequenceMap == null) {
                sequenceMap = new HashMap<String, Double>();
                timeMap.put(condition.toUpperCase(), sequenceMap);
            }
            if (ts[i] != null) {
                sequenceMap.put(sequenceName.toUpperCase(), ts[i]);
                timepoints.add(i);
            }
        }
        Map<String, StatsCalc> seqMap = statsOnTargetSeqCondition.get(condition.toUpperCase());
        if (seqMap == null) {
            seqMap = new HashMap<String, StatsCalc>(sequences);
            statsOnTargetSeqCondition.put(condition.toUpperCase(), seqMap);
        }
        StatsCalc sequence = seqMap.get(sequenceName.toUpperCase());
        if (sequence == null) {
            sequence = new StatsCalc();
            seqMap.put(sequenceName.toUpperCase(), sequence);
        }
        for (Double te : ts) {
            if (te != null) {
                allValues.enter(te);
                sequence.enter(te);
            }
        }
    }

    /**
	 * 
	 * @return the name of the exeriment
	 */
    public String getExperiment() {
        return experiment;
    }

    public StatsCalc getGeneralStats() {
        return allValues;
    }

    public HashSet<Integer> getTimepoints() {
        return timepoints;
    }

    public HashSet<String> getConditions() {
        return conditions;
    }

    public HashSet<String> getTargetSequences() {
        return targetSequences;
    }
}
