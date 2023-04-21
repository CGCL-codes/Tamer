package fr.lelouet.tools.regression;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * the result of a resolution . <br />
 * This associates a weight to all dimensions, plus a weigth to the constant
 * value
 * 
 * @author guigolum
 * 
 */
public class Result {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(Result.class);

    Map<String, Double> estimates = new HashMap<String, Double>();

    public Map<String, Double> getEstimates() {
        return estimates;
    }

    Map<String, Double> errors = new HashMap<String, Double>();

    public Map<String, Double> getErrors() {
        return errors;
    }

    protected double constantEstimate = 0.0;

    public double getConstantEstimate() {
        return constantEstimate;
    }

    public void setConstantEstimate(double constant) {
        constantEstimate = constant;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[ ").append(getConstantEstimate());
        for (Entry<String, Double> e : getEstimates().entrySet()) {
            sb.append(" + ").append(e.getKey() + " * ").append(e.getValue());
        }
        return sb.append(" ]").toString();
    }
}
