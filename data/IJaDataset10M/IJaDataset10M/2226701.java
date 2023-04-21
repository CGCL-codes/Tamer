package _2.ucc.ean;

/**
 * An XML MonetaryAmountOrPercentageType(@urn:ean.ucc:2).
 *
 * This is a complex type.
 */
public interface MonetaryAmountOrPercentageType extends org.apache.xmlbeans.XmlObject {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(MonetaryAmountOrPercentageType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB04E84285D4137534ABAE99FF2BFA955").resolveHandle("monetaryamountorpercentagetypeff4dtype");

    /**
     * Gets the "amount" element
     */
    _2.ucc.ean.AmountType getAmount();

    /**
     * True if has "amount" element
     */
    boolean isSetAmount();

    /**
     * Sets the "amount" element
     */
    void setAmount(_2.ucc.ean.AmountType amount);

    /**
     * Appends and returns a new empty "amount" element
     */
    _2.ucc.ean.AmountType addNewAmount();

    /**
     * Unsets the "amount" element
     */
    void unsetAmount();

    /**
     * Gets the "percentage" element
     */
    java.math.BigDecimal getPercentage();

    /**
     * Gets (as xml) the "percentage" element
     */
    _2.ucc.ean.PercentageType xgetPercentage();

    /**
     * True if has "percentage" element
     */
    boolean isSetPercentage();

    /**
     * Sets the "percentage" element
     */
    void setPercentage(java.math.BigDecimal percentage);

    /**
     * Sets (as xml) the "percentage" element
     */
    void xsetPercentage(_2.ucc.ean.PercentageType percentage);

    /**
     * Unsets the "percentage" element
     */
    void unsetPercentage();

    /**
     * Gets the "measurementValue" element
     */
    _2.ucc.ean.MeasurementValueType getMeasurementValue();

    /**
     * True if has "measurementValue" element
     */
    boolean isSetMeasurementValue();

    /**
     * Sets the "measurementValue" element
     */
    void setMeasurementValue(_2.ucc.ean.MeasurementValueType measurementValue);

    /**
     * Appends and returns a new empty "measurementValue" element
     */
    _2.ucc.ean.MeasurementValueType addNewMeasurementValue();

    /**
     * Unsets the "measurementValue" element
     */
    void unsetMeasurementValue();

    /**
     * Gets the "ratePerUnit" element
     */
    _2.ucc.ean.RatePerUnitType getRatePerUnit();

    /**
     * True if has "ratePerUnit" element
     */
    boolean isSetRatePerUnit();

    /**
     * Sets the "ratePerUnit" element
     */
    void setRatePerUnit(_2.ucc.ean.RatePerUnitType ratePerUnit);

    /**
     * Appends and returns a new empty "ratePerUnit" element
     */
    _2.ucc.ean.RatePerUnitType addNewRatePerUnit();

    /**
     * Unsets the "ratePerUnit" element
     */
    void unsetRatePerUnit();

    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    public static final class Factory {

        public static _2.ucc.ean.MonetaryAmountOrPercentageType newInstance() {
            return (_2.ucc.ean.MonetaryAmountOrPercentageType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static _2.ucc.ean.MonetaryAmountOrPercentageType newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (_2.ucc.ean.MonetaryAmountOrPercentageType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static _2.ucc.ean.MonetaryAmountOrPercentageType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.MonetaryAmountOrPercentageType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static _2.ucc.ean.MonetaryAmountOrPercentageType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.MonetaryAmountOrPercentageType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static _2.ucc.ean.MonetaryAmountOrPercentageType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.MonetaryAmountOrPercentageType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static _2.ucc.ean.MonetaryAmountOrPercentageType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.MonetaryAmountOrPercentageType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static _2.ucc.ean.MonetaryAmountOrPercentageType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.MonetaryAmountOrPercentageType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static _2.ucc.ean.MonetaryAmountOrPercentageType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.MonetaryAmountOrPercentageType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static _2.ucc.ean.MonetaryAmountOrPercentageType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.MonetaryAmountOrPercentageType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static _2.ucc.ean.MonetaryAmountOrPercentageType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.MonetaryAmountOrPercentageType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static _2.ucc.ean.MonetaryAmountOrPercentageType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.MonetaryAmountOrPercentageType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static _2.ucc.ean.MonetaryAmountOrPercentageType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.MonetaryAmountOrPercentageType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static _2.ucc.ean.MonetaryAmountOrPercentageType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.MonetaryAmountOrPercentageType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static _2.ucc.ean.MonetaryAmountOrPercentageType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.MonetaryAmountOrPercentageType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static _2.ucc.ean.MonetaryAmountOrPercentageType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.MonetaryAmountOrPercentageType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static _2.ucc.ean.MonetaryAmountOrPercentageType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.MonetaryAmountOrPercentageType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.ucc.ean.MonetaryAmountOrPercentageType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.ucc.ean.MonetaryAmountOrPercentageType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.ucc.ean.MonetaryAmountOrPercentageType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.ucc.ean.MonetaryAmountOrPercentageType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
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
