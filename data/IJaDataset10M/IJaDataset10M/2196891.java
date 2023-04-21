package com.rapidminer.tools.math.similarity.divergences;

import Jama.Matrix;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.Tools;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.tools.math.matrix.CovarianceMatrix;
import com.rapidminer.tools.math.similarity.BregmanDivergence;

/**
 * The &quot;Mahalanobis distance &quot;.
 * 
 * @author Sebastian Land, Regina Fritsch
 */
public class MahalanobisDistance extends BregmanDivergence {

    private static final long serialVersionUID = -5986526237805285428L;

    private Matrix inverseCovariance;

    @Override
    public double calculateDistance(double[] value1, double[] value2) {
        Matrix x = new Matrix(value1, value1.length);
        Matrix y = new Matrix(value2, value2.length);
        Matrix deltaxy = x.minus(y);
        return Math.sqrt(deltaxy.transpose().times(inverseCovariance).times(deltaxy).get(0, 0));
    }

    @Override
    public void init(ExampleSet exampleSet) throws OperatorException {
        super.init(exampleSet);
        Tools.onlyNumericalAttributes(exampleSet, "value based similarities");
        inverseCovariance = CovarianceMatrix.getCovarianceMatrix(exampleSet).inverse();
    }

    @Override
    public String toString() {
        return "Mahalanobis distance";
    }
}
