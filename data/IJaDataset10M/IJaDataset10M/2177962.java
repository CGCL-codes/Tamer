package mulan.evaluation.measure;

import mulan.evaluation.loss.RankingLossFunction;

/**
 *
 * @author Grigorios Tsoumakas
 * @version 2010.11.10
 */
public abstract class LossBasedRankingMeasureBase extends RankingMeasureBase {

    private final RankingLossFunction loss;

    /**
     * Creates a loss-based ranking measure
     *
     * @param aLoss a ranking loss function
     */
    public LossBasedRankingMeasureBase(RankingLossFunction aLoss) {
        loss = aLoss;
    }

    @Override
    protected void updateRanking(int[] ranking, boolean[] truth) {
        sum += loss.computeLoss(ranking, truth);
        count++;
    }

    public String getName() {
        return loss.getName();
    }

    public double getIdealValue() {
        return 0;
    }
}
