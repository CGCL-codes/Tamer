package com.a9.spec.opensearchdescription.x10;

/**
 * A document containing one Url(@http://a9.com/-/spec/opensearchdescription/1.0/) element.
 *
 * This is a complex type.
 */
public interface UrlDocument extends org.apache.xmlbeans.XmlObject {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(UrlDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s3608F739AD31F37C3321EC544FC4C375").resolveHandle("url13f7doctype");

    /**
     * Gets the "Url" element
     */
    java.lang.String getUrl();

    /**
     * Gets (as xml) the "Url" element
     */
    org.apache.xmlbeans.XmlString xgetUrl();

    /**
     * Sets the "Url" element
     */
    void setUrl(java.lang.String url);

    /**
     * Sets (as xml) the "Url" element
     */
    void xsetUrl(org.apache.xmlbeans.XmlString url);

    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    public static final class Factory {

        public static com.a9.spec.opensearchdescription.x10.UrlDocument newInstance() {
            return (com.a9.spec.opensearchdescription.x10.UrlDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static com.a9.spec.opensearchdescription.x10.UrlDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (com.a9.spec.opensearchdescription.x10.UrlDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static com.a9.spec.opensearchdescription.x10.UrlDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (com.a9.spec.opensearchdescription.x10.UrlDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static com.a9.spec.opensearchdescription.x10.UrlDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (com.a9.spec.opensearchdescription.x10.UrlDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static com.a9.spec.opensearchdescription.x10.UrlDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (com.a9.spec.opensearchdescription.x10.UrlDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static com.a9.spec.opensearchdescription.x10.UrlDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (com.a9.spec.opensearchdescription.x10.UrlDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static com.a9.spec.opensearchdescription.x10.UrlDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (com.a9.spec.opensearchdescription.x10.UrlDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static com.a9.spec.opensearchdescription.x10.UrlDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (com.a9.spec.opensearchdescription.x10.UrlDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static com.a9.spec.opensearchdescription.x10.UrlDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (com.a9.spec.opensearchdescription.x10.UrlDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static com.a9.spec.opensearchdescription.x10.UrlDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (com.a9.spec.opensearchdescription.x10.UrlDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static com.a9.spec.opensearchdescription.x10.UrlDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (com.a9.spec.opensearchdescription.x10.UrlDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static com.a9.spec.opensearchdescription.x10.UrlDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (com.a9.spec.opensearchdescription.x10.UrlDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static com.a9.spec.opensearchdescription.x10.UrlDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (com.a9.spec.opensearchdescription.x10.UrlDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static com.a9.spec.opensearchdescription.x10.UrlDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (com.a9.spec.opensearchdescription.x10.UrlDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static com.a9.spec.opensearchdescription.x10.UrlDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (com.a9.spec.opensearchdescription.x10.UrlDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static com.a9.spec.opensearchdescription.x10.UrlDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (com.a9.spec.opensearchdescription.x10.UrlDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static com.a9.spec.opensearchdescription.x10.UrlDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (com.a9.spec.opensearchdescription.x10.UrlDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static com.a9.spec.opensearchdescription.x10.UrlDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (com.a9.spec.opensearchdescription.x10.UrlDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, type, options);
        }

        private Factory() {
        }
    }
}
