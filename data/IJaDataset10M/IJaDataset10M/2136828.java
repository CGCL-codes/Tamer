package timba.distributions;

import java.util.Random;
import timba.distributions.models.Distribution;

/**
 * calculo de variables por Gamma
 * @author JuanBC - JuanMG - FranciscoAG
 * @version 1.0
 */
public class Gamma extends Distribution {

    private double lamb;

    private double k;

    private double fkmu;

    /**
     * crea una nueva instancia de la clase
     * @param lambda valor de lambda
     */
    public Gamma(double lambda, double recorrido) {
        super();
        this.lamb = lambda;
        this.k = recorrido;
        this.fkmu = this.factorial(recorrido);
    }

    /**
     * un valor mas dentro de la distribucion
     * @return un nuevo valor
     */
    public double getNextDouble() {
        return this.formula();
    }

    /**
     * para que retorne <code>c</code> valores
     * @param c cantidad de valores
     * @return vector con todos los valores
     */
    public double[] getDoubles(int c) {
        double[] a = new double[c];
        for (int i = 0; i < c; i++) {
            a[i] = this.getNextDouble();
        }
        return a;
    }

    private double factorial(double k) {
        if ((int) k != 0) {
            return k * factorial(k - 1);
        }
        return 1;
    }

    /**
     * para obtener el parametro lambda
     * @return lambda
     */
    public double getLambda() {
        return lamb;
    }

    /**
     * retorna parametro k
     * @return k
     */
    public double getK() {
        return k;
    }

    public double formula() {
        Random r = super.getRandomGenerator();
        double x = r.nextDouble();
        double a1 = Math.pow(this.lamb, (-1) * this.k);
        double a2 = Math.pow(x, k - 1);
        double a3 = Math.exp(-1 * (x / this.lamb));
        return ((a1 * a2 * a3) / this.fkmu);
    }
}
