package com.volantis.mcs.build.themes.definitions.values.impl;

import com.volantis.mcs.build.themes.definitions.values.LengthValue;
import java.io.PrintStream;

public class LengthValueImpl implements LengthValue {

    private int intValue;

    private String units;

    public void setInteger(int i) {
        intValue = i;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public void writeConstructCode(String indent, PrintStream out) {
        out.print(indent);
        out.print("styleValueFactory.getLength(null, ");
        out.print(intValue);
        out.print(", LengthUnit." + units.toUpperCase());
        out.print(")");
    }
}
