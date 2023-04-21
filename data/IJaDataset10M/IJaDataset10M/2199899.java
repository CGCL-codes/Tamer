package com.jmex.model.collada.schema;

public class angularType extends com.jmex.xml.xml.Node {

    public angularType(angularType node) {
        super(node);
    }

    public angularType(org.w3c.dom.Node node) {
        super(node);
    }

    public angularType(org.w3c.dom.Document doc) {
        super(doc);
    }

    public angularType(com.jmex.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "stiffness"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "stiffness", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new TargetableFloat(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "damping"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "damping", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new TargetableFloat(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "target_value"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "target_value", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new TargetableFloat(tmpNode).adjustPrefix();
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "angular");
    }

    public static int getstiffnessMinCount() {
        return 0;
    }

    public static int getstiffnessMaxCount() {
        return 1;
    }

    public int getstiffnessCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "stiffness");
    }

    public boolean hasstiffness() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "stiffness");
    }

    public TargetableFloat newstiffness() {
        return new TargetableFloat(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "stiffness"));
    }

    public TargetableFloat getstiffnessAt(int index) throws Exception {
        return new TargetableFloat(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "stiffness", index));
    }

    public org.w3c.dom.Node getStartingstiffnessCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "stiffness");
    }

    public org.w3c.dom.Node getAdvancedstiffnessCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "stiffness", curNode);
    }

    public TargetableFloat getstiffnessValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new TargetableFloat(curNode);
    }

    public TargetableFloat getstiffness() throws Exception {
        return getstiffnessAt(0);
    }

    public void removestiffnessAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "stiffness", index);
    }

    public void removestiffness() {
        removestiffnessAt(0);
    }

    public org.w3c.dom.Node addstiffness(TargetableFloat value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "stiffness", value);
    }

    public void insertstiffnessAt(TargetableFloat value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "stiffness", index, value);
    }

    public void replacestiffnessAt(TargetableFloat value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "stiffness", index, value);
    }

    public static int getdampingMinCount() {
        return 0;
    }

    public static int getdampingMaxCount() {
        return 1;
    }

    public int getdampingCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "damping");
    }

    public boolean hasdamping() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "damping");
    }

    public TargetableFloat newdamping() {
        return new TargetableFloat(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "damping"));
    }

    public TargetableFloat getdampingAt(int index) throws Exception {
        return new TargetableFloat(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "damping", index));
    }

    public org.w3c.dom.Node getStartingdampingCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "damping");
    }

    public org.w3c.dom.Node getAdvanceddampingCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "damping", curNode);
    }

    public TargetableFloat getdampingValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new TargetableFloat(curNode);
    }

    public TargetableFloat getdamping() throws Exception {
        return getdampingAt(0);
    }

    public void removedampingAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "damping", index);
    }

    public void removedamping() {
        removedampingAt(0);
    }

    public org.w3c.dom.Node adddamping(TargetableFloat value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "damping", value);
    }

    public void insertdampingAt(TargetableFloat value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "damping", index, value);
    }

    public void replacedampingAt(TargetableFloat value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "damping", index, value);
    }

    public static int gettarget_valueMinCount() {
        return 0;
    }

    public static int gettarget_valueMaxCount() {
        return 1;
    }

    public int gettarget_valueCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "target_value");
    }

    public boolean hastarget_value() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "target_value");
    }

    public TargetableFloat newtarget_value() {
        return new TargetableFloat(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "target_value"));
    }

    public TargetableFloat gettarget_valueAt(int index) throws Exception {
        return new TargetableFloat(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "target_value", index));
    }

    public org.w3c.dom.Node getStartingtarget_valueCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "target_value");
    }

    public org.w3c.dom.Node getAdvancedtarget_valueCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "target_value", curNode);
    }

    public TargetableFloat gettarget_valueValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new TargetableFloat(curNode);
    }

    public TargetableFloat gettarget_value() throws Exception {
        return gettarget_valueAt(0);
    }

    public void removetarget_valueAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "target_value", index);
    }

    public void removetarget_value() {
        removetarget_valueAt(0);
    }

    public org.w3c.dom.Node addtarget_value(TargetableFloat value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "target_value", value);
    }

    public void inserttarget_valueAt(TargetableFloat value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "target_value", index, value);
    }

    public void replacetarget_valueAt(TargetableFloat value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "target_value", index, value);
    }
}
