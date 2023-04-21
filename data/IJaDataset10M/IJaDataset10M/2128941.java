package org.unece.www.cefact.namespaces.standardbusinessdocumentheader;

/**
 * An XML Manifest(@http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader).
 *
 * This is a complex type.
 */
public interface Manifest extends org.apache.xmlbeans.XmlObject {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Manifest.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB04E84285D4137534ABAE99FF2BFA955").resolveHandle("manifesta047type");

    /**
     * Gets the "NumberOfItems" element
     */
    java.math.BigInteger getNumberOfItems();

    /**
     * Gets (as xml) the "NumberOfItems" element
     */
    org.apache.xmlbeans.XmlInteger xgetNumberOfItems();

    /**
     * Sets the "NumberOfItems" element
     */
    void setNumberOfItems(java.math.BigInteger numberOfItems);

    /**
     * Sets (as xml) the "NumberOfItems" element
     */
    void xsetNumberOfItems(org.apache.xmlbeans.XmlInteger numberOfItems);

    /**
     * Gets array of all "ManifestItem" elements
     */
    org.unece.www.cefact.namespaces.standardbusinessdocumentheader.ManifestItem[] getManifestItemArray();

    /**
     * Gets ith "ManifestItem" element
     */
    org.unece.www.cefact.namespaces.standardbusinessdocumentheader.ManifestItem getManifestItemArray(int i);

    /**
     * Returns number of "ManifestItem" element
     */
    int sizeOfManifestItemArray();

    /**
     * Sets array of all "ManifestItem" element
     */
    void setManifestItemArray(org.unece.www.cefact.namespaces.standardbusinessdocumentheader.ManifestItem[] manifestItemArray);

    /**
     * Sets ith "ManifestItem" element
     */
    void setManifestItemArray(int i, org.unece.www.cefact.namespaces.standardbusinessdocumentheader.ManifestItem manifestItem);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ManifestItem" element
     */
    org.unece.www.cefact.namespaces.standardbusinessdocumentheader.ManifestItem insertNewManifestItem(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "ManifestItem" element
     */
    org.unece.www.cefact.namespaces.standardbusinessdocumentheader.ManifestItem addNewManifestItem();

    /**
     * Removes the ith "ManifestItem" element
     */
    void removeManifestItem(int i);

    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    public static final class Factory {

        public static org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest newInstance() {
            return (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (org.unece.www.cefact.namespaces.standardbusinessdocumentheader.Manifest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
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
