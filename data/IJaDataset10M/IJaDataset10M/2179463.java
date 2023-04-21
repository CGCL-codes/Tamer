package mipt.math.sys.theory.ode;

import mipt.math.function.Function;
import mipt.math.Number;
import mipt.math.array.Vector;

public interface ODETheorySolution {

    /**
 * 
 * @return mipt.math.function.Function
 * @param iY int
 */
    Function getComponentFunction(int iY);

    /**
 * 
 * @return mipt.math.array.Vector
 * @param x mipt.math.Number
 */
    Vector getPoint(Number x);

    /**
 * 
 * @return mipt.math.Number
 * @param x mipt.math.Number
 */
    Number getPointComponent(Number x);
}
