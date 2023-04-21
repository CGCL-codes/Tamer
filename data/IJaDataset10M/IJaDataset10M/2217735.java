package com.goodcodeisbeautiful.xml.archtea.config.search.x10.impl;

/**
 * A document containing one class(@http://xml.goodcodeisbeautiful.com/archtea/config/search/1.0) element.
 *
 * This is a complex type.
 */
public class ClassDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements com.goodcodeisbeautiful.xml.archtea.config.search.x10.ClassDocument {

    private static final long serialVersionUID = 1L;

    public ClassDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName CLASS1$0 = new javax.xml.namespace.QName("http://xml.goodcodeisbeautiful.com/archtea/config/search/1.0", "class");

    /**
     * Gets the "class" element
     */
    public com.goodcodeisbeautiful.xml.archtea.config.search.x10.ClassDocument.Class getClass1() {
        synchronized (monitor()) {
            check_orphaned();
            com.goodcodeisbeautiful.xml.archtea.config.search.x10.ClassDocument.Class target = null;
            target = (com.goodcodeisbeautiful.xml.archtea.config.search.x10.ClassDocument.Class) get_store().find_element_user(CLASS1$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * Sets the "class" element
     */
    public void setClass1(com.goodcodeisbeautiful.xml.archtea.config.search.x10.ClassDocument.Class class1) {
        synchronized (monitor()) {
            check_orphaned();
            com.goodcodeisbeautiful.xml.archtea.config.search.x10.ClassDocument.Class target = null;
            target = (com.goodcodeisbeautiful.xml.archtea.config.search.x10.ClassDocument.Class) get_store().find_element_user(CLASS1$0, 0);
            if (target == null) {
                target = (com.goodcodeisbeautiful.xml.archtea.config.search.x10.ClassDocument.Class) get_store().add_element_user(CLASS1$0);
            }
            target.set(class1);
        }
    }

    /**
     * Appends and returns a new empty "class" element
     */
    public com.goodcodeisbeautiful.xml.archtea.config.search.x10.ClassDocument.Class addNewClass1() {
        synchronized (monitor()) {
            check_orphaned();
            com.goodcodeisbeautiful.xml.archtea.config.search.x10.ClassDocument.Class target = null;
            target = (com.goodcodeisbeautiful.xml.archtea.config.search.x10.ClassDocument.Class) get_store().add_element_user(CLASS1$0);
            return target;
        }
    }

    /**
     * An XML class(@http://xml.goodcodeisbeautiful.com/archtea/config/search/1.0).
     *
     * This is a complex type.
     */
    public static class ClassImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements com.goodcodeisbeautiful.xml.archtea.config.search.x10.ClassDocument.Class {

        private static final long serialVersionUID = 1L;

        public ClassImpl(org.apache.xmlbeans.SchemaType sType) {
            super(sType);
        }

        private static final javax.xml.namespace.QName NAME$0 = new javax.xml.namespace.QName("", "name");

        /**
         * Gets the "name" attribute
         */
        public java.lang.String getName() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(NAME$0);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        /**
         * Gets (as xml) the "name" attribute
         */
        public org.apache.xmlbeans.XmlString xgetName() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(NAME$0);
                return target;
            }
        }

        /**
         * Sets the "name" attribute
         */
        public void setName(java.lang.String name) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(NAME$0);
                if (target == null) {
                    target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(NAME$0);
                }
                target.setStringValue(name);
            }
        }

        /**
         * Sets (as xml) the "name" attribute
         */
        public void xsetName(org.apache.xmlbeans.XmlString name) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString) get_store().find_attribute_user(NAME$0);
                if (target == null) {
                    target = (org.apache.xmlbeans.XmlString) get_store().add_attribute_user(NAME$0);
                }
                target.set(name);
            }
        }
    }
}
