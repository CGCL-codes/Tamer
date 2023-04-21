package com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.impl;

/**
 * A document containing one connector(@http://xml.goodcodeisbeautiful.com/archtea/config/tomcatserver/1.0) element.
 *
 * This is a complex type.
 */
public class ConnectorDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ConnectorDocument {

    private static final long serialVersionUID = 1L;

    public ConnectorDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName CONNECTOR$0 = new javax.xml.namespace.QName("http://xml.goodcodeisbeautiful.com/archtea/config/tomcatserver/1.0", "connector");

    /**
     * Gets the "connector" element
     */
    public com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ConnectorDocument.Connector getConnector() {
        synchronized (monitor()) {
            check_orphaned();
            com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ConnectorDocument.Connector target = null;
            target = (com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ConnectorDocument.Connector) get_store().find_element_user(CONNECTOR$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * Sets the "connector" element
     */
    public void setConnector(com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ConnectorDocument.Connector connector) {
        synchronized (monitor()) {
            check_orphaned();
            com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ConnectorDocument.Connector target = null;
            target = (com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ConnectorDocument.Connector) get_store().find_element_user(CONNECTOR$0, 0);
            if (target == null) {
                target = (com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ConnectorDocument.Connector) get_store().add_element_user(CONNECTOR$0);
            }
            target.set(connector);
        }
    }

    /**
     * Appends and returns a new empty "connector" element
     */
    public com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ConnectorDocument.Connector addNewConnector() {
        synchronized (monitor()) {
            check_orphaned();
            com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ConnectorDocument.Connector target = null;
            target = (com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ConnectorDocument.Connector) get_store().add_element_user(CONNECTOR$0);
            return target;
        }
    }

    /**
     * An XML connector(@http://xml.goodcodeisbeautiful.com/archtea/config/tomcatserver/1.0).
     *
     * This is a complex type.
     */
    public static class ConnectorImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ConnectorDocument.Connector {

        private static final long serialVersionUID = 1L;

        public ConnectorImpl(org.apache.xmlbeans.SchemaType sType) {
            super(sType);
        }

        private static final javax.xml.namespace.QName ADDRESS$0 = new javax.xml.namespace.QName("", "address");

        private static final javax.xml.namespace.QName PORT$2 = new javax.xml.namespace.QName("", "port");

        /**
         * Gets the "address" attribute
         */
        public java.lang.String getAddress() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(ADDRESS$0);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        /**
         * Gets (as xml) the "address" attribute
         */
        public org.apache.xmlbeans.XmlString xgetAddress() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(ADDRESS$0);
                return target;
            }
        }

        /**
         * Sets the "address" attribute
         */
        public void setAddress(java.lang.String address) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(ADDRESS$0);
                if (target == null) {
                    target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(ADDRESS$0);
                }
                target.setStringValue(address);
            }
        }

        /**
         * Sets (as xml) the "address" attribute
         */
        public void xsetAddress(org.apache.xmlbeans.XmlString address) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(ADDRESS$0);
                if (target == null) {
                    target = (org.apache.xmlbeans.XmlString) get_store().add_attribute_user(ADDRESS$0);
                }
                target.set(address);
            }
        }

        /**
         * Gets the "port" attribute
         */
        public java.math.BigDecimal getPort() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(PORT$2);
                if (target == null) {
                    return null;
                }
                return target.getBigDecimalValue();
            }
        }

        /**
         * Gets (as xml) the "port" attribute
         */
        public org.apache.xmlbeans.XmlDecimal xgetPort() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlDecimal target = null;
                target = (org.apache.xmlbeans.XmlDecimal) get_store().find_attribute_user(PORT$2);
                return target;
            }
        }

        /**
         * Sets the "port" attribute
         */
        public void setPort(java.math.BigDecimal port) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(PORT$2);
                if (target == null) {
                    target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(PORT$2);
                }
                target.setBigDecimalValue(port);
            }
        }

        /**
         * Sets (as xml) the "port" attribute
         */
        public void xsetPort(org.apache.xmlbeans.XmlDecimal port) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlDecimal target = null;
                target = (org.apache.xmlbeans.XmlDecimal) get_store().find_attribute_user(PORT$2);
                if (target == null) {
                    target = (org.apache.xmlbeans.XmlDecimal) get_store().add_attribute_user(PORT$2);
                }
                target.set(port);
            }
        }
    }
}
