package com.jmex.model.collada.schema;

public class technique_commonType4 extends com.jmex.xml.xml.Node {

    public technique_commonType4(technique_commonType4 node) {
        super(node);
    }

    public technique_commonType4(org.w3c.dom.Node node) {
        super(node);
    }

    public technique_commonType4(org.w3c.dom.Document doc) {
        super(doc);
    }

    public technique_commonType4(com.jmex.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "ambient"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "ambient", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new ambientType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "directional"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "directional", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new directionalType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "point"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "point", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new pointType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "spot"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "spot", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new spotType(tmpNode).adjustPrefix();
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "technique_common");
    }

    public static int getambientMinCount() {
        return 1;
    }

    public static int getambientMaxCount() {
        return 1;
    }

    public int getambientCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "ambient");
    }

    public boolean hasambient() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "ambient");
    }

    public ambientType newambient() {
        return new ambientType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "ambient"));
    }

    public ambientType getambientAt(int index) throws Exception {
        return new ambientType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "ambient", index));
    }

    public org.w3c.dom.Node getStartingambientCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "ambient");
    }

    public org.w3c.dom.Node getAdvancedambientCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "ambient", curNode);
    }

    public ambientType getambientValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new ambientType(curNode);
    }

    public ambientType getambient() throws Exception {
        return getambientAt(0);
    }

    public void removeambientAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "ambient", index);
    }

    public void removeambient() {
        removeambientAt(0);
    }

    public org.w3c.dom.Node addambient(ambientType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "ambient", value);
    }

    public void insertambientAt(ambientType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "ambient", index, value);
    }

    public void replaceambientAt(ambientType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "ambient", index, value);
    }

    public static int getdirectionalMinCount() {
        return 1;
    }

    public static int getdirectionalMaxCount() {
        return 1;
    }

    public int getdirectionalCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "directional");
    }

    public boolean hasdirectional() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "directional");
    }

    public directionalType newdirectional() {
        return new directionalType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "directional"));
    }

    public directionalType getdirectionalAt(int index) throws Exception {
        return new directionalType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "directional", index));
    }

    public org.w3c.dom.Node getStartingdirectionalCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "directional");
    }

    public org.w3c.dom.Node getAdvanceddirectionalCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "directional", curNode);
    }

    public directionalType getdirectionalValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new directionalType(curNode);
    }

    public directionalType getdirectional() throws Exception {
        return getdirectionalAt(0);
    }

    public void removedirectionalAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "directional", index);
    }

    public void removedirectional() {
        removedirectionalAt(0);
    }

    public org.w3c.dom.Node adddirectional(directionalType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "directional", value);
    }

    public void insertdirectionalAt(directionalType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "directional", index, value);
    }

    public void replacedirectionalAt(directionalType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "directional", index, value);
    }

    public static int getpointMinCount() {
        return 1;
    }

    public static int getpointMaxCount() {
        return 1;
    }

    public int getpointCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "point");
    }

    public boolean haspoint() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "point");
    }

    public pointType newpoint() {
        return new pointType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "point"));
    }

    public pointType getpointAt(int index) throws Exception {
        return new pointType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "point", index));
    }

    public org.w3c.dom.Node getStartingpointCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "point");
    }

    public org.w3c.dom.Node getAdvancedpointCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "point", curNode);
    }

    public pointType getpointValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new pointType(curNode);
    }

    public pointType getpoint() throws Exception {
        return getpointAt(0);
    }

    public void removepointAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "point", index);
    }

    public void removepoint() {
        removepointAt(0);
    }

    public org.w3c.dom.Node addpoint(pointType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "point", value);
    }

    public void insertpointAt(pointType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "point", index, value);
    }

    public void replacepointAt(pointType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "point", index, value);
    }

    public static int getspotMinCount() {
        return 1;
    }

    public static int getspotMaxCount() {
        return 1;
    }

    public int getspotCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "spot");
    }

    public boolean hasspot() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "spot");
    }

    public spotType newspot() {
        return new spotType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "spot"));
    }

    public spotType getspotAt(int index) throws Exception {
        return new spotType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "spot", index));
    }

    public org.w3c.dom.Node getStartingspotCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "spot");
    }

    public org.w3c.dom.Node getAdvancedspotCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "spot", curNode);
    }

    public spotType getspotValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new spotType(curNode);
    }

    public spotType getspot() throws Exception {
        return getspotAt(0);
    }

    public void removespotAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "spot", index);
    }

    public void removespot() {
        removespotAt(0);
    }

    public org.w3c.dom.Node addspot(spotType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "spot", value);
    }

    public void insertspotAt(spotType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "spot", index, value);
    }

    public void replacespotAt(spotType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "spot", index, value);
    }
}
