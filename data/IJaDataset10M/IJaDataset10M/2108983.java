package moa.streams.generators;

import java.util.Random;
import moa.options.IntOption;
import moa.options.FloatOption;
import weka.core.Instance;

/**
 * Stream generator for a random radial basis function stream with drift.
 *
 * @author Albert Bifet (abifet at cs dot waikato dot ac dot nz)
 * @version $Revision: 7 $
 */
public class RandomRBFGeneratorDrift extends RandomRBFGenerator {

    @Override
    public String getPurposeString() {
        return "Generates a random radial basis function stream with drift.";
    }

    private static final long serialVersionUID = 1L;

    public FloatOption speedChangeOption = new FloatOption("speedChange", 's', "Speed of change of centroids in the model.", 0, 0, Float.MAX_VALUE);

    public IntOption numDriftCentroidsOption = new IntOption("numDriftCentroids", 'k', "The number of centroids with drift.", 50, 0, Integer.MAX_VALUE);

    protected double[][] speedCentroids;

    @Override
    public Instance nextInstance() {
        int len = this.numDriftCentroidsOption.getValue();
        if (len > this.centroids.length) {
            len = this.centroids.length;
        }
        for (int j = 0; j < len; j++) {
            for (int i = 0; i < this.numAttsOption.getValue(); i++) {
                this.centroids[j].centre[i] += this.speedCentroids[j][i] * this.speedChangeOption.getValue();
                if (this.centroids[j].centre[i] > 1) {
                    this.centroids[j].centre[i] = 1;
                    this.speedCentroids[j][i] = -this.speedCentroids[j][i];
                }
                if (this.centroids[j].centre[i] < 0) {
                    this.centroids[j].centre[i] = 0;
                    this.speedCentroids[j][i] = -this.speedCentroids[j][i];
                }
            }
        }
        return super.nextInstance();
    }

    @Override
    protected void generateCentroids() {
        super.generateCentroids();
        Random modelRand = new Random(this.modelRandomSeedOption.getValue());
        int len = this.numDriftCentroidsOption.getValue();
        if (len > this.centroids.length) {
            len = this.centroids.length;
        }
        this.speedCentroids = new double[len][this.numAttsOption.getValue()];
        for (int i = 0; i < len; i++) {
            double[] randSpeed = new double[this.numAttsOption.getValue()];
            double normSpeed = 0.0;
            for (int j = 0; j < randSpeed.length; j++) {
                randSpeed[j] = modelRand.nextDouble();
                normSpeed += randSpeed[j] * randSpeed[j];
            }
            normSpeed = Math.sqrt(normSpeed);
            for (int j = 0; j < randSpeed.length; j++) {
                randSpeed[j] /= normSpeed;
            }
            this.speedCentroids[i] = randSpeed;
        }
    }

    @Override
    public void getDescription(StringBuilder sb, int indent) {
    }
}
