package org.mobicents.eclipslee.util.slee.xml.components;

import org.mobicents.eclipslee.util.slee.xml.DTDHandler;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author allenc
 */
public class SbbCMPField extends DTDXML {

    protected SbbCMPField(Document document, Element root, DTDHandler dtd) {
        super(document, root, dtd);
    }

    public String getName() {
        return getChildText(getRoot(), "cmp-field-name");
    }

    public void setName(String name) {
        setChildText(getRoot(), "cmp-field-name", name);
    }

    public void setSbbAliasRef(SbbRefXML ref) {
        setChildText(getRoot(), "sbb-alias-ref", ref.getAlias());
    }

    public void setSbbAliasRef(String ref) {
        setChildText(getRoot(), "sbb-alias-ref", ref);
    }

    public String getSbbAliasRef() {
        return getChildText(getRoot(), "sbb-alias-ref");
    }
}
