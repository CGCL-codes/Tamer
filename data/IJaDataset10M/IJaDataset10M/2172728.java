package com.advancedpwr.record.methods;

public abstract class ObjectBuilderFactory extends AbstractDefaultFactory {

    public boolean accept(Class inClass) {
        return Object.class.equals(inClass);
    }
}
