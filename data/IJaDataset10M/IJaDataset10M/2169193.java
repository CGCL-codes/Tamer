package org.jdmp.core.algorithm.regression;

import org.jdmp.core.dataset.RegressionDataSet;
import org.jdmp.core.sample.Sample;
import org.jdmp.core.util.ExportInterface;
import org.ujmp.core.Matrix;

public interface Regressor extends ExportInterface {

    public void train(RegressionDataSet dataSet) throws Exception;

    public void reset() throws Exception;

    public void train(Matrix input, Matrix sampleWeight, Matrix target) throws Exception;

    public void predict(Sample sample) throws Exception;

    public Matrix predict(Matrix input) throws Exception;

    public void train(Matrix input, Matrix target) throws Exception;

    public Matrix predict(Matrix input, Matrix sampleWeight) throws Exception;

    public void train(Sample sample) throws Exception;

    public void predict(RegressionDataSet dataSet) throws Exception;

    public Regressor emptyCopy() throws Exception;
}
