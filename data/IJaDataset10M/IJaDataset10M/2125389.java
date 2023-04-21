package bitWave.linAlg;

import bitWave.dataStructures.Record;

/**
 * Abstraction of an integrator component that delegates formulation of the problem
 * to an ODE interface. Supports an arbitrary number of dependent variables (y) and a 
 * single independent variable (x).
 * @author fw
 */
public interface ODEIntegrator {

    /**
     * Given values for the variables y[0..n-1] and their derivatives dydx[0..n-1] 
     * known at x, advances the solution over an interval h and updates y,
     * including local truncation error in the second record. The user supplies the 
     * reference solution which allows the integrator to read derivatives dydx for 
     * given y values and x.
     * @param y Current dependent variable values. Updated during the step to reflect
     * the new y.
     * @param dydx Current derivatives of y at x.
     * @param x Independent variable value.
     * @param h Step size by which to advance the solution.
     * @param solution Reference to the solution to be integrated. 
     * @param e Record in which the integrator may output error estimates for the step.
     */
    void step(final ODEProblem solution, final double x, final double h, final Record y, final Record dydx, final Record e);
}
