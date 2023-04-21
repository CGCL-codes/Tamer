package org.vikamine.kernel.subgroup.quality;

import org.vikamine.kernel.subgroup.SGStatistics;

public interface IQualityFunctionStatisticBased extends IQualityFunction {

    public double evaluate(SGStatistics statistics);
}
