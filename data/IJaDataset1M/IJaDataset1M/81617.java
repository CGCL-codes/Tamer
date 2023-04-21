package com.hifiremote.jp1;

public class ZenithTranslator extends Translate {

    public ZenithTranslator(String[] textParms) {
        super(textParms);
    }

    public void in(Value[] parms, Hex hex, DeviceParameter[] devParms, int onlyIndex) {
        if ((parms[0] == null) || (parms[0].getValue() == null)) return;
        Value val = parms[0];
        int i = ((Integer) val.getValue()).intValue();
        if (val.hasUserValue()) i++;
        insert(hex, 4, 4, i);
    }

    public void out(Hex hex, Value[] parms, DeviceParameter[] devParms) {
        Integer val = null;
        int temp = extract(hex, 4, 4);
        if (temp != ((Integer) devParms[0].getDefaultValue()).intValue()) {
            temp--;
            val = new Integer(temp);
        }
        parms[0] = new Value(val, null);
    }
}
