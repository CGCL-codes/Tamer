package eu.fbk.hlt.edits.engines;

import java.util.ArrayList;
import java.util.List;
import eu.fbk.hlt.common.conffile.Type;
import eu.fbk.hlt.common.module.OptionInfo;
import eu.fbk.hlt.edits.EDITS;
import eu.fbk.hlt.edits.etaf.Truth;

/**
 * @author Milen Kouylekov
 */
public class TrainingOptions {

    public enum CrossValidation {

        Best, Combine
    }

    public enum Optimize {

        Accuracy, FMeasure, Precision, Recall
    }

    public static final String CROSS_VALIDATION_OPTION = "cross-validation";

    public static final String FOLDS_OPTION = "folds";

    public static final String OPTIMIZE_PARAMETERS_OPTION = "optimize-parameters";

    public static final String OPTIMIZE_PER_METRIC_OPTION = "optimize-per-metric";

    public static final String OPTIMIZE_PER_RELATION_OPTION = "optimize-per-relation";

    public static final String OPTIMIZE_PER_TASK_OPTION = "optimize-per-task";

    public static final String TRAIN_SUB_ENGINES_OPTION = "train-sub-engines";

    private CrossValidation crossValidation = null;

    private int folds = 10;

    private boolean optimizeParameters = false;

    private Optimize optimizePerMetric = Optimize.Accuracy;

    private Truth optimizePerRelation = Truth.YES;

    private boolean optimizePerTask = false;

    private boolean trainSubEngines = false;

    @Override
    public TrainingOptions clone() {
        TrainingOptions to = new TrainingOptions();
        to.setCrossValidation(crossValidation);
        to.setFolds(folds);
        to.setOptimizePerMetric(optimizePerMetric);
        to.setOptimizePerRelation(optimizePerRelation);
        to.setOptimizePerTask(optimizePerTask);
        return to;
    }

    public CrossValidation getCrossValidation() {
        return crossValidation;
    }

    public int getFolds() {
        return folds;
    }

    public Optimize getOptimizePerMetric() {
        return optimizePerMetric;
    }

    public Truth getOptimizePerRelation() {
        return optimizePerRelation;
    }

    public boolean isOptimizeParameters() {
        return optimizeParameters;
    }

    public boolean isOptimizePerTask() {
        return optimizePerTask;
    }

    public boolean isTrainSubEngines() {
        return trainSubEngines;
    }

    public void readRuntimeOptions(EntailmentEngine e) {
        if (e.option(OPTIMIZE_PER_METRIC_OPTION) != null) optimizePerMetric = parseOptimize(e.option(OPTIMIZE_PER_METRIC_OPTION));
        if (e.option(OPTIMIZE_PER_RELATION_OPTION) != null) optimizePerRelation = parseTruth(e.option(OPTIMIZE_PER_RELATION_OPTION));
        String s = e.option(OPTIMIZE_PER_TASK_OPTION);
        optimizePerTask = s != null && Boolean.parseBoolean(s);
        s = e.option(OPTIMIZE_PARAMETERS_OPTION);
        optimizeParameters = s != null && Boolean.parseBoolean(s);
        s = e.option(TRAIN_SUB_ENGINES_OPTION);
        trainSubEngines = s != null && Boolean.parseBoolean(s);
        String value = e.option(CROSS_VALIDATION_OPTION);
        crossValidation = null;
        if (value != null) {
            if (value.equalsIgnoreCase(CrossValidation.Best.toString())) crossValidation = CrossValidation.Best; else crossValidation = CrossValidation.Combine;
        }
        try {
            if (e.option(FOLDS_OPTION) != null) folds = (int) Double.parseDouble(e.option(FOLDS_OPTION));
        } catch (Exception ex) {
        }
    }

    public void setCrossValidation(CrossValidation crossValidation) {
        this.crossValidation = crossValidation;
    }

    public void setFolds(int folds) {
        this.folds = folds;
    }

    public void setOptimizeParameters(boolean optimizeParameters) {
        this.optimizeParameters = optimizeParameters;
    }

    public void setOptimizePerMetric(Optimize optimizePerMetric) {
        this.optimizePerMetric = optimizePerMetric;
    }

    public void setOptimizePerRelation(Truth optimizePerRelation) {
        this.optimizePerRelation = optimizePerRelation;
    }

    public void setOptimizePerTask(boolean optimizePerTask) {
        this.optimizePerTask = optimizePerTask;
    }

    public void setTrainSubEngines(boolean trainSubEngines) {
        this.trainSubEngines = trainSubEngines;
    }

    public static List<OptionInfo> options() {
        List<OptionInfo> oout = new ArrayList<OptionInfo>();
        OptionInfo o = null;
        o = new OptionInfo();
        o.setName(CROSS_VALIDATION_OPTION);
        o.setAbbreviation("cv");
        o.setMultiple(false);
        o.setRequired(false);
        o.setId(false);
        o.setType(Type.ENUMERATED);
        o.context().add(EntailmentEngineHandler.TRAIN_COMMAND);
        o.setDescription(EDITS.system().description(CROSS_VALIDATION_OPTION));
        for (CrossValidation l : CrossValidation.values()) o.values().add(l.toString());
        oout.add(o);
        o = new OptionInfo();
        o.setName(FOLDS_OPTION);
        o.setAbbreviation("fd");
        o.setMultiple(false);
        o.setRequired(false);
        o.setId(false);
        o.setDefault("10");
        o.setType(Type.NUMBER);
        o.context().add(EntailmentEngineHandler.TRAIN_COMMAND);
        o.setDescription(EDITS.system().description(FOLDS_OPTION));
        oout.add(o);
        o = new OptionInfo();
        o.setName(OPTIMIZE_PER_TASK_OPTION);
        o.setAbbreviation("opt");
        o.setType(Type.BOOLEAN);
        o.setMultiple(false);
        o.setRequired(false);
        o.setId(false);
        o.setDefault("false");
        o.context().add(EntailmentEngineHandler.TRAIN_COMMAND);
        o.setDescription(EDITS.system().description(OPTIMIZE_PER_TASK_OPTION));
        oout.add(o);
        o = new OptionInfo();
        o.setName(TRAIN_SUB_ENGINES_OPTION);
        o.setAbbreviation("tse");
        o.setType(Type.BOOLEAN);
        o.setMultiple(false);
        o.setRequired(false);
        o.setId(false);
        o.setDefault("false");
        o.context().add(EntailmentEngineHandler.TRAIN_COMMAND);
        o.setDescription(EDITS.system().description(TRAIN_SUB_ENGINES_OPTION));
        oout.add(o);
        o = new OptionInfo();
        o.setAbbreviation("opm");
        o.setName(OPTIMIZE_PER_METRIC_OPTION);
        o.setMultiple(false);
        o.setRequired(false);
        o.setId(false);
        o.setDefault(Optimize.Accuracy.toString());
        o.setType(Type.ENUMERATED);
        o.context().add(EntailmentEngineHandler.TRAIN_COMMAND);
        o.setDescription(EDITS.system().description(OPTIMIZE_PER_METRIC_OPTION));
        for (Optimize l : Optimize.values()) o.values().add(l.toString());
        oout.add(o);
        o = new OptionInfo();
        o.setAbbreviation("opc");
        o.setName(OPTIMIZE_PER_RELATION_OPTION);
        o.setType(Type.ENUMERATED);
        o.setMultiple(false);
        o.setRequired(false);
        o.setId(false);
        o.setDefault(Truth.ENTAILMENT.toString());
        o.context().add(EntailmentEngineHandler.TRAIN_COMMAND);
        o.setDescription(EDITS.system().description(OPTIMIZE_PER_RELATION_OPTION));
        for (Truth l : Truth.values()) o.values().add(l.toString());
        oout.add(o);
        o = new OptionInfo();
        o.setName(OPTIMIZE_PARAMETERS_OPTION);
        o.setAbbreviation("op");
        o.setType(Type.BOOLEAN);
        o.setMultiple(false);
        o.setRequired(false);
        o.setId(false);
        o.setDefault("false");
        o.context().add(EntailmentEngineHandler.TRAIN_COMMAND);
        o.setDescription(EDITS.system().description(OPTIMIZE_PARAMETERS_OPTION));
        oout.add(o);
        return oout;
    }

    public static Optimize parseOptimize(String str) {
        if (str == null) return Optimize.Accuracy;
        for (Optimize ov : Optimize.values()) {
            if (ov.toString().equalsIgnoreCase(str)) return ov;
        }
        return Optimize.Accuracy;
    }

    public static Truth parseTruth(String str) {
        if (str == null) return Truth.ENTAILMENT;
        for (Truth ov : Truth.values()) {
            if (ov.toString().equalsIgnoreCase(str)) return ov;
        }
        return Truth.ENTAILMENT;
    }
}
