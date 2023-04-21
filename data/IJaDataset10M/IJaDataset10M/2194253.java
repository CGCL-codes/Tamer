package _2.ucc.ean;

/**
 * An XML InventoryStatusLineItemType(@urn:ean.ucc:2).
 *
 * This is a complex type.
 */
public interface InventoryStatusLineItemType extends _2.ucc.ean.LineItemType {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(InventoryStatusLineItemType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB04E84285D4137534ABAE99FF2BFA955").resolveHandle("inventorystatuslineitemtypef2cetype");

    /**
     * Gets the "inventorySublocation" element
     */
    _2.ucc.ean.PartyIdentificationType getInventorySublocation();

    /**
     * True if has "inventorySublocation" element
     */
    boolean isSetInventorySublocation();

    /**
     * Sets the "inventorySublocation" element
     */
    void setInventorySublocation(_2.ucc.ean.PartyIdentificationType inventorySublocation);

    /**
     * Appends and returns a new empty "inventorySublocation" element
     */
    _2.ucc.ean.PartyIdentificationType addNewInventorySublocation();

    /**
     * Unsets the "inventorySublocation" element
     */
    void unsetInventorySublocation();

    /**
     * Gets the "logisticUnitIdentification" element
     */
    _2.ucc.ean.LogisticUnitIdentificationType getLogisticUnitIdentification();

    /**
     * True if has "logisticUnitIdentification" element
     */
    boolean isSetLogisticUnitIdentification();

    /**
     * Sets the "logisticUnitIdentification" element
     */
    void setLogisticUnitIdentification(_2.ucc.ean.LogisticUnitIdentificationType logisticUnitIdentification);

    /**
     * Appends and returns a new empty "logisticUnitIdentification" element
     */
    _2.ucc.ean.LogisticUnitIdentificationType addNewLogisticUnitIdentification();

    /**
     * Unsets the "logisticUnitIdentification" element
     */
    void unsetLogisticUnitIdentification();

    /**
     * Gets array of all "inventoryStatusQuantitySpecification" elements
     */
    _2.ucc.ean.InventoryStatusQuantitySpecificationType[] getInventoryStatusQuantitySpecificationArray();

    /**
     * Gets ith "inventoryStatusQuantitySpecification" element
     */
    _2.ucc.ean.InventoryStatusQuantitySpecificationType getInventoryStatusQuantitySpecificationArray(int i);

    /**
     * Returns number of "inventoryStatusQuantitySpecification" element
     */
    int sizeOfInventoryStatusQuantitySpecificationArray();

    /**
     * Sets array of all "inventoryStatusQuantitySpecification" element
     */
    void setInventoryStatusQuantitySpecificationArray(_2.ucc.ean.InventoryStatusQuantitySpecificationType[] inventoryStatusQuantitySpecificationArray);

    /**
     * Sets ith "inventoryStatusQuantitySpecification" element
     */
    void setInventoryStatusQuantitySpecificationArray(int i, _2.ucc.ean.InventoryStatusQuantitySpecificationType inventoryStatusQuantitySpecification);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "inventoryStatusQuantitySpecification" element
     */
    _2.ucc.ean.InventoryStatusQuantitySpecificationType insertNewInventoryStatusQuantitySpecification(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "inventoryStatusQuantitySpecification" element
     */
    _2.ucc.ean.InventoryStatusQuantitySpecificationType addNewInventoryStatusQuantitySpecification();

    /**
     * Removes the ith "inventoryStatusQuantitySpecification" element
     */
    void removeInventoryStatusQuantitySpecification(int i);

    /**
     * Gets the "inventoryDateTime" attribute
     */
    java.util.Calendar getInventoryDateTime();

    /**
     * Gets (as xml) the "inventoryDateTime" attribute
     */
    org.apache.xmlbeans.XmlDateTime xgetInventoryDateTime();

    /**
     * True if has "inventoryDateTime" attribute
     */
    boolean isSetInventoryDateTime();

    /**
     * Sets the "inventoryDateTime" attribute
     */
    void setInventoryDateTime(java.util.Calendar inventoryDateTime);

    /**
     * Sets (as xml) the "inventoryDateTime" attribute
     */
    void xsetInventoryDateTime(org.apache.xmlbeans.XmlDateTime inventoryDateTime);

    /**
     * Unsets the "inventoryDateTime" attribute
     */
    void unsetInventoryDateTime();

    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    public static final class Factory {

        public static _2.ucc.ean.InventoryStatusLineItemType newInstance() {
            return (_2.ucc.ean.InventoryStatusLineItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static _2.ucc.ean.InventoryStatusLineItemType newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (_2.ucc.ean.InventoryStatusLineItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static _2.ucc.ean.InventoryStatusLineItemType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.InventoryStatusLineItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static _2.ucc.ean.InventoryStatusLineItemType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.InventoryStatusLineItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static _2.ucc.ean.InventoryStatusLineItemType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.InventoryStatusLineItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static _2.ucc.ean.InventoryStatusLineItemType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.InventoryStatusLineItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static _2.ucc.ean.InventoryStatusLineItemType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.InventoryStatusLineItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static _2.ucc.ean.InventoryStatusLineItemType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.InventoryStatusLineItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static _2.ucc.ean.InventoryStatusLineItemType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.InventoryStatusLineItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static _2.ucc.ean.InventoryStatusLineItemType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.InventoryStatusLineItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static _2.ucc.ean.InventoryStatusLineItemType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.InventoryStatusLineItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static _2.ucc.ean.InventoryStatusLineItemType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.InventoryStatusLineItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static _2.ucc.ean.InventoryStatusLineItemType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.InventoryStatusLineItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static _2.ucc.ean.InventoryStatusLineItemType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.InventoryStatusLineItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static _2.ucc.ean.InventoryStatusLineItemType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.InventoryStatusLineItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static _2.ucc.ean.InventoryStatusLineItemType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.InventoryStatusLineItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.ucc.ean.InventoryStatusLineItemType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.ucc.ean.InventoryStatusLineItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.ucc.ean.InventoryStatusLineItemType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.ucc.ean.InventoryStatusLineItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
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
