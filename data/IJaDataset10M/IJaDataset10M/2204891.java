package org.vikamine.kernel.subgroup.quality;

import org.vikamine.kernel.subgroup.SGStatistics;
import org.vikamine.kernel.subgroup.SGStatisticsBinary;
import org.vikamine.kernel.subgroup.SGStatisticsForNumericTarget;

public class ParametrizedStandardQF extends AbstractSGQualityFunction {

    public ParametrizedStandardQF(double a) {
        super();
        this.a = a;
    }

    /** generated */
    private static final long serialVersionUID = -5538848634118538904L;

    private double a;

    public double getParameter() {
        return a;
    }

    public void setParameter(double a) {
        this.a = a;
    }

    @Override
    public String getName() {
        return "Standard quality function, a=" + a;
    }

    @Override
    public double evaluateContinuousIgnoringMinSupport(SGStatisticsForNumericTarget numStats) {
        return (numStats.getSGMean() - numStats.getPopulationMean()) * Math.pow(numStats.getSubgroupSize(), a);
    }

    @Override
    public double evaluateBinaryIgnoringMinSupport(SGStatisticsBinary statsBin) {
        return (statsBin.getP() - statsBin.getP0()) * Math.pow(statsBin.getSubgroupSize(), a);
    }

    @Override
    public boolean isApplicableForBinary() {
        return true;
    }

    @Override
    public boolean isApplicableForContinuous() {
        return true;
    }

    @Override
    public double estimateQuality(SGStatistics subgroupStatistics) {
        if (!(subgroupStatistics instanceof SGStatisticsBinary)) {
            throw new IllegalArgumentException("Not applicable for subgroups with numeric targets");
        }
        return Math.pow(((SGStatisticsBinary) subgroupStatistics).getTp(), a) * (1 - ((SGStatisticsBinary) subgroupStatistics).getP0());
    }

    @Override
    public double estimateQuality(double n, double tp, double totalPopulationSize, double p_0) {
        return Math.pow(tp, a) * (1 - p_0);
    }
}
