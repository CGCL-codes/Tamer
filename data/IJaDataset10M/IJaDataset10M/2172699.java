package _2.plan.ucc.ean;

/**
 * An XML ItemManagementProfileType(@urn:ean.ucc:plan:2).
 *
 * This is a complex type.
 */
public interface ItemManagementProfileType extends org.apache.xmlbeans.XmlObject {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ItemManagementProfileType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB04E84285D4137534ABAE99FF2BFA955").resolveHandle("itemmanagementprofiletyped627type");

    /**
     * Gets the "frozenPeriodDays" element
     */
    java.math.BigInteger getFrozenPeriodDays();

    /**
     * Gets (as xml) the "frozenPeriodDays" element
     */
    org.apache.xmlbeans.XmlInteger xgetFrozenPeriodDays();

    /**
     * Sets the "frozenPeriodDays" element
     */
    void setFrozenPeriodDays(java.math.BigInteger frozenPeriodDays);

    /**
     * Sets (as xml) the "frozenPeriodDays" element
     */
    void xsetFrozenPeriodDays(org.apache.xmlbeans.XmlInteger frozenPeriodDays);

    /**
     * Gets the "minimumInventory" element
     */
    float getMinimumInventory();

    /**
     * Gets (as xml) the "minimumInventory" element
     */
    org.apache.xmlbeans.XmlFloat xgetMinimumInventory();

    /**
     * Sets the "minimumInventory" element
     */
    void setMinimumInventory(float minimumInventory);

    /**
     * Sets (as xml) the "minimumInventory" element
     */
    void xsetMinimumInventory(org.apache.xmlbeans.XmlFloat minimumInventory);

    /**
     * Gets the "orderIntervalDays" element
     */
    java.math.BigInteger getOrderIntervalDays();

    /**
     * Gets (as xml) the "orderIntervalDays" element
     */
    org.apache.xmlbeans.XmlInteger xgetOrderIntervalDays();

    /**
     * Sets the "orderIntervalDays" element
     */
    void setOrderIntervalDays(java.math.BigInteger orderIntervalDays);

    /**
     * Sets (as xml) the "orderIntervalDays" element
     */
    void xsetOrderIntervalDays(org.apache.xmlbeans.XmlInteger orderIntervalDays);

    /**
     * Gets the "orderingLeadTimeDays" element
     */
    java.math.BigInteger getOrderingLeadTimeDays();

    /**
     * Gets (as xml) the "orderingLeadTimeDays" element
     */
    org.apache.xmlbeans.XmlInteger xgetOrderingLeadTimeDays();

    /**
     * Sets the "orderingLeadTimeDays" element
     */
    void setOrderingLeadTimeDays(java.math.BigInteger orderingLeadTimeDays);

    /**
     * Sets (as xml) the "orderingLeadTimeDays" element
     */
    void xsetOrderingLeadTimeDays(org.apache.xmlbeans.XmlInteger orderingLeadTimeDays);

    /**
     * Gets the "orderQuantityMinimum" element
     */
    float getOrderQuantityMinimum();

    /**
     * Gets (as xml) the "orderQuantityMinimum" element
     */
    org.apache.xmlbeans.XmlFloat xgetOrderQuantityMinimum();

    /**
     * Sets the "orderQuantityMinimum" element
     */
    void setOrderQuantityMinimum(float orderQuantityMinimum);

    /**
     * Sets (as xml) the "orderQuantityMinimum" element
     */
    void xsetOrderQuantityMinimum(org.apache.xmlbeans.XmlFloat orderQuantityMinimum);

    /**
     * Gets the "orderQuantityMultiple" element
     */
    float getOrderQuantityMultiple();

    /**
     * Gets (as xml) the "orderQuantityMultiple" element
     */
    org.apache.xmlbeans.XmlFloat xgetOrderQuantityMultiple();

    /**
     * Sets the "orderQuantityMultiple" element
     */
    void setOrderQuantityMultiple(float orderQuantityMultiple);

    /**
     * Sets (as xml) the "orderQuantityMultiple" element
     */
    void xsetOrderQuantityMultiple(org.apache.xmlbeans.XmlFloat orderQuantityMultiple);

    /**
     * Gets the "replenishmentOwner" element
     */
    java.lang.String getReplenishmentOwner();

    /**
     * Gets (as xml) the "replenishmentOwner" element
     */
    org.apache.xmlbeans.XmlString xgetReplenishmentOwner();

    /**
     * Sets the "replenishmentOwner" element
     */
    void setReplenishmentOwner(java.lang.String replenishmentOwner);

    /**
     * Sets (as xml) the "replenishmentOwner" element
     */
    void xsetReplenishmentOwner(org.apache.xmlbeans.XmlString replenishmentOwner);

    /**
     * Gets the "targetInventory" element
     */
    _2.ucc.ean.MeasurementValueType getTargetInventory();

    /**
     * Sets the "targetInventory" element
     */
    void setTargetInventory(_2.ucc.ean.MeasurementValueType targetInventory);

    /**
     * Appends and returns a new empty "targetInventory" element
     */
    _2.ucc.ean.MeasurementValueType addNewTargetInventory();

    /**
     * Gets the "targetServiceLevel" element
     */
    java.math.BigDecimal getTargetServiceLevel();

    /**
     * Gets (as xml) the "targetServiceLevel" element
     */
    _2.ucc.ean.PercentageType xgetTargetServiceLevel();

    /**
     * Sets the "targetServiceLevel" element
     */
    void setTargetServiceLevel(java.math.BigDecimal targetServiceLevel);

    /**
     * Sets (as xml) the "targetServiceLevel" element
     */
    void xsetTargetServiceLevel(_2.ucc.ean.PercentageType targetServiceLevel);

    /**
     * Gets the "collaborativeTradeItem" element
     */
    _2.plan.ucc.ean.CollaborativeTradeItemType getCollaborativeTradeItem();

    /**
     * Sets the "collaborativeTradeItem" element
     */
    void setCollaborativeTradeItem(_2.plan.ucc.ean.CollaborativeTradeItemType collaborativeTradeItem);

    /**
     * Appends and returns a new empty "collaborativeTradeItem" element
     */
    _2.plan.ucc.ean.CollaborativeTradeItemType addNewCollaborativeTradeItem();

    /**
     * Gets the "effectivePeriod" element
     */
    _2.ucc.ean.TimePeriodType getEffectivePeriod();

    /**
     * Sets the "effectivePeriod" element
     */
    void setEffectivePeriod(_2.ucc.ean.TimePeriodType effectivePeriod);

    /**
     * Appends and returns a new empty "effectivePeriod" element
     */
    _2.ucc.ean.TimePeriodType addNewEffectivePeriod();

    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    public static final class Factory {

        public static _2.plan.ucc.ean.ItemManagementProfileType newInstance() {
            return (_2.plan.ucc.ean.ItemManagementProfileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static _2.plan.ucc.ean.ItemManagementProfileType newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (_2.plan.ucc.ean.ItemManagementProfileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static _2.plan.ucc.ean.ItemManagementProfileType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ItemManagementProfileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static _2.plan.ucc.ean.ItemManagementProfileType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ItemManagementProfileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static _2.plan.ucc.ean.ItemManagementProfileType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ItemManagementProfileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static _2.plan.ucc.ean.ItemManagementProfileType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ItemManagementProfileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static _2.plan.ucc.ean.ItemManagementProfileType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ItemManagementProfileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static _2.plan.ucc.ean.ItemManagementProfileType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ItemManagementProfileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static _2.plan.ucc.ean.ItemManagementProfileType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ItemManagementProfileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static _2.plan.ucc.ean.ItemManagementProfileType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ItemManagementProfileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static _2.plan.ucc.ean.ItemManagementProfileType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ItemManagementProfileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static _2.plan.ucc.ean.ItemManagementProfileType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ItemManagementProfileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static _2.plan.ucc.ean.ItemManagementProfileType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ItemManagementProfileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static _2.plan.ucc.ean.ItemManagementProfileType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ItemManagementProfileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static _2.plan.ucc.ean.ItemManagementProfileType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ItemManagementProfileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static _2.plan.ucc.ean.ItemManagementProfileType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ItemManagementProfileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.plan.ucc.ean.ItemManagementProfileType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.plan.ucc.ean.ItemManagementProfileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.plan.ucc.ean.ItemManagementProfileType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.plan.ucc.ean.ItemManagementProfileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
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
