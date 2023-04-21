package uk.ac.shef.wit.aleph.dataset.observer;

import no.uib.cipr.matrix.Vector;
import uk.ac.shef.wit.aleph.AdaptiveVector;
import uk.ac.shef.wit.aleph.AlephException;
import uk.ac.shef.wit.aleph.dataset.Dataset;
import uk.ac.shef.wit.aleph.dataset.Instance;

/**
 * Sums all the features weights, per feature and separately for the positive and negative classes, in the instances
 * observed so far.
 *
 * @author Jose' Iria, NLP Group, University of Sheffield
 *         (<a  href="mailto:J.Iria@dcs.shef.ac.uk" >email</a>)
 */
public final class DatasetObserverFeatureWeightBinary implements DatasetObserver {

    private final Vector[] _weights;

    public DatasetObserverFeatureWeightBinary() {
        _weights = new Vector[] { new AdaptiveVector(), new AdaptiveVector() };
    }

    public void observe(final Dataset dataset, final Instance instance) throws AlephException {
        final double target = instance.getTarget();
        if (0.0 != target) _weights[(0.0 < target ? 1 : 0)].add(instance.getFeatures());
    }

    public double getFeaturesWeight(final boolean positives) {
        return _weights[positives ? 1 : 0].norm(Vector.Norm.One);
    }

    public double getFeatureWeight(final int feature, final boolean positives) {
        return _weights[positives ? 1 : 0].get(feature);
    }

    public int getMaxIndex() {
        return Math.max(_weights[0].size(), _weights[1].size()) - 1;
    }

    public Vector getWeightsAsVector(final boolean positives) {
        return _weights[positives ? 1 : 0];
    }
}
