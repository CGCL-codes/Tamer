package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaID;
import homura.hde.util.xml.types.SchemaNCName;

public class lightType extends homura.hde.util.xml.xml.Node {

    public lightType(lightType node) {
        super(node);
    }

    public lightType(org.w3c.dom.Node node) {
        super(node);
    }

    public lightType(org.w3c.dom.Document doc) {
        super(doc);
    }

    public lightType(homura.hde.util.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
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
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "technique_common"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "technique_common", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new technique_commonType4(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "technique"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "technique", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new techniqueType5(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new extraType(tmpNode).adjustPrefix();
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "light");
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

    public static int gettechnique_commonMinCount() {
        return 1;
    }

    public static int gettechnique_commonMaxCount() {
        return 1;
    }

    public int gettechnique_commonCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "technique_common");
    }

    public boolean hastechnique_common() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "technique_common");
    }

    public technique_commonType4 newtechnique_common() {
        return new technique_commonType4(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "technique_common"));
    }

    public technique_commonType4 gettechnique_commonAt(int index) throws Exception {
        return new technique_commonType4(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "technique_common", index));
    }

    public org.w3c.dom.Node getStartingtechnique_commonCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "technique_common");
    }

    public org.w3c.dom.Node getAdvancedtechnique_commonCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "technique_common", curNode);
    }

    public technique_commonType4 gettechnique_commonValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new technique_commonType4(curNode);
    }

    public technique_commonType4 gettechnique_common() throws Exception {
        return gettechnique_commonAt(0);
    }

    public void removetechnique_commonAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "technique_common", index);
    }

    public void removetechnique_common() {
        removetechnique_commonAt(0);
    }

    public org.w3c.dom.Node addtechnique_common(technique_commonType4 value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "technique_common", value);
    }

    public void inserttechnique_commonAt(technique_commonType4 value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "technique_common", index, value);
    }

    public void replacetechnique_commonAt(technique_commonType4 value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "technique_common", index, value);
    }

    public static int gettechniqueMinCount() {
        return 0;
    }

    public static int gettechniqueMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int gettechniqueCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "technique");
    }

    public boolean hastechnique() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "technique");
    }

    public techniqueType5 newtechnique() {
        return new techniqueType5(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "technique"));
    }

    public techniqueType5 gettechniqueAt(int index) throws Exception {
        return new techniqueType5(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "technique", index));
    }

    public org.w3c.dom.Node getStartingtechniqueCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "technique");
    }

    public org.w3c.dom.Node getAdvancedtechniqueCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "technique", curNode);
    }

    public techniqueType5 gettechniqueValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new techniqueType5(curNode);
    }

    public techniqueType5 gettechnique() throws Exception {
        return gettechniqueAt(0);
    }

    public void removetechniqueAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "technique", index);
    }

    public void removetechnique() {
        while (hastechnique()) removetechniqueAt(0);
    }

    public org.w3c.dom.Node addtechnique(techniqueType5 value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "technique", value);
    }

    public void inserttechniqueAt(techniqueType5 value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "technique", index, value);
    }

    public void replacetechniqueAt(techniqueType5 value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "technique", index, value);
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
