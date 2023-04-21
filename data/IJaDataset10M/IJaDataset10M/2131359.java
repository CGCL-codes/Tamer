package org.edits.engines;

import org.edits.definition.EditsOption;
import org.edits.definition.ModuleDefinition;
import org.edits.etaf.Truth;

/**
 * @author Milen Kouylekov
 */
public class TrainingOptions {

    public static final String OPTIMIZE = "optimize";

    public static final String USE_TASK = "use_task";

    private boolean accuracy;

    private boolean defaultOptimize = true;

    private String optimize = "accuracy";

    private boolean precision;

    private boolean recall;

    private Truth relation;

    private boolean useTask = false;

    public boolean accuracy() {
        return accuracy;
    }

    @Override
    public TrainingOptions clone() {
        TrainingOptions to = new TrainingOptions();
        to.setOptimizePerTask(useTask);
        to.setOptimize(optimize);
        return to;
    }

    public String getOptimize() {
        return optimize;
    }

    public boolean isDefaultOptimize() {
        return defaultOptimize;
    }

    public boolean isOptimizePerTask() {
        return useTask;
    }

    public Truth optimizeOnRelation() {
        if (optimize.length() == 1) return Truth.YES;
        if (optimize.endsWith("y")) return Truth.YES;
        if (optimize.endsWith("n")) return Truth.NO;
        if (optimize.endsWith("e")) return Truth.ENTAILMENT;
        if (optimize.endsWith("u")) return Truth.UNKNOWN;
        return Truth.CONTRADICTION;
    }

    public boolean precision() {
        return precision;
    }

    public void readRuntimeOptions(ModuleDefinition e) {
        EditsOption s = e.option(OPTIMIZE);
        defaultOptimize = s.isDefaultValue();
        optimize = s != null ? s.getValue().toLowerCase() : "a";
        readOptiomize();
        s = e.option(USE_TASK);
        useTask = s != null && Boolean.parseBoolean(s.getValue());
    }

    public boolean recall() {
        return recall;
    }

    public Truth relation() {
        if (relation == null) relation = optimizeOnRelation();
        return relation;
    }

    public void setOptimize(String optimizePerMetric) {
        optimize = optimizePerMetric;
        readOptiomize();
    }

    public void setOptimizePerTask(boolean optimizePerTask) {
        useTask = optimizePerTask;
    }

    private void readOptiomize() {
        accuracy = optimize.equals("a");
        precision = optimize.startsWith("p");
        recall = optimize.startsWith("r");
        relation = optimizeOnRelation();
    }
}
