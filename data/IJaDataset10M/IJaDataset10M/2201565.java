package com.jmex.model.collada.schema;

import com.jmex.xml.types.SchemaNCName;
import com.jmex.xml.types.SchemaType;

public class TargetableFloat3 extends com.jmex.xml.xml.Node {

    public TargetableFloat3(TargetableFloat3 node) {
        super(node);
    }

    public TargetableFloat3(org.w3c.dom.Node node) {
        super(node);
    }

    public TargetableFloat3(org.w3c.dom.Document doc) {
        super(doc);
    }

    public TargetableFloat3(com.jmex.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public float3 getValue() {
        return new float3(getDomNodeValue(domNode));
    }

    public void setValue(SchemaType value) {
        setDomNodeValue(domNode, value.toString());
    }

    public void assign(SchemaType value) {
        setValue(value);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "sid"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "sid", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "TargetableFloat3");
    }

    public static int getsidMinCount() {
        return 0;
    }

    public static int getsidMaxCount() {
        return 1;
    }

    public int getsidCount() {
        return getDomChildCount(Attribute, null, "sid");
    }

    public boolean hassid() {
        return hasDomChild(Attribute, null, "sid");
    }

    public SchemaNCName newsid() {
        return new SchemaNCName();
    }

    public SchemaNCName getsidAt(int index) throws Exception {
        return new SchemaNCName(getDomNodeValue(getDomChildAt(Attribute, null, "sid", index)));
    }

    public org.w3c.dom.Node getStartingsidCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "sid");
    }

    public org.w3c.dom.Node getAdvancedsidCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "sid", curNode);
    }

    public SchemaNCName getsidValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new SchemaNCName(getDomNodeValue(curNode));
    }

    public SchemaNCName getsid() throws Exception {
        return getsidAt(0);
    }

    public void removesidAt(int index) {
        removeDomChildAt(Attribute, null, "sid", index);
    }

    public void removesid() {
        removesidAt(0);
    }

    public org.w3c.dom.Node addsid(SchemaNCName value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "sid", value.toString());
    }

    public org.w3c.dom.Node addsid(String value) throws Exception {
        return addsid(new SchemaNCName(value));
    }

    public void insertsidAt(SchemaNCName value, int index) {
        insertDomChildAt(Attribute, null, "sid", index, value.toString());
    }

    public void insertsidAt(String value, int index) throws Exception {
        insertsidAt(new SchemaNCName(value), index);
    }

    public void replacesidAt(SchemaNCName value, int index) {
        replaceDomChildAt(Attribute, null, "sid", index, value.toString());
    }

    public void replacesidAt(String value, int index) throws Exception {
        replacesidAt(new SchemaNCName(value), index);
    }
}
