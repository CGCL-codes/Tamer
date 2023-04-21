package com.ark.fix.model.fixml;

import com.ark.fix.model.*;

public class IOI_QualifierList extends FIXMLAggregate {

    private NoIOI_Qualifiers _NoIOI_Qualifiers;

    private FIXMLObjSeq _IOI_Qualifier;

    public NoIOI_Qualifiers getNoIOI_Qualifiers() {
        return _NoIOI_Qualifiers;
    }

    public void setNoIOI_Qualifiers(NoIOI_Qualifiers obj) {
        _NoIOI_Qualifiers = obj;
    }

    public void initNoIOI_Qualifiers(Object obj) throws ModelException {
        if (_NoIOI_Qualifiers != null) throw new ModelException("Value has already been initialized for NoIOI_Qualifiers.");
        setNoIOI_Qualifiers((NoIOI_Qualifiers) obj);
    }

    public FIXMLObjSeq getIOI_Qualifier() {
        return _IOI_Qualifier;
    }

    public void setIOI_Qualifier(FIXMLObjSeq objs) {
        _IOI_Qualifier = objs;
    }

    public void initIOI_Qualifier(Object obj) {
        if (_IOI_Qualifier == null) {
            _IOI_Qualifier = new FIXMLObjSeq(IOI_Qualifier.class);
        }
        _IOI_Qualifier.add((IOI_Qualifier) obj);
    }

    public String[] getProperties() {
        String[] properties = { "NoIOI_Qualifiers", "IOI_Qualifier" };
        return properties;
    }

    public String[] getRequiredProperties() {
        String[] properties = {};
        return properties;
    }

    public String toFIXMessage() {
        StringBuffer sb = new StringBuffer("");
        if (_NoIOI_Qualifiers != null) sb.append(_NoIOI_Qualifiers.toFIXMessage());
        if (_IOI_Qualifier != null) sb.append(_IOI_Qualifier.toFIXMessage());
        return sb.toString();
    }

    public String toFIXML(String ident) {
        StringBuffer sb = new StringBuffer("");
        sb.append(ident + "<IOI_QualifierList>\n");
        if (_NoIOI_Qualifiers != null) sb.append(_NoIOI_Qualifiers.toFIXML(ident + "\t") + "\n");
        if (_IOI_Qualifier != null) sb.append(_IOI_Qualifier.toFIXML(ident + "\t") + "\n");
        sb.append(ident + "</IOI_QualifierList>");
        return sb.toString();
    }
}
