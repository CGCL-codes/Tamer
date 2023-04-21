package moa.evaluation;

import moa.AbstractMOAObject;
import moa.core.Measurement;
import weka.core.Instance;

/**
 * Regression evaluator that performs basic incremental evaluation.
 *
 * @author Albert Bifet (abifet at cs dot waikato dot ac dot nz)
 * @version $Revision: 7 $
 */
public class BasicRegressionPerformanceEvaluator extends AbstractMOAObject implements ClassificationPerformanceEvaluator {

    private static final long serialVersionUID = 1L;

    protected double weightObserved;

    protected double squareError;

    protected double averageError;

    @Override
    public void reset() {
        this.weightObserved = 0.0;
        this.squareError = 0.0;
        this.averageError = 0.0;
    }

    @Override
    public void addResult(Instance inst, double[] prediction) {
        if (inst.weight() > 0.0) {
            this.weightObserved += inst.weight();
            if (prediction.length > 0 && this.weightObserved != inst.weight()) {
                this.squareError += (inst.classValue() - prediction[0]) * (inst.classValue() - prediction[0]);
                this.averageError += Math.abs(inst.classValue() - prediction[0]);
            }
        }
    }

    @Override
    public Measurement[] getPerformanceMeasurements() {
        return new Measurement[] { new Measurement("classified instances", getTotalWeightObserved()), new Measurement("mean absolute error", getMeanError()), new Measurement("root mean squared error", getSquareError()) };
    }

    public double getTotalWeightObserved() {
        return this.weightObserved;
    }

    public double getMeanError() {
        return this.weightObserved > 0.0 ? this.averageError / this.weightObserved : 0.0;
    }

    public double getSquareError() {
        return Math.sqrt(this.weightObserved > 0.0 ? this.squareError / this.weightObserved : 0.0);
    }

    @Override
    public void getDescription(StringBuilder sb, int indent) {
        Measurement.getMeasurementsDescription(getPerformanceMeasurements(), sb, indent);
    }
}
