package mipt.math.sys.num;

import mipt.math.Number;
import mipt.math.array.Matrix;
import mipt.math.array.Vector;

public interface NormOperator extends Method {

    Number calcNorm(Vector v);

    Number calcNorm(Matrix A);
}
