package br.edu.ufcg.msnlab.methods.ridders;

/**
 * 
 * @author Eugenia de Sousa
 * @author Jaluska Rodrigues
 * 
 */
public class RiddersResultImpl implements RiddersResult {

    private double max;

    private double min;

    private double result;

    public RiddersResultImpl(double max, double min, double result) {
        this.max = max;
        this.min = min;
        this.result = result;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public double getX() {
        return result;
    }
}
