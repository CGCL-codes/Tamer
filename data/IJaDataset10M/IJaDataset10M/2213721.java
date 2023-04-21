package edu.cmu.minorthird.classify.algorithms.linear;

import edu.cmu.minorthird.classify.Classifier;
import edu.cmu.minorthird.classify.Example;
import edu.cmu.minorthird.classify.OnlineBinaryClassifierLearner;

/**
 * Perceptron algorithm. Slighly modified to (a) update when
 * examples don't satisfy a margin requirement and (b) optionally,
 * classify with a voting scheme.
 *
 * @author William Cohen
 */
public class MarginPerceptron extends OnlineBinaryClassifierLearner {

    private Hyperplane s_t, w_t;

    private double minMargin = 1.0;

    private boolean voteBeforeTrainingComplete;

    private boolean voteAfterTrainingComplete;

    private boolean trainingComplete;

    private double numExamples;

    public MarginPerceptron() {
        this(1.0, false, false);
    }

    public MarginPerceptron(double minMargin, boolean voteBeforeTrainingComplete, boolean voteAfterTrainingComplete) {
        this.minMargin = minMargin;
        this.voteBeforeTrainingComplete = voteBeforeTrainingComplete;
        this.voteAfterTrainingComplete = voteAfterTrainingComplete;
        reset();
    }

    public void reset() {
        s_t = new Hyperplane();
        w_t = new Hyperplane();
        trainingComplete = false;
        numExamples = 0;
    }

    public void addExample(Example example) {
        numExamples++;
        double y_t = example.getLabel().numericLabel();
        if (w_t.score(example.asInstance()) * y_t <= minMargin) {
            w_t.increment(example, y_t);
        }
        if (voteAfterTrainingComplete || voteBeforeTrainingComplete) {
            s_t.increment(w_t, 1.0);
        }
    }

    public void completeTraining() {
        trainingComplete = true;
        s_t.multiply(1.0 / numExamples);
    }

    public Classifier getClassifier() {
        if (voteBeforeTrainingComplete) return s_t; else if (voteAfterTrainingComplete && trainingComplete) return s_t; else return w_t;
    }

    public String toString() {
        return "[MarginPerceptron " + minMargin + ";" + voteBeforeTrainingComplete + ";" + voteAfterTrainingComplete + "]";
    }
}
