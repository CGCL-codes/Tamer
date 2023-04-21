package org.skins.dao2.config.xml;

/**
 * An XML XmlColumnElement(@http://www.example.org/dao2).
 *
 * This is a complex type.
 */
public interface XmlColumnElement extends org.apache.xmlbeans.XmlObject {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XmlColumnElement.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9099820870E04092553F60835EB9148E").resolveHandle("xmlcolumnelement7ff0type");

    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.apache.xmlbeans.XmlString xgetName();

    /**
     * True if has "name" attribute
     */
    boolean isSetName();

    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.apache.xmlbeans.XmlString name);

    /**
     * Unsets the "name" attribute
     */
    void unsetName();

    /**
     * Gets the "alias" attribute
     */
    java.lang.String getAlias();

    /**
     * Gets (as xml) the "alias" attribute
     */
    org.apache.xmlbeans.XmlString xgetAlias();

    /**
     * True if has "alias" attribute
     */
    boolean isSetAlias();

    /**
     * Sets the "alias" attribute
     */
    void setAlias(java.lang.String alias);

    /**
     * Sets (as xml) the "alias" attribute
     */
    void xsetAlias(org.apache.xmlbeans.XmlString alias);

    /**
     * Unsets the "alias" attribute
     */
    void unsetAlias();

    /**
     * Gets the "prefix" attribute
     */
    java.lang.String getPrefix();

    /**
     * Gets (as xml) the "prefix" attribute
     */
    org.apache.xmlbeans.XmlString xgetPrefix();

    /**
     * True if has "prefix" attribute
     */
    boolean isSetPrefix();

    /**
     * Sets the "prefix" attribute
     */
    void setPrefix(java.lang.String prefix);

    /**
     * Sets (as xml) the "prefix" attribute
     */
    void xsetPrefix(org.apache.xmlbeans.XmlString prefix);

    /**
     * Unsets the "prefix" attribute
     */
    void unsetPrefix();

    /**
     * Gets the "orderType" attribute
     */
    org.skins.dao2.config.xml.XmlColumnElement.OrderType.Enum getOrderType();

    /**
     * Gets (as xml) the "orderType" attribute
     */
    org.skins.dao2.config.xml.XmlColumnElement.OrderType xgetOrderType();

    /**
     * True if has "orderType" attribute
     */
    boolean isSetOrderType();

    /**
     * Sets the "orderType" attribute
     */
    void setOrderType(org.skins.dao2.config.xml.XmlColumnElement.OrderType.Enum orderType);

    /**
     * Sets (as xml) the "orderType" attribute
     */
    void xsetOrderType(org.skins.dao2.config.xml.XmlColumnElement.OrderType orderType);

    /**
     * Unsets the "orderType" attribute
     */
    void unsetOrderType();

    /**
     * An XML orderType(@).
     *
     * This is an atomic type that is a restriction of org.skins.dao2.config.xml.XmlColumnElement$OrderType.
     */
    public interface OrderType extends org.apache.xmlbeans.XmlString {

        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(OrderType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9099820870E04092553F60835EB9148E").resolveHandle("ordertypeb2e0attrtype");

        org.apache.xmlbeans.StringEnumAbstractBase enumValue();

        void set(org.apache.xmlbeans.StringEnumAbstractBase e);

        static final Enum ASC = Enum.forString("ASC");

        static final Enum DESC = Enum.forString("DESC");

        static final int INT_ASC = Enum.INT_ASC;

        static final int INT_DESC = Enum.INT_DESC;

        /**
         * Enumeration value class for org.skins.dao2.config.xml.XmlColumnElement$OrderType.
         * These enum values can be used as follows:
         * <pre>
         * enum.toString(); // returns the string value of the enum
         * enum.intValue(); // returns an int value, useful for switches
         * // e.g., case Enum.INT_ASC
         * Enum.forString(s); // returns the enum value for a string
         * Enum.forInt(i); // returns the enum value for an int
         * </pre>
         * Enumeration objects are immutable singleton objects that
         * can be compared using == object equality. They have no
         * public constructor. See the constants defined within this
         * class for all the valid values.
         */
        static final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase {

            /**
             * Returns the enum value for a string, or null if none.
             */
            public static Enum forString(java.lang.String s) {
                return (Enum) table.forString(s);
            }

            /**
             * Returns the enum value corresponding to an int, or null if none.
             */
            public static Enum forInt(int i) {
                return (Enum) table.forInt(i);
            }

            private Enum(java.lang.String s, int i) {
                super(s, i);
            }

            static final int INT_ASC = 1;

            static final int INT_DESC = 2;

            public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table = new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] { new Enum("ASC", INT_ASC), new Enum("DESC", INT_DESC) });

            private static final long serialVersionUID = 1L;

            private java.lang.Object readResolve() {
                return forInt(intValue());
            }
        }

        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        public static final class Factory {

            public static org.skins.dao2.config.xml.XmlColumnElement.OrderType newValue(java.lang.Object obj) {
                return (org.skins.dao2.config.xml.XmlColumnElement.OrderType) type.newValue(obj);
            }

            public static org.skins.dao2.config.xml.XmlColumnElement.OrderType newInstance() {
                return (org.skins.dao2.config.xml.XmlColumnElement.OrderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
            }

            public static org.skins.dao2.config.xml.XmlColumnElement.OrderType newInstance(org.apache.xmlbeans.XmlOptions options) {
                return (org.skins.dao2.config.xml.XmlColumnElement.OrderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
            }

            private Factory() {
            }
        }
    }

    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    public static final class Factory {

        public static org.skins.dao2.config.xml.XmlColumnElement newInstance() {
            return (org.skins.dao2.config.xml.XmlColumnElement) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static org.skins.dao2.config.xml.XmlColumnElement newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (org.skins.dao2.config.xml.XmlColumnElement) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static org.skins.dao2.config.xml.XmlColumnElement parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (org.skins.dao2.config.xml.XmlColumnElement) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static org.skins.dao2.config.xml.XmlColumnElement parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (org.skins.dao2.config.xml.XmlColumnElement) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static org.skins.dao2.config.xml.XmlColumnElement parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.skins.dao2.config.xml.XmlColumnElement) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static org.skins.dao2.config.xml.XmlColumnElement parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.skins.dao2.config.xml.XmlColumnElement) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static org.skins.dao2.config.xml.XmlColumnElement parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.skins.dao2.config.xml.XmlColumnElement) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static org.skins.dao2.config.xml.XmlColumnElement parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.skins.dao2.config.xml.XmlColumnElement) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static org.skins.dao2.config.xml.XmlColumnElement parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.skins.dao2.config.xml.XmlColumnElement) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static org.skins.dao2.config.xml.XmlColumnElement parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.skins.dao2.config.xml.XmlColumnElement) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static org.skins.dao2.config.xml.XmlColumnElement parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.skins.dao2.config.xml.XmlColumnElement) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static org.skins.dao2.config.xml.XmlColumnElement parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.skins.dao2.config.xml.XmlColumnElement) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static org.skins.dao2.config.xml.XmlColumnElement parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (org.skins.dao2.config.xml.XmlColumnElement) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static org.skins.dao2.config.xml.XmlColumnElement parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (org.skins.dao2.config.xml.XmlColumnElement) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static org.skins.dao2.config.xml.XmlColumnElement parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (org.skins.dao2.config.xml.XmlColumnElement) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static org.skins.dao2.config.xml.XmlColumnElement parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (org.skins.dao2.config.xml.XmlColumnElement) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.skins.dao2.config.xml.XmlColumnElement parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (org.skins.dao2.config.xml.XmlColumnElement) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.skins.dao2.config.xml.XmlColumnElement parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (org.skins.dao2.config.xml.XmlColumnElement) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
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
