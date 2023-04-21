package samples.timeseries;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Time series, a set of mapping from timestamp to a double value at this
 * timestamp with a plethora of useful functions around that.
 * 
 * @author Marcin Brodziak (marcin.brodziak@gmail.com)
 */
public class TimeSeries implements Comparable<TimeSeries> {

    /** Map from time to value. */
    private final HashMap<Long, Double> valueMap;

    /** Name of the series. */
    private final String name;

    /** 
   * Creates time series object. 
   * @param name Name of the series.
   */
    public TimeSeries(String name) {
        valueMap = new HashMap<Long, Double>();
        this.name = name;
    }

    /**
   * Adds new value to the time series.
   * @param time At which time value should be added.
   * @param value What is the value.
   */
    public void addValue(Long time, double value) {
        valueMap.put(time, value);
    }

    /**
   * Returns a value for given point in time.
   * @param time Time for which value should be returned.
   * @return Value of the timeseries at given point.
   */
    public double getValue(Long time) {
        return valueMap.get(time);
    }

    /**
   * Returns minimum value for the time series.
   * @return Minumum value for time series.
   */
    public double getMin() {
        double min = Double.MAX_VALUE;
        for (Double d : valueMap.values()) {
            if (d < min) {
                min = d;
            }
        }
        return min;
    }

    /**
   * Returns maximum value in the timeseries.
   * @return Maximum value for the timeseries.
   */
    public Double getMax() {
        double max = Double.MIN_VALUE;
        for (Double d : valueMap.values()) {
            if (d > max) {
                max = d;
            }
        }
        return max;
    }

    /**
   * Returns mean value in the time series.
   * @return Mean value in the time series.
   */
    public Double getMean() {
        double sum = 0;
        for (Double d : valueMap.values()) {
            sum += d;
        }
        return sum / valueMap.size();
    }

    /**
   * Returns standard deviation corrected for linear trend of 
   * values in the timeseries.
   * @return Corrected standard deviation of 
   *    values in the timeseries.
   */
    public Double getTrendCorrectedStandardDeviation() {
        double intercept = getIntercept();
        double slope = getSlope();
        TimeSeries t = new TimeSeries(name);
        for (Entry<Long, Double> entry : valueMap.entrySet()) {
            t.addValue(entry.getKey(), entry.getValue() - (slope * entry.getKey() + intercept));
        }
        return t.getStandardDeviation();
    }

    /**
   * Returns standard deviation of values in the timeseries.
   * @return Standard deviation of values in the timeseries.
   */
    public Double getStandardDeviation() {
        double sum = 0;
        double mean = getMean();
        for (Double d : valueMap.values()) {
            sum += Math.pow(d - mean, 2);
        }
        return Math.sqrt(sum / (valueMap.size() - 1));
    }

    /**
   * Returns the intercept for linear regression for 
   * this time series.
   * @return intercept for the linear regression.
   */
    public Double getIntercept() {
        double sx = 0.0;
        double sy = 0.0;
        for (Entry<Long, Double> s : valueMap.entrySet()) {
            sx += s.getKey();
            sy += s.getValue();
        }
        return (sy - getSlope() * sx) / valueMap.size();
    }

    /**
   * Returns the slope of the linear regression for the time
   * series. 
   * @return slope of the linear regression.
   */
    public Double getSlope() {
        double slope = 0.0;
        double sx = 0.0;
        double sy = 0.0;
        double sxy = 0.0;
        double sx2 = 0.0;
        for (Entry<Long, Double> s : valueMap.entrySet()) {
            sx += s.getKey();
            sy += s.getValue();
            sxy += s.getKey() * s.getValue();
            sx2 += s.getKey() * s.getKey();
        }
        slope = (valueMap.size() * sxy - sx * sy) / (valueMap.size() * sx2 - sx * sx);
        return slope;
    }

    /**
   * Returns the name of the series.
   * @return name of the series.
   */
    public String getName() {
        return name;
    }

    /**
   * {@inheritDoc}
   */
    public int compareTo(TimeSeries series) {
        return name.compareTo(series.name);
    }

    /**
   * Returns timestamps in the timeseries.
   * @return timestamp in the series.
   */
    public Set<Long> getTimestamps() {
        return valueMap.keySet();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("<");
        for (Double v : valueMap.values()) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(v);
        }
        sb.append(">");
        return sb.toString();
    }
}
