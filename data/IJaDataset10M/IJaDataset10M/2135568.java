package org.debellor.weka;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.debellor.base.evaluator.EvaluatorCell;
import org.debellor.base.evaluator.TrainAndTest;
import org.debellor.base.evaluator.score.RMSE;
import org.debellor.core.Cell;
import org.debellor.core.Sample;
import org.debellor.core.Cell.Stream;
import org.debellor.core.Sample.SampleType;
import org.debellor.core.cell.BatchOfSamples;
import org.debellor.core.data.DataVector.DataVectorType;
import org.debellor.regression.CommonData;
import org.debellor.weka.ArffReader;
import org.debellor.weka.WekaClassifier;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Marcin Wojnarski
 *
 */
public class WekaOptionHandling {

    static final double EXPECTED_K1 = 1.6900;

    static final double EXPECTED_K10 = 1.6190;

    @Test()
    public void kNN_on_cloud() throws Exception {
        Cell arff = new ArffReader();
        arff.set("filename", "data/cloud.arff");
        arff.set("decisionAttr", "last");
        Cell learner;
        double v;
        learner = new WekaClassifier(weka.classifiers.lazy.IBk.class, "-K 1");
        learner.setSource(arff);
        learner.learn();
        learner.open();
        v = learner.next().decision.value();
        assertEquals(EXPECTED_K1, v, 0.0001);
        learner.close();
        learner = new WekaClassifier("IBk      -K 10");
        learner.setSource(arff);
        learner.learn();
        learner.open();
        v = learner.next().decision.value();
        assertEquals(EXPECTED_K10, v, 0.0001);
        learner.close();
        learner = new WekaClassifier("IBk -K 1");
        learner.set("KNN", 10);
        learner.setSource(arff);
        learner.learn();
        learner.open();
        v = learner.next().decision.value();
        assertEquals(EXPECTED_K10, v, 0.0001);
    }
}
