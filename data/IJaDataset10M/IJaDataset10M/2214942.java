package org.isi.monet.core.bpi.rhino;

import org.isi.monet.core.bpi.BPINodeReference;
import org.isi.monet.core.constants.Common;
import org.isi.monet.core.constants.Strings;
import org.isi.monet.core.library.LibraryDate;
import org.isi.monet.core.model.NodeReference;
import org.isi.monet.core.model.ReferenceAttributeDefinition;
import org.isi.monet.core.model.ReferenceDefinition;
import org.isi.monet.core.model.ReferenceSystemDefinition;
import org.mozilla.javascript.ScriptableObject;

public class BPINodeReferenceImpl extends ScriptableObject implements BPINodeReference {

    private static final long serialVersionUID = 1L;

    private NodeReference oNodeReference;

    private ReferenceDefinition oDefinition;

    public BPINodeReferenceImpl() {
    }

    @Override
    public String getClassName() {
        return "BPINodeReference";
    }

    public Boolean setNodeReference(NodeReference oNodeReference) {
        this.oNodeReference = oNodeReference;
        return true;
    }

    public Boolean setDefinition(ReferenceDefinition oDefinition) {
        this.oDefinition = oDefinition;
        return true;
    }

    public String getLabel() {
        return this.oNodeReference.getLabel();
    }

    public String jsFunction_getLabel() {
        return this.getLabel();
    }

    public void setLabel(String sLabel) {
        this.oNodeReference.setLabel(sLabel);
    }

    public void jsFunction_setLabel(String sLabel) {
        this.setLabel(sLabel);
    }

    public String getDescription() {
        return this.oNodeReference.getDescription();
    }

    public String jsFunction_getDescription() {
        return this.getDescription();
    }

    public void setDescription(String sDescription) {
        this.oNodeReference.setLabel(sDescription);
    }

    public void jsFunction_setDescription(String sDescription) {
        this.setLabel(sDescription);
    }

    public String getAttributeValue(String code) {
        return this.oNodeReference.getAttributeValue(code);
    }

    public String jsFunction_getAttributeValue(String code) {
        return this.getAttributeValue(code);
    }

    public void setAttributeValue(String code, String sValue) {
        ReferenceAttributeDefinition oAttributeDefinition = this.oDefinition.getAttribute(code);
        if (oAttributeDefinition == null) oAttributeDefinition = (new ReferenceSystemDefinition()).getAttribute(code);
        if (oAttributeDefinition == null) this.oNodeReference.addAttribute(code, sValue); else if (oAttributeDefinition.isBoolean()) {
            sValue = sValue.toLowerCase();
            this.oNodeReference.addAttribute(code, ((sValue.equals(Strings.TRUE) || sValue.equals(Strings.YES) || sValue.equals(Common.Booleans.TRUE)) ? true : false));
        } else if (oAttributeDefinition.isDate()) this.oNodeReference.addAttribute(code, LibraryDate.parseDate(sValue)); else if ((oAttributeDefinition.isText()) || oAttributeDefinition.isMemo()) this.oNodeReference.addAttribute(code, sValue); else if (oAttributeDefinition.isNumber()) this.oNodeReference.addAttribute(code, Integer.valueOf(sValue)); else this.oNodeReference.addAttribute(code, sValue);
    }

    public void jsFunction_setAttributeValue(String code, String sValue) {
        this.setAttributeValue(code, sValue);
    }
}
