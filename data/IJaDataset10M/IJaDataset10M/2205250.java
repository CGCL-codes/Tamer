package com.rapidminer.operator.preprocessing.transformation.aggregation;

/**
 * This is an {@link Aggregator} for the {@link VarianceAggregationFunction}
 * 
 * @author Sebastian Land
 */
public class StandardDeviationAggregator extends NumericalAggregator {

    private double valueSum = 0d;

    private double squaredValueSum = 0d;

    private double totalWeightSum = 0d;

    private double count = 0;

    public StandardDeviationAggregator(AggregationFunction function) {
        super(function);
    }

    @Override
    public void count(double value) {
        valueSum += value;
        squaredValueSum += value * value;
        totalWeightSum++;
        count++;
    }

    @Override
    public void count(double value, double weight) {
        valueSum += weight * value;
        squaredValueSum += weight * value * value;
        totalWeightSum += weight;
        count++;
    }

    @Override
    public double getValue() {
        if (count > 0) {
            double value = (squaredValueSum - valueSum * valueSum / totalWeightSum) / ((count - 1) / count * totalWeightSum);
            if (value > 0d) return Math.sqrt(value);
            return 0d;
        } else return Double.NaN;
    }
}
