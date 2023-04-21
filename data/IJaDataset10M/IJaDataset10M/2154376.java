package org.apache.ws.commons.schema;

import javax.xml.namespace.QName;

/**
 * Class for complex types with a complex content model that are derived
 * by restriction. Restricts the contents of the complex type to a subset
 * of the inherited complex type. Represents the World Wide Web Consortium
 * (W3C) restriction element for complex content.
 */
public class XmlSchemaComplexContentRestriction extends XmlSchemaContent {

    /**
     * Creates new XmlSchemaComplexContentRestriction
     */
    public XmlSchemaComplexContentRestriction() {
        attributes = new XmlSchemaObjectCollection();
    }

    XmlSchemaAnyAttribute anyAttribute;

    public void setAnyAttribute(XmlSchemaAnyAttribute anyAttribute) {
        this.anyAttribute = anyAttribute;
    }

    public XmlSchemaAnyAttribute getAnyAttribute() {
        return this.anyAttribute;
    }

    XmlSchemaObjectCollection attributes;

    public XmlSchemaObjectCollection getAttributes() {
        return this.attributes;
    }

    QName baseTypeName;

    public void setBaseTypeName(QName baseTypeName) {
        this.baseTypeName = baseTypeName;
    }

    public QName getBaseTypeName() {
        return this.baseTypeName;
    }

    XmlSchemaParticle particle;

    public XmlSchemaParticle getParticle() {
        return this.particle;
    }

    public void setParticle(XmlSchemaParticle particle) {
        this.particle = particle;
    }

    public String toString(String prefix, int tab) {
        String xml = new String();
        for (int i = 0; i < tab; i++) xml += "\t";
        if (!prefix.equals("") && prefix.indexOf(":") == -1) prefix += ":";
        xml += "<" + prefix + "restriction>\n";
        if (particle != null) xml += particle.toString(prefix, (tab + 1));
        for (int i = 0; i < tab; i++) xml += "\t";
        xml += "</" + prefix + "restriction>\n";
        return xml;
    }
}
