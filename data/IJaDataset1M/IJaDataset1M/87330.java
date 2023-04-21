package rna.activation;

/**
 *
 * @author Jhielson
 */
public class SigmoideSimetrica implements Activation {

    public double activate(double v) {
        double a = 1;
        double b = Math.exp(-a * v);
        double c = 1 / (1 + b);
        return c - 0.5;
    }

    public double activate(double a, double v) {
        double b = Math.exp(-a * v);
        double c = 1 / (1 + b);
        return c - 0.5;
    }

    public double derivada(double v) {
        double derivada;
        double beta = 1.0;
        derivada = beta * v * (1 - v);
        return derivada;
    }
}
