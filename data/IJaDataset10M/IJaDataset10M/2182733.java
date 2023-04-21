package com.kescom.matrix.core.filter.sos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.kescom.matrix.core.filter.SeriesFilterBase;
import com.kescom.matrix.core.filter.SeriesProxy;
import com.kescom.matrix.core.series.DataPoint;
import com.kescom.matrix.core.series.IDataPoint;
import com.kescom.matrix.core.series.ISeries;

public class SosAverageFilter extends SeriesFilterBase {

    private static class Accumulator {

        int count = 0;

        double sum = 0.0;
    }

    private static class AccumulatorsSample implements Comparable<AccumulatorsSample> {

        long time;

        Accumulator[] accumulators;

        public int compareTo(AccumulatorsSample o) {
            if (time > o.time) return 1; else if (time < o.time) return -1; else return 0;
        }
    }

    List<AccumulatorsSample> accumSamples;

    AccumulatorsSample tempSample;

    ISeries baseSeries;

    public ISeries getSeries(ISeries series) {
        ISeries representativeSeries = baseSeries;
        accumInit();
        for (IDataPoint dp : series.getDataPoints()) for (Object value : dp.getValues()) if (value instanceof ISeries) {
            if (representativeSeries == null) representativeSeries = (ISeries) value;
            accumSeries((ISeries) value);
        }
        List<IDataPoint> dataPoints = new ArrayList<IDataPoint>();
        for (AccumulatorsSample sample : accumSamples) {
            DataPoint dp = new DataPoint();
            dp.setTime(sample.time);
            Object[] values = new Object[sample.accumulators.length];
            dp.setValues(values);
            for (int n = 0; n < values.length; n++) if (sample.accumulators[n] != null && sample.accumulators[n].count != 0) values[n] = sample.accumulators[n].sum / sample.accumulators[n].count;
            dataPoints.add(dp);
        }
        if (representativeSeries != null) return new SeriesProxy(representativeSeries, dataPoints); else return null;
    }

    private void accumInit() {
        accumSamples = new ArrayList<AccumulatorsSample>();
        tempSample = new AccumulatorsSample();
    }

    private void accumSeries(ISeries series) {
        for (IDataPoint dp : series.getDataPoints()) {
            long time = dp.getTime();
            Object[] values = dp.getValues();
            AccumulatorsSample sample = accumFindSample(time, values.length);
            int fieldIndex = 0;
            for (Object value : values) {
                if (value instanceof Number) {
                    Accumulator accum = sample.accumulators[fieldIndex];
                    accum.count++;
                    accum.sum += ((Number) value).doubleValue();
                }
                fieldIndex++;
            }
        }
    }

    private AccumulatorsSample accumFindSample(long time, int requiredAccumulators) {
        AccumulatorsSample sample = null;
        tempSample.time = time;
        int index = Collections.binarySearch(accumSamples, tempSample);
        if (index >= 0) {
            sample = accumSamples.get(index);
            if (sample.accumulators.length < requiredAccumulators) {
                Accumulator[] org = sample.accumulators;
                sample.accumulators = new Accumulator[requiredAccumulators];
                for (int n = 0; n < org.length; n++) sample.accumulators[n] = org[n];
                for (int n = org.length; n < requiredAccumulators; n++) sample.accumulators[n] = new Accumulator();
            }
            return sample;
        } else {
            sample = new AccumulatorsSample();
            sample.time = time;
            sample.accumulators = new Accumulator[requiredAccumulators];
            for (int n = 0; n < requiredAccumulators; n++) sample.accumulators[n] = new Accumulator();
            accumSamples.add(-(index + 1), sample);
            return sample;
        }
    }

    public ISeries getBaseSeries() {
        return baseSeries;
    }

    public void setBaseSeries(ISeries baseSeries) {
        this.baseSeries = baseSeries;
    }
}
