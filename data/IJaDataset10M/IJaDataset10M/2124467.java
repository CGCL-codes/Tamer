package org.monet.bpi.java;

import org.monet.bpi.BPIFieldPattern;
import org.monet.kernel.model.Indicator;

public class BPIFieldPatternImpl extends BPIFieldImpl<String> implements BPIFieldPattern {

    @Override
    public String get() {
        return this.attribute.getIndicatorValue(Indicator.VALUE);
    }

    @Override
    public void set(String value) {
        this.setIndicatorValue(Indicator.VALUE, value);
    }

    @Override
    public boolean equals(String value) {
        return this.get().equals(value);
    }

    @Override
    public void clear() {
        this.set("");
    }

    @Override
    public String getGroup(String name) {
        return "TODO";
    }

    @Override
    public String getGroup(int index) {
        return "TODO";
    }
}
