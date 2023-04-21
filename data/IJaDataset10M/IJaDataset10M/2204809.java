package org.apache.xmlbeans.impl.xb.xsdschema;

/**
 * An XML topLevelComplexType(@http://www.w3.org/2001/XMLSchema).
 *
 * This is a complex type.
 */
public interface TopLevelComplexType extends org.apache.xmlbeans.impl.xb.xsdschema.ComplexType {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(TopLevelComplexType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("toplevelcomplextypee58atype");

    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.apache.xmlbeans.XmlNCName xgetName();

    /**
     * True if has "name" attribute
     */
    boolean isSetName();

    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.apache.xmlbeans.XmlNCName name);

    /**
     * Unsets the "name" attribute
     */
    void unsetName();

    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    public static final class Factory {

        public static org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType newInstance() {
            return (org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
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
