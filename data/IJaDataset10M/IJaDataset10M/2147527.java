package weka.classifiers.bnc;

import BayesianNetworks.BayesNet;
import jbnc.graphs.BNCInference;
import weka.classifiers.bnc.util.InstancesToDatasetDiscretizerAndConverter;
import weka.classifiers.Classifier;
import weka.core.Instance;

/**
 * Wrapper for jBNC inducers.
 */
public abstract class BayesianNetworkClassifier extends Classifier {

    /**
     * Enables automatic discretization of continous attributes.
     */
    protected InstancesToDatasetDiscretizerAndConverter converter;

    /**
     * Bayesian network representing this classifier
     */
    protected BayesNet bayesNet;

    /** */
    protected BNCInference bncInference;

    /**
     * Alpha is used for estimating the probability tables and can be interpreted as the initial count on each value.
     * Alpha should be larger than 0.
     */
    protected double alpha = 1;

    /**
     * Classifies a given instance.
     *
     * @param instance the instance to be classified
     * @return index of the predicted class as a double if the class is nominal, otherwise the predicted value
     * @throws java.lang.Exception if instance could not be classified successfully
     */
    public double classifyInstance(Instance instance) throws Exception {
        if (converter == null || bayesNet == null) {
            throw new IllegalStateException("Need to build this classifier before classifying instances.");
        }
        int nbVars = instance.numAttributes();
        int nbAttrib = nbVars - 1;
        int[] caseIndexes = new int[nbVars];
        if (bncInference == null) bncInference = new BNCInference(bayesNet);
        int[] thisCase = converter.toCase(instance);
        for (int j = 0; j < nbAttrib; ++j) {
            caseIndexes[j] = thisCase[j];
        }
        double[] classProb = bncInference.getCondClassProb(caseIndexes);
        int maxIndex = -1;
        double maxPr = -1;
        for (int j = 0; j < classProb.length; ++j) {
            if (classProb[j] > maxPr) {
                maxPr = classProb[j];
                maxIndex = j;
            }
        }
        return maxIndex;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * @return a string describing the Alpha option.
     */
    public String alphaTipText() {
        return "Alpha is used for estimating the probability tables and can be interpreted" + " as the initial count on each value. Alpha should be larger than 0.";
    }

    /**
     * This will return a string describing the classifier.
     *
     * @return The string.
     */
    public abstract String globalInfo();

    /**
     * Clear internal state.
     */
    protected void clear() {
        converter = null;
        bayesNet = null;
        bncInference = null;
    }

    /**
     * Return description of the calssifier
     */
    public String toString() {
        return globalInfo();
    }
}
