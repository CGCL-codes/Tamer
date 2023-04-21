package iclab.estimation.bnetparams;

import iclab.core.ICData;
import iclab.exceptions.ICParameterException;
import iclab.math.statistics.ICFreqEstimator;

/**
 * Object to set the parameter of a Bayesian network by hand
 */
public class ICBNetCustomParamLearner implements ICBNetParamLearner {

    double[][][] _cpd;

    /**
	 * Basic constructor.
   * @param cpd Conditional probability distributions for all the attributes and all the possible values of the parents. To check how this parameter has to look like, please see {@link ICBNet}
	 */
    public ICBNetCustomParamLearner(double[][][] cpd) {
        _cpd = cpd;
    }

    /**
	 * This method return a ICFreqEstimator containing the conditional probability distributions
	 * of the Bayesian network
	 * @param data Dataset descibing the list of attributes. The information about instances is ignored
	 * @param parentList List of parents of each attribute. 
	 * @return ICFreqEstimator with the parameters of the Bayesnet
	 * @throws ICParameterException
	 */
    @Override
    public ICFreqEstimator getParameters(ICData data, int[][] parentList) throws ICParameterException {
        return new ICFreqEstimator(data.getAttList(), parentList, _cpd);
    }
}
