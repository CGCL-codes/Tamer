package com.ikaad.mathnotepad.engine.builtinfunc;

import com.ikaad.mathnotepad.engine.Real;
import com.ikaad.mathnotepad.engine.SymTb;

public class Bf_Ceil extends BuiltinFunction {

    public static final Bf_Ceil instance = new Bf_Ceil();

    private Bf_Ceil() {
        super(new String[] { "a" });
    }

    public Object exec(SymTb tb) {
        Real a = tb.getReal("a");
        a.ceil();
        return a;
    }

    public String getName() {
        return "ceil";
    }

    public String getDescription() {
        return "the smallest integer not less than x";
    }

    public String getPrototype() {
        return "ceil(x)";
    }
}
