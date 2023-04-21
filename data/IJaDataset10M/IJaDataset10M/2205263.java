package brainbots.util;

public abstract class BBUtil {

    /**A += B*k*/
    public static void add(double[] a, double[] b, double k) {
        for (int i = 0; i < a.length; ++i) {
            a[i] += b[i] * k;
        }
    }

    public static double sqr(double x) {
        return x * x;
    }

    public static void normalize(double[] vec) {
        double s = sqr(vec[0]);
        for (int i = 1; i < vec.length; ++i) {
            s += sqr(vec[i]);
        }
        if (s == 0.0) return;
        scale(vec, 1.0 / Math.sqrt(s));
    }

    public static void scale(double[] vec, double k) {
        for (int i = 0; i < vec.length; ++i) {
            vec[i] *= k;
        }
    }

    public static void linspace(double[] vec, double x0, double x1) {
        if (vec.length == 0) return;
        if (vec.length == 1) {
            vec[0] = x0;
            return;
        }
        double d = (x1 - x0) / (vec.length - 1);
        for (int i = 0; i < vec.length; ++i) {
            vec[i] = x0 + i * d;
        }
    }
}
