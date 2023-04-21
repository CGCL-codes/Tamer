package mipt.math.sys.num.ode;

import mipt.math.array.MutableMatrix;
import mipt.math.array.MutableVector;

public interface ODESolutionWrapper extends ODESolution {

    /**
 * 
 * @param y mipt.math.array.MutableVector
 */
    void setArgs(MutableVector x);

    /**
 * 
 * @param y mipt.math.array.MutableMatrix
 */
    void setMatrix(MutableMatrix y);
}
