package org.apache.ws.commons.schema;

/**
 * Class for defining minLength facets. Represents the World Wide
 * Web Consortium (W3C) minLength facet.
 */
public class XmlSchemaMinLengthFacet extends XmlSchemaNumericFacet {

    /**
     * Creates new XmlSchemaMinLengthFacet
     */
    public XmlSchemaMinLengthFacet() {
    }

    public XmlSchemaMinLengthFacet(Object value, boolean fixed) {
        super(value, fixed);
    }

    public String toString(String prefix, int tab) {
        StringBuffer xml = new StringBuffer();
        for (int i = 0; i < tab; i++) {
            xml.append("\t");
        }
        xml.append("<minLength value=\"").append(super.getValue()).append("\" ");
        xml.append("fixed=\"").append(super.isFixed()).append("\"/>\n");
        return xml.toString();
    }
}
