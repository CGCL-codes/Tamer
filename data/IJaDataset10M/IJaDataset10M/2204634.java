package com.oat.domains.cfo.problems.geatbx;

import com.oat.domains.cfo.CFOProblem;

/**
 * Type: SixHumpCamelBackFunction<br/>
 * Date: 10/03/2006<br/>
 * <br/>
 * Description: geatbx (http://www.geatbx.com/docu/fcnindex-01.html)
 * <br/>
 * @author Jason Brownlee
 * 
 * <pre>
 * Change History
 * ----------------------------------------------------------------------------
 * 
 * </pre>
 */
public class SixHumpCamelBackFunction extends CFOProblem {

    @Override
    protected double problemSpecificCost(double[] v) {
        double x = v[0];
        double y = v[1];
        double a = (4.0 - 2.1 * (x * x) + (x * x * x * x) / 3.0) * (x * x) + x * y + (-4 + 4 * (y * y)) * (y * y);
        return a;
    }

    @Override
    protected double[][] preapreMinMax() {
        return new double[][] { { -3, +3 }, { -2, +2 } };
    }

    @Override
    protected double[][] preapreOptima() {
        return new double[][] { { -0.0898, 0.7126 }, { 0.0898, -0.7126 } };
    }

    @Override
    public boolean isMinimization() {
        return true;
    }

    @Override
    public String getName() {
        return "Six-Hump Camel Back Function";
    }

    @Override
    public SUPPORTED_DIMENSIONS[] getSupportDimensionality() {
        return new SUPPORTED_DIMENSIONS[] { SUPPORTED_DIMENSIONS.TWO_DIMENSIONAL };
    }
}
