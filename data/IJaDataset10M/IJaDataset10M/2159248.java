package util;

public class Matrix33 {

    public float[][] _m;

    public Matrix33() {
        _m = new float[3][3];
    }

    public Matrix33(float xx, float xy, float xz, float yx, float yy, float yz, float zx, float zy, float zz) {
        _m = new float[3][3];
        _m[0][0] = xx;
        _m[0][1] = xy;
        _m[0][2] = xz;
        _m[1][0] = yx;
        _m[1][1] = yy;
        _m[1][2] = yz;
        _m[2][0] = zx;
        _m[2][1] = zy;
        _m[2][2] = zz;
    }

    public static Matrix33 createUnit() {
        Matrix33 result = new Matrix33();
        for (int i = 0; i < 3; i++) {
            result._m[i][i] = 1.0f;
        }
        return result;
    }

    public static Matrix33 createTranslation(Point2 t) {
        Matrix33 result = createUnit();
        for (int i = 0; i < 2; i++) {
            result._m[i][2] = t._p[i];
        }
        return result;
    }

    public static Matrix33 createRotation(float angle) {
        Matrix33 result = createUnit();
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        result._m[0][0] = c;
        result._m[0][1] = -s;
        result._m[1][0] = s;
        result._m[1][1] = c;
        return result;
    }

    public String toString() {
        String result = "m33[\n";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result += _m[i][j] + " ";
            }
            result += "\n";
        }
        result += "]";
        return result;
    }

    public void setCol(int i, Point3 p) {
        _m[0][i] = p._p[0];
        _m[1][i] = p._p[1];
        _m[2][i] = p._p[2];
    }

    public void setRow(int i, Point3 p) {
        _m[i][0] = p._p[0];
        _m[i][1] = p._p[1];
        _m[i][2] = p._p[1];
    }

    public Matrix33 affineInverse() {
        float Tx, Ty;
        Matrix33 result = new Matrix33();
        result._m[0][0] = _m[0][0];
        result._m[0][1] = _m[1][0];
        result._m[1][0] = _m[0][1];
        result._m[1][1] = _m[1][1];
        result._m[2][0] = 0f;
        result._m[2][1] = 0f;
        result._m[2][2] = 1f;
        Tx = _m[0][2];
        Ty = _m[1][2];
        result._m[0][2] = -(_m[0][0] * Tx + _m[1][0] * Ty);
        result._m[1][2] = -(_m[0][1] * Tx + _m[1][1] * Ty);
        return result;
    }

    public static Matrix33 mult(Matrix33 a, Matrix33 b) {
        Matrix33 result = new Matrix33();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                float temp = 0f;
                for (int k = 0; k < 3; k++) {
                    temp += a._m[i][k] * b._m[k][j];
                }
                result._m[i][j] = temp;
            }
        }
        return result;
    }

    public static boolean equals(Matrix33 a, Matrix33 b, float eps) {
        boolean result = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result &= Math.abs(a._m[i][j] - b._m[i][j]) < eps;
            }
        }
        return result;
    }

    public Point3 applyOn(Point3 p) {
        Point3 result = new Point3();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result._p[i] += _m[i][j] * p._p[j];
            }
        }
        return result;
    }

    /**
     * multiplication without creation.
     */
    public static void mult(Matrix33 m, Point3 in, Point3 out) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                out._p[i] += m._m[i][j] * in._p[j];
            }
        }
    }
}
