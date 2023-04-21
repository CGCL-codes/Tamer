package _2.plan.ucc.ean;

/**
 * An XML PurchaseConditionsPriceInformationType(@urn:ean.ucc:plan:2).
 *
 * This is a complex type.
 */
public interface PurchaseConditionsPriceInformationType extends org.apache.xmlbeans.XmlObject {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(PurchaseConditionsPriceInformationType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB04E84285D4137534ABAE99FF2BFA955").resolveHandle("purchaseconditionspriceinformationtype74cctype");

    /**
     * Gets the "itemPriceExclusiveAllowancesCharges" element
     */
    float getItemPriceExclusiveAllowancesCharges();

    /**
     * Gets (as xml) the "itemPriceExclusiveAllowancesCharges" element
     */
    org.apache.xmlbeans.XmlFloat xgetItemPriceExclusiveAllowancesCharges();

    /**
     * Sets the "itemPriceExclusiveAllowancesCharges" element
     */
    void setItemPriceExclusiveAllowancesCharges(float itemPriceExclusiveAllowancesCharges);

    /**
     * Sets (as xml) the "itemPriceExclusiveAllowancesCharges" element
     */
    void xsetItemPriceExclusiveAllowancesCharges(org.apache.xmlbeans.XmlFloat itemPriceExclusiveAllowancesCharges);

    /**
     * Gets the "itemPriceBaseQuantity" element
     */
    _2.ucc.ean.QuantityType getItemPriceBaseQuantity();

    /**
     * True if has "itemPriceBaseQuantity" element
     */
    boolean isSetItemPriceBaseQuantity();

    /**
     * Sets the "itemPriceBaseQuantity" element
     */
    void setItemPriceBaseQuantity(_2.ucc.ean.QuantityType itemPriceBaseQuantity);

    /**
     * Appends and returns a new empty "itemPriceBaseQuantity" element
     */
    _2.ucc.ean.QuantityType addNewItemPriceBaseQuantity();

    /**
     * Unsets the "itemPriceBaseQuantity" element
     */
    void unsetItemPriceBaseQuantity();

    /**
     * Gets the "quantityRange" element
     */
    _2.ucc.ean.QuantityRangeType getQuantityRange();

    /**
     * True if has "quantityRange" element
     */
    boolean isSetQuantityRange();

    /**
     * Sets the "quantityRange" element
     */
    void setQuantityRange(_2.ucc.ean.QuantityRangeType quantityRange);

    /**
     * Appends and returns a new empty "quantityRange" element
     */
    _2.ucc.ean.QuantityRangeType addNewQuantityRange();

    /**
     * Unsets the "quantityRange" element
     */
    void unsetQuantityRange();

    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    public static final class Factory {

        public static _2.plan.ucc.ean.PurchaseConditionsPriceInformationType newInstance() {
            return (_2.plan.ucc.ean.PurchaseConditionsPriceInformationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static _2.plan.ucc.ean.PurchaseConditionsPriceInformationType newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (_2.plan.ucc.ean.PurchaseConditionsPriceInformationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static _2.plan.ucc.ean.PurchaseConditionsPriceInformationType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.PurchaseConditionsPriceInformationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static _2.plan.ucc.ean.PurchaseConditionsPriceInformationType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.PurchaseConditionsPriceInformationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static _2.plan.ucc.ean.PurchaseConditionsPriceInformationType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.PurchaseConditionsPriceInformationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static _2.plan.ucc.ean.PurchaseConditionsPriceInformationType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.PurchaseConditionsPriceInformationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static _2.plan.ucc.ean.PurchaseConditionsPriceInformationType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.PurchaseConditionsPriceInformationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static _2.plan.ucc.ean.PurchaseConditionsPriceInformationType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.PurchaseConditionsPriceInformationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static _2.plan.ucc.ean.PurchaseConditionsPriceInformationType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.PurchaseConditionsPriceInformationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static _2.plan.ucc.ean.PurchaseConditionsPriceInformationType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.PurchaseConditionsPriceInformationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static _2.plan.ucc.ean.PurchaseConditionsPriceInformationType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.PurchaseConditionsPriceInformationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static _2.plan.ucc.ean.PurchaseConditionsPriceInformationType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.PurchaseConditionsPriceInformationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static _2.plan.ucc.ean.PurchaseConditionsPriceInformationType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.PurchaseConditionsPriceInformationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static _2.plan.ucc.ean.PurchaseConditionsPriceInformationType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.PurchaseConditionsPriceInformationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static _2.plan.ucc.ean.PurchaseConditionsPriceInformationType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.PurchaseConditionsPriceInformationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static _2.plan.ucc.ean.PurchaseConditionsPriceInformationType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.PurchaseConditionsPriceInformationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.plan.ucc.ean.PurchaseConditionsPriceInformationType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.plan.ucc.ean.PurchaseConditionsPriceInformationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.plan.ucc.ean.PurchaseConditionsPriceInformationType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.plan.ucc.ean.PurchaseConditionsPriceInformationType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
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
