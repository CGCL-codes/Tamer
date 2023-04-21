package org.mitre.ocil.refimpl.gui.common;

import gov.nist.scap.schema.ocil.x20.ChoiceGroupType;
import gov.nist.scap.schema.ocil.x20.ChoiceType;

/**
 *
 * @author mcasipe
 */
public class ChoiceObj {

    private Object obj = null;

    private String id = null;

    private String value = null;

    private boolean isChoiceGroup = false;

    public ChoiceObj(ChoiceType c) {
        obj = c;
        id = c.getId();
        isChoiceGroup = false;
        if (c.isSetVarRef()) value = DocManager.getVariableValue(c.getVarRef()); else value = c.getStringValue();
    }

    public ChoiceObj(ChoiceGroupType cg) {
        obj = cg;
        id = cg.getId();
        value = cg.toString();
        isChoiceGroup = true;
    }

    public String getId() {
        return id;
    }

    public boolean isChoiceGroup() {
        return isChoiceGroup;
    }

    public Object getObject() {
        return obj;
    }

    @Override
    public String toString() {
        return value;
    }
}
