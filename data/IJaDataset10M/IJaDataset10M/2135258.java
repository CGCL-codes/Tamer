package mulan.evaluation.measure;

import mulan.evaluation.loss.BipartitionLossFunction;

/**
 *
 * @author Grigorios Tsoumakas
 * @version 2010.11.10
 */
public abstract class LossBasedBipartitionMeasureBase extends ExampleBasedBipartitionMeasureBase {

    private final BipartitionLossFunction loss;

    /**
     * Creates a loss-based bipartition measure
     *
     * @param aLoss a bipartition loss function
     */
    public LossBasedBipartitionMeasureBase(BipartitionLossFunction aLoss) {
        loss = aLoss;
    }

    @Override
    public void updateBipartition(boolean[] bipartition, boolean[] truth) {
        sum += loss.computeLoss(bipartition, truth);
        count++;
    }

    public String getName() {
        return loss.getName();
    }

    public double getIdealValue() {
        return 0;
    }
}
