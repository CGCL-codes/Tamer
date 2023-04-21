package mipt.math.sys.num.ode;

import mipt.math.Number;
import mipt.math.array.Vector;

/**
 * Supplies data to use in StepAlgorithm to calculate step size:
 *  solution y, ys obtained with 2 methods or steps, right part (dy/dt)
 * Right part here can relate to any point of solving t interval!
 * Context must clone right part if it is of need!   
 * These data are known to different objects and not all of the data
 *   are needed for all algorithms (that's why this object is needed)
 * In order to remove solver dependence on knowing the data needed
 *   this object or created by algorightm and only "visit" some
 *   another objects (like InitialAlgorithms) to get the data
 * Step algorithm (or its method) can also set shouldRepeat  
 * @author evdokimov
 */
public interface StepAlgorithmContext {

    Vector getSolution();

    Vector getRightPart();

    Number getCurrentX();

    Number getOldStep();

    void setSolution(Vector solution);

    void setRightPart(Vector rightPart);

    void setCurrentX(Number X);

    void setOldStep(Number oldStep);

    void setShouldRepeatStep(boolean shouldRepeatStep);

    boolean shouldRepeatStep();
}
