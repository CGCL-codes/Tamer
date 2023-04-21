package nts.noad;

import nts.base.Dimen;

public abstract class Egg implements TransfConstants {

    public static final Egg NULL = null;

    public abstract Dimen getHeight();

    public abstract Dimen getDepth();

    public abstract void chipShell(Nodery nodery);

    public abstract byte spacingType();

    public boolean isBin() {
        return false;
    }

    public void dontBeBin() {
    }

    public boolean isPenalty() {
        return false;
    }

    public boolean ignoreNextScriptSpace() {
        return false;
    }

    public Dimen getItalCorr() {
        return Dimen.NULL;
    }

    public void suppressItalCorr() {
    }
}
