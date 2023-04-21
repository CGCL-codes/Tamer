package com.dukesoftware.utils.solve;

import com.dukesoftware.utils.math.MathUtils;
import com.dukesoftware.utils.math.function.TypedArgFunction;
import com.dukesoftware.utils.math.function.TypedMinimizer;

public class AnnealingOptimizer implements TypedMinimizer<double[]> {

    private double t;

    private int step;

    private double cool;

    @Override
    public double minimize(TypedArgFunction<double[]> f, Domain[] domain, double[] ans) {
        double[] vecb = new double[ans.length];
        final int maxIndex = ans.length;
        int i;
        Domain di;
        double eb = f.apply(ans), ea;
        while (t > 0.1) {
            i = MathUtils.randInt(0, maxIndex);
            System.arraycopy(ans, 0, vecb, 0, maxIndex);
            di = domain[i];
            vecb[i] += MathUtils.randInt(-step, step);
            if (vecb[i] < di.min) {
                vecb[i] = di.min;
            } else if (vecb[i] > di.max) {
                vecb[i] = di.max;
            }
            ea = f.apply(ans);
            eb = f.apply(vecb);
            if (eb < ea || Math.random() < Math.pow(Math.E, -Math.abs(eb - ea) / t)) {
                ans = vecb;
            }
            t = t * cool;
        }
        return eb;
    }
}
