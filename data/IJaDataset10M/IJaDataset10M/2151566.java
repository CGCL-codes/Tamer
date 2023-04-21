package tr.com.srdc.www.retailer;

/**
 * A document containing one pcMessage(@http://www.srdc.com.tr/retailer/) element.
 *
 * This is a complex type.
 */
public interface PcMessageDocument extends org.apache.xmlbeans.XmlObject {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(PcMessageDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB04E84285D4137534ABAE99FF2BFA955").resolveHandle("pcmessageb587doctype");

    /**
     * Gets the "pcMessage" element
     */
    org.unece.www.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocument1 getPcMessage();

    /**
     * Sets the "pcMessage" element
     */
    void setPcMessage(org.unece.www.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocument1 pcMessage);

    /**
     * Appends and returns a new empty "pcMessage" element
     */
    org.unece.www.cefact.namespaces.standardbusinessdocumentheader.StandardBusinessDocument1 addNewPcMessage();

    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    public static final class Factory {

        public static tr.com.srdc.www.retailer.PcMessageDocument newInstance() {
            return (tr.com.srdc.www.retailer.PcMessageDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static tr.com.srdc.www.retailer.PcMessageDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (tr.com.srdc.www.retailer.PcMessageDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static tr.com.srdc.www.retailer.PcMessageDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (tr.com.srdc.www.retailer.PcMessageDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static tr.com.srdc.www.retailer.PcMessageDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (tr.com.srdc.www.retailer.PcMessageDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static tr.com.srdc.www.retailer.PcMessageDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (tr.com.srdc.www.retailer.PcMessageDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static tr.com.srdc.www.retailer.PcMessageDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (tr.com.srdc.www.retailer.PcMessageDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static tr.com.srdc.www.retailer.PcMessageDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (tr.com.srdc.www.retailer.PcMessageDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static tr.com.srdc.www.retailer.PcMessageDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (tr.com.srdc.www.retailer.PcMessageDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static tr.com.srdc.www.retailer.PcMessageDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (tr.com.srdc.www.retailer.PcMessageDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static tr.com.srdc.www.retailer.PcMessageDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (tr.com.srdc.www.retailer.PcMessageDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static tr.com.srdc.www.retailer.PcMessageDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (tr.com.srdc.www.retailer.PcMessageDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static tr.com.srdc.www.retailer.PcMessageDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (tr.com.srdc.www.retailer.PcMessageDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static tr.com.srdc.www.retailer.PcMessageDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (tr.com.srdc.www.retailer.PcMessageDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static tr.com.srdc.www.retailer.PcMessageDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (tr.com.srdc.www.retailer.PcMessageDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static tr.com.srdc.www.retailer.PcMessageDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (tr.com.srdc.www.retailer.PcMessageDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static tr.com.srdc.www.retailer.PcMessageDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (tr.com.srdc.www.retailer.PcMessageDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static tr.com.srdc.www.retailer.PcMessageDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (tr.com.srdc.www.retailer.PcMessageDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static tr.com.srdc.www.retailer.PcMessageDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (tr.com.srdc.www.retailer.PcMessageDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
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
