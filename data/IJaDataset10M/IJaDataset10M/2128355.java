package net.sourceforge.processdash.ev.ci;

import net.sourceforge.processdash.Settings;
import net.sourceforge.processdash.util.TDistribution;

public class LinearRegressionConfidenceInterval extends AbstractLinearConfidenceInterval {

    protected void calcBetaParameters() {
        double x_sum, y_sum, xx_sum, xy_sum;
        x_sum = y_sum = xx_sum = xy_sum = 0.0;
        for (int i = 0; i < numSamples; i++) {
            DataPoint p = getPoint(i);
            x_sum += p.x;
            y_sum += p.y;
            xy_sum += p.x * p.y;
            xx_sum += p.x * p.x;
        }
        double x_avg = x_sum / numSamples;
        double y_avg = y_sum / numSamples;
        beta1 = ((xy_sum - (numSamples * x_avg * y_avg)) / (xx_sum - (numSamples * x_avg * x_avg)));
        beta0 = y_avg - (beta1 * x_avg);
    }

    /** Heuristically estimate how viable this confidence interval appears
     * to be.
     */
    protected void calcViability() {
        double independentForecast = input * y_avg / x_avg;
        double range = Math.abs(projection - independentForecast);
        double stud_t = range / stddev / rangeRadical;
        double prob = 2 * Math.abs(TDistribution.cumulative(stud_t, numSamples - 2) - 0.5);
        double cutoff = Settings.getInt("linCI.cutoffProbability", 30) / 100.0;
        if (prob > cutoff) {
            viability = SERIOUS_PROBLEM;
        } else {
            viability = NOMINAL * (1 - prob);
        }
    }
}
