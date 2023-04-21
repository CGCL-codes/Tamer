package nts.command;

import nts.base.Glue;
import nts.io.Log;

/**
 * Setting glue register primitive.
 */
public class MuSkipPrim extends RegisterPrim {

    /**
     * Creates a new |MuSkipPrim| with given name and stores it
     * in language interpreter |EqTable|.
     * @param	name the name of the |MuSkipPrim|
     */
    public MuSkipPrim(String name) {
        super(name);
    }

    public final void set(int idx, Glue val, boolean glob) {
        if (glob) getEqt().gput(tabKind, idx, val); else getEqt().put(tabKind, idx, val);
    }

    public final Glue get(int idx) {
        Glue val = (Glue) getEqt().get(tabKind, idx);
        return (val != Glue.NULL) ? val : Glue.ZERO;
    }

    public void addEqValueOn(int idx, Log log) {
        log.add(get(idx).toString("mu"));
    }

    public void perform(int idx, int operation, boolean glob) {
        set(idx, performFor(get(idx), operation, true), glob);
    }

    /**
     * Performs the assignment.
     * @param	src source token for diagnostic output.
     * @param	glob indication that the assignment is global.
     */
    protected final void assign(Token src, boolean glob) {
        int idx = scanRegisterCode();
        skipOptEquals();
        set(idx, scanMuGlue(), glob);
    }

    public final void perform(int operation, boolean glob, Command after) {
        perform(scanRegisterCode(), operation, glob);
    }

    public boolean hasMuGlueValue() {
        return true;
    }

    public Glue getMuGlueValue() {
        return get(scanRegisterCode());
    }
}
