package com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.impl;

/**
 * A document containing one tomcat-server(@http://xml.goodcodeisbeautiful.com/archtea/config/tomcatserver/1.0) element.
 *
 * This is a complex type.
 */
public class TomcatServerDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.TomcatServerDocument {

    private static final long serialVersionUID = 1L;

    public TomcatServerDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName TOMCATSERVER$0 = new javax.xml.namespace.QName("http://xml.goodcodeisbeautiful.com/archtea/config/tomcatserver/1.0", "tomcat-server");

    /**
     * Gets the "tomcat-server" element
     */
    public com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.TomcatServerDocument.TomcatServer getTomcatServer() {
        synchronized (monitor()) {
            check_orphaned();
            com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.TomcatServerDocument.TomcatServer target = null;
            target = (com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.TomcatServerDocument.TomcatServer) get_store().find_element_user(TOMCATSERVER$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * Sets the "tomcat-server" element
     */
    public void setTomcatServer(com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.TomcatServerDocument.TomcatServer tomcatServer) {
        synchronized (monitor()) {
            check_orphaned();
            com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.TomcatServerDocument.TomcatServer target = null;
            target = (com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.TomcatServerDocument.TomcatServer) get_store().find_element_user(TOMCATSERVER$0, 0);
            if (target == null) {
                target = (com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.TomcatServerDocument.TomcatServer) get_store().add_element_user(TOMCATSERVER$0);
            }
            target.set(tomcatServer);
        }
    }

    /**
     * Appends and returns a new empty "tomcat-server" element
     */
    public com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.TomcatServerDocument.TomcatServer addNewTomcatServer() {
        synchronized (monitor()) {
            check_orphaned();
            com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.TomcatServerDocument.TomcatServer target = null;
            target = (com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.TomcatServerDocument.TomcatServer) get_store().add_element_user(TOMCATSERVER$0);
            return target;
        }
    }

    /**
     * An XML tomcat-server(@http://xml.goodcodeisbeautiful.com/archtea/config/tomcatserver/1.0).
     *
     * This is a complex type.
     */
    public static class TomcatServerImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.TomcatServerDocument.TomcatServer {

        private static final long serialVersionUID = 1L;

        public TomcatServerImpl(org.apache.xmlbeans.SchemaType sType) {
            super(sType);
        }

        private static final javax.xml.namespace.QName SERVICE$0 = new javax.xml.namespace.QName("http://xml.goodcodeisbeautiful.com/archtea/config/tomcatserver/1.0", "service");

        private static final javax.xml.namespace.QName VERSION$2 = new javax.xml.namespace.QName("", "version");

        /**
         * Gets the "service" element
         */
        public com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ServiceDocument.Service getService() {
            synchronized (monitor()) {
                check_orphaned();
                com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ServiceDocument.Service target = null;
                target = (com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ServiceDocument.Service) get_store().find_element_user(SERVICE$0, 0);
                if (target == null) {
                    return null;
                }
                return target;
            }
        }

        /**
         * Sets the "service" element
         */
        public void setService(com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ServiceDocument.Service service) {
            synchronized (monitor()) {
                check_orphaned();
                com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ServiceDocument.Service target = null;
                target = (com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ServiceDocument.Service) get_store().find_element_user(SERVICE$0, 0);
                if (target == null) {
                    target = (com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ServiceDocument.Service) get_store().add_element_user(SERVICE$0);
                }
                target.set(service);
            }
        }

        /**
         * Appends and returns a new empty "service" element
         */
        public com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ServiceDocument.Service addNewService() {
            synchronized (monitor()) {
                check_orphaned();
                com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ServiceDocument.Service target = null;
                target = (com.goodcodeisbeautiful.xml.archtea.config.tomcatserver.x10.ServiceDocument.Service) get_store().add_element_user(SERVICE$0);
                return target;
            }
        }

        /**
         * Gets the "version" attribute
         */
        public java.lang.String getVersion() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(VERSION$2);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        /**
         * Gets (as xml) the "version" attribute
         */
        public org.apache.xmlbeans.XmlString xgetVersion() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(VERSION$2);
                return target;
            }
        }

        /**
         * Sets the "version" attribute
         */
        public void setVersion(java.lang.String version) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(VERSION$2);
                if (target == null) {
                    target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(VERSION$2);
                }
                target.setStringValue(version);
            }
        }

        /**
         * Sets (as xml) the "version" attribute
         */
        public void xsetVersion(org.apache.xmlbeans.XmlString version) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(VERSION$2);
                if (target == null) {
                    target = (org.apache.xmlbeans.XmlString) get_store().add_attribute_user(VERSION$2);
                }
                target.set(version);
            }
        }
    }
}
