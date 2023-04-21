package iclab.classification.pul;

import iclab.classification.pul.ICwPBC.ICwPBCScore;
import iclab.core.ICData;
import iclab.core.ICInstance;
import iclab.estimation.dag.ICNaiveBayesStrLearner;
import iclab.evaluation.pul.ICPULClassifierEvaluator.ICwPBCpEst;
import iclab.exceptions.ICParameterException;
import iclab.math.statistics.ICFreqEstimator.ICEstimatorType;

/**
 * Class representing the wrapper positive naive Bayes learning algorithm.
 */
public class ICwPNB implements ICPULClassifier {

    private ICwPBC _model;

    private int _positive;

    public ICwPNB(int positiveValue, int numIntervals, double delta, int numSamples, ICwPBCScore score, int maxIt, ICwPBCpEst pEstimationMethod, boolean verbose) throws ICParameterException {
        _positive = positiveValue;
        _model = new ICwPBC(new ICNaiveBayesStrLearner(), positiveValue, numIntervals, delta, numSamples, score, maxIt, pEstimationMethod, verbose);
    }

    public ICwPNB(ICEstimatorType estimator, int positiveValue, int numIntervals, double delta, int numSamples, ICwPBCScore score, int maxIt, ICwPBCpEst pEstimationMethod, boolean verbose) throws ICParameterException {
        _model = new ICwPBC(estimator, new ICNaiveBayesStrLearner(), positiveValue, numIntervals, delta, numSamples, score, maxIt, pEstimationMethod, verbose);
    }

    @Override
    public double[] classDistribution(ICInstance instance) throws ICParameterException {
        return _model.classDistribution(instance);
    }

    @Override
    public void learn(ICData dataset) throws ICParameterException {
        _model.learn(dataset);
    }

    @Override
    public double classify(ICInstance instance) throws ICParameterException {
        return _model.classify(instance);
    }

    @Override
    public void classify(ICData dataset) throws ICParameterException {
        _model.classify(dataset);
    }

    public double[][][] getCPD() {
        return _model.getFinalClassifier().getCPD();
    }

    public double getOptimalP() {
        return _model.getOptimalP();
    }

    public double[][] getScoreVSpPoints() {
        return _model.getScoreVSpPoints();
    }

    public ICPBNC getFinalClassifier() {
        return _model.getFinalClassifier();
    }

    @Override
    public int getPositiveClassIndex() {
        return (int) _positive;
    }

    @Override
    public void setPositiveClassIndex(int positive) {
        _positive = positive;
    }
}
