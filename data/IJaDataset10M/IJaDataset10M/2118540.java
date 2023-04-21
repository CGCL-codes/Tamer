package org.openliberty.xmltooling.ps;

import java.util.List;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.signature.AbstractSignableXMLObject;

public class TargetPSObjectID extends AbstractSignableXMLObject implements XMLObject {

    public static String LOCAL_NAME = "TargetObjectID";

    private String value;

    protected TargetPSObjectID(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    public void setValue(String value) {
        this.value = prepareForAssignment(this.value, value);
    }

    public String getValue() {
        return value;
    }

    public List<XMLObject> getOrderedChildren() {
        return null;
    }
}
