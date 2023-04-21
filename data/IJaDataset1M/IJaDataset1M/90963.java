package mipt.math.sys.num.ode;

import mipt.math.array.*;
import mipt.math.Number;

public interface ODESolution extends mipt.math.sys.num.Solution {

    /**
 * 0 <= iX < getPointCount()
 * @return mipt.math.Number
 * @param iX int
 */
    Number getArg(int iX);

    /**
 * 
 * @return mipt.math.array.Vector
 */
    Vector getArgs();

    /**
 * 0 <= iY < getComponentCount()
 * @return mipt.math.array.Vector
 * @param iY int
 */
    Vector getComponent(int iY);

    /**
 * 
 * @return int
 */
    int getComponentCount();

    /**
 * 
 * @return mipt.math.array.Matrix
 */
    Matrix getMatrix();

    /**
 * 0 <= iX < getPointCount()
 * @return mipt.math.array.Vector
 * @param iX int
 */
    Vector getPoint(int iX);

    /**
 * 
 * @return mipt.math.Number
 * @param iX int
 * @param iY int
 */
    Number getPointComponent(int iX, int iY);

    /**
 * 
 * @return int
 */
    int getPointCount();

    /**
 * As a rule returns by getPointCount()-1
 * @return int
 */
    int getStepCount();
}
