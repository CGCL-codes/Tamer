package org.apache.xmlbeans.impl.xb.xsdschema.impl;

/**
 * An XML group(@http://www.w3.org/2001/XMLSchema).
 *
 * This is a complex type.
 */
public class GroupImpl extends org.apache.xmlbeans.impl.xb.xsdschema.impl.AnnotatedImpl implements org.apache.xmlbeans.impl.xb.xsdschema.Group {

    public GroupImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName ELEMENT$0 = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "element");

    private static final javax.xml.namespace.QName GROUP$2 = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "group");

    private static final javax.xml.namespace.QName ALL$4 = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "all");

    private static final javax.xml.namespace.QName CHOICE$6 = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "choice");

    private static final javax.xml.namespace.QName SEQUENCE$8 = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "sequence");

    private static final javax.xml.namespace.QName ANY$10 = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "any");

    private static final javax.xml.namespace.QName NAME$12 = new javax.xml.namespace.QName("", "name");

    private static final javax.xml.namespace.QName REF$14 = new javax.xml.namespace.QName("", "ref");

    private static final javax.xml.namespace.QName MINOCCURS$16 = new javax.xml.namespace.QName("", "minOccurs");

    private static final javax.xml.namespace.QName MAXOCCURS$18 = new javax.xml.namespace.QName("", "maxOccurs");

    /**
     * Gets array of all "element" elements
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.LocalElement[] getElementArray() {
        synchronized (monitor()) {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ELEMENT$0, targetList);
            org.apache.xmlbeans.impl.xb.xsdschema.LocalElement[] result = new org.apache.xmlbeans.impl.xb.xsdschema.LocalElement[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }

    /**
     * Gets ith "element" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.LocalElement getElementArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.LocalElement target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.LocalElement) get_store().find_element_user(ELEMENT$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "element" element
     */
    public int sizeOfElementArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(ELEMENT$0);
        }
    }

    /**
     * Sets array of all "element" element
     */
    public void setElementArray(org.apache.xmlbeans.impl.xb.xsdschema.LocalElement[] elementArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(elementArray, ELEMENT$0);
        }
    }

    /**
     * Sets ith "element" element
     */
    public void setElementArray(int i, org.apache.xmlbeans.impl.xb.xsdschema.LocalElement element) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.LocalElement target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.LocalElement) get_store().find_element_user(ELEMENT$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(element);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "element" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.LocalElement insertNewElement(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.LocalElement target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.LocalElement) get_store().insert_element_user(ELEMENT$0, i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "element" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.LocalElement addNewElement() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.LocalElement target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.LocalElement) get_store().add_element_user(ELEMENT$0);
            return target;
        }
    }

    /**
     * Removes the ith "element" element
     */
    public void removeElement(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ELEMENT$0, i);
        }
    }

    /**
     * Gets array of all "group" elements
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.GroupRef[] getGroupArray() {
        synchronized (monitor()) {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(GROUP$2, targetList);
            org.apache.xmlbeans.impl.xb.xsdschema.GroupRef[] result = new org.apache.xmlbeans.impl.xb.xsdschema.GroupRef[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }

    /**
     * Gets ith "group" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.GroupRef getGroupArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.GroupRef target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.GroupRef) get_store().find_element_user(GROUP$2, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "group" element
     */
    public int sizeOfGroupArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(GROUP$2);
        }
    }

    /**
     * Sets array of all "group" element
     */
    public void setGroupArray(org.apache.xmlbeans.impl.xb.xsdschema.GroupRef[] groupArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(groupArray, GROUP$2);
        }
    }

    /**
     * Sets ith "group" element
     */
    public void setGroupArray(int i, org.apache.xmlbeans.impl.xb.xsdschema.GroupRef group) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.GroupRef target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.GroupRef) get_store().find_element_user(GROUP$2, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(group);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "group" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.GroupRef insertNewGroup(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.GroupRef target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.GroupRef) get_store().insert_element_user(GROUP$2, i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "group" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.GroupRef addNewGroup() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.GroupRef target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.GroupRef) get_store().add_element_user(GROUP$2);
            return target;
        }
    }

    /**
     * Removes the ith "group" element
     */
    public void removeGroup(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(GROUP$2, i);
        }
    }

    /**
     * Gets array of all "all" elements
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.All[] getAllArray() {
        synchronized (monitor()) {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ALL$4, targetList);
            org.apache.xmlbeans.impl.xb.xsdschema.All[] result = new org.apache.xmlbeans.impl.xb.xsdschema.All[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }

    /**
     * Gets ith "all" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.All getAllArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.All target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.All) get_store().find_element_user(ALL$4, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "all" element
     */
    public int sizeOfAllArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(ALL$4);
        }
    }

    /**
     * Sets array of all "all" element
     */
    public void setAllArray(org.apache.xmlbeans.impl.xb.xsdschema.All[] allArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(allArray, ALL$4);
        }
    }

    /**
     * Sets ith "all" element
     */
    public void setAllArray(int i, org.apache.xmlbeans.impl.xb.xsdschema.All all) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.All target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.All) get_store().find_element_user(ALL$4, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(all);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "all" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.All insertNewAll(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.All target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.All) get_store().insert_element_user(ALL$4, i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "all" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.All addNewAll() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.All target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.All) get_store().add_element_user(ALL$4);
            return target;
        }
    }

    /**
     * Removes the ith "all" element
     */
    public void removeAll(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ALL$4, i);
        }
    }

    /**
     * Gets array of all "choice" elements
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup[] getChoiceArray() {
        synchronized (monitor()) {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CHOICE$6, targetList);
            org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup[] result = new org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }

    /**
     * Gets ith "choice" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup getChoiceArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup) get_store().find_element_user(CHOICE$6, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "choice" element
     */
    public int sizeOfChoiceArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(CHOICE$6);
        }
    }

    /**
     * Sets array of all "choice" element
     */
    public void setChoiceArray(org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup[] choiceArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(choiceArray, CHOICE$6);
        }
    }

    /**
     * Sets ith "choice" element
     */
    public void setChoiceArray(int i, org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup choice) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup) get_store().find_element_user(CHOICE$6, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(choice);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "choice" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup insertNewChoice(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup) get_store().insert_element_user(CHOICE$6, i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "choice" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup addNewChoice() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup) get_store().add_element_user(CHOICE$6);
            return target;
        }
    }

    /**
     * Removes the ith "choice" element
     */
    public void removeChoice(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CHOICE$6, i);
        }
    }

    /**
     * Gets array of all "sequence" elements
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup[] getSequenceArray() {
        synchronized (monitor()) {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(SEQUENCE$8, targetList);
            org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup[] result = new org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }

    /**
     * Gets ith "sequence" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup getSequenceArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup) get_store().find_element_user(SEQUENCE$8, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "sequence" element
     */
    public int sizeOfSequenceArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(SEQUENCE$8);
        }
    }

    /**
     * Sets array of all "sequence" element
     */
    public void setSequenceArray(org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup[] sequenceArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(sequenceArray, SEQUENCE$8);
        }
    }

    /**
     * Sets ith "sequence" element
     */
    public void setSequenceArray(int i, org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup sequence) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup) get_store().find_element_user(SEQUENCE$8, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(sequence);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sequence" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup insertNewSequence(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup) get_store().insert_element_user(SEQUENCE$8, i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sequence" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup addNewSequence() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup) get_store().add_element_user(SEQUENCE$8);
            return target;
        }
    }

    /**
     * Removes the ith "sequence" element
     */
    public void removeSequence(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SEQUENCE$8, i);
        }
    }

    /**
     * Gets array of all "any" elements
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any[] getAnyArray() {
        synchronized (monitor()) {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ANY$10, targetList);
            org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any[] result = new org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }

    /**
     * Gets ith "any" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any getAnyArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any) get_store().find_element_user(ANY$10, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "any" element
     */
    public int sizeOfAnyArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(ANY$10);
        }
    }

    /**
     * Sets array of all "any" element
     */
    public void setAnyArray(org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any[] anyArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(anyArray, ANY$10);
        }
    }

    /**
     * Sets ith "any" element
     */
    public void setAnyArray(int i, org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any any) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any) get_store().find_element_user(ANY$10, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(any);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "any" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any insertNewAny(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any) get_store().insert_element_user(ANY$10, i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "any" element
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any addNewAny() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any) get_store().add_element_user(ANY$10);
            return target;
        }
    }

    /**
     * Removes the ith "any" element
     */
    public void removeAny(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ANY$10, i);
        }
    }

    /**
     * Gets the "name" attribute
     */
    public java.lang.String getName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(NAME$12);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "name" attribute
     */
    public org.apache.xmlbeans.XmlNCName xgetName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlNCName target = null;
            target = (org.apache.xmlbeans.XmlNCName) get_store().find_attribute_user(NAME$12);
            return target;
        }
    }

    /**
     * True if has "name" attribute
     */
    public boolean isSetName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(NAME$12) != null;
        }
    }

    /**
     * Sets the "name" attribute
     */
    public void setName(java.lang.String name) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(NAME$12);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(NAME$12);
            }
            target.setStringValue(name);
        }
    }

    /**
     * Sets (as xml) the "name" attribute
     */
    public void xsetName(org.apache.xmlbeans.XmlNCName name) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlNCName target = null;
            target = (org.apache.xmlbeans.XmlNCName) get_store().find_attribute_user(NAME$12);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlNCName) get_store().add_attribute_user(NAME$12);
            }
            target.set(name);
        }
    }

    /**
     * Unsets the "name" attribute
     */
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAME$12);
        }
    }

    /**
     * Gets the "ref" attribute
     */
    public javax.xml.namespace.QName getRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(REF$14);
            if (target == null) {
                return null;
            }
            return target.getQNameValue();
        }
    }

    /**
     * Gets (as xml) the "ref" attribute
     */
    public org.apache.xmlbeans.XmlQName xgetRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlQName target = null;
            target = (org.apache.xmlbeans.XmlQName) get_store().find_attribute_user(REF$14);
            return target;
        }
    }

    /**
     * True if has "ref" attribute
     */
    public boolean isSetRef() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(REF$14) != null;
        }
    }

    /**
     * Sets the "ref" attribute
     */
    public void setRef(javax.xml.namespace.QName ref) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(REF$14);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(REF$14);
            }
            target.setQNameValue(ref);
        }
    }

    /**
     * Sets (as xml) the "ref" attribute
     */
    public void xsetRef(org.apache.xmlbeans.XmlQName ref) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlQName target = null;
            target = (org.apache.xmlbeans.XmlQName) get_store().find_attribute_user(REF$14);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlQName) get_store().add_attribute_user(REF$14);
            }
            target.set(ref);
        }
    }

    /**
     * Unsets the "ref" attribute
     */
    public void unsetRef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(REF$14);
        }
    }

    /**
     * Gets the "minOccurs" attribute
     */
    public java.math.BigInteger getMinOccurs() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MINOCCURS$16);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_default_attribute_value(MINOCCURS$16);
            }
            if (target == null) {
                return null;
            }
            return target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "minOccurs" attribute
     */
    public org.apache.xmlbeans.XmlNonNegativeInteger xgetMinOccurs() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlNonNegativeInteger target = null;
            target = (org.apache.xmlbeans.XmlNonNegativeInteger) get_store().find_attribute_user(MINOCCURS$16);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlNonNegativeInteger) get_default_attribute_value(MINOCCURS$16);
            }
            return target;
        }
    }

    /**
     * True if has "minOccurs" attribute
     */
    public boolean isSetMinOccurs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(MINOCCURS$16) != null;
        }
    }

    /**
     * Sets the "minOccurs" attribute
     */
    public void setMinOccurs(java.math.BigInteger minOccurs) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MINOCCURS$16);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(MINOCCURS$16);
            }
            target.setBigIntegerValue(minOccurs);
        }
    }

    /**
     * Sets (as xml) the "minOccurs" attribute
     */
    public void xsetMinOccurs(org.apache.xmlbeans.XmlNonNegativeInteger minOccurs) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlNonNegativeInteger target = null;
            target = (org.apache.xmlbeans.XmlNonNegativeInteger) get_store().find_attribute_user(MINOCCURS$16);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlNonNegativeInteger) get_store().add_attribute_user(MINOCCURS$16);
            }
            target.set(minOccurs);
        }
    }

    /**
     * Unsets the "minOccurs" attribute
     */
    public void unsetMinOccurs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MINOCCURS$16);
        }
    }

    /**
     * Gets the "maxOccurs" attribute
     */
    public java.lang.Object getMaxOccurs() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MAXOCCURS$18);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_default_attribute_value(MAXOCCURS$18);
            }
            if (target == null) {
                return null;
            }
            return target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "maxOccurs" attribute
     */
    public org.apache.xmlbeans.impl.xb.xsdschema.AllNNI xgetMaxOccurs() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.AllNNI target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.AllNNI) get_store().find_attribute_user(MAXOCCURS$18);
            if (target == null) {
                target = (org.apache.xmlbeans.impl.xb.xsdschema.AllNNI) get_default_attribute_value(MAXOCCURS$18);
            }
            return target;
        }
    }

    /**
     * True if has "maxOccurs" attribute
     */
    public boolean isSetMaxOccurs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(MAXOCCURS$18) != null;
        }
    }

    /**
     * Sets the "maxOccurs" attribute
     */
    public void setMaxOccurs(java.lang.Object maxOccurs) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MAXOCCURS$18);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(MAXOCCURS$18);
            }
            target.setObjectValue(maxOccurs);
        }
    }

    /**
     * Sets (as xml) the "maxOccurs" attribute
     */
    public void xsetMaxOccurs(org.apache.xmlbeans.impl.xb.xsdschema.AllNNI maxOccurs) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.impl.xb.xsdschema.AllNNI target = null;
            target = (org.apache.xmlbeans.impl.xb.xsdschema.AllNNI) get_store().find_attribute_user(MAXOCCURS$18);
            if (target == null) {
                target = (org.apache.xmlbeans.impl.xb.xsdschema.AllNNI) get_store().add_attribute_user(MAXOCCURS$18);
            }
            target.set(maxOccurs);
        }
    }

    /**
     * Unsets the "maxOccurs" attribute
     */
    public void unsetMaxOccurs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MAXOCCURS$18);
        }
    }
}
