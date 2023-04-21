package de.grogra.ext.exchangegraph.xmlbeans;

/**
 * An XML Edge(@).
 *
 * This is a complex type.
 */
public interface Edge extends org.apache.xmlbeans.XmlObject {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Edge.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s39D0E01DA061D3219FD31D6307765808").resolveHandle("edge8fc6type");

    /**
     * Gets the "id" attribute
     */
    long getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    de.grogra.ext.exchangegraph.xmlbeans.IdType xgetId();

    /**
     * True if has "id" attribute
     */
    boolean isSetId();

    /**
     * Sets the "id" attribute
     */
    void setId(long id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(de.grogra.ext.exchangegraph.xmlbeans.IdType id);

    /**
     * Unsets the "id" attribute
     */
    void unsetId();

    /**
     * Gets the "src_id" attribute
     */
    long getSrcId();

    /**
     * Gets (as xml) the "src_id" attribute
     */
    de.grogra.ext.exchangegraph.xmlbeans.IdType xgetSrcId();

    /**
     * Sets the "src_id" attribute
     */
    void setSrcId(long srcId);

    /**
     * Sets (as xml) the "src_id" attribute
     */
    void xsetSrcId(de.grogra.ext.exchangegraph.xmlbeans.IdType srcId);

    /**
     * Gets the "dest_id" attribute
     */
    long getDestId();

    /**
     * Gets (as xml) the "dest_id" attribute
     */
    de.grogra.ext.exchangegraph.xmlbeans.IdType xgetDestId();

    /**
     * Sets the "dest_id" attribute
     */
    void setDestId(long destId);

    /**
     * Sets (as xml) the "dest_id" attribute
     */
    void xsetDestId(de.grogra.ext.exchangegraph.xmlbeans.IdType destId);

    /**
     * Gets the "type" attribute
     */
    java.lang.String getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.apache.xmlbeans.XmlString xgetType();

    /**
     * Sets the "type" attribute
     */
    void setType(java.lang.String type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.apache.xmlbeans.XmlString type);

    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    public static final class Factory {

        public static de.grogra.ext.exchangegraph.xmlbeans.Edge newInstance() {
            return (de.grogra.ext.exchangegraph.xmlbeans.Edge) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static de.grogra.ext.exchangegraph.xmlbeans.Edge newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (de.grogra.ext.exchangegraph.xmlbeans.Edge) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static de.grogra.ext.exchangegraph.xmlbeans.Edge parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (de.grogra.ext.exchangegraph.xmlbeans.Edge) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static de.grogra.ext.exchangegraph.xmlbeans.Edge parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (de.grogra.ext.exchangegraph.xmlbeans.Edge) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static de.grogra.ext.exchangegraph.xmlbeans.Edge parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (de.grogra.ext.exchangegraph.xmlbeans.Edge) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static de.grogra.ext.exchangegraph.xmlbeans.Edge parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (de.grogra.ext.exchangegraph.xmlbeans.Edge) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static de.grogra.ext.exchangegraph.xmlbeans.Edge parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (de.grogra.ext.exchangegraph.xmlbeans.Edge) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static de.grogra.ext.exchangegraph.xmlbeans.Edge parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (de.grogra.ext.exchangegraph.xmlbeans.Edge) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static de.grogra.ext.exchangegraph.xmlbeans.Edge parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (de.grogra.ext.exchangegraph.xmlbeans.Edge) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static de.grogra.ext.exchangegraph.xmlbeans.Edge parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (de.grogra.ext.exchangegraph.xmlbeans.Edge) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static de.grogra.ext.exchangegraph.xmlbeans.Edge parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (de.grogra.ext.exchangegraph.xmlbeans.Edge) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static de.grogra.ext.exchangegraph.xmlbeans.Edge parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (de.grogra.ext.exchangegraph.xmlbeans.Edge) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static de.grogra.ext.exchangegraph.xmlbeans.Edge parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (de.grogra.ext.exchangegraph.xmlbeans.Edge) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static de.grogra.ext.exchangegraph.xmlbeans.Edge parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (de.grogra.ext.exchangegraph.xmlbeans.Edge) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static de.grogra.ext.exchangegraph.xmlbeans.Edge parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (de.grogra.ext.exchangegraph.xmlbeans.Edge) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static de.grogra.ext.exchangegraph.xmlbeans.Edge parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (de.grogra.ext.exchangegraph.xmlbeans.Edge) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.grogra.ext.exchangegraph.xmlbeans.Edge parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (de.grogra.ext.exchangegraph.xmlbeans.Edge) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static de.grogra.ext.exchangegraph.xmlbeans.Edge parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (de.grogra.ext.exchangegraph.xmlbeans.Edge) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
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
