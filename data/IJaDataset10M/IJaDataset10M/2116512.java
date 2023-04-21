package org.apache.xmlbeans.samples.validation.todolist.impl;

/**
 * An XML actionType(@http://xmlbeans.apache.org/samples/validation/todolist).
 *
 * This is an atomic type that is a restriction of org.apache.xmlbeans.XmlString.
 */
public class ActionTypeImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements org.apache.xmlbeans.samples.validation.todolist.ActionType {

    public ActionTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType, false);
    }

    protected ActionTypeImpl(org.apache.xmlbeans.SchemaType sType, boolean b) {
        super(sType, b);
    }
}
