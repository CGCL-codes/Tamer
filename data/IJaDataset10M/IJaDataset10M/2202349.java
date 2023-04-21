package org.vikamine.kernel.subgroup.quality.functions;

import org.vikamine.kernel.subgroup.SG;
import org.vikamine.kernel.subgroup.quality.AbstractQFSimpleStatisticBased;

public class FMeasureQF extends AbstractQFSimpleStatisticBased {

    private static final String NAME = "F-Measure";

    double param;

    public FMeasureQF() {
        this(1.0);
    }

    public FMeasureQF(double param) {
        super();
        this.param = param;
    }

    public static final PrecisionQF PRECISION_QF = new PrecisionQF();

    public static final RecallQF RECALL_QF = new RecallQF();

    @Override
    public String getID() {
        return "FMeasureQF(" + param + ")";
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public double evaluateNum(double subgroupSize, double subgroupMean, double definedPopulationCount, double populationMean) {
        return Double.NaN;
    }

    @Override
    public double evaluateBin(double subgroupSize, double subgroupPositives, double definedPopulationCount, double populationPositives) {
        double precision = PRECISION_QF.evaluateBin(subgroupSize, subgroupPositives, definedPopulationCount, populationPositives);
        double recall = RECALL_QF.evaluateBin(subgroupSize, subgroupPositives, definedPopulationCount, populationPositives);
        return ((1 + param * param) * (precision * recall)) / (param * param * precision + recall);
    }

    @Override
    public AbstractQFSimpleStatisticBased clone() {
        return new FMeasureQF(param);
    }

    @Override
    public boolean isApplicable(SG subgroup) {
        return (subgroup.getTarget() != null) && (subgroup.getTarget().isBoolean());
    }
}
