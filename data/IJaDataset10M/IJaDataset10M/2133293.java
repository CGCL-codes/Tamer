package jenes.probability;

/**
 *
 * @author troiano
 */
public class UniformDistribution implements Distribution {

    private double min;

    private double max;

    public UniformDistribution() {
        this(0, 1);
    }

    public UniformDistribution(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double pdf(double x) {
        return x > min && x <= max ? 1 / (max - min) : 0;
    }

    public double cdf(double x) {
        if (x <= min) return 0; else if (x <= max) return x / (max - min); else return 1;
    }

    public double getLowerBound() {
        return 0;
    }

    public double getUpperBound() {
        return 1;
    }
}
