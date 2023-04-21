package _2.plan.ucc.ean;

/**
 * An XML ProductActivityDataItemType(@urn:ean.ucc:plan:2).
 *
 * This is a complex type.
 */
public interface ProductActivityDataItemType extends _2.plan.ucc.ean.TimeSeriesDataItemType {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ProductActivityDataItemType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB04E84285D4137534ABAE99FF2BFA955").resolveHandle("productactivitydataitemtype5edftype");

    /**
     * Gets the "activityType" element
     */
    _2.plan.ucc.ean.ActivityTypeCodeListType.Enum getActivityType();

    /**
     * Gets (as xml) the "activityType" element
     */
    _2.plan.ucc.ean.ActivityTypeCodeListType xgetActivityType();

    /**
     * Sets the "activityType" element
     */
    void setActivityType(_2.plan.ucc.ean.ActivityTypeCodeListType.Enum activityType);

    /**
     * Sets (as xml) the "activityType" element
     */
    void xsetActivityType(_2.plan.ucc.ean.ActivityTypeCodeListType activityType);

    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    public static final class Factory {

        public static _2.plan.ucc.ean.ProductActivityDataItemType newInstance() {
            return (_2.plan.ucc.ean.ProductActivityDataItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static _2.plan.ucc.ean.ProductActivityDataItemType newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (_2.plan.ucc.ean.ProductActivityDataItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static _2.plan.ucc.ean.ProductActivityDataItemType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ProductActivityDataItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static _2.plan.ucc.ean.ProductActivityDataItemType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ProductActivityDataItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static _2.plan.ucc.ean.ProductActivityDataItemType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ProductActivityDataItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static _2.plan.ucc.ean.ProductActivityDataItemType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ProductActivityDataItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static _2.plan.ucc.ean.ProductActivityDataItemType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ProductActivityDataItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static _2.plan.ucc.ean.ProductActivityDataItemType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ProductActivityDataItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static _2.plan.ucc.ean.ProductActivityDataItemType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ProductActivityDataItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static _2.plan.ucc.ean.ProductActivityDataItemType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ProductActivityDataItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static _2.plan.ucc.ean.ProductActivityDataItemType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ProductActivityDataItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static _2.plan.ucc.ean.ProductActivityDataItemType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.plan.ucc.ean.ProductActivityDataItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static _2.plan.ucc.ean.ProductActivityDataItemType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ProductActivityDataItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static _2.plan.ucc.ean.ProductActivityDataItemType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ProductActivityDataItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static _2.plan.ucc.ean.ProductActivityDataItemType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ProductActivityDataItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static _2.plan.ucc.ean.ProductActivityDataItemType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.plan.ucc.ean.ProductActivityDataItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.plan.ucc.ean.ProductActivityDataItemType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.plan.ucc.ean.ProductActivityDataItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.plan.ucc.ean.ProductActivityDataItemType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.plan.ucc.ean.ProductActivityDataItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
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
