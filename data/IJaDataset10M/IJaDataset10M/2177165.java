package com.advancedpwr.record.methods;

import com.advancedpwr.record.AccessPath;

public class IntBuilder extends AbstractPrimitiveBuilder implements MethodWriterFactory {

    public String resultBuilder() {
        return "new Integer( " + result() + " )";
    }

    public boolean accept(Class inClass) {
        return int.class.isAssignableFrom(inClass) || Integer.class.isAssignableFrom(inClass);
    }

    public BuildMethodWriter createMethodBuilder(AccessPath inPath) {
        return new IntBuilder();
    }
}
