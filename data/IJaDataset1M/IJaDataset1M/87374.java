package playground.dsantani.gpsProcessing;

import java.util.ArrayList;

public class GPSModeChainInTrip extends GPSModeChain {

    private double overallProbability;

    private double probabilityFromFuzzyLogic;

    private double probabilityFromModeTransitions;

    private double probabilityFromMapMatching;

    public GPSModeChainInTrip(String name, ArrayList<String> chain) {
        super(name, chain);
    }

    @Override
    public double getOverallProbability() {
        return this.overallProbability;
    }

    @Override
    public double getProbabilityFromFuzzyLogic() {
        return this.probabilityFromFuzzyLogic;
    }

    @Override
    public double getProbabilityFromMapMatching() {
        return this.probabilityFromMapMatching;
    }

    @Override
    public double getProbabilityFromModeTransitions() {
        return this.probabilityFromModeTransitions;
    }

    @Override
    public void setOverallProbability(double overallProbability) {
        this.overallProbability = overallProbability;
    }

    @Override
    public void setProbabilityFromFuzzyLogic(double probabilityFuzzy) {
        this.probabilityFromFuzzyLogic = probabilityFuzzy;
    }

    @Override
    public void setProbabilityFromMapMatching(double probabilityMapMatching) {
        this.probabilityFromMapMatching = probabilityMapMatching;
    }

    @Override
    public void setProbabilityFromModeTransitions(double probabilityModeTransition) {
        this.probabilityFromModeTransitions = probabilityModeTransition;
    }
}
