package com.advancedpwr.record.methods;

public abstract class AbstractPrimitiveBuilder extends BuildMethodWriter {

    @Override
    public void buildMethod() {
    }

    protected String result() {
        return getAccessPath().getResult().toString();
    }

    protected String instanceName() {
        return resultBuilder();
    }
}
