package com.cjssolutions.plex.service.preferences;

import ObRun.ObFunction.ObFunction;
import ObRun.ObFunction.ObVariableGroup;
import ObRun.ObFunction.ObVariableGroupX;
import ObRun.ObFunction.ObVariableX;
import ObRun.ObRTTypes.ObCharFld;
import ObRun.ObRTTypes.ObField;
import ObRun.ObRTTypes.ObObjFld;

public final class CJpF_ObIn extends ObVariableGroupX {

    public CJpF_ObIn() {
        super(ObVariableGroup.VT_INPUT, null);
        addVariable("CJpF_DeleteKey", initvarCJpF_CJpF_DeleteKey());
    }

    public CJpF_ObIn(ObFunction fnc) {
        super(ObVariableGroup.VT_INPUT, fnc);
        addVariable("CJpF_DeleteKey", initvarCJpF_CJpF_DeleteKey());
    }

    public ObVariableX initvarCJpF_CJpF_DeleteKey() {
        ObVariableX var = new ObVariableX(this, "varCJpF_CJpF_DeleteKey");
        var.addField("CJeA", new ObObjFld(ObField.DUAL, 0, 0, 'j', "CJeA", ""));
        var.addField("CJ9A", new ObCharFld(ObField.DUAL, 80, 'c', "CJ9A", true, true, false, ""));
        return var;
    }
}
