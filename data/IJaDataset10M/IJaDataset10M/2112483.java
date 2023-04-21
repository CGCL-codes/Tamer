package moa.classifiers;

import moa.core.Measurement;
import moa.options.FloatOption;
import weka.core.Instance;

/**
 * Single perceptron classifier.
 *
 * <p>Performs classic perceptron multiclass learning incrementally.</p>
 *
 * <p>Parameters:</p>
 * <ul>
 * <li>-r : Learning ratio of the classifier</li>
 * </ul>
 *
 * @author Albert Bifet (abifet at cs dot waikato dot ac dot nz)
 * @version $Revision: 7 $
 */
public class Perceptron extends AbstractClassifier {

    private static final long serialVersionUID = 221L;

    @SuppressWarnings("hiding")
    public static final String classifierPurposeString = "Perceptron classifier: Single perceptron classifier.";

    public FloatOption learningRatioOption = new FloatOption("learningRatio", 'r', "Learning ratio", 1);

    protected double[][] weightAttribute;

    protected boolean reset;

    protected int numberAttributes;

    protected int numberClasses;

    protected int numberDetections;

    @Override
    public void resetLearningImpl() {
        this.reset = true;
    }

    @Override
    public void trainOnInstanceImpl(Instance inst) {
        if (this.reset == true) {
            this.reset = false;
            this.numberAttributes = inst.numAttributes();
            this.numberClasses = inst.numClasses();
            this.weightAttribute = new double[inst.numClasses()][inst.numAttributes()];
            for (int i = 0; i < inst.numClasses(); i++) {
                for (int j = 0; j < inst.numAttributes(); j++) {
                    weightAttribute[i][j] = 0.2 * Math.random() - 0.1;
                }
            }
        }
        double[] preds = new double[inst.numClasses()];
        for (int i = 0; i < inst.numClasses(); i++) {
            preds[i] = prediction(inst, i);
        }
        double learningRatio = learningRatioOption.getValue();
        int actualClass = (int) inst.classValue();
        for (int i = 0; i < inst.numClasses(); i++) {
            double actual = (i == actualClass) ? 1.0 : 0.0;
            double delta = (actual - preds[i]) * preds[i] * (1 - preds[i]);
            for (int j = 0; j < inst.numAttributes() - 1; j++) {
                this.weightAttribute[i][j] += learningRatio * delta * inst.value(j);
            }
            this.weightAttribute[i][inst.numAttributes() - 1] += learningRatio * delta;
        }
    }

    public void setWeights(double[][] w) {
        this.weightAttribute = w;
    }

    public double[][] getWeights() {
        return this.weightAttribute;
    }

    public int getNumberAttributes() {
        return this.numberAttributes;
    }

    public int getNumberClasses() {
        return this.numberClasses;
    }

    public double prediction(Instance inst, int classVal) {
        double sum = 0.0;
        for (int i = 0; i < inst.numAttributes() - 1; i++) {
            sum += weightAttribute[classVal][i] * inst.value(i);
        }
        sum += weightAttribute[classVal][inst.numAttributes() - 1];
        return 1.0 / (1.0 + Math.exp(-sum));
    }

    @Override
    public double[] getVotesForInstance(Instance inst) {
        double[] votes = new double[inst.numClasses()];
        if (this.reset == false) {
            for (int i = 0; i < votes.length; i++) {
                votes[i] = prediction(inst, i);
            }
            try {
                weka.core.Utils.normalize(votes);
            } catch (Exception e) {
            }
        }
        return votes;
    }

    @Override
    protected Measurement[] getModelMeasurementsImpl() {
        return null;
    }

    @Override
    public void getModelDescription(StringBuilder out, int indent) {
    }

    @Override
    public boolean isRandomizable() {
        return false;
    }
}
