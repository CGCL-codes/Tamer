package statistics;

import java.io.Serializable;
import java.util.Random;

/** Generating integers between 0 and nb-1 following Poisson 's laws */
public class Poisson implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** An auxiliary array to compute returns (see algorithm) */
    private double[] range;

    /** The upper bound of possible integers */
    private int maxValues;

    /** The law's coefficient */
    private double lambda;

    private Random generateur = new Random();

    /** Fill the auxiliary array */
    public Poisson(double lam, int nb) {
        this.maxValues = nb;
        this.lambda = lam;
        this.range = new double[this.maxValues];
        double k = Math.exp(-lambda);
        range[0] = 0;
        for (int i = 1; i < this.maxValues; i++) {
            range[i] = range[i - 1] + k;
            k *= lambda / i;
        }
    }

    /** Return a number in the range, following the current Poisson's law */
    public int get() {
        double value = generateur.nextDouble();
        int i = this.maxValues - 1;
        while ((i >= 0) && (value <= range[i])) {
            i--;
        }
        return i;
    }
}
