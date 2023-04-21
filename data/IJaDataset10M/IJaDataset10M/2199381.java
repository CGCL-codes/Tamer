package emil.poker.ai.opponentEvaluators;

import java.util.Map;
import java.util.Map.Entry;

public class EvaluatorUtils {

    @Deprecated
    public static void _weightChancePlayerHasValues(Map map) {
        double sumOfAllChances = 0;
        for (Object entry : map.entrySet()) {
            sumOfAllChances += (Double) ((Entry) entry).getValue();
        }
        for (Object entry : map.entrySet()) {
            double weightedProbability = (Double) ((Entry) entry).getValue() / sumOfAllChances;
            if (Double.isNaN(weightedProbability)) {
                map.put(((Entry) entry).getKey(), 0.0);
            } else {
                map.put(((Entry) entry).getKey(), weightedProbability);
            }
        }
    }
}
