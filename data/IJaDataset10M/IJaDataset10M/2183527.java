package lpcforsos.evaluation.losses;

import java.util.ArrayList;

/**
 * Calculate the Kendall's Tau Measure normalized to a value between 0 and 1.
 * This is defined as
 * <p>
 * 
 * <pre>
 * Number of discordant pairs of labels
 * ------------------------------------
 * 0.5 * m * (m-1)
 *  
 * m: number of labels
 * </pre>
 * 
 * @author Tobias Plötz
 * 
 */
public class KendallsTauNormalizedLoss implements ILabelRankingLossFunction {

    @Override
    public double calculateLossFunction(ArrayList<String> predictedOrderOfLabels, ArrayList<String> totalOrderOfLabels) {
        double numDiscordantLabels = new KendallsTauLoss().calculateLossFunction(predictedOrderOfLabels, totalOrderOfLabels);
        int m = predictedOrderOfLabels.size();
        double normalized = 2 * numDiscordantLabels / (m * (m - 1));
        return normalized;
    }
}
