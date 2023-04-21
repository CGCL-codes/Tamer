package homura.hde.ext.model.collada.schema;

public class splineType extends homura.hde.util.xml.xml.Node {

    public splineType(splineType node) {
        super(node);
    }

    public splineType(org.w3c.dom.Node node) {
        super(node);
    }

    public splineType(org.w3c.dom.Document doc) {
        super(doc);
    }

    public splineType(homura.hde.util.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "closed"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "closed", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "source"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "source", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new sourceType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "control_vertices"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "control_vertices", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new control_verticesType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new extraType(tmpNode).adjustPrefix();
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "spline");
    }

    public static int getclosedMinCount() {
        return 0;
    }

    public static int getclosedMaxCount() {
        return 1;
    }

    public int getclosedCount() {
        return getDomChildCount(Attribute, null, "closed");
    }

    public boolean hasclosed() {
        return hasDomChild(Attribute, null, "closed");
    }

    public bool newclosed() {
        return new bool();
    }

    public bool getclosedAt(int index) throws Exception {
        return new bool(getDomNodeValue(getDomChildAt(Attribute, null, "closed", index)));
    }

    public org.w3c.dom.Node getStartingclosedCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "closed");
    }

    public org.w3c.dom.Node getAdvancedclosedCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "closed", curNode);
    }

    public bool getclosedValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new bool(getDomNodeValue(curNode));
    }

    public bool getclosed() throws Exception {
        return getclosedAt(0);
    }

    public void removeclosedAt(int index) {
        removeDomChildAt(Attribute, null, "closed", index);
    }

    public void removeclosed() {
        removeclosedAt(0);
    }

    public org.w3c.dom.Node addclosed(bool value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "closed", value.toString());
    }

    public org.w3c.dom.Node addclosed(String value) throws Exception {
        return addclosed(new bool(value));
    }

    public void insertclosedAt(bool value, int index) {
        insertDomChildAt(Attribute, null, "closed", index, value.toString());
    }

    public void insertclosedAt(String value, int index) throws Exception {
        insertclosedAt(new bool(value), index);
    }

    public void replaceclosedAt(bool value, int index) {
        replaceDomChildAt(Attribute, null, "closed", index, value.toString());
    }

    public void replaceclosedAt(String value, int index) throws Exception {
        replaceclosedAt(new bool(value), index);
    }

    public static int getsourceMinCount() {
        return 1;
    }

    public static int getsourceMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int getsourceCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "source");
    }

    public boolean hassource() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "source");
    }

    public sourceType newsource() {
        return new sourceType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "source"));
    }

    public sourceType getsourceAt(int index) throws Exception {
        return new sourceType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "source", index));
    }

    public org.w3c.dom.Node getStartingsourceCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "source");
    }

    public org.w3c.dom.Node getAdvancedsourceCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "source", curNode);
    }

    public sourceType getsourceValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new sourceType(curNode);
    }

    public sourceType getsource() throws Exception {
        return getsourceAt(0);
    }

    public void removesourceAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "source", index);
    }

    public void removesource() {
        while (hassource()) removesourceAt(0);
    }

    public org.w3c.dom.Node addsource(sourceType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "source", value);
    }

    public void insertsourceAt(sourceType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "source", index, value);
    }

    public void replacesourceAt(sourceType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "source", index, value);
    }

    public static int getcontrol_verticesMinCount() {
        return 1;
    }

    public static int getcontrol_verticesMaxCount() {
        return 1;
    }

    public int getcontrol_verticesCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "control_vertices");
    }

    public boolean hascontrol_vertices() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "control_vertices");
    }

    public control_verticesType newcontrol_vertices() {
        return new control_verticesType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "control_vertices"));
    }

    public control_verticesType getcontrol_verticesAt(int index) throws Exception {
        return new control_verticesType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "control_vertices", index));
    }

    public org.w3c.dom.Node getStartingcontrol_verticesCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "control_vertices");
    }

    public org.w3c.dom.Node getAdvancedcontrol_verticesCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "control_vertices", curNode);
    }

    public control_verticesType getcontrol_verticesValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new control_verticesType(curNode);
    }

    public control_verticesType getcontrol_vertices() throws Exception {
        return getcontrol_verticesAt(0);
    }

    public void removecontrol_verticesAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "control_vertices", index);
    }

    public void removecontrol_vertices() {
        removecontrol_verticesAt(0);
    }

    public org.w3c.dom.Node addcontrol_vertices(control_verticesType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "control_vertices", value);
    }

    public void insertcontrol_verticesAt(control_verticesType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "control_vertices", index, value);
    }

    public void replacecontrol_verticesAt(control_verticesType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "control_vertices", index, value);
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
