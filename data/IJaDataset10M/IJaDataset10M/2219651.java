package dr.inference.distribution;

import dr.inference.model.Parameter;

/**
 * @author Marc A. Suchard
 */
public class LogisticRegression extends GeneralizedLinearModel {

    public LogisticRegression(Parameter dependentParam) {
        super(dependentParam);
    }

    protected double calculateLogLikelihoodAndGradient(double[] beta, double[] gradient) {
        return 0;
    }

    protected double calculateLogLikelihood(double[] beta) {
        throw new RuntimeException("Not yet implemented for optimization");
    }

    public boolean requiresScale() {
        return false;
    }

    protected double calculateLogLikelihood() {
        double logLikelihood = 0;
        double[] xBeta = getXBeta();
        for (int i = 0; i < N; i++) {
            logLikelihood += dependentParam.getParameterValue(i) * xBeta[i] - Math.log(1.0 + Math.exp(xBeta[i]));
        }
        return logLikelihood;
    }

    public boolean confirmIndependentParameters() {
        return true;
    }
}
