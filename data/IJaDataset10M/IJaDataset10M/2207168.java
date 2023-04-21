package org.systemsbiology.apml;

/**
 * An XML AlignmentType(@http://www.systemsbiology.org/apml).
 *
 * This is a complex type.
 */
public interface AlignmentType extends org.apache.xmlbeans.XmlObject {

    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(AlignmentType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s5330CD344C212E37C5979FF11445E252").resolveHandle("alignmenttype09d1type");

    /**
     * Gets the "feature_source_list" element
     */
    org.systemsbiology.apml.AlignmentType.FeatureSourceList getFeatureSourceList();

    /**
     * Sets the "feature_source_list" element
     */
    void setFeatureSourceList(org.systemsbiology.apml.AlignmentType.FeatureSourceList featureSourceList);

    /**
     * Appends and returns a new empty "feature_source_list" element
     */
    org.systemsbiology.apml.AlignmentType.FeatureSourceList addNewFeatureSourceList();

    /**
     * Gets the "aligned_features" element
     */
    org.systemsbiology.apml.AlignedFeaturesType getAlignedFeatures();

    /**
     * Sets the "aligned_features" element
     */
    void setAlignedFeatures(org.systemsbiology.apml.AlignedFeaturesType alignedFeatures);

    /**
     * Appends and returns a new empty "aligned_features" element
     */
    org.systemsbiology.apml.AlignedFeaturesType addNewAlignedFeatures();

    /**
     * An XML feature_source_list(@http://www.systemsbiology.org/apml).
     *
     * This is a complex type.
     */
    public interface FeatureSourceList extends org.apache.xmlbeans.XmlObject {

        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(FeatureSourceList.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s5330CD344C212E37C5979FF11445E252").resolveHandle("featuresourcelistaeccelemtype");

        /**
         * Gets array of all "source" elements
         */
        org.systemsbiology.apml.AlignmentType.FeatureSourceList.Source[] getSourceArray();

        /**
         * Gets ith "source" element
         */
        org.systemsbiology.apml.AlignmentType.FeatureSourceList.Source getSourceArray(int i);

        /**
         * Returns number of "source" element
         */
        int sizeOfSourceArray();

        /**
         * Sets array of all "source" element
         */
        void setSourceArray(org.systemsbiology.apml.AlignmentType.FeatureSourceList.Source[] sourceArray);

        /**
         * Sets ith "source" element
         */
        void setSourceArray(int i, org.systemsbiology.apml.AlignmentType.FeatureSourceList.Source source);

        /**
         * Inserts and returns a new empty value (as xml) as the ith "source" element
         */
        org.systemsbiology.apml.AlignmentType.FeatureSourceList.Source insertNewSource(int i);

        /**
         * Appends and returns a new empty value (as xml) as the last "source" element
         */
        org.systemsbiology.apml.AlignmentType.FeatureSourceList.Source addNewSource();

        /**
         * Removes the ith "source" element
         */
        void removeSource(int i);

        /**
         * Gets the "count" attribute
         */
        int getCount();

        /**
         * Gets (as xml) the "count" attribute
         */
        org.apache.xmlbeans.XmlInt xgetCount();

        /**
         * Sets the "count" attribute
         */
        void setCount(int count);

        /**
         * Sets (as xml) the "count" attribute
         */
        void xsetCount(org.apache.xmlbeans.XmlInt count);

        /**
         * An XML source(@http://www.systemsbiology.org/apml).
         *
         * This is a complex type.
         */
        public interface Source extends org.apache.xmlbeans.XmlObject {

            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType) org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Source.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s5330CD344C212E37C5979FF11445E252").resolveHandle("sourcee5f5elemtype");

            /**
             * Gets the "id" attribute
             */
            java.math.BigInteger getId();

            /**
             * Gets (as xml) the "id" attribute
             */
            org.apache.xmlbeans.XmlNonNegativeInteger xgetId();

            /**
             * True if has "id" attribute
             */
            boolean isSetId();

            /**
             * Sets the "id" attribute
             */
            void setId(java.math.BigInteger id);

            /**
             * Sets (as xml) the "id" attribute
             */
            void xsetId(org.apache.xmlbeans.XmlNonNegativeInteger id);

            /**
             * Unsets the "id" attribute
             */
            void unsetId();

            /**
             * Gets the "location" attribute
             */
            java.lang.String getLocation();

            /**
             * Gets (as xml) the "location" attribute
             */
            org.apache.xmlbeans.XmlAnyURI xgetLocation();

            /**
             * Sets the "location" attribute
             */
            void setLocation(java.lang.String location);

            /**
             * Sets (as xml) the "location" attribute
             */
            void xsetLocation(org.apache.xmlbeans.XmlAnyURI location);

            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            public static final class Factory {

                public static org.systemsbiology.apml.AlignmentType.FeatureSourceList.Source newInstance() {
                    return (org.systemsbiology.apml.AlignmentType.FeatureSourceList.Source) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
                }

                public static org.systemsbiology.apml.AlignmentType.FeatureSourceList.Source newInstance(org.apache.xmlbeans.XmlOptions options) {
                    return (org.systemsbiology.apml.AlignmentType.FeatureSourceList.Source) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
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

            public static org.systemsbiology.apml.AlignmentType.FeatureSourceList newInstance() {
                return (org.systemsbiology.apml.AlignmentType.FeatureSourceList) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
            }

            public static org.systemsbiology.apml.AlignmentType.FeatureSourceList newInstance(org.apache.xmlbeans.XmlOptions options) {
                return (org.systemsbiology.apml.AlignmentType.FeatureSourceList) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
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

        public static org.systemsbiology.apml.AlignmentType newInstance() {
            return (org.systemsbiology.apml.AlignmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, null);
        }

        public static org.systemsbiology.apml.AlignmentType newInstance(org.apache.xmlbeans.XmlOptions options) {
            return (org.systemsbiology.apml.AlignmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance(type, options);
        }

        /** @param xmlAsString the string value to parse */
        public static org.systemsbiology.apml.AlignmentType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
            return (org.systemsbiology.apml.AlignmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, null);
        }

        public static org.systemsbiology.apml.AlignmentType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (org.systemsbiology.apml.AlignmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xmlAsString, type, options);
        }

        /** @param file the file from which to load an xml document */
        public static org.systemsbiology.apml.AlignmentType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.systemsbiology.apml.AlignmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, null);
        }

        public static org.systemsbiology.apml.AlignmentType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.systemsbiology.apml.AlignmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(file, type, options);
        }

        public static org.systemsbiology.apml.AlignmentType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.systemsbiology.apml.AlignmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, null);
        }

        public static org.systemsbiology.apml.AlignmentType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.systemsbiology.apml.AlignmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(u, type, options);
        }

        public static org.systemsbiology.apml.AlignmentType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.systemsbiology.apml.AlignmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, null);
        }

        public static org.systemsbiology.apml.AlignmentType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.systemsbiology.apml.AlignmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(is, type, options);
        }

        public static org.systemsbiology.apml.AlignmentType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.systemsbiology.apml.AlignmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, null);
        }

        public static org.systemsbiology.apml.AlignmentType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
            return (org.systemsbiology.apml.AlignmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(r, type, options);
        }

        public static org.systemsbiology.apml.AlignmentType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
            return (org.systemsbiology.apml.AlignmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, null);
        }

        public static org.systemsbiology.apml.AlignmentType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (org.systemsbiology.apml.AlignmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(sr, type, options);
        }

        public static org.systemsbiology.apml.AlignmentType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
            return (org.systemsbiology.apml.AlignmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, null);
        }

        public static org.systemsbiology.apml.AlignmentType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
            return (org.systemsbiology.apml.AlignmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(node, type, options);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.systemsbiology.apml.AlignmentType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (org.systemsbiology.apml.AlignmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, null);
        }

        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.systemsbiology.apml.AlignmentType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
            return (org.systemsbiology.apml.AlignmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse(xis, type, options);
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
