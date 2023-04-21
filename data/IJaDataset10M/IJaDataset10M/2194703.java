package org.isi.monet.applications.manager.core.model;

import org.jdom.Element;
import org.isi.monet.core.constants.Strings;
import org.isi.monet.core.model.BaseModel;

public class Use extends BaseModel {

    private String sTarget;

    private String sOutput;

    public Use() {
        super();
        this.sTarget = Strings.EMPTY;
        this.sOutput = Strings.EMPTY;
    }

    public String getTarget() {
        return this.sTarget;
    }

    public Boolean setTarget(String sTarget) {
        this.sTarget = sTarget;
        return true;
    }

    public String getOutput() {
        return this.sOutput;
    }

    public Boolean setOutput(String sOutput) {
        this.sOutput = sOutput;
        return true;
    }

    public StringBuffer serializeToXML(Boolean bAddHeader) {
        StringBuffer oResult = new StringBuffer();
        if (bAddHeader) oResult.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        oResult.append("<use name=\"" + this.sName + "\" target=\"" + this.sTarget + "\" output=\"" + this.sOutput + "\"/>");
        return oResult;
    }

    public Boolean unserializeFromXML(String sData) {
        return null;
    }

    public Boolean unserializeFromXML(Element oElement) {
        if (oElement.getAttribute("name") != null) this.sName = oElement.getAttributeValue("name");
        if (oElement.getAttribute("target") != null) this.sTarget = oElement.getAttributeValue("target");
        if (oElement.getAttribute("output") != null) this.sOutput = oElement.getAttributeValue("output");
        return true;
    }
}
