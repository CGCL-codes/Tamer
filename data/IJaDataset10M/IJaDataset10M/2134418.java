package nts.command;

import nts.base.IntProvider;
import nts.base.Num;
import nts.io.Log;

/**
 * Setting number parameter primitive.
 */
public class NumParam extends PerformableParam implements IntProvider, Num.Provider {

    private Num value;

    /**
     * Creates a new NumParam with given name and value and stores it
     * in language interpreter |EqTable|.
     * @param	name the name of the NumParam
     * @param	val the value of the NumParam
     */
    public NumParam(String name, Num val) {
        super(name);
        value = val;
    }

    public NumParam(String name, int val) {
        this(name, Num.valueOf(val));
    }

    /**
     * Creates a new NumParam with given name and stores it
     * in language interpreter |EqTable|.
     * @param	name the name of the NumParam
     */
    public NumParam(String name) {
        this(name, Num.ZERO);
    }

    public final Object getEqValue() {
        return value;
    }

    public final void setEqValue(Object val) {
        value = (Num) val;
    }

    public final void addEqValueOn(Log log) {
        log.add(value.toString());
    }

    public Num get() {
        return value;
    }

    public void set(Num val, boolean glob) {
        beforeSetting(glob);
        value = val;
    }

    public final int intVal() {
        return get().intVal();
    }

    protected void scanValue(Token src, boolean glob) {
        set(scanNum(), glob);
    }

    protected void perform(int operation, boolean glob) {
        set(performFor(get(), operation), glob);
    }

    public boolean hasNumValue() {
        return true;
    }

    public Num getNumValue() {
        return get();
    }
}
