package br.edu.ufcg.msnlab.methods.bus;

/**
 * TODO DOCUMENT ME!
 * @author Edigley Pereira Fraga
 * @author Jaindson Valentim Santana
 */
public class BusAlgorithmMResultImpl implements BusAlgorithmMResult {

    private double x;

    public BusAlgorithmMResultImpl(double x) {
        super();
        this.x = x;
    }

    public double getX() {
        return this.x;
    }

    public String toString() {
        return String.valueOf(this.x);
    }
}
