package com.jmex.model.collada.schema;

public class common_color_or_texture_type extends com.jmex.xml.xml.Node {

    public common_color_or_texture_type(common_color_or_texture_type node) {
        super(node);
    }

    public common_color_or_texture_type(org.w3c.dom.Node node) {
        super(node);
    }

    public common_color_or_texture_type(org.w3c.dom.Document doc) {
        super(doc);
    }

    public common_color_or_texture_type(com.jmex.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "color"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "color", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new colorType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "param"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "param", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new paramType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "texture"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "texture", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new textureType(tmpNode).adjustPrefix();
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "common_color_or_texture_type");
    }

    public static int getcolorMinCount() {
        return 1;
    }

    public static int getcolorMaxCount() {
        return 1;
    }

    public int getcolorCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "color");
    }

    public boolean hascolor() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "color");
    }

    public colorType newcolor() {
        return new colorType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "color"));
    }

    public colorType getcolorAt(int index) throws Exception {
        return new colorType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "color", index));
    }

    public org.w3c.dom.Node getStartingcolorCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "color");
    }

    public org.w3c.dom.Node getAdvancedcolorCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "color", curNode);
    }

    public colorType getcolorValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new colorType(curNode);
    }

    public colorType getcolor() throws Exception {
        return getcolorAt(0);
    }

    public void removecolorAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "color", index);
    }

    public void removecolor() {
        removecolorAt(0);
    }

    public org.w3c.dom.Node addcolor(colorType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "color", value);
    }

    public void insertcolorAt(colorType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "color", index, value);
    }

    public void replacecolorAt(colorType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "color", index, value);
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

    public paramType newparam() {
        return new paramType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "param"));
    }

    public paramType getparamAt(int index) throws Exception {
        return new paramType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "param", index));
    }

    public org.w3c.dom.Node getStartingparamCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "param");
    }

    public org.w3c.dom.Node getAdvancedparamCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "param", curNode);
    }

    public paramType getparamValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new paramType(curNode);
    }

    public paramType getparam() throws Exception {
        return getparamAt(0);
    }

    public void removeparamAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "param", index);
    }

    public void removeparam() {
        removeparamAt(0);
    }

    public org.w3c.dom.Node addparam(paramType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "param", value);
    }

    public void insertparamAt(paramType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "param", index, value);
    }

    public void replaceparamAt(paramType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "param", index, value);
    }

    public static int gettextureMinCount() {
        return 1;
    }

    public static int gettextureMaxCount() {
        return 1;
    }

    public int gettextureCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "texture");
    }

    public boolean hastexture() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "texture");
    }

    public textureType newtexture() {
        return new textureType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "texture"));
    }

    public textureType gettextureAt(int index) throws Exception {
        return new textureType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "texture", index));
    }

    public org.w3c.dom.Node getStartingtextureCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "texture");
    }

    public org.w3c.dom.Node getAdvancedtextureCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "texture", curNode);
    }

    public textureType gettextureValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new textureType(curNode);
    }

    public textureType gettexture() throws Exception {
        return gettextureAt(0);
    }

    public void removetextureAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "texture", index);
    }

    public void removetexture() {
        removetextureAt(0);
    }

    public org.w3c.dom.Node addtexture(textureType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "texture", value);
    }

    public void inserttextureAt(textureType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "texture", index, value);
    }

    public void replacetextureAt(textureType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "texture", index, value);
    }
}
