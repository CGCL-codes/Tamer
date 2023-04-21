package com.cjssolutions.plex.service.preferences;

import ObRun.ObFunction.ObFunction;
import ObRun.ObFunction.ObVariableGroup;
import ObRun.ObFunction.ObVariableGroupX;
import ObRun.ObFunction.ObVariableX;
import ObRun.ObRTTypes.ObCharFld;
import ObRun.ObRTTypes.ObField;
import ObRun.ObRTTypes.ObIntFld;
import ObRun.ObRTTypes.ObLongDblFld;
import ObRun.ObRTTypes.ObLongFld;
import ObRun.ObRTTypes.ObObjFld;

public final class CJqF_ObIn extends ObVariableGroupX {

    public CJqF_ObIn() {
        super(ObVariableGroup.VT_INPUT, null);
        addVariable("CJqF_InsertData", initvarCJqF_CJqF_InsertData());
    }

    public CJqF_ObIn(ObFunction fnc) {
        super(ObVariableGroup.VT_INPUT, fnc);
        addVariable("CJqF_InsertData", initvarCJqF_CJqF_InsertData());
    }

    public ObVariableX initvarCJqF_CJqF_InsertData() {
        ObVariableX var = new ObVariableX(this, "varCJqF_CJqF_InsertData");
        var.addField("CJeA", new ObObjFld(ObField.DUAL, 0, 0, 'j', "CJeA", ""));
        var.addField("CJ9A", new ObCharFld(ObField.DUAL, 80, 'c', "CJ9A", true, true, false, ""));
        var.addField("CJaA", new ObCharFld(ObField.DUAL, 32, 'c', "CJaA", false, true, false, ""));
        var.addField("CJbA", new ObCharFld(ObField.DUAL, 1024, 'c', "CJbA", true, true, false, ""));
        var.addField("CJiA", new ObCharFld(ObField.DUAL, 1, 'c', "CJiA", false, false, false, ""));
        var.addField("CJlA", new ObLongDblFld(ObField.DUAL, 19, 9, 'p', "CJlA", ""));
        var.addField("CJmA", new ObLongDblFld(ObField.DUAL, 19, 7, 'p', "CJmA", ""));
        var.addField("CJkA", new ObIntFld(ObField.DUAL, 5, 0, 'p', "CJkA", ""));
        var.addField("CJjA", new ObLongFld(ObField.DUAL, 10, 0, 'p', "CJjA", ""));
        return var;
    }
}
