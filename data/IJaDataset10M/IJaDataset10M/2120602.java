package org.apache.commons.math.ode.nonstiff;

/**
 * This class implements the Gill fourth order Runge-Kutta
 * integrator for Ordinary Differential Equations .

 * <p>This method is an explicit Runge-Kutta method, its Butcher-array
 * is the following one :
 * <pre>
 *    0  |    0        0       0      0
 *   1/2 |   1/2       0       0      0
 *   1/2 | (q-1)/2  (2-q)/2    0      0
 *    1  |    0       -q/2  (2+q)/2   0
 *       |-------------------------------
 *       |   1/6    (2-q)/6 (2+q)/6  1/6
 * </pre>
 * where q = sqrt(2)</p>
 *
 * @see EulerIntegrator
 * @see ClassicalRungeKuttaIntegrator
 * @see MidpointIntegrator
 * @see ThreeEighthesIntegrator
 * @version $Revision: 673069 $ $Date: 2008-07-01 14:36:20 +0200 (Di, 01 Jul 2008) $
 * @since 1.2
 */
public class GillIntegrator extends RungeKuttaIntegrator {

    /** Serializable version identifier. */
    private static final long serialVersionUID = 5566682259665027132L;

    /** Time steps Butcher array. */
    private static final double[] c = { 1.0 / 2.0, 1.0 / 2.0, 1.0 };

    /** Internal weights Butcher array. */
    private static final double[][] a = { { 1.0 / 2.0 }, { (Math.sqrt(2.0) - 1.0) / 2.0, (2.0 - Math.sqrt(2.0)) / 2.0 }, { 0.0, -Math.sqrt(2.0) / 2.0, (2.0 + Math.sqrt(2.0)) / 2.0 } };

    /** Propagation weights Butcher array. */
    private static final double[] b = { 1.0 / 6.0, (2.0 - Math.sqrt(2.0)) / 6.0, (2.0 + Math.sqrt(2.0)) / 6.0, 1.0 / 6.0 };

    /** Simple constructor.
   * Build a fourth-order Gill integrator with the given step.
   * @param step integration step
   */
    public GillIntegrator(final double step) {
        super("Gill", c, a, b, new GillStepInterpolator(), step);
    }
}
