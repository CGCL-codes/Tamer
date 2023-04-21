package uk.ac.shef.wit.aleph.algorithm.svm.loss;

/**
 * Implements a hinge loss function.
 *
 * @author Jose' Iria, NLP Group, University of Sheffield
 *         (<a  href="mailto:J.Iria@dcs.shef.ac.uk" >email</a>)
 */
public class LossHinge implements Loss {

    public final double loss(final double z) {
        return 1.0 > z ? 1.0 - z : 0.0;
    }

    public final double dloss(final double z) {
        return 1.0 > z ? 1.0 : 0.0;
    }
}
