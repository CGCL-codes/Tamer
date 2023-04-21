package syn3d.util;

/**
 * This class Generates geodes of any order.
 * @author Nicolas Brodu
 */
public class GeodeMaker {

    public static double[] vproduct(double[] p1, double[] p2) {
        double[] res = new double[3];
        res[0] = p1[1] * p2[2] - p1[2] * p2[1];
        res[1] = p1[2] * p2[0] - p1[0] * p2[2];
        res[2] = p1[0] * p2[1] - p1[1] * p2[0];
        double norm = Math.sqrt(res[0] * res[0] + res[1] * res[1] + res[2] * res[2]);
        res[0] /= norm;
        res[1] /= norm;
        res[2] /= norm;
        return res;
    }

    public static double sproduct(double[] p1, double[] p2) {
        double res = p1[0] * p2[0] + p1[1] * p2[1] + p1[2] * p2[2];
        if (res > 1) res = 1;
        if (res < -1) res = -1;
        return res;
    }

    public static double[] combine(double[] p1, double[] p2, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double[] res = new double[3];
        res[0] = p1[0] * cos + p2[0] * sin;
        res[1] = p1[1] * cos + p2[1] * sin;
        res[2] = p1[2] * cos + p2[2] * sin;
        return res;
    }

    public static double golden_ratio = (Math.sqrt(5.0) + 1.0) / 2.0;

    public static double norm = Math.sqrt(2 + golden_ratio);

    public static double[] icosaedron_vertices = { golden_ratio / norm, 1 / norm, 0, -golden_ratio / norm, 1 / norm, 0, -golden_ratio / norm, -1 / norm, 0, golden_ratio / norm, -1 / norm, 0, 0, golden_ratio / norm, 1 / norm, 0, -golden_ratio / norm, 1 / norm, 0, -golden_ratio / norm, -1 / norm, 0, golden_ratio / norm, -1 / norm, 1 / norm, 0, golden_ratio / norm, 1 / norm, 0, -golden_ratio / norm, -1 / norm, 0, -golden_ratio / norm, -1 / norm, 0, golden_ratio / norm };

    public static int[] icosaedron_triangles_index = { 0, 7, 4, 1, 4, 7, 2, 6, 5, 3, 5, 6, 4, 11, 8, 5, 8, 11, 6, 9, 10, 7, 10, 9, 8, 3, 0, 9, 0, 3, 10, 1, 2, 11, 2, 1, 0, 4, 8, 0, 9, 7, 1, 11, 4, 1, 7, 10, 2, 5, 11, 2, 10, 6, 3, 8, 5, 3, 6, 9 };

    public static float[] createGeode(int ndiv) {
        if (ndiv <= 1) ndiv = 1;
        float[] ret = new float[20 * ndiv * ndiv * 9];
        int idx = 0;
        for (int i = 0; i < 20; ++i) {
            double[] p1 = new double[] { icosaedron_vertices[icosaedron_triangles_index[i * 3] * 3], icosaedron_vertices[icosaedron_triangles_index[i * 3] * 3 + 1], icosaedron_vertices[icosaedron_triangles_index[i * 3] * 3 + 2] };
            double[] p2 = new double[] { icosaedron_vertices[icosaedron_triangles_index[i * 3 + 1] * 3], icosaedron_vertices[icosaedron_triangles_index[i * 3 + 1] * 3 + 1], icosaedron_vertices[icosaedron_triangles_index[i * 3 + 1] * 3 + 2] };
            double[] p3 = new double[] { icosaedron_vertices[icosaedron_triangles_index[i * 3 + 2] * 3], icosaedron_vertices[icosaedron_triangles_index[i * 3 + 2] * 3 + 1], icosaedron_vertices[icosaedron_triangles_index[i * 3 + 2] * 3 + 2] };
            double[] ortho12 = vproduct(vproduct(p1, p2), p1);
            double[] ortho13 = vproduct(vproduct(p1, p3), p1);
            double[][] prevPoints = { p1 };
            for (int j = 1; j <= ndiv; ++j) {
                double ratio = j / (double) ndiv;
                double angle_ratio = ratio * Math.acos(sproduct(p1, p2));
                double[] p12 = combine(p1, ortho12, angle_ratio);
                angle_ratio = ratio * Math.acos(sproduct(p1, p3));
                double[] p13 = combine(p1, ortho13, angle_ratio);
                double[] ortho = vproduct(vproduct(p12, p13), p12);
                double[][] points = new double[j + 1][];
                for (int k = 0; k <= j; ++k) {
                    ratio = k / (double) j;
                    angle_ratio = ratio * Math.acos(sproduct(p12, p13));
                    points[k] = combine(p12, ortho, angle_ratio);
                }
                ret[idx++] = (float) points[0][0];
                ret[idx++] = (float) points[0][1];
                ret[idx++] = (float) points[0][2];
                ret[idx++] = (float) points[1][0];
                ret[idx++] = (float) points[1][1];
                ret[idx++] = (float) points[1][2];
                ret[idx++] = (float) prevPoints[0][0];
                ret[idx++] = (float) prevPoints[0][1];
                ret[idx++] = (float) prevPoints[0][2];
                for (int k = 1; k < j; ++k) {
                    ret[idx++] = (float) prevPoints[k - 1][0];
                    ret[idx++] = (float) prevPoints[k - 1][1];
                    ret[idx++] = (float) prevPoints[k - 1][2];
                    ret[idx++] = (float) points[k][0];
                    ret[idx++] = (float) points[k][1];
                    ret[idx++] = (float) points[k][2];
                    ret[idx++] = (float) prevPoints[k][0];
                    ret[idx++] = (float) prevPoints[k][1];
                    ret[idx++] = (float) prevPoints[k][2];
                    ret[idx++] = (float) points[k][0];
                    ret[idx++] = (float) points[k][1];
                    ret[idx++] = (float) points[k][2];
                    ret[idx++] = (float) points[k + 1][0];
                    ret[idx++] = (float) points[k + 1][1];
                    ret[idx++] = (float) points[k + 1][2];
                    ret[idx++] = (float) prevPoints[k][0];
                    ret[idx++] = (float) prevPoints[k][1];
                    ret[idx++] = (float) prevPoints[k][2];
                }
                prevPoints = points;
            }
        }
        return ret;
    }
}
