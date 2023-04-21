package com.oat.domains.cfo.problems.mahfoud;

import com.oat.domains.cfo.CFOProblem;

/**
 * Type: M5<br/>
 * Date: 14/03/2006<br/>
 * <br/>
 * Description: Niching Methods for Genetic Algorithms (Mahfoud) (1995)
 * <br/>
 * @author Jason Brownlee
 * 
 * <pre>
 * Change History
 * ----------------------------------------------------------------------------
 * 
 * </pre>
 */
public class M5 extends CFOProblem {

    @Override
    protected double problemSpecificCost(double[] v) {
        double x = v[0];
        double y = v[1];
        return (2186.0 - (x * x - Math.pow(x * x + y - 11, 2) - Math.pow(x + y * y - 7, 2))) / 2186.0;
    }

    @Override
    protected double[][] preapreMinMax() {
        double[][] d = new double[dimensions][];
        for (int i = 0; i < d.length; i++) {
            d[i] = new double[] { -6, +6 };
        }
        return d;
    }

    @Override
    protected double[][] preapreOptima() {
        return new double[][] { { 3, 2 }, { 3.584, -1.848 }, { -3.779, -3.283 }, { -2.805, 3.131 } };
    }

    @Override
    public boolean isMinimization() {
        return true;
    }

    @Override
    public String getName() {
        return "M5 (Himmelblau's Function)";
    }

    @Override
    public SUPPORTED_DIMENSIONS[] getSupportDimensionality() {
        return new SUPPORTED_DIMENSIONS[] { SUPPORTED_DIMENSIONS.TWO_DIMENSIONAL };
    }
}
