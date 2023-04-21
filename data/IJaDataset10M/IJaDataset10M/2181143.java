package org.mobicents.eclipslee.util.slee.xml.components;

import org.mobicents.eclipslee.util.slee.xml.DTDHandler;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author allenc
 */
public class SbbChildRelationXML extends DTDXML {

    protected SbbChildRelationXML(Document document, Element root, DTDHandler dtd) {
        super(document, root, dtd);
    }

    public void setDescription(String desc) {
        setChildText(getRoot(), "description", desc);
    }

    public String getDescription() {
        return getChildText(getRoot(), "description");
    }

    /**
	 * N.B. this method requires an SbbAliasRefXML structure which
	 * can be acquired via SbbJarXML.getSbbAliasRef(name, vendor, version)
	 * or via SbbJarXML.createSbbAliasRef().
	 * @param ref
	 */
    public void setSbbAliasRef(SbbRefXML ref) {
        setChildText(getRoot(), "sbb-alias-ref", ref.getAlias());
    }

    public String getSbbAliasRef() {
        return getChildText(getRoot(), "sbb-alias-ref");
    }

    public void setChildRelationMethodName(String name) {
        setChildText(getRoot(), "get-child-relation-method-name", name);
    }

    public String getChildRelationMethodName() {
        return getChildText(getRoot(), "get-child-relation-method-name");
    }

    public void setDefaultPriority(int priority) {
        setChildText(getRoot(), "default-priority", new Integer(priority).toString());
    }

    public int getDefaultPriority() {
        String priority = getChildText(getRoot(), "default-priority");
        if (priority == null) return 0;
        return Integer.parseInt(priority);
    }

    public String toString() {
        String output = super.toString();
        output += "\n";
        output += "Alias: " + getSbbAliasRef() + "\n";
        output += "Method: " + getChildRelationMethodName() + "\n";
        output += "Priority: " + getDefaultPriority() + "\n";
        return output;
    }
}
