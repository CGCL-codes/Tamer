package com.jmex.model.collada.schema;

import com.jmex.xml.types.SchemaNCName;

public class paramType2 extends com.jmex.xml.xml.Node {

    public paramType2(paramType2 node) {
        super(node);
    }

    public paramType2(org.w3c.dom.Node node) {
        super(node);
    }

    public paramType2(org.w3c.dom.Document doc) {
        super(doc);
    }

    public paramType2(com.jmex.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "ref"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "ref", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "param");
    }

    public static int getrefMinCount() {
        return 1;
    }

    public static int getrefMaxCount() {
        return 1;
    }

    public int getrefCount() {
        return getDomChildCount(Attribute, null, "ref");
    }

    public boolean hasref() {
        return hasDomChild(Attribute, null, "ref");
    }

    public SchemaNCName newref() {
        return new SchemaNCName();
    }

    public SchemaNCName getrefAt(int index) throws Exception {
        return new SchemaNCName(getDomNodeValue(getDomChildAt(Attribute, null, "ref", index)));
    }

    public org.w3c.dom.Node getStartingrefCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "ref");
    }

    public org.w3c.dom.Node getAdvancedrefCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "ref", curNode);
    }

    public SchemaNCName getrefValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new SchemaNCName(getDomNodeValue(curNode));
    }

    public SchemaNCName getref() throws Exception {
        return getrefAt(0);
    }

    public void removerefAt(int index) {
        removeDomChildAt(Attribute, null, "ref", index);
    }

    public void removeref() {
        removerefAt(0);
    }

    public org.w3c.dom.Node addref(SchemaNCName value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "ref", value.toString());
    }

    public org.w3c.dom.Node addref(String value) throws Exception {
        return addref(new SchemaNCName(value));
    }

    public void insertrefAt(SchemaNCName value, int index) {
        insertDomChildAt(Attribute, null, "ref", index, value.toString());
    }

    public void insertrefAt(String value, int index) throws Exception {
        insertrefAt(new SchemaNCName(value), index);
    }

    public void replacerefAt(SchemaNCName value, int index) {
        replaceDomChildAt(Attribute, null, "ref", index, value.toString());
    }

    public void replacerefAt(String value, int index) throws Exception {
        replacerefAt(new SchemaNCName(value), index);
    }
}
