package uk.ac.shef.wit.aleph.algorithm.baseline;

import uk.ac.shef.wit.aleph.AlephException;
import uk.ac.shef.wit.aleph.algorithm.graph.TransductiveClassifier;
import uk.ac.shef.wit.aleph.algorithm.Classifier;
import uk.ac.shef.wit.aleph.dataset.Dataset;

/**
 * Implements a {@link uk.ac.shef.wit.aleph.algorithm.Learner} that does nothing, that is, returns the input dataset as predictions.
 *
 * @author Jose' Iria, NLP Group, University of Sheffield
 *         (<a  href="mailto:J.Iria@dcs.shef.ac.uk" >email</a>)
 */
public class LearnerNull implements TransductiveClassifier {

    public Classifier learn(final Dataset data) throws AlephException {
        return this;
    }

    public Classifier learn(final Dataset dataset, final Classifier classifier) throws AlephException {
        return this;
    }

    public Dataset classify(final Dataset dataset) throws AlephException {
        return dataset;
    }
}
