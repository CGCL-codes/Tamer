package com.jmex.model.collada.schema;

import com.jmex.xml.types.SchemaNCName;
import com.jmex.xml.types.SchemaShort;

public class maskType3 extends com.jmex.xml.xml.Node {

    public maskType3(maskType3 node) {
        super(node);
    }

    public maskType3(org.w3c.dom.Node node) {
        super(node);
    }

    public maskType3(org.w3c.dom.Document doc) {
        super(doc);
    }

    public maskType3(com.jmex.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "value"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "value", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "param"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "param", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "mask");
    }

    public static int getvalue2MinCount() {
        return 0;
    }

    public static int getvalue2MaxCount() {
        return 1;
    }

    public int getvalue2Count() {
        return getDomChildCount(Attribute, null, "value");
    }

    public boolean hasvalue2() {
        return hasDomChild(Attribute, null, "value");
    }

    public SchemaShort newvalue2() {
        return new SchemaShort();
    }

    public SchemaShort getvalue2At(int index) throws Exception {
        return new SchemaShort(getDomNodeValue(getDomChildAt(Attribute, null, "value", index)));
    }

    public org.w3c.dom.Node getStartingvalue2Cursor() throws Exception {
        return getDomFirstChild(Attribute, null, "value");
    }

    public org.w3c.dom.Node getAdvancedvalue2Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "value", curNode);
    }

    public SchemaShort getvalue2ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new SchemaShort(getDomNodeValue(curNode));
    }

    public SchemaShort getvalue2() throws Exception {
        return getvalue2At(0);
    }

    public void removevalue2At(int index) {
        removeDomChildAt(Attribute, null, "value", index);
    }

    public void removevalue2() {
        removevalue2At(0);
    }

    public org.w3c.dom.Node addvalue2(SchemaShort value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "value", value.toString());
    }

    public org.w3c.dom.Node addvalue2(String value) throws Exception {
        return addvalue2(new SchemaShort(value));
    }

    public void insertvalue2At(SchemaShort value, int index) {
        insertDomChildAt(Attribute, null, "value", index, value.toString());
    }

    public void insertvalue2At(String value, int index) throws Exception {
        insertvalue2At(new SchemaShort(value), index);
    }

    public void replacevalue2At(SchemaShort value, int index) {
        replaceDomChildAt(Attribute, null, "value", index, value.toString());
    }

    public void replacevalue2At(String value, int index) throws Exception {
        replacevalue2At(new SchemaShort(value), index);
    }

    public static int getparamMinCount() {
        return 0;
    }

    public static int getparamMaxCount() {
        return 1;
    }

    public int getparamCount() {
        return getDomChildCount(Attribute, null, "param");
    }

    public boolean hasparam() {
        return hasDomChild(Attribute, null, "param");
    }

    public SchemaNCName newparam() {
        return new SchemaNCName();
    }

    public SchemaNCName getparamAt(int index) throws Exception {
        return new SchemaNCName(getDomNodeValue(getDomChildAt(Attribute, null, "param", index)));
    }

    public org.w3c.dom.Node getStartingparamCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "param");
    }

    public org.w3c.dom.Node getAdvancedparamCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "param", curNode);
    }

    public SchemaNCName getparamValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new SchemaNCName(getDomNodeValue(curNode));
    }

    public SchemaNCName getparam() throws Exception {
        return getparamAt(0);
    }

    public void removeparamAt(int index) {
        removeDomChildAt(Attribute, null, "param", index);
    }

    public void removeparam() {
        removeparamAt(0);
    }

    public org.w3c.dom.Node addparam(SchemaNCName value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "param", value.toString());
    }

    public org.w3c.dom.Node addparam(String value) throws Exception {
        return addparam(new SchemaNCName(value));
    }

    public void insertparamAt(SchemaNCName value, int index) {
        insertDomChildAt(Attribute, null, "param", index, value.toString());
    }

    public void insertparamAt(String value, int index) throws Exception {
        insertparamAt(new SchemaNCName(value), index);
    }

    public void replaceparamAt(SchemaNCName value, int index) {
        replaceDomChildAt(Attribute, null, "param", index, value.toString());
    }

    public void replaceparamAt(String value, int index) throws Exception {
        replaceparamAt(new SchemaNCName(value), index);
    }
}
