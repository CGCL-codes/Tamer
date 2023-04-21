package com.google.code.p.keytooliui.shared.awt.awtevent;

public class DRangeAbstractEvent extends java.awt.AWTEvent {

    private static final int _f_s_intIdFirst = PRangeToValueEvent.PRANGETOVALUEEVENT_LAST + 1;

    public static final int DRANGEABSTRACTEVENT_LAST = _f_s_intIdFirst;

    public static final int DRANGEABSTRACTEVENT_VALUECHANGED = _f_s_intIdFirst;

    public int[] getValueInts() {
        return this._ints;
    }

    public DRangeAbstractEvent(com.google.code.p.keytooliui.shared.swing.dialog.DRangeAbstract swcSource, int intId, int[] ints) {
        super(swcSource, intId);
        this._ints = ints;
    }

    private int[] _ints = null;
}
