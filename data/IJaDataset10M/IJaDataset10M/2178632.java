package homura.hde.ext.model.collada.schema;

public class cg_surface_type extends fx_surface_common {

    public cg_surface_type(cg_surface_type node) {
        super(node);
    }

    public cg_surface_type(org.w3c.dom.Node node) {
        super(node);
    }

    public cg_surface_type(org.w3c.dom.Document doc) {
        super(doc);
    }

    public cg_surface_type(homura.hde.util.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "generator"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "generator", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new generatorType(tmpNode).adjustPrefix();
        }
        super.adjustPrefix();
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "cg_surface_type");
    }

    public static int getgeneratorMinCount() {
        return 0;
    }

    public static int getgeneratorMaxCount() {
        return 1;
    }

    public int getgeneratorCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "generator");
    }

    public boolean hasgenerator() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "generator");
    }

    public generatorType newgenerator() {
        return new generatorType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "generator"));
    }

    public generatorType getgeneratorAt(int index) throws Exception {
        return new generatorType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "generator", index));
    }

    public org.w3c.dom.Node getStartinggeneratorCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "generator");
    }

    public org.w3c.dom.Node getAdvancedgeneratorCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "generator", curNode);
    }

    public generatorType getgeneratorValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new generatorType(curNode);
    }

    public generatorType getgenerator() throws Exception {
        return getgeneratorAt(0);
    }

    public void removegeneratorAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "generator", index);
    }

    public void removegenerator() {
        removegeneratorAt(0);
    }

    public org.w3c.dom.Node addgenerator(generatorType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "generator", value);
    }

    public void insertgeneratorAt(generatorType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "generator", index, value);
    }

    public void replacegeneratorAt(generatorType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "generator", index, value);
    }
}
