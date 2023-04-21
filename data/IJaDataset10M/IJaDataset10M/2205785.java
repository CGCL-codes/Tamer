package mulan.evaluation.measure;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the average precision measure. It evaluates the average
 * fraction of labels ranked above a particular relevant label, that are
 * actually relevant.
 * 
 * @author Jozef Vilcek
 * @author Grigorios Tsoumakas
 * @version 2010.11.05
 */
public class AveragePrecision extends RankingMeasureBase {

    public String getName() {
        return "Average Precision";
    }

    @Override
    public double getIdealValue() {
        return 1;
    }

    protected void updateRanking(int[] ranking, boolean[] trueLabels) {
        double avgP = 0;
        int numLabels = trueLabels.length;
        List<Integer> relevant = new ArrayList<Integer>();
        for (int index = 0; index < numLabels; index++) {
            if (trueLabels[index]) {
                relevant.add(index);
            }
        }
        if (relevant.size() != 0) {
            for (int r : relevant) {
                double rankedAbove = 0;
                for (int rr : relevant) {
                    if (ranking[rr] <= ranking[r]) {
                        rankedAbove++;
                    }
                }
                avgP += (rankedAbove / ranking[r]);
            }
            avgP /= relevant.size();
            sum += avgP;
            count++;
        }
    }
}
