package wekaadapters;

import static org.junit.Assert.*;
import game.data.GlobalData;
import game.data.IOData;
import game.data.TreeData;
import game.data.OutputAttribute;
import game.models.Models;
import org.junit.BeforeClass;
import org.junit.Test;
import weka.core.Instances;
import weka.classifiers.Evaluation;
import weka.classifiers.Classifier;
import configuration.models.single.LinearModelConfig;
import configuration.models.game.trainers.QuasiNewtonConfig;

/**
 * Test the correct classifier function by introducing DummyModel. This way we know what
 * results the model will give.
 */
public class TestClassifierDummy {

    static final String filename = "../../data/Test/dummy-data.txt";

    static IOData ioData;

    static TreeData data;

    static GAMEToWekaInstancesFactory instancesFactory;

    static GAMEToWekaClassifierFactory classifierFactory;

    static Instances instances;

    static DummyModel linearModel;

    static Models models;

    static OutputAttribute output;

    static Classifier classifier;

    static Evaluation evaluation;

    static LinearModelConfig cfg;

    /**
     * Creates the basic objects we need for testing. This means loading the data into TreeData,
     * constructing models over the data and creating the Weka Evaluation object which includes
     * Classifier and Instances.
     */
    @BeforeClass
    public static void loadTreeDataAndGenerateModels() {
        int[] modelsUsed = { 0, 1, 2 };
        models = Models.getInstance();
        data = GlobalData.getInstance();
        ioData = new IOData(data);
        ioData.readDataFile(filename, true);
        instancesFactory = GAMEToWekaInstancesFactory.getInstance();
        classifierFactory = GAMEToWekaClassifierFactory.getInstance();
        for (int i = 0; i < data.getONumber(); i++) {
            output = (OutputAttribute) data.getOAttr().elementAt(i % data.getONumber());
            cfg = new LinearModelConfig();
            DummyModel l = new DummyModel(i);
            cfg.setTargetVariable(i);
            cfg.setTrainerCfg(new QuasiNewtonConfig());
            cfg.setTrainerClassName("QuasiNewtonTrainer");
            models.generateSingleModel(l, cfg);
        }
        try {
            classifier = GAMEToWekaClassifierFactory.getInstance().createClassifier(data, models, modelsUsed);
            instances = GAMEToWekaInstancesFactory.getInstance().generateWekaInstances(data);
            evaluation = new Evaluation(instances);
            evaluation.evaluateModel(classifier, instances);
        } catch (Exception ex) {
            fail("Exception caused fail.");
        }
    }

    /**
     * Test if number of correctly classified classes match.
     */
    @Test
    public void testCorrect() {
        assertEquals("Number of correctly classified:", 11.0, evaluation.correct(), 0.1);
    }

    /**
     * Test if number of not classified classes match.
     */
    @Test
    public void testNotClassified() {
        assertEquals("Number of not classified:", 1.0, evaluation.unclassified(), 0.1);
    }

    /**
     * Test if number of incorrectly classified classes match.
     */
    @Test
    public void testIncorrectlyClassified() {
        assertEquals("Number of incorrectly classified:", 3.0, evaluation.incorrect(), 0.1);
    }

    /**
     * Test if mean root mean squared error matches.
     */
    @Test
    public void testMeanAbsoluteError() {
        assertEquals("Mean absolute error:", 0.377, evaluation.rootMeanSquaredError(), 0.005);
    }

    /**
     * Testes false positive rate.
     */
    @Test
    public void testFPr() {
        assertEquals("FPr 1:", 0.22, evaluation.falsePositiveRate(0), 0.005);
        assertEquals("FPr 2:", 0.11, evaluation.falsePositiveRate(1), 0.005);
        assertEquals("FPr 3:", 0.0, evaluation.falsePositiveRate(2), 0.005);
    }

    /**
     * Testes true positive rate.
     */
    @Test
    public void testTPr() {
        assertEquals("TPr 1:", 0.8, evaluation.truePositiveRate(0), 0.005);
        assertEquals("TPr 2:", 0.6, evaluation.truePositiveRate(1), 0.005);
        assertEquals("TPr 3:", 1.0, evaluation.truePositiveRate(2), 0.005);
    }
}
