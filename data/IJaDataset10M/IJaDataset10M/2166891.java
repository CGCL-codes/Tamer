package model.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.map.Weather;

/**
 * A firing range for a {@link Weapon}.
 * 
 * @23 juil. 2010
 * @author Gronowski St�phane stephane.gronowski@gmail.com
 */
public class FiringRange {

    private Map<Weather, List<BasicRange>> firingRanges = new HashMap<Weather, List<BasicRange>>();

    /**
     * Create a empty {@link FiringRange};
     */
    public FiringRange() {
        for (Weather weather : Weather.values()) firingRanges.put(weather, new ArrayList<BasicRange>(0));
    }

    /**
     * Return the Firing range for a specific {@link Weather}.
     * 
     * @param weather
     *            the {@link Weather}.
     * @return the different {@link BasicRange}, the nearest to the farthest
     */
    public List<BasicRange> getFiringRange(Weather weather) {
        return firingRanges.get(weather);
    }

    /**
     * Duplicate the {@link FiringRange}.
     * 
     * @return a copy of this {@link FiringRange}
     */
    public FiringRange duplicate() {
        FiringRange res = new FiringRange();
        for (Weather weather : firingRanges.keySet()) {
            List<BasicRange> tmp = new ArrayList<BasicRange>();
            for (BasicRange range : firingRanges.get(weather)) tmp.add(range.duplicate());
            res.addFiringRange(weather, tmp);
        }
        return res;
    }

    /**
     * Add a firing range.
     * 
     * @param weather
     *            the specific weather
     * @param ranges
     *            the ranges
     */
    public void addFiringRange(Weather weather, List<BasicRange> ranges) {
        for (int i = 1; i < ranges.size(); i++) if (ranges.get(i).getMin() < ranges.get(i - 1).getMax()) throw new IllegalArgumentException("Illegal ranges, rank : " + i + ", they must be the nearest to the farthest");
        firingRanges.put(weather, ranges);
    }

    /**
     * Merge the specified {@link FiringRange} with this {@link FiringRange}.
     * 
     * @param firingRange
     *            the {@link FiringRange} to merge
     */
    public void mergeFiringRange(FiringRange firingRange) {
        for (Weather weather : firingRanges.keySet()) {
            for (BasicRange range : firingRange.getFiringRange(weather)) {
                BasicRange tmp = null;
                List<BasicRange> ranges = firingRanges.get(weather);
                int i;
                for (i = 0; i < ranges.size(); i++) {
                    tmp = ranges.get(i);
                    if (tmp.merge(range)) break;
                    if (range.getMax() < tmp.getMin()) {
                        ranges.add(i, range.duplicate());
                        break;
                    }
                }
                if (i == ranges.size()) ranges.add(range);
            }
        }
    }
}
