package wekaadapters;

import configuration.classifiers.ClassifierConfig;

/**
 * This object adapts game classifier to weka classifier
 */
public class WGClassifier implements game.classifiers.Classifier {

    private weka.classifiers.Classifier wekaCls;

    /**
     * Constructor that loads the Weka classifer
     *
     * @param wekaclassifier     The source Weka classifer.
     */
    protected WGClassifier(weka.classifiers.Classifier wekaclassifier) {
        super();
        this.wekaCls = wekaclassifier;
    }

    public void setOutputsNumber(int outputs) {
    }

    public void init(ClassifierConfig cfg) {
    }

    public void learn() {
    }

    public void relearn() {
    }

    public void learn(int modelIndex) {
    }

    public double[] getOutputProbabilities(double[] input_vector) {
        return new double[0];
    }

    public double[] getNormalizedOutputProbabilities(double[] normalized_input_vector) {
        return new double[0];
    }

    public int getOutput(double[] input_vector) {
        return 0;
    }

    public int getMaxLearningVectors() {
        return 0;
    }

    public void setMaxLearningVectors(int maxVectors) {
    }

    public void resizeDataFields(int updatedMaxLearningVectors, int updatedInputsNumber, int updatedOutputsNumber) {
    }

    public void useLearningVector(int index) {
    }

    public void storeLearningVector(double[] input, double[] output) {
    }

    public boolean isLearned() {
        return false;
    }

    public String toEquation(String[] inputEquation) {
        return null;
    }

    public String[] getEquations(String[] inputEquation) {
        return new String[0];
    }

    public String getName() {
        return null;
    }

    public void setName(String name) {
    }

    public int[] getTargetVariables() {
        return new int[0];
    }

    public void deleteLearningVectors() {
    }

    public void resetLearningData() {
    }

    public void setInputsNumber(int inputs) {
    }

    public Class getConfigClass() {
        return null;
    }
}
