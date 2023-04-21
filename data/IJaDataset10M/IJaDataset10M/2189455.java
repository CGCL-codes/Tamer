package org.jrobin.data;

import org.jrobin.core.ConsolFuns;
import org.jrobin.core.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Aggregator implements ConsolFuns {

    private long timestamps[], step;

    private double[] values;

    Aggregator(long[] timestamps, double[] values) {
        assert timestamps.length == values.length : "Incompatible timestamps/values arrays (unequal lengths)";
        assert timestamps.length >= 2 : "At least two timestamps must be supplied";
        this.timestamps = timestamps;
        this.values = values;
        this.step = timestamps[1] - timestamps[0];
    }

    Aggregates getAggregates(long tStart, long tEnd) {
        Aggregates agg = new Aggregates();
        long totalSeconds = 0;
        boolean firstFound = false;
        for (int i = 0; i < timestamps.length; i++) {
            long left = Math.max(timestamps[i] - step, tStart);
            long right = Math.min(timestamps[i], tEnd);
            long delta = right - left;
            if (delta > 0) {
                double value = values[i];
                agg.min = Util.min(agg.min, value);
                agg.max = Util.max(agg.max, value);
                if (!firstFound) {
                    agg.first = value;
                    firstFound = true;
                    agg.last = value;
                } else if (delta >= step) {
                    agg.last = value;
                }
                if (!Double.isNaN(value)) {
                    agg.total = Util.sum(agg.total, delta * value);
                    totalSeconds += delta;
                }
            }
        }
        agg.average = totalSeconds > 0 ? (agg.total / totalSeconds) : Double.NaN;
        return agg;
    }

    double getPercentile(long tStart, long tEnd, double percentile) {
        List<Double> valueList = new ArrayList<Double>();
        for (int i = 0; i < timestamps.length; i++) {
            long left = Math.max(timestamps[i] - step, tStart);
            long right = Math.min(timestamps[i], tEnd);
            if (right > left && !Double.isNaN(values[i])) {
                valueList.add(values[i]);
            }
        }
        int count = valueList.size();
        if (count > 1) {
            double[] valuesCopy = new double[count];
            for (int i = 0; i < count; i++) {
                valuesCopy[i] = valueList.get(i);
            }
            Arrays.sort(valuesCopy);
            double topPercentile = (100.0 - percentile) / 100.0;
            count -= (int) Math.ceil(count * topPercentile);
            if (count > 0) {
                return valuesCopy[count - 1];
            }
        }
        return Double.NaN;
    }
}
