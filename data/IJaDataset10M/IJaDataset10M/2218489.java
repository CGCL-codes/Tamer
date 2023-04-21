package be.vds.jtbdive.model;

import java.util.List;
import java.util.Map;

/**
 * This is the profile of a Dive, containing the different entries (second,
 * depth) and the different warnings that are referenced with the seconds of the
 * depth entries
 * 
 * @author Vanderslyen.G
 * 
 */
public class DiveProfile {

    private Map<Integer, Double> depthEntries;

    private List<Integer> decoWarnings;

    private List<Integer> ascentWarnings;

    private List<Integer> remainingBottomTimeWarnings;

    private List<Integer> decoEntries;

    public Map<Integer, Double> getDepthEntries() {
        return depthEntries;
    }

    public void setDepthEntries(Map<Integer, Double> depthEntries) {
        this.depthEntries = depthEntries;
    }

    public void setDecoWarnings(List<Integer> decoWarnings) {
        this.decoWarnings = decoWarnings;
    }

    public List<Integer> getAscentWarnings() {
        return ascentWarnings;
    }

    public void setAscentWarnings(List<Integer> ascentWarnings) {
        this.ascentWarnings = ascentWarnings;
    }

    public List<Integer> getDecoWarnings() {
        return decoWarnings;
    }

    /**
	 * Gets the last second recorded in the depth entries
	 * 
	 * @return
	 */
    public int getLastDepthEntry() {
        int result = 0;
        for (Integer second : depthEntries.keySet()) {
            if (second > result) {
                result = second;
            }
        }
        return result;
    }

    public void setRemainingBottomTimeWarnings(List<Integer> remainingBottomTimeWarnings) {
        this.remainingBottomTimeWarnings = remainingBottomTimeWarnings;
    }

    public void setDecoEntries(List<Integer> decoEntries) {
        this.decoEntries = decoEntries;
    }

    public List<Integer> getRemainingBottomTimeWarnings() {
        return remainingBottomTimeWarnings;
    }

    public List<Integer> getDecoEntries() {
        return decoEntries;
    }
}
