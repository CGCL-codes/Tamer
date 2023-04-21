package org.openliberty.igf.attributeService.schema;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * A role is a value that defines a right that one or more subjects may possess.
 * A �role� is locally meaningful to an application, and like a predicate must
 * be interpreted by an attribute authority.
 * 
 * As with predicate, the use of descriptive text and DataType URIs will be used
 * by attribute authorities to determine how to map and interpret roles.
 * 
 * Roles also differ from predicates in that they are handled as values rather
 * than specific test. E.g. subject.getRoles() returns the roles the user
 * possesses.
 */
public class RoleDef extends baseAttribute {

    public static final String roleElement = "Role";

    public RoleDef(Node xmlAttribute) {
        super(xmlAttribute);
    }

    public RoleDef(String name, String displayName, String description) {
        super(name, displayName, description);
    }

    public void appendChildren(Node parent) {
        Document doc = parent.getOwnerDocument();
        Element ae;
        ae = doc.createElement(roleElement);
        super.addAttributesToParentElement(ae);
        parent.appendChild(ae);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Role: ").append(this.getNameId());
        return buf.toString();
    }
}
