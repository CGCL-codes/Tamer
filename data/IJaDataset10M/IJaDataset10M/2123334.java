package uk.ac.shef.wit.aleph.algorithm.svm.loss;

/**
 * Implements a log loss function.
 *
 * @author Jose' Iria, NLP Group, University of Sheffield
 *         (<a  href="mailto:J.Iria@dcs.shef.ac.uk" >email</a>)
 */
public class LossLog implements Loss {

    private static final double BOUND = 18.0;

    public final double loss(final double z) {
        return BOUND < z ? Math.exp(-z) : -BOUND > z ? -z : Math.log(1.0 + Math.exp(-z));
    }

    public final double dloss(final double z) {
        return BOUND < z ? Math.exp(-z) : -BOUND > z ? 1.0 : 1.0 / (Math.exp(z) + 1.0);
    }
}
