package org.apache.xmlbeans.impl.xb.xsdschema;

/**
 * An XML extensionType(@http://www.w3.org/2001/XMLSchema).
 *
 * This is a complex type.
 */
public interface ExtensionType extends org.apache.xmlbeans.impl.xb.xsdschema.Annotated {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ExtensionType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("extensiontypeed4ctype");

    /**
     * Gets the "group" element
     */
    org.apache.xmlbeans.impl.xb.xsdschema.GroupRef getGroup();

    /**
     * True if has "group" element
     */
    boolean isSetGroup();

    /**
     * Sets the "group" element
     */
    void setGroup(org.apache.xmlbeans.impl.xb.xsdschema.GroupRef group);

    /**
     * Appends and returns a new empty "group" element
     */
    org.apache.xmlbeans.impl.xb.xsdschema.GroupRef addNewGroup();

    /**
     * Unsets the "group" element
     */
    void unsetGroup();

    /**
     * Gets the "all" element
     */
    org.apache.xmlbeans.impl.xb.xsdschema.All getAll();

    /**
     * True if has "all" element
     */
    boolean isSetAll();

    /**
     * Sets the "all" element
     */
    void setAll(org.apache.xmlbeans.impl.xb.xsdschema.All all);

    /**
     * Appends and returns a new empty "all" element
     */
    org.apache.xmlbeans.impl.xb.xsdschema.All addNewAll();

    /**
     * Unsets the "all" element
     */
    void unsetAll();

    /**
     * Gets the "choice" element
     */
    org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup getChoice();

    /**
     * True if has "choice" element
     */
    boolean isSetChoice();

    /**
     * Sets the "choice" element
     */
    void setChoice(org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup choice);

    /**
     * Appends and returns a new empty "choice" element
     */
    org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup addNewChoice();

    /**
     * Unsets the "choice" element
     */
    void unsetChoice();

    /**
     * Gets the "sequence" element
     */
    org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup getSequence();

    /**
     * True if has "sequence" element
     */
    boolean isSetSequence();

    /**
     * Sets the "sequence" element
     */
    void setSequence(org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup sequence);

    /**
     * Appends and returns a new empty "sequence" element
     */
    org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup addNewSequence();

    /**
     * Unsets the "sequence" element
     */
    void unsetSequence();

    /**
     * Gets array of all "attribute" elements
     */
    org.apache.xmlbeans.impl.xb.xsdschema.Attribute[] getAttributeArray();

    /**
     * Gets ith "attribute" element
     */
    org.apache.xmlbeans.impl.xb.xsdschema.Attribute getAttributeArray(int i);

    /**
     * Returns number of "attribute" element
     */
    int sizeOfAttributeArray();

    /**
     * Sets array of all "attribute" element
     */
    void setAttributeArray(org.apache.xmlbeans.impl.xb.xsdschema.Attribute[] attributeArray);

    /**
     * Sets ith "attribute" element
     */
    void setAttributeArray(int i, org.apache.xmlbeans.impl.xb.xsdschema.Attribute attribute);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "attribute" element
     */
    org.apache.xmlbeans.impl.xb.xsdschema.Attribute insertNewAttribute(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "attribute" element
     */
    org.apache.xmlbeans.impl.xb.xsdschema.Attribute addNewAttribute();

    /**
     * Removes the ith "attribute" element
     */
    void removeAttribute(int i);

    /**
     * Gets array of all "attributeGroup" elements
     */
    org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroupRef[] getAttributeGroupArray();

    /**
     * Gets ith "attributeGroup" element
     */
    org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroupRef getAttributeGroupArray(int i);

    /**
     * Returns number of "attributeGroup" element
     */
    int sizeOfAttributeGroupArray();

    /**
     * Sets array of all "attributeGroup" element
     */
    void setAttributeGroupArray(org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroupRef[] attributeGroupArray);

    /**
     * Sets ith "attributeGroup" element
     */
    void setAttributeGroupArray(int i, org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroupRef attributeGroup);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "attributeGroup" element
     */
    org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroupRef insertNewAttributeGroup(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "attributeGroup" element
     */
    org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroupRef addNewAttributeGroup();

    /**
     * Removes the ith "attributeGroup" element
     */
    void removeAttributeGroup(int i);

    /**
     * Gets the "anyAttribute" element
     */
    org.apache.xmlbeans.impl.xb.xsdschema.Wildcard getAnyAttribute();

    /**
     * True if has "anyAttribute" element
     */
    boolean isSetAnyAttribute();

    /**
     * Sets the "anyAttribute" element
     */
    void setAnyAttribute(org.apache.xmlbeans.impl.xb.xsdschema.Wildcard anyAttribute);

    /**
     * Appends and returns a new empty "anyAttribute" element
     */
    org.apache.xmlbeans.impl.xb.xsdschema.Wildcard addNewAnyAttribute();

    /**
     * Unsets the "anyAttribute" element
     */
    void unsetAnyAttribute();

    /**
     * Gets the "base" attribute
     */
    javax.xml.namespace.QName getBase();

    /**
     * Gets (as xml) the "base" attribute
     */
    org.apache.xmlbeans.XmlQName xgetBase();

    /**
     * Sets the "base" attribute
     */
    void setBase(javax.xml.namespace.QName base);

    /**
     * Sets (as xml) the "base" attribute
     */
    void xsetBase(org.apache.xmlbeans.XmlQName base);

    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    public static final class Factory {

        public static org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType newInstance() {
            return (org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
        }

        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, type, null);
        }

        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, type, options);
        }

        private Factory() {
        }
    }
}
