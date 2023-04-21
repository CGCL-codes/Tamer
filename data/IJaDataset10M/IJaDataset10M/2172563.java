package com.oat.domains.cfo.problems.garrett;

import com.oat.domains.cfo.CFOProblem;

/**
 * 
 * Type: Ripples<br/>
 * Date: 14/03/2006<br/>
 * <br/>
 * Description: Parameter free adaptive clonal selection (2004)
 * Adjust to avoid divide by zero
 * <br/>
 * @author Jason Brownlee
 * 
 * <pre>
 * Change History
 * ----------------------------------------------------------------------------
 * 
 * </pre>
 */
public class Ripples extends CFOProblem {

    @Override
    protected double problemSpecificCost(double[] v) {
        double p1 = 0.0;
        for (int i = 0; i < dimensions; i++) {
            p1 += (v[i] * v[i]);
        }
        p1 = Math.sin(5.0 * Math.sqrt(p1));
        double p2 = 0.0;
        for (int i = 0; i < dimensions; i++) {
            p2 += (v[i] * v[i]);
        }
        p2 = 5.0 * Math.sqrt(p2);
        if (p2 == 0.0) {
            return 0;
        }
        return 1.0 - (p1 / p2);
    }

    @Override
    protected double[][] preapreMinMax() {
        double[][] m = new double[dimensions][2];
        for (int i = 0; i < m.length; i++) {
            m[i][0] = -10.0;
            m[i][1] = +10.0;
        }
        return m;
    }

    @Override
    protected double[][] preapreOptima() {
        double[][] d = new double[1][dimensions];
        for (int i = 0; i < d[0].length; i++) {
            d[0][i] = 0;
        }
        return d;
    }

    @Override
    public boolean isMinimization() {
        return true;
    }

    @Override
    public String getName() {
        return "Ripples";
    }

    @Override
    public SUPPORTED_DIMENSIONS[] getSupportDimensionality() {
        return new SUPPORTED_DIMENSIONS[] { SUPPORTED_DIMENSIONS.ANY };
    }
}
