package net.wsnware.filter.multilateration.models;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MathException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math.analysis.interpolation.UnivariateRealInterpolator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This model interpolates a set of given points [RSSI, Distance] which are
 * obtained sperimentally for each anchor.
 *
 * Interpolation is based on Apache Commons Math's SplineInterpolator.
 * 
 * @see     http://wsnware.sourceforge.net/docs/WSNWARE.Multilateration.pdf
 * @author  Alessandro Polo <contact@alessandropolo.name>
 * @version 1.0.0
 * @date    2011-05-26
 */
public class RssiModelSpline extends RssiModelTrained {

    public static class AnchorTrainingSpline extends RssiAnchorTrainingSet {

        protected UnivariateRealInterpolator interpolator = new SplineInterpolator();

        protected UnivariateRealFunction function;

        @Override
        public boolean finalizeTraining() {
            double[] v_rssi = new double[samples.size()];
            double[] v_distance = new double[samples.size()];
            int index = 0;
            for (Sample sample : samples) {
                v_rssi[index] = sample.rssi;
                if (logScale) v_distance[index] = Math.log10(sample.distance); else v_distance[index] = sample.distance;
                ++index;
            }
            try {
                function = interpolator.interpolate(v_rssi, v_distance);
            } catch (MathException ex) {
                Logger.getLogger(RssiModelSpline.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            return true;
        }

        @Override
        public double evaluate(double rssi) {
            if (function == null) return Double.NaN;
            try {
                if (logScale) return Math.pow(10, function.value(rssi)); else return function.value(rssi);
            } catch (FunctionEvaluationException ex) {
                Logger.getLogger(RssiModelSpline.class.getName()).log(Level.SEVERE, null, ex);
                return Double.NaN;
            }
        }
    }

    @Override
    protected RssiAnchorTrainingSet createAnchorTraining(long anchor_ID) {
        return new AnchorTrainingSpline();
    }

    protected static boolean logScale = false;
}
