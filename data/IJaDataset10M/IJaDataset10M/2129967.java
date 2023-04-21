package org.apache.commons.math.ode.sampling;

import java.io.Serializable;
import org.apache.commons.math.ode.DerivativeException;

/**
 * This interface represents a handler that should be called after
 * each successful fixed step.

 * <p>This interface should be implemented by anyone who is interested
 * in getting the solution of an ordinary differential equation at
 * fixed time steps. Objects implementing this interface should be
 * wrapped within an instance of {@link StepNormalizer} that itself
 * is used as the general {@link StepHandler} by the integrator. The
 * {@link StepNormalizer} object is called according to the integrator
 * internal algorithms and it calls objects implementing this
 * interface as necessary at fixed time steps.</p>
 *
 * @see StepHandler
 * @see StepNormalizer
 * @version $Revision: 676612 $ $Date: 2008-07-14 16:58:57 +0200 (Mo, 14 Jul 2008) $
 * @since 1.2
 */
public interface FixedStepHandler extends Serializable {

    /**
   * Handle the last accepted step
   * @param t time of the current step
   * @param y state vector at t. For efficiency purposes, the {@link
   * StepNormalizer} class reuses the same array on each call, so if
   * the instance wants to keep it across all calls (for example to
   * provide at the end of the integration a complete array of all
   * steps), it should build a local copy store this copy.
   * @param yDot derivatives of the state vector state vector at t.
   * For efficiency purposes, the {@link StepNormalizer} class reuses
   * the same array on each call, so if
   * the instance wants to keep it across all calls (for example to
   * provide at the end of the integration a complete array of all
   * steps), it should build a local copy store this copy.
   * @param isLast true if the step is the last one
   * @throws DerivativeException if some error condition is encountered
   */
    public void handleStep(double t, double[] y, double[] yDot, boolean isLast) throws DerivativeException;
}
