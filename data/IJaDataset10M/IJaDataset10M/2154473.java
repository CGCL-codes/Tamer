package homura.hde.ext.model.collada.schema;

public class control_verticesType extends homura.hde.util.xml.xml.Node {

    public control_verticesType(control_verticesType node) {
        super(node);
    }

    public control_verticesType(org.w3c.dom.Node node) {
        super(node);
    }

    public control_verticesType(org.w3c.dom.Document doc) {
        super(doc);
    }

    public control_verticesType(homura.hde.util.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "input"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "input", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new InputLocal(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new extraType(tmpNode).adjustPrefix();
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "control_vertices");
    }

    public static int getinputMinCount() {
        return 1;
    }

    public static int getinputMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int getinputCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "input");
    }

    public boolean hasinput() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "input");
    }

    public InputLocal newinput() {
        return new InputLocal(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "input"));
    }

    public InputLocal getinputAt(int index) throws Exception {
        return new InputLocal(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "input", index));
    }

    public org.w3c.dom.Node getStartinginputCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "input");
    }

    public org.w3c.dom.Node getAdvancedinputCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "input", curNode);
    }

    public InputLocal getinputValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new InputLocal(curNode);
    }

    public InputLocal getinput() throws Exception {
        return getinputAt(0);
    }

    public void removeinputAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "input", index);
    }

    public void removeinput() {
        while (hasinput()) removeinputAt(0);
    }

    public org.w3c.dom.Node addinput(InputLocal value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "input", value);
    }

    public void insertinputAt(InputLocal value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "input", index, value);
    }

    public void replaceinputAt(InputLocal value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "input", index, value);
    }

    public static int getextraMinCount() {
        return 0;
    }

    public static int getextraMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int getextraCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra");
    }

    public boolean hasextra() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra");
    }

    public extraType newextra() {
        return new extraType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "extra"));
    }

    public extraType getextraAt(int index) throws Exception {
        return new extraType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra", index));
    }

    public org.w3c.dom.Node getStartingextraCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra");
    }

    public org.w3c.dom.Node getAdvancedextraCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra", curNode);
    }

    public extraType getextraValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new extraType(curNode);
    }

    public extraType getextra() throws Exception {
        return getextraAt(0);
    }

    public void removeextraAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra", index);
    }

    public void removeextra() {
        while (hasextra()) removeextraAt(0);
    }

    public org.w3c.dom.Node addextra(extraType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "extra", value);
    }

    public void insertextraAt(extraType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "extra", index, value);
    }

    public void replaceextraAt(extraType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "extra", index, value);
    }
}
