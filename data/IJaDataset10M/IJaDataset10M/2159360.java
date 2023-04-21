package _2.plan.ucc.ean;

/**
 * An XML ForecastType(@urn:ean.ucc:plan:2).
 *
 * This is a complex type.
 */
public interface ForecastType extends _2.plan.ucc.ean.AbstractForecastType {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ForecastType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB04E84285D4137534ABAE99FF2BFA955").resolveHandle("forecasttype978dtype");

    /**
     * Gets array of all "forecastDataItem" elements
     */
    _2.plan.ucc.ean.ForecastDataItemType[] getForecastDataItemArray();

    /**
     * Gets ith "forecastDataItem" element
     */
    _2.plan.ucc.ean.ForecastDataItemType getForecastDataItemArray(int i);

    /**
     * Returns number of "forecastDataItem" element
     */
    int sizeOfForecastDataItemArray();

    /**
     * Sets array of all "forecastDataItem" element
     */
    void setForecastDataItemArray(_2.plan.ucc.ean.ForecastDataItemType[] forecastDataItemArray);

    /**
     * Sets ith "forecastDataItem" element
     */
    void setForecastDataItemArray(int i, _2.plan.ucc.ean.ForecastDataItemType forecastDataItem);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "forecastDataItem" element
     */
    _2.plan.ucc.ean.ForecastDataItemType insertNewForecastDataItem(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "forecastDataItem" element
     */
    _2.plan.ucc.ean.ForecastDataItemType addNewForecastDataItem();

    /**
     * Removes the ith "forecastDataItem" element
     */
    void removeForecastDataItem(int i);

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
     * Gets the "isForecastBasedOnConsensus" attribute
     */
    boolean getIsForecastBasedOnConsensus();

    /**
     * Gets (as xml) the "isForecastBasedOnConsensus" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetIsForecastBasedOnConsensus();

    /**
     * Sets the "isForecastBasedOnConsensus" attribute
     */
    void setIsForecastBasedOnConsensus(boolean isForecastBasedOnConsensus);

    /**
     * Sets (as xml) the "isForecastBasedOnConsensus" attribute
     */
    void xsetIsForecastBasedOnConsensus(org.apache.xmlbeans.XmlBoolean isForecastBasedOnConsensus);

    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    public static final class Factory {

        public static _2.plan.ucc.ean.ForecastType newInstance() {
            return (_2.plan.ucc.ean.ForecastType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static _2.plan.ucc.ean.ForecastType newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (_2.plan.ucc.ean.ForecastType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static _2.plan.ucc.ean.ForecastType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ForecastType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static _2.plan.ucc.ean.ForecastType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ForecastType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static _2.plan.ucc.ean.ForecastType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ForecastType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static _2.plan.ucc.ean.ForecastType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ForecastType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static _2.plan.ucc.ean.ForecastType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ForecastType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static _2.plan.ucc.ean.ForecastType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ForecastType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static _2.plan.ucc.ean.ForecastType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ForecastType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static _2.plan.ucc.ean.ForecastType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ForecastType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static _2.plan.ucc.ean.ForecastType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ForecastType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static _2.plan.ucc.ean.ForecastType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ForecastType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static _2.plan.ucc.ean.ForecastType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ForecastType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static _2.plan.ucc.ean.ForecastType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ForecastType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static _2.plan.ucc.ean.ForecastType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ForecastType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static _2.plan.ucc.ean.ForecastType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ForecastType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.plan.ucc.ean.ForecastType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.plan.ucc.ean.ForecastType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.plan.ucc.ean.ForecastType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.plan.ucc.ean.ForecastType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
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
