package methods.interpolators;

import java.awt.geom.Point2D;
import java.util.List;
import methods.Funcion;

/**
 * La <b>Interpolacion De Newton</b> es un tipo de interpolacion polinomica que utiliza el polinomio de Newton.
 * La interpolacion polinomica es una t�cnica de interpolaci�n de un conjunto de datos o de una funci�n por un polinomio. Es 
 * decir, dado cierto numero de puntos obtenidos por muestreo o a partir de un experimento, se pretende encontrar un polinomio 
 * que pase por todos esos puntos.
 * Con el polinomio de interpolacion de Newton se logra aproximar los valor de la funci�n f(x) para cualquier x conocido. 
 * Este polinomio puede ser de grado 0, caso particular llamado <i>Interpolacion Lineal</i>, o de grado mayor.
 * 
 *
 */
public class InterpolacionNewton {

    /**
     * Interpola la funcion usando <b>Interpolacion de Newton</b>
     * @param points Set de puntos a usarse para la interpolacion
     * @return Funcion generada por la interpolacion
     */
    public static final Funcion interpolate(final List<Point2D.Double> points) {
        final double[] a = divDif(points);
        return new Funcion() {

            public double eval(double x) {
                return horner(a, points).eval(x);
            }
        };
    }

    private static double[] divDif(List<Point2D.Double> points) {
        int N = points.size();
        double[][] M = new double[N][N];
        for (int i = 0; i < N; i++) M[i][0] = (points.get(i).y);
        for (int j = 1; j < N; j++) for (int i = 0; i < N - j; i++) M[i][j] = (M[i + 1][j - 1] - M[i][j - 1]) / (points.get(i + j).x - (points.get(i).x));
        return M[0];
    }

    private static final Funcion horner(final double[] a, final List<Point2D.Double> points) {
        return new Funcion() {

            public double eval(double x) {
                int n = a.length;
                double v = a[n - 1];
                for (int i = 1; i < n; i++) v = v * (x - points.get(n - 1 - i).x) + a[n - 1 - i];
                return v;
            }
        };
    }
}
