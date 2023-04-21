package com.google.code.joto.datatype;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SomeDataTypeToBeIgnoredOnProcessing {

    private int dummy;

    private String someText;

    public SomeDataTypeToBeIgnoredOnProcessing(int dummy, String someText) {
        this.dummy = dummy;
        this.someText = someText;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
