package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaID;
import homura.hde.util.xml.types.SchemaNCName;

public class cameraType extends homura.hde.util.xml.xml.Node {

    public cameraType(cameraType node) {
        super(node);
    }

    public cameraType(org.w3c.dom.Node node) {
        super(node);
    }

    public cameraType(org.w3c.dom.Document doc) {
        super(doc);
    }

    public cameraType(homura.hde.util.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "id"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "id", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "name"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "name", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "asset"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "asset", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new assetType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "optics"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "optics", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new opticsType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "imager"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "imager", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new imagerType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new extraType(tmpNode).adjustPrefix();
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "camera");
    }

    public static int getidMinCount() {
        return 0;
    }

    public static int getidMaxCount() {
        return 1;
    }

    public int getidCount() {
        return getDomChildCount(Attribute, null, "id");
    }

    public boolean hasid() {
        return hasDomChild(Attribute, null, "id");
    }

    public SchemaID newid() {
        return new SchemaID();
    }

    public SchemaID getidAt(int index) throws Exception {
        return new SchemaID(getDomNodeValue(getDomChildAt(Attribute, null, "id", index)));
    }

    public org.w3c.dom.Node getStartingidCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "id");
    }

    public org.w3c.dom.Node getAdvancedidCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "id", curNode);
    }

    public SchemaID getidValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new SchemaID(getDomNodeValue(curNode));
    }

    public SchemaID getid() throws Exception {
        return getidAt(0);
    }

    public void removeidAt(int index) {
        removeDomChildAt(Attribute, null, "id", index);
    }

    public void removeid() {
        removeidAt(0);
    }

    public org.w3c.dom.Node addid(SchemaID value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "id", value.toString());
    }

    public org.w3c.dom.Node addid(String value) throws Exception {
        return addid(new SchemaID(value));
    }

    public void insertidAt(SchemaID value, int index) {
        insertDomChildAt(Attribute, null, "id", index, value.toString());
    }

    public void insertidAt(String value, int index) throws Exception {
        insertidAt(new SchemaID(value), index);
    }

    public void replaceidAt(SchemaID value, int index) {
        replaceDomChildAt(Attribute, null, "id", index, value.toString());
    }

    public void replaceidAt(String value, int index) throws Exception {
        replaceidAt(new SchemaID(value), index);
    }

    public static int getnameMinCount() {
        return 0;
    }

    public static int getnameMaxCount() {
        return 1;
    }

    public int getnameCount() {
        return getDomChildCount(Attribute, null, "name");
    }

    public boolean hasname() {
        return hasDomChild(Attribute, null, "name");
    }

    public SchemaNCName newname() {
        return new SchemaNCName();
    }

    public SchemaNCName getnameAt(int index) throws Exception {
        return new SchemaNCName(getDomNodeValue(getDomChildAt(Attribute, null, "name", index)));
    }

    public org.w3c.dom.Node getStartingnameCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "name");
    }

    public org.w3c.dom.Node getAdvancednameCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "name", curNode);
    }

    public SchemaNCName getnameValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new SchemaNCName(getDomNodeValue(curNode));
    }

    public SchemaNCName getname() throws Exception {
        return getnameAt(0);
    }

    public void removenameAt(int index) {
        removeDomChildAt(Attribute, null, "name", index);
    }

    public void removename() {
        removenameAt(0);
    }

    public org.w3c.dom.Node addname(SchemaNCName value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "name", value.toString());
    }

    public org.w3c.dom.Node addname(String value) throws Exception {
        return addname(new SchemaNCName(value));
    }

    public void insertnameAt(SchemaNCName value, int index) {
        insertDomChildAt(Attribute, null, "name", index, value.toString());
    }

    public void insertnameAt(String value, int index) throws Exception {
        insertnameAt(new SchemaNCName(value), index);
    }

    public void replacenameAt(SchemaNCName value, int index) {
        replaceDomChildAt(Attribute, null, "name", index, value.toString());
    }

    public void replacenameAt(String value, int index) throws Exception {
        replacenameAt(new SchemaNCName(value), index);
    }

    public static int getassetMinCount() {
        return 0;
    }

    public static int getassetMaxCount() {
        return 1;
    }

    public int getassetCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "asset");
    }

    public boolean hasasset() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "asset");
    }

    public assetType newasset() {
        return new assetType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "asset"));
    }

    public assetType getassetAt(int index) throws Exception {
        return new assetType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "asset", index));
    }

    public org.w3c.dom.Node getStartingassetCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "asset");
    }

    public org.w3c.dom.Node getAdvancedassetCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "asset", curNode);
    }

    public assetType getassetValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new assetType(curNode);
    }

    public assetType getasset() throws Exception {
        return getassetAt(0);
    }

    public void removeassetAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "asset", index);
    }

    public void removeasset() {
        removeassetAt(0);
    }

    public org.w3c.dom.Node addasset(assetType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "asset", value);
    }

    public void insertassetAt(assetType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "asset", index, value);
    }

    public void replaceassetAt(assetType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "asset", index, value);
    }

    public static int getopticsMinCount() {
        return 1;
    }

    public static int getopticsMaxCount() {
        return 1;
    }

    public int getopticsCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "optics");
    }

    public boolean hasoptics() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "optics");
    }

    public opticsType newoptics() {
        return new opticsType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "optics"));
    }

    public opticsType getopticsAt(int index) throws Exception {
        return new opticsType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "optics", index));
    }

    public org.w3c.dom.Node getStartingopticsCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "optics");
    }

    public org.w3c.dom.Node getAdvancedopticsCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "optics", curNode);
    }

    public opticsType getopticsValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new opticsType(curNode);
    }

    public opticsType getoptics() throws Exception {
        return getopticsAt(0);
    }

    public void removeopticsAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "optics", index);
    }

    public void removeoptics() {
        removeopticsAt(0);
    }

    public org.w3c.dom.Node addoptics(opticsType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "optics", value);
    }

    public void insertopticsAt(opticsType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "optics", index, value);
    }

    public void replaceopticsAt(opticsType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "optics", index, value);
    }

    public static int getimagerMinCount() {
        return 0;
    }

    public static int getimagerMaxCount() {
        return 1;
    }

    public int getimagerCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "imager");
    }

    public boolean hasimager() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "imager");
    }

    public imagerType newimager() {
        return new imagerType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "imager"));
    }

    public imagerType getimagerAt(int index) throws Exception {
        return new imagerType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "imager", index));
    }

    public org.w3c.dom.Node getStartingimagerCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "imager");
    }

    public org.w3c.dom.Node getAdvancedimagerCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "imager", curNode);
    }

    public imagerType getimagerValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new imagerType(curNode);
    }

    public imagerType getimager() throws Exception {
        return getimagerAt(0);
    }

    public void removeimagerAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "imager", index);
    }

    public void removeimager() {
        removeimagerAt(0);
    }

    public org.w3c.dom.Node addimager(imagerType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "imager", value);
    }

    public void insertimagerAt(imagerType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "imager", index, value);
    }

    public void replaceimagerAt(imagerType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "imager", index, value);
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
