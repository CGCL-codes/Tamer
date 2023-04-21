package _2.plan.ucc.ean;

/**
 * An XML ForecastRevisionType(@urn:ean.ucc:plan:2).
 *
 * This is a complex type.
 */
public interface ForecastRevisionType extends _2.plan.ucc.ean.AbstractForecastType {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ForecastRevisionType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB04E84285D4137534ABAE99FF2BFA955").resolveHandle("forecastrevisiontype7532type");

    /**
     * Gets array of all "forecastRevisionDataItem" elements
     */
    _2.plan.ucc.ean.ForecastRevisionDataItemType[] getForecastRevisionDataItemArray();

    /**
     * Gets ith "forecastRevisionDataItem" element
     */
    _2.plan.ucc.ean.ForecastRevisionDataItemType getForecastRevisionDataItemArray(int i);

    /**
     * Returns number of "forecastRevisionDataItem" element
     */
    int sizeOfForecastRevisionDataItemArray();

    /**
     * Sets array of all "forecastRevisionDataItem" element
     */
    void setForecastRevisionDataItemArray(_2.plan.ucc.ean.ForecastRevisionDataItemType[] forecastRevisionDataItemArray);

    /**
     * Sets ith "forecastRevisionDataItem" element
     */
    void setForecastRevisionDataItemArray(int i, _2.plan.ucc.ean.ForecastRevisionDataItemType forecastRevisionDataItem);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "forecastRevisionDataItem" element
     */
    _2.plan.ucc.ean.ForecastRevisionDataItemType insertNewForecastRevisionDataItem(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "forecastRevisionDataItem" element
     */
    _2.plan.ucc.ean.ForecastRevisionDataItemType addNewForecastRevisionDataItem();

    /**
     * Removes the ith "forecastRevisionDataItem" element
     */
    void removeForecastRevisionDataItem(int i);

    /**
     * Gets the "extension" element
     */
    _2.ucc.ean.ExtensionType getExtension();

    /**
     * True if has "extension" element
     */
    boolean isSetExtension();

    /**
     * Sets the "extension" element
     */
    void setExtension(_2.ucc.ean.ExtensionType extension);

    /**
     * Appends and returns a new empty "extension" element
     */
    _2.ucc.ean.ExtensionType addNewExtension();

    /**
     * Unsets the "extension" element
     */
    void unsetExtension();

    /**
     * Gets the "revisionStatus" attribute
     */
    _2.plan.ucc.ean.RevisionStatusCodeListType.Enum getRevisionStatus();

    /**
     * Gets (as xml) the "revisionStatus" attribute
     */
    _2.plan.ucc.ean.RevisionStatusCodeListType xgetRevisionStatus();

    /**
     * True if has "revisionStatus" attribute
     */
    boolean isSetRevisionStatus();

    /**
     * Sets the "revisionStatus" attribute
     */
    void setRevisionStatus(_2.plan.ucc.ean.RevisionStatusCodeListType.Enum revisionStatus);

    /**
     * Sets (as xml) the "revisionStatus" attribute
     */
    void xsetRevisionStatus(_2.plan.ucc.ean.RevisionStatusCodeListType revisionStatus);

    /**
     * Unsets the "revisionStatus" attribute
     */
    void unsetRevisionStatus();

    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    public static final class Factory {

        public static _2.plan.ucc.ean.ForecastRevisionType newInstance() {
            return (_2.plan.ucc.ean.ForecastRevisionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static _2.plan.ucc.ean.ForecastRevisionType newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (_2.plan.ucc.ean.ForecastRevisionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static _2.plan.ucc.ean.ForecastRevisionType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ForecastRevisionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static _2.plan.ucc.ean.ForecastRevisionType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ForecastRevisionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static _2.plan.ucc.ean.ForecastRevisionType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ForecastRevisionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static _2.plan.ucc.ean.ForecastRevisionType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ForecastRevisionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static _2.plan.ucc.ean.ForecastRevisionType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ForecastRevisionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static _2.plan.ucc.ean.ForecastRevisionType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ForecastRevisionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static _2.plan.ucc.ean.ForecastRevisionType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ForecastRevisionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static _2.plan.ucc.ean.ForecastRevisionType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ForecastRevisionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static _2.plan.ucc.ean.ForecastRevisionType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ForecastRevisionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static _2.plan.ucc.ean.ForecastRevisionType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ForecastRevisionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static _2.plan.ucc.ean.ForecastRevisionType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ForecastRevisionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static _2.plan.ucc.ean.ForecastRevisionType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ForecastRevisionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static _2.plan.ucc.ean.ForecastRevisionType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ForecastRevisionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static _2.plan.ucc.ean.ForecastRevisionType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ForecastRevisionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.plan.ucc.ean.ForecastRevisionType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.plan.ucc.ean.ForecastRevisionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.plan.ucc.ean.ForecastRevisionType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.plan.ucc.ean.ForecastRevisionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
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
