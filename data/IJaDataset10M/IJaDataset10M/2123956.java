package piaba.symlib.bool;

import java.util.Random;
import piaba.env.Env;

public class SymBoolConst extends SymBool {

    protected SymBoolConst() {
    }

    public boolean interpret(Env env) {
        return this == TRUE;
    }

    @Override
    public boolean interpretRan(Random ran) {
        return interpret(null);
    }

    public SymBool neg() {
        return this == TRUE ? FALSE : TRUE;
    }

    public String toString() {
        return this == TRUE ? "true" : "false";
    }

    public boolean eval() {
        return interpret(null);
    }

    public static SymBool create(boolean b) {
        return b ? TRUE : FALSE;
    }

    public boolean isCte() {
        return true;
    }
}
