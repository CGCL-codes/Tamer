package com.iver.cit.gvsig.geoprocess.core.fmap;

import com.hardcode.gdbms.engine.values.NumericValue;
import com.hardcode.gdbms.engine.values.ValueFactory;

public class AverageFunction implements SummarizationFunction {

    double sum = 0d;

    int numElements = 0;

    public void process(NumericValue value) {
        sum += value.doubleValue();
        numElements++;
    }

    public NumericValue getSumarizeValue() {
        if (numElements == 0) return ValueFactory.createValue(0d);
        double res = sum / (double) numElements;
        return ValueFactory.createValue(res);
    }

    public String toString() {
        return "avg";
    }

    public void reset() {
        sum = 0d;
        numElements = 0;
    }
}
