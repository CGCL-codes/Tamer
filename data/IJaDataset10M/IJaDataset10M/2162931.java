package fr.lelouet.tools.regression.normalized;

import java.util.HashMap;
import fr.lelouet.tools.regression.Result;

public class RelativeResult extends Result {

    public RelativeResult() {
    }

    public RelativeResult(Result r) {
        getEstimates().putAll(r.getEstimates());
        setConstantEstimate(r.getConstantEstimate());
        getErrors().putAll(r.getErrors());
    }

    private HashMap<String, Double> relativeWeights = new HashMap<String, Double>();

    public HashMap<String, Double> getRelativeWeights() {
        return relativeWeights;
    }
}
