package com.ikaad.mathnotepad.engine.builtinfunc;

import com.ikaad.mathnotepad.engine.Real;
import com.ikaad.mathnotepad.engine.SymTb;

public class Bf_Asin extends BuiltinFunction {

    public static final Bf_Asin instance = new Bf_Asin();

    private Bf_Asin() {
        super(new String[] { "a" });
    }

    public Object exec(SymTb tb) {
        Real a = tb.getReal("a");
        a.asin();
        return a;
    }

    public String getName() {
        return "asin";
    }

    public String getDescription() {
        return "arc sine of x";
    }

    public String getPrototype() {
        return "asin(x)";
    }
}
