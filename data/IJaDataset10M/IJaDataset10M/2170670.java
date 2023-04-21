package com.jmex.model.collada.schema;

import com.jmex.model.collada.types.SchemaNCName;

public class texture_env_modeType2 extends com.jmex.model.collada.xml.Node {

    private static final long serialVersionUID = 1L;

    public texture_env_modeType2(texture_env_modeType2 node) {
        super(node);
    }

    public texture_env_modeType2(org.w3c.dom.Node node) {
        super(node);
    }

    public texture_env_modeType2(org.w3c.dom.Document doc) {
        super(doc);
    }

    public texture_env_modeType2(com.jmex.model.collada.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "value"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "value", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "param"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "param", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "index"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "index", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
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

    public string newvalue2() {
        return new string();
    }

    public string getvalue2At(int index) throws Exception {
        return new string(getDomNodeValue(dereference(getDomChildAt(Attribute, null, "value", index))));
    }

    public org.w3c.dom.Node getStartingvalue2Cursor() throws Exception {
        return getDomFirstChild(Attribute, null, "value");
    }

    public org.w3c.dom.Node getAdvancedvalue2Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "value", curNode);
    }

    public string getvalue2ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.model.collada.xml.XmlException("Out of range"); else return new string(getDomNodeValue(dereference(curNode)));
    }

    public string getvalue2() throws Exception {
        return getvalue2At(0);
    }

    public void removevalue2At(int index) {
        removeDomChildAt(Attribute, null, "value", index);
    }

    public void removevalue2() {
        while (hasvalue2()) removevalue2At(0);
    }

    public void addvalue2(string value) {
        if (value.isNull() == false) {
            appendDomChild(Attribute, null, "value", value.toString());
        }
    }

    public void addvalue2(String value) throws Exception {
        addvalue2(new string(value));
    }

    public void insertvalue2At(string value, int index) {
        insertDomChildAt(Attribute, null, "value", index, value.toString());
    }

    public void insertvalue2At(String value, int index) throws Exception {
        insertvalue2At(new string(value), index);
    }

    public void replacevalue2At(string value, int index) {
        replaceDomChildAt(Attribute, null, "value", index, value.toString());
    }

    public void replacevalue2At(String value, int index) throws Exception {
        replacevalue2At(new string(value), index);
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
        return new SchemaNCName(getDomNodeValue(dereference(getDomChildAt(Attribute, null, "param", index))));
    }

    public org.w3c.dom.Node getStartingparamCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "param");
    }

    public org.w3c.dom.Node getAdvancedparamCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "param", curNode);
    }

    public SchemaNCName getparamValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.model.collada.xml.XmlException("Out of range"); else return new SchemaNCName(getDomNodeValue(dereference(curNode)));
    }

    public SchemaNCName getparam() throws Exception {
        return getparamAt(0);
    }

    public void removeparamAt(int index) {
        removeDomChildAt(Attribute, null, "param", index);
    }

    public void removeparam() {
        while (hasparam()) removeparamAt(0);
    }

    public void addparam(SchemaNCName value) {
        if (value.isNull() == false) {
            appendDomChild(Attribute, null, "param", value.toString());
        }
    }

    public void addparam(String value) throws Exception {
        addparam(new SchemaNCName(value));
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

    public static int getindexMinCount() {
        return 0;
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

    private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
        return node;
    }
}
