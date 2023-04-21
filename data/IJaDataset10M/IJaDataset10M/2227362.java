package com.directmodelling.impl;

import com.directmodelling.api.DoubleValue;

public class DoubleVar extends Variable<Double> implements DoubleValue.Modifiable {

    @Override
    public DoubleVar set(final double value) {
        setValue(value);
        return this;
    }

    @Override
    public double get() {
        return getValue();
    }

    @Override
    public Type type() {
        return Type.tDouble;
    }
}
