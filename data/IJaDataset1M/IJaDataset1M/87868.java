package mulan.evaluation.loss;

import mulan.classifier.MultiLabelOutput;

/**
 * Interfance for loss functions
 *
 * @author Grigorios Tsoumakas
 * @version 2010.11.10
 */
public interface MultiLabelLossFunction {

    /**
     * Returns the name of the loss function
     *
     * @return the name of the loss function
     */
    public String getName();

    /**
     * Computes the loss function
     * 
     * @param prediction the prediction of the learner for an example
     * @param groundTruth the ground truth of the example
     * @return the value of the loss function
     */
    public double computeLoss(MultiLabelOutput prediction, boolean[] groundTruth);
}
