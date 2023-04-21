package playground.johannes.socialnetworks.statistics;

import org.apache.commons.math.stat.descriptive.UnivariateStatistic;
import playground.johannes.sna.math.UnivariatePiStatistic;

/**
 * @author illenberger
 *
 */
public class HorwitzThompsonEstimator implements UnivariatePiStatistic {

    private final double N;

    private double[] piValues;

    public HorwitzThompsonEstimator(double N) {
        this.N = N;
    }

    @Override
    public void setPiValues(double[] piValues) {
        this.piValues = piValues;
    }

    @Override
    public UnivariateStatistic copy() {
        HorwitzThompsonEstimator ht = new HorwitzThompsonEstimator(N);
        ht.setPiValues(piValues);
        return ht;
    }

    @Override
    public double evaluate(double[] values) {
        return evaluate(values, 0, values.length);
    }

    @Override
    public double evaluate(double[] values, int begin, int length) {
        double sum = 0;
        for (int i = begin; i < (begin + length); i++) {
            sum += values[i] / piValues[i];
        }
        return sum / N;
    }
}
