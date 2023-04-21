package iclab.classification.pul;

import iclab.classification.supervised.ICBNC;
import iclab.core.ICData;
import iclab.core.ICInstance;
import iclab.estimation.ICPBCLearner;
import iclab.estimation.bnetparams.ICBNetPULParamLearner;
import iclab.estimation.dag.ICDAGStructureLearner;
import iclab.exceptions.ICParameterException;
import iclab.math.statistics.ICFreqEstimator.ICEstimatorType;

/**
 *
 * @author b0rxa
 */
public class ICPBNC implements ICPULClassifier {

    private int _positive;

    private double _p;

    private double _a;

    private double _b;

    private boolean _averaged;

    private ICBNC _model;

    private ICBNetPULParamLearner _paramLearner;

    private ICDAGStructureLearner _strLearner;

    public ICPBNC(double p, int positiveValue, ICDAGStructureLearner strLearner) throws ICParameterException {
        _strLearner = strLearner;
        _paramLearner = new ICBNetPULParamLearner(ICEstimatorType.laplace, positiveValue, p);
        _positive = positiveValue;
        setPParam(p);
    }

    public ICPBNC(ICEstimatorType estimator, double p, int positiveValue, ICDAGStructureLearner strLearner) throws ICParameterException {
        _strLearner = strLearner;
        _paramLearner = new ICBNetPULParamLearner(estimator, positiveValue, p);
        _positive = positiveValue;
        setPParam(p);
    }

    public ICPBNC(double alpha, double beta, int positiveValue, ICDAGStructureLearner strLearner) throws ICParameterException {
        _strLearner = strLearner;
        _paramLearner = new ICBNetPULParamLearner(ICEstimatorType.laplace, positiveValue, alpha, beta);
        _positive = positiveValue;
        setBetaParams(alpha, beta);
    }

    public ICPBNC(ICEstimatorType estimator, double alpha, double beta, int positiveValue, ICDAGStructureLearner strLearner) throws ICParameterException {
        _strLearner = strLearner;
        _paramLearner = new ICBNetPULParamLearner(estimator, positiveValue, alpha, beta);
        _positive = positiveValue;
        setBetaParams(alpha, beta);
    }

    @Override
    public double[] classDistribution(ICInstance instance) throws ICParameterException {
        return _model.classDistribution(instance);
    }

    @Override
    public void learn(ICData dataset) throws ICParameterException {
        _model = new ICBNC(_strLearner, _paramLearner);
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

    public void setPParam(double p) {
        _p = p;
        _averaged = false;
        if (_strLearner instanceof ICPBCLearner) ((ICPBCLearner) _strLearner).setPParam(p);
        _paramLearner.setPParam(p);
    }

    public void setBetaParams(double alpha, double beta) {
        _a = alpha;
        _b = beta;
        _averaged = true;
        if (_strLearner instanceof ICPBCLearner) ((ICPBCLearner) _strLearner).setPParam(alpha, beta);
        _paramLearner.setPParam(alpha, beta);
    }

    public boolean isAveraged() {
        return _averaged;
    }

    public double getTrainingP() {
        double p;
        if (_averaged) p = _a / (_a + _b); else p = _p;
        return p;
    }

    public double[][][] getCPD() {
        return _model.getCPD();
    }

    public ICBNC getModel() {
        return _model;
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
