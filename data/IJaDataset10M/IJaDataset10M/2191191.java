package _2.ucc.ean;

/**
 * An XML TransactionalItemDataType(@urn:ean.ucc:2).
 *
 * This is a complex type.
 */
public interface TransactionalItemDataType extends org.apache.xmlbeans.XmlObject {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(TransactionalItemDataType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB04E84285D4137534ABAE99FF2BFA955").resolveHandle("transactionalitemdatatype669ftype");

    /**
     * Gets the "availableForSaleDate" element
     */
    java.util.Calendar getAvailableForSaleDate();

    /**
     * Gets (as xml) the "availableForSaleDate" element
     */
    org.apache.xmlbeans.XmlDate xgetAvailableForSaleDate();

    /**
     * True if has "availableForSaleDate" element
     */
    boolean isSetAvailableForSaleDate();

    /**
     * Sets the "availableForSaleDate" element
     */
    void setAvailableForSaleDate(java.util.Calendar availableForSaleDate);

    /**
     * Sets (as xml) the "availableForSaleDate" element
     */
    void xsetAvailableForSaleDate(org.apache.xmlbeans.XmlDate availableForSaleDate);

    /**
     * Unsets the "availableForSaleDate" element
     */
    void unsetAvailableForSaleDate();

    /**
     * Gets the "batchNumber" element
     */
    java.lang.String getBatchNumber();

    /**
     * Gets (as xml) the "batchNumber" element
     */
    org.apache.xmlbeans.XmlString xgetBatchNumber();

    /**
     * True if has "batchNumber" element
     */
    boolean isSetBatchNumber();

    /**
     * Sets the "batchNumber" element
     */
    void setBatchNumber(java.lang.String batchNumber);

    /**
     * Sets (as xml) the "batchNumber" element
     */
    void xsetBatchNumber(org.apache.xmlbeans.XmlString batchNumber);

    /**
     * Unsets the "batchNumber" element
     */
    void unsetBatchNumber();

    /**
     * Gets the "bestBeforeDate" element
     */
    java.util.Calendar getBestBeforeDate();

    /**
     * Gets (as xml) the "bestBeforeDate" element
     */
    org.apache.xmlbeans.XmlDate xgetBestBeforeDate();

    /**
     * True if has "bestBeforeDate" element
     */
    boolean isSetBestBeforeDate();

    /**
     * Sets the "bestBeforeDate" element
     */
    void setBestBeforeDate(java.util.Calendar bestBeforeDate);

    /**
     * Sets (as xml) the "bestBeforeDate" element
     */
    void xsetBestBeforeDate(org.apache.xmlbeans.XmlDate bestBeforeDate);

    /**
     * Unsets the "bestBeforeDate" element
     */
    void unsetBestBeforeDate();

    /**
     * Gets the "countryOfOrigin" element
     */
    _2.ucc.ean.ISO31661CodeType getCountryOfOrigin();

    /**
     * True if has "countryOfOrigin" element
     */
    boolean isSetCountryOfOrigin();

    /**
     * Sets the "countryOfOrigin" element
     */
    void setCountryOfOrigin(_2.ucc.ean.ISO31661CodeType countryOfOrigin);

    /**
     * Appends and returns a new empty "countryOfOrigin" element
     */
    _2.ucc.ean.ISO31661CodeType addNewCountryOfOrigin();

    /**
     * Unsets the "countryOfOrigin" element
     */
    void unsetCountryOfOrigin();

    /**
     * Gets the "itemExpirationDate" element
     */
    java.util.Calendar getItemExpirationDate();

    /**
     * Gets (as xml) the "itemExpirationDate" element
     */
    org.apache.xmlbeans.XmlDate xgetItemExpirationDate();

    /**
     * True if has "itemExpirationDate" element
     */
    boolean isSetItemExpirationDate();

    /**
     * Sets the "itemExpirationDate" element
     */
    void setItemExpirationDate(java.util.Calendar itemExpirationDate);

    /**
     * Sets (as xml) the "itemExpirationDate" element
     */
    void xsetItemExpirationDate(org.apache.xmlbeans.XmlDate itemExpirationDate);

    /**
     * Unsets the "itemExpirationDate" element
     */
    void unsetItemExpirationDate();

    /**
     * Gets the "lotNumber" element
     */
    java.lang.String getLotNumber();

    /**
     * Gets (as xml) the "lotNumber" element
     */
    org.apache.xmlbeans.XmlString xgetLotNumber();

    /**
     * True if has "lotNumber" element
     */
    boolean isSetLotNumber();

    /**
     * Sets the "lotNumber" element
     */
    void setLotNumber(java.lang.String lotNumber);

    /**
     * Sets (as xml) the "lotNumber" element
     */
    void xsetLotNumber(org.apache.xmlbeans.XmlString lotNumber);

    /**
     * Unsets the "lotNumber" element
     */
    void unsetLotNumber();

    /**
     * Gets the "packagingDate" element
     */
    java.util.Calendar getPackagingDate();

    /**
     * Gets (as xml) the "packagingDate" element
     */
    org.apache.xmlbeans.XmlDate xgetPackagingDate();

    /**
     * True if has "packagingDate" element
     */
    boolean isSetPackagingDate();

    /**
     * Sets the "packagingDate" element
     */
    void setPackagingDate(java.util.Calendar packagingDate);

    /**
     * Sets (as xml) the "packagingDate" element
     */
    void xsetPackagingDate(org.apache.xmlbeans.XmlDate packagingDate);

    /**
     * Unsets the "packagingDate" element
     */
    void unsetPackagingDate();

    /**
     * Gets the "productionDate" element
     */
    java.util.Calendar getProductionDate();

    /**
     * Gets (as xml) the "productionDate" element
     */
    org.apache.xmlbeans.XmlDate xgetProductionDate();

    /**
     * True if has "productionDate" element
     */
    boolean isSetProductionDate();

    /**
     * Sets the "productionDate" element
     */
    void setProductionDate(java.util.Calendar productionDate);

    /**
     * Sets (as xml) the "productionDate" element
     */
    void xsetProductionDate(org.apache.xmlbeans.XmlDate productionDate);

    /**
     * Unsets the "productionDate" element
     */
    void unsetProductionDate();

    /**
     * Gets the "productQualityIndication" element
     */
    _2.ucc.ean.QuantityType getProductQualityIndication();

    /**
     * True if has "productQualityIndication" element
     */
    boolean isSetProductQualityIndication();

    /**
     * Sets the "productQualityIndication" element
     */
    void setProductQualityIndication(_2.ucc.ean.QuantityType productQualityIndication);

    /**
     * Appends and returns a new empty "productQualityIndication" element
     */
    _2.ucc.ean.QuantityType addNewProductQualityIndication();

    /**
     * Unsets the "productQualityIndication" element
     */
    void unsetProductQualityIndication();

    /**
     * Gets the "sellByDate" element
     */
    java.util.Calendar getSellByDate();

    /**
     * Gets (as xml) the "sellByDate" element
     */
    org.apache.xmlbeans.XmlDate xgetSellByDate();

    /**
     * True if has "sellByDate" element
     */
    boolean isSetSellByDate();

    /**
     * Sets the "sellByDate" element
     */
    void setSellByDate(java.util.Calendar sellByDate);

    /**
     * Sets (as xml) the "sellByDate" element
     */
    void xsetSellByDate(org.apache.xmlbeans.XmlDate sellByDate);

    /**
     * Unsets the "sellByDate" element
     */
    void unsetSellByDate();

    /**
     * Gets the "shelfLife" element
     */
    java.lang.String getShelfLife();

    /**
     * Gets (as xml) the "shelfLife" element
     */
    org.apache.xmlbeans.XmlString xgetShelfLife();

    /**
     * True if has "shelfLife" element
     */
    boolean isSetShelfLife();

    /**
     * Sets the "shelfLife" element
     */
    void setShelfLife(java.lang.String shelfLife);

    /**
     * Sets (as xml) the "shelfLife" element
     */
    void xsetShelfLife(org.apache.xmlbeans.XmlString shelfLife);

    /**
     * Unsets the "shelfLife" element
     */
    void unsetShelfLife();

    /**
     * Gets array of all "transactionalItemWeight" elements
     */
    _2.ucc.ean.UnitMeasurementType[] getTransactionalItemWeightArray();

    /**
     * Gets ith "transactionalItemWeight" element
     */
    _2.ucc.ean.UnitMeasurementType getTransactionalItemWeightArray(int i);

    /**
     * Returns number of "transactionalItemWeight" element
     */
    int sizeOfTransactionalItemWeightArray();

    /**
     * Sets array of all "transactionalItemWeight" element
     */
    void setTransactionalItemWeightArray(_2.ucc.ean.UnitMeasurementType[] transactionalItemWeightArray);

    /**
     * Sets ith "transactionalItemWeight" element
     */
    void setTransactionalItemWeightArray(int i, _2.ucc.ean.UnitMeasurementType transactionalItemWeight);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "transactionalItemWeight" element
     */
    _2.ucc.ean.UnitMeasurementType insertNewTransactionalItemWeight(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "transactionalItemWeight" element
     */
    _2.ucc.ean.UnitMeasurementType addNewTransactionalItemWeight();

    /**
     * Removes the ith "transactionalItemWeight" element
     */
    void removeTransactionalItemWeight(int i);

    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    public static final class Factory {

        public static _2.ucc.ean.TransactionalItemDataType newInstance() {
            return (_2.ucc.ean.TransactionalItemDataType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static _2.ucc.ean.TransactionalItemDataType newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (_2.ucc.ean.TransactionalItemDataType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static _2.ucc.ean.TransactionalItemDataType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.TransactionalItemDataType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static _2.ucc.ean.TransactionalItemDataType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.TransactionalItemDataType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static _2.ucc.ean.TransactionalItemDataType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.TransactionalItemDataType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static _2.ucc.ean.TransactionalItemDataType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.TransactionalItemDataType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static _2.ucc.ean.TransactionalItemDataType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.TransactionalItemDataType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static _2.ucc.ean.TransactionalItemDataType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.TransactionalItemDataType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static _2.ucc.ean.TransactionalItemDataType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.TransactionalItemDataType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static _2.ucc.ean.TransactionalItemDataType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.TransactionalItemDataType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static _2.ucc.ean.TransactionalItemDataType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.TransactionalItemDataType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static _2.ucc.ean.TransactionalItemDataType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.TransactionalItemDataType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static _2.ucc.ean.TransactionalItemDataType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.TransactionalItemDataType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static _2.ucc.ean.TransactionalItemDataType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.TransactionalItemDataType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static _2.ucc.ean.TransactionalItemDataType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.TransactionalItemDataType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static _2.ucc.ean.TransactionalItemDataType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.TransactionalItemDataType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.ucc.ean.TransactionalItemDataType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.ucc.ean.TransactionalItemDataType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.ucc.ean.TransactionalItemDataType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.ucc.ean.TransactionalItemDataType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
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
