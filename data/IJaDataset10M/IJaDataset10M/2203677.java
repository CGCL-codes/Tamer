package yapgen.base.knowledge.plan.decision;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author riccardo
 */
public class ProbabilityDeltaMap {

    private Map<Object, Double> map = new HashMap<Object, Double>();

    public static class Data {

        private double probabilityMultiplier;

        private double probability;

        public Data(double probabilityMultiplier, double probability) {
            this.probabilityMultiplier = probabilityMultiplier;
            this.probability = probability;
        }

        public double getProbability() {
            return probability;
        }

        public double getProbabilityMultiplier() {
            return probabilityMultiplier;
        }
    }

    public ProbabilityDeltaMap(Map<Object, Data> probabilityDataMap) {
        double probabilityDelta;
        Object object;
        Data data;
        Object otherObject;
        Data otherData;
        for (Entry<Object, Data> dataEntry : probabilityDataMap.entrySet()) {
            object = dataEntry.getKey();
            data = dataEntry.getValue();
            probabilityDelta = 0.0;
            for (Entry<Object, Data> otherDataEntry : probabilityDataMap.entrySet()) {
                otherObject = otherDataEntry.getKey();
                otherData = otherDataEntry.getValue();
                if (object == otherObject) continue;
                if (otherData.getProbabilityMultiplier() < 1) {
                    probabilityDelta += (1 - otherData.getProbabilityMultiplier()) * otherData.getProbability() / (probabilityDataMap.size() - 1);
                }
            }
            if (data.getProbabilityMultiplier() < 1) {
                probabilityDelta -= data.getProbability() * (1 - data.getProbabilityMultiplier());
            }
            if (probabilityDelta != 0.0) {
                this.map.put(object, probabilityDelta);
            }
        }
    }

    public Map<Object, Double> getMap() {
        return map;
    }
}
