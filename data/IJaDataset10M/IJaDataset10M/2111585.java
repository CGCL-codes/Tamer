package simulator;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

/**
 * A hook between colt and vecmath.
 * 
 * @author Yung-Chang Tan
 *
 */
class SimMath {

    public static DoubleMatrix2D fromMatrix3d(Matrix3d m) {
        return new DenseDoubleMatrix2D(new double[][] { { m.m00, m.m01, m.m02 }, { m.m10, m.m11, m.m12 }, { m.m20, m.m21, m.m22 } });
    }

    public static double[] v3dDouble(Vector3d v) {
        return new double[] { v.x, v.y, v.z };
    }

    public static DoubleMatrix1D v3dDM1D(Vector3d v) {
        return new DenseDoubleMatrix1D(new double[] { v.x, v.y, v.z });
    }
}
