package org.jdmp.core.algorithm.clustering;

import org.jdmp.core.algorithm.AbstractAlgorithm;
import org.jdmp.core.dataset.RegressionDataSet;
import org.jdmp.core.sample.Sample;
import org.ujmp.core.Matrix;

public abstract class AbstractClusterer extends AbstractAlgorithm implements Clusterer {

    private static final long serialVersionUID = -8045773409890719666L;

    public AbstractClusterer() {
        super();
    }

    public void predict(RegressionDataSet dataSet) throws Exception {
        for (Sample sample : dataSet.getSamples()) {
            predict(sample);
        }
    }

    public final void predict(Sample sample) throws Exception {
        Matrix output = predict(sample.getVariables().getMatrix(INPUT), sample.getVariables().getMatrix(WEIGHT));
        sample.getVariables().setMatrix(PREDICTED, output);
    }
}
