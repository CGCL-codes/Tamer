package org.jrobin.data;

import org.jrobin.core.RrdException;

class SDef extends Source {

    private String defName;

    private String consolFun;

    private double value;

    SDef(String name, String defName, String consolFun) {
        super(name);
        this.defName = defName;
        this.consolFun = consolFun;
    }

    String getDefName() {
        return defName;
    }

    String getConsolFun() {
        return consolFun;
    }

    void setValue(double value) {
        this.value = value;
        int count = getTimestamps().length;
        double[] values = new double[count];
        for (int i = 0; i < count; i++) {
            values[i] = value;
        }
        setValues(values);
    }

    Aggregates getAggregates(long tStart, long tEnd) throws RrdException {
        Aggregates agg = new Aggregates();
        agg.first = agg.last = agg.min = agg.max = agg.average = value;
        agg.total = value * (tEnd - tStart);
        return agg;
    }

    double getPercentile(long tStart, long tEnd, double percentile) throws RrdException {
        return value;
    }
}
