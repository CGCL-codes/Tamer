package com.goodcodeisbeautiful.xml.archtea.config.web.x10;

/**
 * A document containing one archtea-web-config(@http://xml.goodcodeisbeautiful.com/archtea/config/web/1.0) element.
 *
 * This is a complex type.
 */
public interface ArchteaWebConfigDocument extends org.apache.xmlbeans.XmlObject {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ArchteaWebConfigDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s3608F739AD31F37C3321EC544FC4C375").resolveHandle("archteawebconfig5e0fdoctype");

    /**
     * Gets the "archtea-web-config" element
     */
    com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument.ArchteaWebConfig getArchteaWebConfig();

    /**
     * Sets the "archtea-web-config" element
     */
    void setArchteaWebConfig(com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument.ArchteaWebConfig archteaWebConfig);

    /**
     * Appends and returns a new empty "archtea-web-config" element
     */
    com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument.ArchteaWebConfig addNewArchteaWebConfig();

    /**
     * An XML archtea-web-config(@http://xml.goodcodeisbeautiful.com/archtea/config/web/1.0).
     *
     * This is a complex type.
     */
    public interface ArchteaWebConfig extends org.apache.xmlbeans.XmlObject {

        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ArchteaWebConfig.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s3608F739AD31F37C3321EC544FC4C375").resolveHandle("archteawebconfigc2a5elemtype");

        /**
         * Gets the "path-maps" element
         */
        com.goodcodeisbeautiful.xml.archtea.config.web.x10.PathMapsDocument.PathMaps getPathMaps();

        /**
         * Sets the "path-maps" element
         */
        void setPathMaps(com.goodcodeisbeautiful.xml.archtea.config.web.x10.PathMapsDocument.PathMaps pathMaps);

        /**
         * Appends and returns a new empty "path-maps" element
         */
        com.goodcodeisbeautiful.xml.archtea.config.web.x10.PathMapsDocument.PathMaps addNewPathMaps();

        /**
         * Gets the "version" attribute
         */
        java.math.BigDecimal getVersion();

        /**
         * Gets (as xml) the "version" attribute
         */
        org.apache.xmlbeans.XmlDecimal xgetVersion();

        /**
         * Sets the "version" attribute
         */
        void setVersion(java.math.BigDecimal version);

        /**
         * Sets (as xml) the "version" attribute
         */
        void xsetVersion(org.apache.xmlbeans.XmlDecimal version);

        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        public static final class Factory {

            public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument.ArchteaWebConfig newInstance() {
                return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument.ArchteaWebConfig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
            }

            public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument.ArchteaWebConfig newInstance(org.apache.xmlbeans.XmlOptions options) {
                return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument.ArchteaWebConfig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
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

        public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument newInstance() {
            return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (com.goodcodeisbeautiful.xml.archtea.config.web.x10.ArchteaWebConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
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
