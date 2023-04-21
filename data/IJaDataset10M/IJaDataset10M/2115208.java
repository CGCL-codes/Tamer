package noNamespace;

/**
 * An XML ResponseTransactionType(@).
 *
 * This is a complex type.
 */
public interface ResponseTransactionType extends org.apache.xmlbeans.XmlObject {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ResponseTransactionType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9B755B3F7219A48EA385FA8800E9C758").resolveHandle("responsetransactiontyped80etype");

    /**
     * Gets the "Response" element
     */
    noNamespace.ResponseType getResponse();

    /**
     * True if has "Response" element
     */
    boolean isSetResponse();

    /**
     * Sets the "Response" element
     */
    void setResponse(noNamespace.ResponseType response);

    /**
     * Appends and returns a new empty "Response" element
     */
    noNamespace.ResponseType addNewResponse();

    /**
     * Unsets the "Response" element
     */
    void unsetResponse();

    /**
     * Gets the "RetrefNum" element
     */
    java.lang.String getRetrefNum();

    /**
     * Gets (as xml) the "RetrefNum" element
     */
    org.apache.xmlbeans.XmlString xgetRetrefNum();

    /**
     * True if has "RetrefNum" element
     */
    boolean isSetRetrefNum();

    /**
     * Sets the "RetrefNum" element
     */
    void setRetrefNum(java.lang.String retrefNum);

    /**
     * Sets (as xml) the "RetrefNum" element
     */
    void xsetRetrefNum(org.apache.xmlbeans.XmlString retrefNum);

    /**
     * Unsets the "RetrefNum" element
     */
    void unsetRetrefNum();

    /**
     * Gets the "AuthCode" element
     */
    java.lang.String getAuthCode();

    /**
     * Gets (as xml) the "AuthCode" element
     */
    org.apache.xmlbeans.XmlString xgetAuthCode();

    /**
     * True if has "AuthCode" element
     */
    boolean isSetAuthCode();

    /**
     * Sets the "AuthCode" element
     */
    void setAuthCode(java.lang.String authCode);

    /**
     * Sets (as xml) the "AuthCode" element
     */
    void xsetAuthCode(org.apache.xmlbeans.XmlString authCode);

    /**
     * Unsets the "AuthCode" element
     */
    void unsetAuthCode();

    /**
     * Gets the "BatchNum" element
     */
    java.lang.String getBatchNum();

    /**
     * Gets (as xml) the "BatchNum" element
     */
    org.apache.xmlbeans.XmlString xgetBatchNum();

    /**
     * True if has "BatchNum" element
     */
    boolean isSetBatchNum();

    /**
     * Sets the "BatchNum" element
     */
    void setBatchNum(java.lang.String batchNum);

    /**
     * Sets (as xml) the "BatchNum" element
     */
    void xsetBatchNum(org.apache.xmlbeans.XmlString batchNum);

    /**
     * Unsets the "BatchNum" element
     */
    void unsetBatchNum();

    /**
     * Gets the "SequenceNum" element
     */
    java.lang.String getSequenceNum();

    /**
     * Gets (as xml) the "SequenceNum" element
     */
    org.apache.xmlbeans.XmlString xgetSequenceNum();

    /**
     * True if has "SequenceNum" element
     */
    boolean isSetSequenceNum();

    /**
     * Sets the "SequenceNum" element
     */
    void setSequenceNum(java.lang.String sequenceNum);

    /**
     * Sets (as xml) the "SequenceNum" element
     */
    void xsetSequenceNum(org.apache.xmlbeans.XmlString sequenceNum);

    /**
     * Unsets the "SequenceNum" element
     */
    void unsetSequenceNum();

    /**
     * Gets the "ProvDate" element
     */
    java.lang.String getProvDate();

    /**
     * Gets (as xml) the "ProvDate" element
     */
    org.apache.xmlbeans.XmlString xgetProvDate();

    /**
     * True if has "ProvDate" element
     */
    boolean isSetProvDate();

    /**
     * Sets the "ProvDate" element
     */
    void setProvDate(java.lang.String provDate);

    /**
     * Sets (as xml) the "ProvDate" element
     */
    void xsetProvDate(org.apache.xmlbeans.XmlString provDate);

    /**
     * Unsets the "ProvDate" element
     */
    void unsetProvDate();

    /**
     * Gets the "HostMsgList" element
     */
    noNamespace.HostMsgListType getHostMsgList();

    /**
     * True if has "HostMsgList" element
     */
    boolean isSetHostMsgList();

    /**
     * Sets the "HostMsgList" element
     */
    void setHostMsgList(noNamespace.HostMsgListType hostMsgList);

    /**
     * Appends and returns a new empty "HostMsgList" element
     */
    noNamespace.HostMsgListType addNewHostMsgList();

    /**
     * Unsets the "HostMsgList" element
     */
    void unsetHostMsgList();

    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    public static final class Factory {

        public static noNamespace.ResponseTransactionType newInstance() {
            return (noNamespace.ResponseTransactionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static noNamespace.ResponseTransactionType newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (noNamespace.ResponseTransactionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static noNamespace.ResponseTransactionType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (noNamespace.ResponseTransactionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static noNamespace.ResponseTransactionType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (noNamespace.ResponseTransactionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static noNamespace.ResponseTransactionType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (noNamespace.ResponseTransactionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static noNamespace.ResponseTransactionType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (noNamespace.ResponseTransactionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static noNamespace.ResponseTransactionType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (noNamespace.ResponseTransactionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static noNamespace.ResponseTransactionType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (noNamespace.ResponseTransactionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static noNamespace.ResponseTransactionType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (noNamespace.ResponseTransactionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static noNamespace.ResponseTransactionType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (noNamespace.ResponseTransactionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static noNamespace.ResponseTransactionType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (noNamespace.ResponseTransactionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static noNamespace.ResponseTransactionType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (noNamespace.ResponseTransactionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static noNamespace.ResponseTransactionType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (noNamespace.ResponseTransactionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static noNamespace.ResponseTransactionType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (noNamespace.ResponseTransactionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static noNamespace.ResponseTransactionType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (noNamespace.ResponseTransactionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static noNamespace.ResponseTransactionType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (noNamespace.ResponseTransactionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static noNamespace.ResponseTransactionType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (noNamespace.ResponseTransactionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static noNamespace.ResponseTransactionType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (noNamespace.ResponseTransactionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
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
