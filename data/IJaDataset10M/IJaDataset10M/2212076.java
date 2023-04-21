package org.vikamine.kernel.subgroup.quality.functions;

import org.vikamine.kernel.subgroup.SG;
import org.vikamine.kernel.subgroup.quality.AbstractQFSimpleStatisticBased;

/**
 * Implements the Lift quality function, i.e., (targetShare/populationShare),
 * (mean/populationMean), respectively.
 * 
 * @author atzmueller
 * 
 */
public class LiftQF extends AbstractQFSimpleStatisticBased {

    private static final String ID = "LiftQF";

    private static final String NAME = "Lift";

    public static double computeLiftNumeric(double subgroupMean, double populationMean) {
        return (subgroupMean / populationMean);
    }

    public static double computeLiftBinary(double subgroupSize, double subgroupPositives, double definedPopulationCount, double populationPositives) {
        return (subgroupPositives / subgroupSize) / (populationPositives / definedPopulationCount);
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public double evaluateNum(double subgroupSize, double subgroupMean, double definedPopulationCount, double populationMean) {
        return computeLiftNumeric(subgroupMean, populationMean);
    }

    @Override
    public double evaluateBin(double subgroupSize, double subgroupPositives, double definedPopulationCount, double populationPositives) {
        return computeLiftBinary(subgroupSize, subgroupPositives, definedPopulationCount, populationPositives);
    }

    @Override
    public AbstractQFSimpleStatisticBased clone() {
        return new LiftQF();
    }

    @Override
    public boolean isApplicable(SG subgroup) {
        return true;
    }
}
