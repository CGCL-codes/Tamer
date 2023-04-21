package _2.ucc.ean;

/**
 * An XML DescriptionType(@urn:ean.ucc:2).
 *
 * This is a complex type.
 */
public interface DescriptionType extends org.apache.xmlbeans.XmlObject {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(DescriptionType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB04E84285D4137534ABAE99FF2BFA955").resolveHandle("descriptiontype01d5type");

    /**
     * Gets the "language" element
     */
    _2.ucc.ean.ISO639CodeType getLanguage();

    /**
     * Sets the "language" element
     */
    void setLanguage(_2.ucc.ean.ISO639CodeType language);

    /**
     * Appends and returns a new empty "language" element
     */
    _2.ucc.ean.ISO639CodeType addNewLanguage();

    /**
     * Gets the "text" element
     */
    java.lang.String getText();

    /**
     * Gets (as xml) the "text" element
     */
    _2.ucc.ean.DescriptionType.Text xgetText();

    /**
     * Sets the "text" element
     */
    void setText(java.lang.String text);

    /**
     * Sets (as xml) the "text" element
     */
    void xsetText(_2.ucc.ean.DescriptionType.Text text);

    /**
     * An XML text(@).
     *
     * This is an atomic type that is a restriction of _2.ucc.ean.DescriptionType$Text.
     */
    public interface Text extends org.apache.xmlbeans.XmlString {

        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Text.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB04E84285D4137534ABAE99FF2BFA955").resolveHandle("texta98eelemtype");

        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        public static final class Factory {

            public static _2.ucc.ean.DescriptionType.Text newValue(java.lang.Object obj) {
                return (_2.ucc.ean.DescriptionType.Text) type.newValue(obj);
            }

            public static _2.ucc.ean.DescriptionType.Text newInstance() {
                return (_2.ucc.ean.DescriptionType.Text) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
            }

            public static _2.ucc.ean.DescriptionType.Text newInstance(org.apache.xmlbeans.XmlOptions options) {
                return (_2.ucc.ean.DescriptionType.Text) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
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

        public static _2.ucc.ean.DescriptionType newInstance() {
            return (_2.ucc.ean.DescriptionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static _2.ucc.ean.DescriptionType newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (_2.ucc.ean.DescriptionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static _2.ucc.ean.DescriptionType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.DescriptionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static _2.ucc.ean.DescriptionType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.DescriptionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static _2.ucc.ean.DescriptionType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.DescriptionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static _2.ucc.ean.DescriptionType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.DescriptionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static _2.ucc.ean.DescriptionType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.DescriptionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static _2.ucc.ean.DescriptionType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.DescriptionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static _2.ucc.ean.DescriptionType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.DescriptionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static _2.ucc.ean.DescriptionType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.DescriptionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static _2.ucc.ean.DescriptionType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.DescriptionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static _2.ucc.ean.DescriptionType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (_2.ucc.ean.DescriptionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static _2.ucc.ean.DescriptionType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.DescriptionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static _2.ucc.ean.DescriptionType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.DescriptionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static _2.ucc.ean.DescriptionType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.DescriptionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static _2.ucc.ean.DescriptionType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (_2.ucc.ean.DescriptionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.ucc.ean.DescriptionType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.ucc.ean.DescriptionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static _2.ucc.ean.DescriptionType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (_2.ucc.ean.DescriptionType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
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
