package com.superscape.anttasks.alienbrain;

import org.apache.tools.ant.types.EnumeratedAttribute;

public class MergeContentResponseValue extends EnumeratedAttribute {

    public String[] getValues() {
        return new String[] { "t", "s", "co", "e", "c" };
    }
}
