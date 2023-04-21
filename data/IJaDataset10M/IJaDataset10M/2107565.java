package mosi.logic.simulation.statistics.distribution;

import java.util.Vector;

public class BetaDistribution extends Distribution {

    public static Vector<String> parameterlabels;

    public static final double DEFAULT_ALPHA = 1.0;

    public static final double DEFAULT_BETA = 1.0;

    public static final double[] DEFAULT_VALUES = { DEFAULT_ALPHA, DEFAULT_BETA };

    static {
        parameterlabels = new Vector<String>(2);
        parameterlabels.addElement("Alpha");
        parameterlabels.addElement("Beta");
    }

    /**
	 * BetaDistribution
	 */
    public BetaDistribution() {
    }

    public BetaDistribution(double alpha, double beta) throws InvalidDistributionException {
        if (alpha <= 0) throw new InvalidDistributionException("Alpha should be positive!");
        if (beta <= 0) throw new InvalidDistributionException("Beta should be positive");
        super.parameterList = new Vector<Double>(2);
        super.parameterList.addElement(alpha);
        super.parameterList.addElement(beta);
        lnkMersenneTwisterFast = new MersenneTwisterFast(System.nanoTime());
    }

    public double getDensity(double x) {
        return 0.0;
    }

    public double getRandomNumber() {
        return 0.0;
    }

    public double getErwartungswert() {
        return 0.0;
    }

    public void setProposal(double x) {
    }

    @Override
    public String getRRFunction() {
        return "rbeta(n, " + parameterList.get(0).toString() + ", " + parameterList.get(1).toString() + ")";
    }

    @Override
    public String getRDFunction() {
        return "dbeta(x, " + parameterList.get(0).toString() + ", " + parameterList.get(1).toString() + ")";
    }

    @Override
    public String getRPFunction() {
        return "pbeta(q, " + parameterList.get(0).toString() + ", " + parameterList.get(1).toString() + ")";
    }

    @Override
    public String getRQFunction() {
        return "qbeta(p, " + parameterList.get(0).toString() + ", " + parameterList.get(1).toString() + ")";
    }
}
