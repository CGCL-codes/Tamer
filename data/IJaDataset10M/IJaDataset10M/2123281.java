package playground.mrieser.svi.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mrieser
 */
public class DynamicODMatrix {

    private final Map<Integer, Map<String, Map<String, Integer>>> odms = new HashMap<Integer, Map<String, Map<String, Integer>>>();

    private final int binSize;

    private final double maxTime;

    public DynamicODMatrix(final int binSize, final double maxTime) {
        this.binSize = binSize;
        this.maxTime = maxTime;
    }

    public int getBinSize() {
        return this.binSize;
    }

    public int getNOfBins() {
        return (int) this.maxTime / this.binSize;
    }

    public Map<String, Map<String, Integer>> getMatrixForTimeBin(final int timeBinIndex) {
        return this.odms.get(timeBinIndex);
    }

    public void addTrip(final double time, final String fromZoneId, final String toZoneId) {
        addTrip(time, fromZoneId, toZoneId, 1);
    }

    public void addTrip(final double time, final String fromZoneId, final String toZoneId, final int nOfTrips) {
        int slot = getTimeSlot(time);
        Map<String, Map<String, Integer>> odm = this.odms.get(slot);
        if (odm == null) {
            odm = new HashMap<String, Map<String, Integer>>();
            this.odms.put(slot, odm);
        }
        Map<String, Integer> toValues = odm.get(fromZoneId);
        if (toValues == null) {
            toValues = new HashMap<String, Integer>();
            odm.put(fromZoneId, toValues);
            toValues.put(toZoneId, nOfTrips);
        } else {
            Integer oldValue = toValues.get(toZoneId);
            if (oldValue == null) {
                toValues.put(toZoneId, nOfTrips);
            } else {
                toValues.put(toZoneId, oldValue.intValue() + nOfTrips);
            }
        }
    }

    private int getTimeSlot(final double time) {
        if (time > this.maxTime) {
            return (int) this.maxTime / this.binSize;
        }
        if (time < 0) {
            return 0;
        }
        return (int) time / this.binSize;
    }
}
