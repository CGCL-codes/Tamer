package org.apache.xmlbeans.impl.xb.xsdschema.impl;

/**
 * A document containing one documentation(@http://www.w3.org/2001/XMLSchema) element.
 *
 * This is a complex type.
 */
public class DocumentationDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument {

    public DocumentationDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName DOCUMENTATION$0 = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "documentation");

    /**
     * Gets the "documentation" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation getDocumentation() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation) get_store().find_element_user(DOCUMENTATION$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * Sets the "documentation" element
     */
    public void setDocumentation(org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation documentation) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation) get_store().find_element_user(DOCUMENTATION$0, 0);
            if (target == null) {
                target = (org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation) get_store().add_element_user(DOCUMENTATION$0);
            }
            target.set(documentation);
        }
    }

    /**
     * Appends and returns a new empty "documentation" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation addNewDocumentation() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation) get_store().add_element_user(DOCUMENTATION$0);
            return target;
        }
    }

    /**
     * An XML documentation(@http://www.w3.org/2001/XMLSchema).
     *
     * This is a complex type.
     */
    public static class DocumentationImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation {

        public DocumentationImpl(org.apache.xmlbeans.SchemaType sType) {
            super(sType);
        }

        private static final javax.xml.namespace.QName SOURCE$0 = new javax.xml.namespace.QName("", "source");

        private static final javax.xml.namespace.QName LANG$2 = new javax.xml.namespace.QName("http://www.w3.org/XML/1998/namespace", "lang");

        /**
         * Gets the "source" attribute
         */
        public java.lang.String getSource() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(SOURCE$0);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        /**
         * Gets (as xml) the "source" attribute
         */
        public org.apache.xmlbeans.XmlAnyURI xgetSource() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlAnyURI target = null;
                target = (org.apache.xmlbeans.XmlAnyURI) get_store().find_attribute_user(SOURCE$0);
                return target;
            }
        }

        /**
         * True if has "source" attribute
         */
        public boolean isSetSource() {
            synchronized (monitor()) {
                check_orphaned();
                return get_store().find_attribute_user(SOURCE$0) != null;
            }
        }

        /**
         * Sets the "source" attribute
         */
        public void setSource(java.lang.String source) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(SOURCE$0);
                if (target == null) {
                    target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(SOURCE$0);
                }
                target.setStringValue(source);
            }
        }

        /**
         * Sets (as xml) the "source" attribute
         */
        public void xsetSource(org.apache.xmlbeans.XmlAnyURI source) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlAnyURI target = null;
                target = (org.apache.xmlbeans.XmlAnyURI) get_store().find_attribute_user(SOURCE$0);
                if (target == null) {
                    target = (org.apache.xmlbeans.XmlAnyURI) get_store().add_attribute_user(SOURCE$0);
                }
                target.set(source);
            }
        }

        /**
         * Unsets the "source" attribute
         */
        public void unsetSource() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(SOURCE$0);
            }
        }

        /**
         * Gets the "lang" attribute
         */
        public java.lang.String getLang() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(LANG$2);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        /**
         * Gets (as xml) the "lang" attribute
         */
        public org.apache.xmlbeans.XmlLanguage xgetLang() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlLanguage target = null;
                target = (org.apache.xmlbeans.XmlLanguage) get_store().find_attribute_user(LANG$2);
                return target;
            }
        }

        /**
         * True if has "lang" attribute
         */
        public boolean isSetLang() {
            synchronized (monitor()) {
                check_orphaned();
                return get_store().find_attribute_user(LANG$2) != null;
            }
        }

        /**
         * Sets the "lang" attribute
         */
        public void setLang(java.lang.String lang) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(LANG$2);
                if (target == null) {
                    target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(LANG$2);
                }
                target.setStringValue(lang);
            }
        }

        /**
         * Sets (as xml) the "lang" attribute
         */
        public void xsetLang(org.apache.xmlbeans.XmlLanguage lang) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlLanguage target = null;
                target = (org.apache.xmlbeans.XmlLanguage) get_store().find_attribute_user(LANG$2);
                if (target == null) {
                    target = (org.apache.xmlbeans.XmlLanguage) get_store().add_attribute_user(LANG$2);
                }
                target.set(lang);
            }
        }

        /**
         * Unsets the "lang" attribute
         */
        public void unsetLang() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(LANG$2);
            }
        }
    }
}
