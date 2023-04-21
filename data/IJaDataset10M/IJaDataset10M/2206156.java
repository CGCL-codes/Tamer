package edu.cmu.ece.wnp5.bandit;

import com.sun.squawk.util.MathUtils;
import java.util.Random;

/**
 *
 * @author shahriyar
 */
public class Exp3Gambler extends AbstractGambler {

    private static Random random = new Random();

    private double gamma;

    private double[] w;

    private double wSum;

    private double[] p;

    public Exp3Gambler(double gamma) {
        if (gamma <= 0.0 || gamma > 1.0) {
        }
        this.gamma = gamma;
    }

    public void reset() {
        super.reset();
        w = new double[getLeverCount()];
        for (int i = 0; i < w.length; i++) w[i] = 1.0;
        wSum = (double) getLeverCount();
        p = new double[getLeverCount()];
    }

    public int play(int horizon) {
        for (int i = 0; i < getLeverCount(); i++) p[i] = (1.0 - gamma) * w[i] / wSum + gamma / (double) getLeverCount();
        double[] cumP = new double[getLeverCount()];
        cumP[0] = p[0];
        for (int i = 1; i < getLeverCount(); i++) cumP[i] = p[i] + cumP[i - 1];
        double threshold = random.nextDouble();
        int index = 0;
        while (cumP[index] < threshold) index++;
        return index;
    }

    public void observe(int index, double reward) {
        super.observe(index, reward);
        wSum -= w[index];
        w[index] *= MathUtils.exp(gamma * reward / (p[index] * (double) getLeverCount()));
        if (Double.isNaN(w[index]) || Double.isInfinite(w[index]) || Math.abs(w[index]) > 10e+9) {
            w[index] = 1.0;
        }
        wSum += w[index];
    }
}
