package com.jmex.model.collada.schema;

import com.jmex.model.collada.types.SchemaNCName;

public class texture2DType2 extends com.jmex.model.collada.xml.Node {

    private static final long serialVersionUID = 1L;

    public texture2DType2(texture2DType2 node) {
        super(node);
    }

    public texture2DType2(org.w3c.dom.Node node) {
        super(node);
    }

    public texture2DType2(org.w3c.dom.Document doc) {
        super(doc);
    }

    public texture2DType2(com.jmex.model.collada.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "index"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "index", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "value"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "value", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new gl_sampler2D(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "param"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "param", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
    }

    public static int getindexMinCount() {
        return 1;
    }

    public static int getindexMaxCount() {
        return 1;
    }

    public int getindexCount() {
        return getDomChildCount(Attribute, null, "index");
    }

    public boolean hasindex() {
        return hasDomChild(Attribute, null, "index");
    }

    public GL_MAX_TEXTURE_IMAGE_UNITS_index newindex() {
        return new GL_MAX_TEXTURE_IMAGE_UNITS_index();
    }

    public GL_MAX_TEXTURE_IMAGE_UNITS_index getindexAt(int index) throws Exception {
        return new GL_MAX_TEXTURE_IMAGE_UNITS_index(getDomNodeValue(dereference(getDomChildAt(Attribute, null, "index", index))));
    }

    public org.w3c.dom.Node getStartingindexCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "index");
    }

    public org.w3c.dom.Node getAdvancedindexCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "index", curNode);
    }

    public GL_MAX_TEXTURE_IMAGE_UNITS_index getindexValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.model.collada.xml.XmlException("Out of range"); else return new GL_MAX_TEXTURE_IMAGE_UNITS_index(getDomNodeValue(dereference(curNode)));
    }

    public GL_MAX_TEXTURE_IMAGE_UNITS_index getindex() throws Exception {
        return getindexAt(0);
    }

    public void removeindexAt(int index) {
        removeDomChildAt(Attribute, null, "index", index);
    }

    public void removeindex() {
        while (hasindex()) removeindexAt(0);
    }

    public void addindex(GL_MAX_TEXTURE_IMAGE_UNITS_index value) {
        if (value.isNull() == false) {
            appendDomChild(Attribute, null, "index", value.toString());
        }
    }

    public void addindex(String value) throws Exception {
        addindex(new GL_MAX_TEXTURE_IMAGE_UNITS_index(value));
    }

    public void insertindexAt(GL_MAX_TEXTURE_IMAGE_UNITS_index value, int index) {
        insertDomChildAt(Attribute, null, "index", index, value.toString());
    }

    public void insertindexAt(String value, int index) throws Exception {
        insertindexAt(new GL_MAX_TEXTURE_IMAGE_UNITS_index(value), index);
    }

    public void replaceindexAt(GL_MAX_TEXTURE_IMAGE_UNITS_index value, int index) {
        replaceDomChildAt(Attribute, null, "index", index, value.toString());
    }

    public void replaceindexAt(String value, int index) throws Exception {
        replaceindexAt(new GL_MAX_TEXTURE_IMAGE_UNITS_index(value), index);
    }

    public static int getvalue2MinCount() {
        return 1;
    }

    public static int getvalue2MaxCount() {
        return 1;
    }

    public int getvalue2Count() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "value");
    }

    public boolean hasvalue2() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "value");
    }

    public gl_sampler2D newvalue2() {
        return new gl_sampler2D(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "value"));
    }

    public gl_sampler2D getvalue2At(int index) throws Exception {
        return new gl_sampler2D(dereference(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "value", index)));
    }

    public org.w3c.dom.Node getStartingvalue2Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "value");
    }

    public org.w3c.dom.Node getAdvancedvalue2Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "value", curNode);
    }

    public gl_sampler2D getvalue2ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.model.collada.xml.XmlException("Out of range"); else return new gl_sampler2D(dereference(curNode));
    }

    public gl_sampler2D getvalue2() throws Exception {
        return getvalue2At(0);
    }

    public void removevalue2At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "value", index);
    }

    public void removevalue2() {
        while (hasvalue2()) removevalue2At(0);
    }

    public void addvalue2(gl_sampler2D value) {
        appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "value", value);
    }

    public void insertvalue2At(gl_sampler2D value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "value", index, value);
    }

    public void replacevalue2At(gl_sampler2D value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "value", index, value);
    }

    public static int getparamMinCount() {
        return 1;
    }

    public static int getparamMaxCount() {
        return 1;
    }

    public int getparamCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "param");
    }

    public boolean hasparam() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "param");
    }

    public SchemaNCName newparam() {
        return new SchemaNCName();
    }

    public SchemaNCName getparamAt(int index) throws Exception {
        return new SchemaNCName(getDomNodeValue(dereference(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "param", index))));
    }

    public org.w3c.dom.Node getStartingparamCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "param");
    }

    public org.w3c.dom.Node getAdvancedparamCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "param", curNode);
    }

    public SchemaNCName getparamValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.model.collada.xml.XmlException("Out of range"); else return new SchemaNCName(getDomNodeValue(dereference(curNode)));
    }

    public SchemaNCName getparam() throws Exception {
        return getparamAt(0);
    }

    public void removeparamAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "param", index);
    }

    public void removeparam() {
        while (hasparam()) removeparamAt(0);
    }

    public void addparam(SchemaNCName value) {
        if (value.isNull() == false) {
            appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "param", value.toString());
        }
    }

    public void addparam(String value) throws Exception {
        addparam(new SchemaNCName(value));
    }

    public void insertparamAt(SchemaNCName value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "param", index, value.toString());
    }

    public void insertparamAt(String value, int index) throws Exception {
        insertparamAt(new SchemaNCName(value), index);
    }

    public void replaceparamAt(SchemaNCName value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "param", index, value.toString());
    }

    public void replaceparamAt(String value, int index) throws Exception {
        replaceparamAt(new SchemaNCName(value), index);
    }

    private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
        return node;
    }
}
