package com.od.jtimeseries.timeseries;

import com.od.jtimeseries.util.numeric.Numeric;

/**
 * Created by IntelliJ IDEA.
 * User: Nick Ebbutt
 * Date: 31/01/11
 * Time: 11:07
 *
 * TimeSeries are made up of TimeSeriesItem, each TimeSeriesItem has a timestamp with one or more
 * associated values (Numeric)
 *
 * In general TimeSeriesItem are expected to be immutable by JTimeseries library. Changing a time series item
 * after adding it to a timeseries may result in unexpected behaviour.
 */
public interface TimeSeriesItem {

    long getTimestamp();

    Numeric getValue();

    double doubleValue();

    long longValue();

    /**
     * @return number of Numeric values which are available at this time point
     */
    int getValueCount();

    /**
     * @return value at valueIndex for this time point, null if valueIndex >= getValueCount()
     */
    Numeric getValue(int valueIndex);
}
