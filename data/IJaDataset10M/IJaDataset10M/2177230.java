package edu.colorado.emml.classifiers;

import java.util.ArrayList;
import edu.colorado.emml.ensemble.PredictionSet;
import edu.colorado.emml.ensemble_deprecated.ICombinationRule;
import edu.colorado.emml.ensemble_deprecated.IPredictionSet;

public class PredictionResult {

    private PredictionSet trainPredictions;

    private PredictionSet testPredictions;

    public PredictionResult(ArrayList<double[]> trainPredictions, ArrayList<double[]> testPredictions) {
        this(new PredictionSet("train", trainPredictions), new PredictionSet("test", testPredictions));
    }

    public PredictionResult(PredictionSet trainPredictions, PredictionSet testPredictions) {
        this.trainPredictions = trainPredictions;
        this.testPredictions = testPredictions;
    }

    public void setDescription(String description) {
        this.trainPredictions.setDescription(description);
        this.testPredictions.setDescription(description);
    }

    public PredictionSet getTrainPredictions() {
        return trainPredictions;
    }

    public PredictionSet getTestPredictions() {
        return testPredictions;
    }

    private static IPredictionSet[] getTestResults(PredictionResult[] results) {
        IPredictionSet[] train = new IPredictionSet[results.length];
        for (int i = 0; i < train.length; i++) {
            train[i] = results[i].getTestPredictions();
        }
        return train;
    }

    private static IPredictionSet[] getTrainResults(PredictionResult[] results) {
        IPredictionSet[] train = new IPredictionSet[results.length];
        for (int i = 0; i < train.length; i++) {
            train[i] = results[i].getTrainPredictions();
        }
        return train;
    }

    public static PredictionResult getVote(PredictionResult[] results) {
        return combine(new ICombinationRule.Vote(), results);
    }

    public static PredictionResult getAverage(PredictionResult[] results) {
        return combine(new ICombinationRule.Average(), results);
    }

    private static PredictionResult combine(ICombinationRule combinationRule, PredictionResult[] results) {
        return new PredictionResult(PredictionSet.getCombination(combinationRule, getTrainResults(results)), PredictionSet.getCombination(combinationRule, getTestResults(results)));
    }

    public String toString() {
        return "trainPredictions=" + trainPredictions + ", testPredictions=" + testPredictions;
    }
}
