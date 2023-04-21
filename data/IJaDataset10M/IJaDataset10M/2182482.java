package mulan.evaluation.measure;

/**
 * Base class for label-based bipartition measures
 *
 * @author Grigorios Tsoumakas
 * @version 2011.09.06
 */
public abstract class LabelBasedBipartitionMeasureBase extends BipartitionMeasureBase {

    /** the number of labels */
    protected int numOfLabels;

    /** the number of false negative for each label */
    protected double[] falseNegatives;

    /** the number of true positives for each label */
    protected double[] truePositives;

    /** the number of false positives for each label */
    protected double[] falsePositives;

    /** the number of true negatives for each label */
    protected double[] trueNegatives;

    /**
     * Creates a new instance of this class
     *
     * @param aNumOfLabels the number of labels
     */
    public LabelBasedBipartitionMeasureBase(int aNumOfLabels) {
        numOfLabels = aNumOfLabels;
        falseNegatives = new double[numOfLabels];
        truePositives = new double[numOfLabels];
        falsePositives = new double[numOfLabels];
        trueNegatives = new double[numOfLabels];
    }

    public void reset() {
        for (int labelIndex = 0; labelIndex < numOfLabels; labelIndex++) {
            falseNegatives[labelIndex] = 0;
            truePositives[labelIndex] = 0;
            falsePositives[labelIndex] = 0;
            trueNegatives[labelIndex] = 0;
        }
    }
}
