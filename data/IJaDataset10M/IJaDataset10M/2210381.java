package neuron.geometry;

public class Matrix33d {

    public double a[];

    public Matrix33d() {
        a = new double[9];
    }

    public Matrix33d(double a00, double a01, double a02, double a10, double a11, double a12, double a20, double a21, double a22) {
        a = new double[] { a00, a01, a02, a10, a11, a12, a20, a21, a22 };
    }

    /**
	 * Multiply vector with this matrix, ret = Av
	 * @param v
	 * @return new vector, A * v
	 */
    public Vector3d mulVec(Vector3d v) {
        Vector3d w = new Vector3d();
        w.x = a[0] * v.x + a[1] * v.y + a[2] * v.z;
        w.y = a[3] * v.x + a[4] * v.y + a[5] * v.z;
        w.z = a[6] * v.x + a[7] * v.y + a[8] * v.z;
        return w;
    }

    /**
	 * Multiply this matrix with matrix m, ret = this * m 
	 * @param m
	 * @return new matrix, this*m
	 */
    public Matrix33d mulMat(Matrix33d m) {
        Matrix33d out = new Matrix33d();
        for (int row = 0; row < 3; row++) {
            int o = row * 3;
            double r = a[0] * m.a[o + 0] + a[3] * m.a[o + 1] + a[6] * m.a[o + 2];
            double s = a[1] * m.a[o + 0] + a[4] * m.a[o + 1] + a[7] * m.a[o + 2];
            double t = a[2] * m.a[o + 0] + a[5] * m.a[o + 1] + a[8] * m.a[o + 2];
            out.a[o + 0] = r;
            out.a[o + 1] = s;
            out.a[o + 2] = t;
        }
        return out;
    }

    /**
	 * 3D Rotation matrix to rotate in YZ plane (around X axis) 
	 * @param rad angle in radians
	 * @return
	 */
    public static Matrix33d rotYZ(double rad) {
        return new Matrix33d(1, 0, 0, 0, Math.cos(rad), -Math.sin(rad), 0, Math.sin(rad), Math.cos(rad));
    }

    /**
	 * 3D Rotation matrix to rotate in XZ plane (around Y axis) 
	 * @param rad angle in radians
	 * @return
	 */
    public static Matrix33d rotXZ(double rad) {
        return new Matrix33d(Math.cos(rad), 0, Math.sin(rad), 0, 1, 0, -Math.sin(rad), 0, Math.cos(rad));
    }

    /**
	 * 3D Rotation matrix to rotate in XY plane (around Z axis) 
	 * @param rad angle in radians
	 * @return
	 */
    public static Matrix33d rotXY(double rad) {
        return new Matrix33d(Math.cos(rad), -Math.sin(rad), 0, Math.sin(rad), Math.cos(rad), 0, 0, 0, 1);
    }
}
