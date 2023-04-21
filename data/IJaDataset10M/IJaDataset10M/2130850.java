package org.vikamine.kernel.subgroup;

import org.vikamine.kernel.subgroup.target.NumericTarget;

/**
 * Builder for {@link SGStatistics}s: Binary, Numeric with/without recalculation
 * on the dataset.
 * 
 * @author mat
 * 
 */
public class SGStatisticsBuilder {

    public static SGStatistics buildSGStatisticsWithCalculation(SG subgroup, Options constraints) {
        if (subgroup.getTarget() instanceof NumericTarget) {
            return new SGStatisticsNumeric(subgroup, constraints);
        } else {
            return new SGStatisticsBinary(subgroup, constraints);
        }
    }

    public static SGStatistics buildSGStatisticsWithoutCalculation(SG subgroup) {
        if (subgroup.getTarget() instanceof NumericTarget) {
            return new SGStatisticsNumeric(subgroup);
        } else {
            return new SGStatisticsBinary(subgroup);
        }
    }

    public static SGStatisticsBinary createFixedSGStatisticsBinaryWithNullConstraints(SG subgroup, double definedPopulationCount, double undefinedPopulationCount, double positives, double tp, double subgroupSize) {
        if (!subgroup.getTarget().isBoolean()) {
            throw new IllegalArgumentException("Not applicable for subgroups with numeric targets");
        }
        SGStatisticsBinary stats = (SGStatisticsBinary) buildSGStatisticsWithoutCalculation(subgroup);
        stats.definedPopulationCount = definedPopulationCount;
        stats.undefinedPopulationCount = undefinedPopulationCount;
        stats.positives = positives;
        stats.negatives = definedPopulationCount - positives;
        stats.subgroupSize = subgroupSize;
        stats.tp = tp;
        stats.fp = subgroupSize - tp;
        stats.options = null;
        return stats;
    }

    public static SGStatisticsNumeric createSGStatisticsNumeric(SG subgroup, double sgSumOfValues, double popMean, double sgSize, double popSize) {
        SGStatisticsNumeric stats = new SGStatisticsNumeric(subgroup);
        stats.sumOfTargetValuesSG = sgSumOfValues;
        if (sgSize == 0) {
            stats.sgMean = 0;
        } else {
            stats.sgMean = sgSumOfValues / sgSize;
        }
        stats.populationMean = popMean;
        stats.subgroupSize = sgSize;
        stats.definedPopulationCount = popSize;
        subgroup.setStatistics(stats);
        return stats;
    }

    public static SGStatistics createSGStatistics(SG subgroup, double tp, double fp, double pos, double neg, double totalPopulationSize, int descriptionLength, Options options) {
        if (!subgroup.getTarget().isBoolean()) {
            throw new IllegalArgumentException("Not applicable for subgroups with numeric targets");
        }
        SGStatistics stats = buildSGStatisticsWithoutCalculation(subgroup);
        stats.options = options;
        stats.descriptionLength = descriptionLength;
        stats.definedPopulationCount = pos + neg;
        stats.undefinedPopulationCount = totalPopulationSize - stats.definedPopulationCount;
        if (subgroup.getTarget().isBoolean()) {
            SGStatisticsBinary binStats = (SGStatisticsBinary) stats;
            binStats.positives = pos;
            binStats.negatives = neg;
            stats.subgroupSize = tp + fp;
            binStats.tp = tp;
            binStats.fp = fp;
        }
        subgroup.setStatistics(stats);
        return stats;
    }

    public static void updateStatistics(SG subgroup, SGStatistics statistics) {
        subgroup.setStatistics(statistics);
    }
}
