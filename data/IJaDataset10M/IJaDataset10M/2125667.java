package _2.deliver.ucc.ean;

/**
 * An XML InventoryActivityOrInventoryStatusType(@urn:ean.ucc:deliver:2).
 *
 * This is a complex type.
 */
public interface InventoryActivityOrInventoryStatusType extends _2.ucc.ean.DocumentType {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(InventoryActivityOrInventoryStatusType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB04E84285D4137534ABAE99FF2BFA955").resolveHandle("inventoryactivityorinventorystatustypea376type");

    /**
     * Gets the "inventoryActivityOrInventoryStatusIdentification" element
     */
    _2.ucc.ean.EntityIdentificationType getInventoryActivityOrInventoryStatusIdentification();

    /**
     * Sets the "inventoryActivityOrInventoryStatusIdentification" element
     */
    void setInventoryActivityOrInventoryStatusIdentification(_2.ucc.ean.EntityIdentificationType inventoryActivityOrInventoryStatusIdentification);

    /**
     * Appends and returns a new empty "inventoryActivityOrInventoryStatusIdentification" element
     */
    _2.ucc.ean.EntityIdentificationType addNewInventoryActivityOrInventoryStatusIdentification();

    /**
     * Gets the "beginDateTime" element
     */
    java.util.Calendar getBeginDateTime();

    /**
     * Gets (as xml) the "beginDateTime" element
     */
    org.apache.xmlbeans.XmlDateTime xgetBeginDateTime();

    /**
     * Sets the "beginDateTime" element
     */
    void setBeginDateTime(java.util.Calendar beginDateTime);

    /**
     * Sets (as xml) the "beginDateTime" element
     */
    void xsetBeginDateTime(org.apache.xmlbeans.XmlDateTime beginDateTime);

    /**
     * Gets the "inventoryDocumentType" element
     */
    _2.deliver.ucc.ean.InventoryDocumentTypeListType.Enum getInventoryDocumentType();

    /**
     * Gets (as xml) the "inventoryDocumentType" element
     */
    _2.deliver.ucc.ean.InventoryDocumentTypeListType xgetInventoryDocumentType();

    /**
     * Sets the "inventoryDocumentType" element
     */
    void setInventoryDocumentType(_2.deliver.ucc.ean.InventoryDocumentTypeListType.Enum inventoryDocumentType);

    /**
     * Sets (as xml) the "inventoryDocumentType" element
     */
    void xsetInventoryDocumentType(_2.deliver.ucc.ean.InventoryDocumentTypeListType inventoryDocumentType);

    /**
     * Gets the "structureType" element
     */
    _2.ucc.ean.StructureTypeListType.Enum getStructureType();

    /**
     * Gets (as xml) the "structureType" element
     */
    _2.ucc.ean.StructureTypeListType xgetStructureType();

    /**
     * Sets the "structureType" element
     */
    void setStructureType(_2.ucc.ean.StructureTypeListType.Enum structureType);

    /**
     * Sets (as xml) the "structureType" element
     */
    void xsetStructureType(_2.ucc.ean.StructureTypeListType structureType);

    /**
     * Gets the "endDateTime" element
     */
    java.util.Calendar getEndDateTime();

    /**
     * Gets (as xml) the "endDateTime" element
     */
    org.apache.xmlbeans.XmlDateTime xgetEndDateTime();

    /**
     * True if has "endDateTime" element
     */
    boolean isSetEndDateTime();

    /**
     * Sets the "endDateTime" element
     */
    void setEndDateTime(java.util.Calendar endDateTime);

    /**
     * Sets (as xml) the "endDateTime" element
     */
    void xsetEndDateTime(org.apache.xmlbeans.XmlDateTime endDateTime);

    /**
     * Unsets the "endDateTime" element
     */
    void unsetEndDateTime();

    /**
     * Gets the "inventoryReportingParty" element
     */
    _2.ucc.ean.PartyIdentificationType getInventoryReportingParty();

    /**
     * Sets the "inventoryReportingParty" element
     */
    void setInventoryReportingParty(_2.ucc.ean.PartyIdentificationType inventoryReportingParty);

    /**
     * Appends and returns a new empty "inventoryReportingParty" element
     */
    _2.ucc.ean.PartyIdentificationType addNewInventoryReportingParty();

    /**
     * Gets the "inventoryReportToParty" element
     */
    _2.ucc.ean.PartyIdentificationType getInventoryReportToParty();

    /**
     * Sets the "inventoryReportToParty" element
     */
    void setInventoryReportToParty(_2.ucc.ean.PartyIdentificationType inventoryReportToParty);

    /**
     * Appends and returns a new empty "inventoryReportToParty" element
     */
    _2.ucc.ean.PartyIdentificationType addNewInventoryReportToParty();

    /**
     * Gets array of all "inventoryItemLocationInformation" elements
     */
    _2.deliver.ucc.ean.InventoryItemLocationInformationType[] getInventoryItemLocationInformationArray();

    /**
     * Gets ith "inventoryItemLocationInformation" element
     */
    _2.deliver.ucc.ean.InventoryItemLocationInformationType getInventoryItemLocationInformationArray(int i);

    /**
     * Returns number of "inventoryItemLocationInformation" element
     */
    int sizeOfInventoryItemLocationInformationArray();

    /**
     * Sets array of all "inventoryItemLocationInformation" element
     */
    void setInventoryItemLocationInformationArray(_2.deliver.ucc.ean.InventoryItemLocationInformationType[] inventoryItemLocationInformationArray);

    /**
     * Sets ith "inventoryItemLocationInformation" element
     */
    void setInventoryItemLocationInformationArray(int i, _2.deliver.ucc.ean.InventoryItemLocationInformationType inventoryItemLocationInformation);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "inventoryItemLocationInformation" element
     */
    _2.deliver.ucc.ean.InventoryItemLocationInformationType insertNewInventoryItemLocationInformation(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "inventoryItemLocationInformation" element
     */
    _2.deliver.ucc.ean.InventoryItemLocationInformationType addNewInventoryItemLocationInformation();

    /**
     * Removes the ith "inventoryItemLocationInformation" element
     */
    void removeInventoryItemLocationInformation(int i);

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
     * A factory class with static methods for creating instances
     * of this type.
     */
    public static final class Factory {

        public static _2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType newInstance() {
            return (_2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static _2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (_2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static _2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (_2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static _2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static _2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static _2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static _2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static _2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static _2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static _2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static _2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static _2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static _2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (_2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static _2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static _2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (_2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static _2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.deliver.ucc.ean.InventoryActivityOrInventoryStatusType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
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
