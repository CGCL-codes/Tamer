package org.jdmp.core;

import static org.junit.Assert.assertEquals;
import org.jdmp.core.algorithm.classification.Classifier;
import org.jdmp.core.algorithm.classification.RandomClassifier;
import org.jdmp.core.dataset.ClassificationDataSet;
import org.jdmp.core.dataset.CrossValidation;
import org.jdmp.core.dataset.DataSetFactory;
import org.junit.Test;
import org.ujmp.core.listmatrix.ListMatrix;

public class TestRandomClassifier {

    @Test
    public void testIrisClassification() throws Exception {
        ClassificationDataSet iris = DataSetFactory.IRIS();
        Classifier c = new RandomClassifier();
        ListMatrix<Double> results = CrossValidation.run(c, iris, 10, 10, 0);
        assertEquals(0.33, results.getMeanValue(), 0.04);
    }
}
