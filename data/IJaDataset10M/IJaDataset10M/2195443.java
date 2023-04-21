package net.sourceforge.piqle.util;

/**
	 * Fast version of unipolar sigmoid function.
	 */
public class FastLogisticSigmoidFunction extends AbstractFastSigmoidFunction {

    @Override
    public Double apply(Double d) {
        int i = (int) (d / SIGMO_RANGE * sigmoPrecomputed.length + sigmoPrecomputed.length / 2);
        if (i < 0) return 0.01;
        if (i >= sigmoPrecomputed.length) return 0.99;
        return sigmoPrecomputed[i];
    }

    @Override
    public Double derivative(Double t) {
        return t * (1 - t);
    }
}
