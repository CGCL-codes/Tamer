package br.edu.ufcg.msnlab.methods.bus;

/**
 * TODO DOCUMENT ME!
 * @author Edigley Pereira Fraga
 * @author Jaindson Valentim Santana
 */
public class BusAlgorithmRResultImpl implements BusAlgorithmRResult {

    private double x;

    public BusAlgorithmRResultImpl(double x) {
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
