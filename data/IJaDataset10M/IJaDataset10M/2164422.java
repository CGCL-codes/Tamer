package org.apache.ws.commons.schema;

/**
 * Class for defining fractionDigits facets. Represents the World Wide
 * Web Consortium (W3C) fractionDigits facet.
 */
public class XmlSchemaFractionDigitsFacet extends XmlSchemaNumericFacet {

    /**
     * Creates new XmlSchemaFractionDigitsFacet
     */
    public XmlSchemaFractionDigitsFacet() {
    }

    public XmlSchemaFractionDigitsFacet(Object value, boolean fixed) {
        super(value, fixed);
    }

    public String toString(String prefix, int tab) {
        StringBuffer xml = new StringBuffer();
        for (int i = 0; i < tab; i++) {
            xml.append("\t");
        }
        xml.append("<fractionDigits value=\"");
        xml.append(super.getValue());
        xml.append("\" ");
        xml.append("fixed=\"");
        xml.append(super.isFixed());
        xml.append("\"/>\n");
        return xml.toString();
    }
}
