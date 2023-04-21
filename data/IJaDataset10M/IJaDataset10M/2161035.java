package com.jmex.model.collada.schema;

public class directionalType extends com.jmex.xml.xml.Node {

    public directionalType(directionalType node) {
        super(node);
    }

    public directionalType(org.w3c.dom.Node node) {
        super(node);
    }

    public directionalType(org.w3c.dom.Document doc) {
        super(doc);
    }

    public directionalType(com.jmex.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "color"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "color", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new TargetableFloat3(tmpNode).adjustPrefix();
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "directional");
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

    public TargetableFloat3 newcolor() {
        return new TargetableFloat3(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "color"));
    }

    public TargetableFloat3 getcolorAt(int index) throws Exception {
        return new TargetableFloat3(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "color", index));
    }

    public org.w3c.dom.Node getStartingcolorCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "color");
    }

    public org.w3c.dom.Node getAdvancedcolorCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "color", curNode);
    }

    public TargetableFloat3 getcolorValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new TargetableFloat3(curNode);
    }

    public TargetableFloat3 getcolor() throws Exception {
        return getcolorAt(0);
    }

    public void removecolorAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "color", index);
    }

    public void removecolor() {
        removecolorAt(0);
    }

    public org.w3c.dom.Node addcolor(TargetableFloat3 value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "color", value);
    }

    public void insertcolorAt(TargetableFloat3 value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "color", index, value);
    }

    public void replacecolorAt(TargetableFloat3 value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "color", index, value);
    }
}
