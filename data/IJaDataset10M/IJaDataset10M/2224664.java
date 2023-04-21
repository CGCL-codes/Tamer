package org.jdmp.core.algorithm.regression;

import org.jdmp.core.algorithm.classification.AbstractClassifier;
import org.jdmp.core.algorithm.classification.Classifier;
import org.jdmp.core.dataset.RegressionDataSet;
import org.jdmp.core.variable.Variable;
import org.jdmp.core.variable.VariableFactory;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.exceptions.MatrixException;

/**
 * AlgorithmLinearRegression extends AlgorithmClassifier and not
 * AlgorithmRegression because also classification is possible using regression
 */
public class LinearRegression extends AbstractClassifier {

    private static final long serialVersionUID = 3483912497269476834L;

    public static final String PARAMETERS = "Parameters";

    public LinearRegression() {
        super();
        setParameterVariable(VariableFactory.labeledVariable("Regression Parameters"));
    }

    public void setParameterVariable(Variable variable) {
        setVariable(PARAMETERS, variable);
    }

    public Variable getParameterVariable() {
        return getVariables().get(PARAMETERS);
    }

    public Matrix getParameterMatrix() {
        return getParameterVariable().getMatrix();
    }

    public Matrix predict(Matrix input, Matrix sampleWeight) throws Exception {
        Matrix bias = MatrixFactory.ones(input.getRowCount(), 1);
        return MatrixFactory.horCat(input, bias).mtimes(getParameterMatrix());
    }

    public void train(Matrix input, Matrix sampleWeight, Matrix targetOutput) throws Exception {
        throw (new Exception("pattern-by-pattern learning not supported"));
    }

    public void train(RegressionDataSet dataSet) throws Exception {
        Matrix input = dataSet.getInputMatrix();
        Matrix bias = MatrixFactory.ones(input.getRowCount(), 1);
        Matrix x = MatrixFactory.horCat(input, bias);
        Matrix y = dataSet.getTargetMatrix();
        Matrix parameters = x.pinv().mtimes(y);
        setParameterMatrix(parameters);
    }

    public void setParameterMatrix(Matrix parameters) {
        getVariables().setMatrix(PARAMETERS, parameters);
    }

    public void reset() throws MatrixException {
        getParameterVariable().clear();
    }

    public Classifier emptyCopy() {
        return new LinearRegression();
    }
}
