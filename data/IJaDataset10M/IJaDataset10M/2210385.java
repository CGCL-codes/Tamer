package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaID;
import homura.hde.util.xml.types.SchemaNCName;
import homura.hde.util.xml.types.SchemaString;
import homura.hde.util.xml.types.SchemaToken;

public class imageType extends homura.hde.util.xml.xml.Node {

    public imageType(imageType node) {
        super(node);
    }

    public imageType(org.w3c.dom.Node node) {
        super(node);
    }

    public imageType(org.w3c.dom.Document doc) {
        super(doc);
    }

    public imageType(homura.hde.util.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "id"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "id", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "name"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "name", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "format"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "format", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "height"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "height", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "width"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "width", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "depth"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "depth", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "asset"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "asset", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new assetType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "data"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "data", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new extraType(tmpNode).adjustPrefix();
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "image");
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

    public static int getformatMinCount() {
        return 0;
    }

    public static int getformatMaxCount() {
        return 1;
    }

    public int getformatCount() {
        return getDomChildCount(Attribute, null, "format");
    }

    public boolean hasformat() {
        return hasDomChild(Attribute, null, "format");
    }

    public SchemaToken newformat() {
        return new SchemaToken();
    }

    public SchemaToken getformatAt(int index) throws Exception {
        return new SchemaToken(getDomNodeValue(getDomChildAt(Attribute, null, "format", index)));
    }

    public org.w3c.dom.Node getStartingformatCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "format");
    }

    public org.w3c.dom.Node getAdvancedformatCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "format", curNode);
    }

    public SchemaToken getformatValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new SchemaToken(getDomNodeValue(curNode));
    }

    public SchemaToken getformat() throws Exception {
        return getformatAt(0);
    }

    public void removeformatAt(int index) {
        removeDomChildAt(Attribute, null, "format", index);
    }

    public void removeformat() {
        removeformatAt(0);
    }

    public org.w3c.dom.Node addformat(SchemaToken value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "format", value.toString());
    }

    public org.w3c.dom.Node addformat(String value) throws Exception {
        return addformat(new SchemaToken(value));
    }

    public void insertformatAt(SchemaToken value, int index) {
        insertDomChildAt(Attribute, null, "format", index, value.toString());
    }

    public void insertformatAt(String value, int index) throws Exception {
        insertformatAt(new SchemaToken(value), index);
    }

    public void replaceformatAt(SchemaToken value, int index) {
        replaceDomChildAt(Attribute, null, "format", index, value.toString());
    }

    public void replaceformatAt(String value, int index) throws Exception {
        replaceformatAt(new SchemaToken(value), index);
    }

    public static int getheightMinCount() {
        return 0;
    }

    public static int getheightMaxCount() {
        return 1;
    }

    public int getheightCount() {
        return getDomChildCount(Attribute, null, "height");
    }

    public boolean hasheight() {
        return hasDomChild(Attribute, null, "height");
    }

    public uint newheight() {
        return new uint();
    }

    public uint getheightAt(int index) throws Exception {
        return new uint(getDomNodeValue(getDomChildAt(Attribute, null, "height", index)));
    }

    public org.w3c.dom.Node getStartingheightCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "height");
    }

    public org.w3c.dom.Node getAdvancedheightCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "height", curNode);
    }

    public uint getheightValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new uint(getDomNodeValue(curNode));
    }

    public uint getheight() throws Exception {
        return getheightAt(0);
    }

    public void removeheightAt(int index) {
        removeDomChildAt(Attribute, null, "height", index);
    }

    public void removeheight() {
        removeheightAt(0);
    }

    public org.w3c.dom.Node addheight(uint value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "height", value.toString());
    }

    public org.w3c.dom.Node addheight(String value) throws Exception {
        return addheight(new uint(value));
    }

    public void insertheightAt(uint value, int index) {
        insertDomChildAt(Attribute, null, "height", index, value.toString());
    }

    public void insertheightAt(String value, int index) throws Exception {
        insertheightAt(new uint(value), index);
    }

    public void replaceheightAt(uint value, int index) {
        replaceDomChildAt(Attribute, null, "height", index, value.toString());
    }

    public void replaceheightAt(String value, int index) throws Exception {
        replaceheightAt(new uint(value), index);
    }

    public static int getwidthMinCount() {
        return 0;
    }

    public static int getwidthMaxCount() {
        return 1;
    }

    public int getwidthCount() {
        return getDomChildCount(Attribute, null, "width");
    }

    public boolean haswidth() {
        return hasDomChild(Attribute, null, "width");
    }

    public uint newwidth() {
        return new uint();
    }

    public uint getwidthAt(int index) throws Exception {
        return new uint(getDomNodeValue(getDomChildAt(Attribute, null, "width", index)));
    }

    public org.w3c.dom.Node getStartingwidthCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "width");
    }

    public org.w3c.dom.Node getAdvancedwidthCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "width", curNode);
    }

    public uint getwidthValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new uint(getDomNodeValue(curNode));
    }

    public uint getwidth() throws Exception {
        return getwidthAt(0);
    }

    public void removewidthAt(int index) {
        removeDomChildAt(Attribute, null, "width", index);
    }

    public void removewidth() {
        removewidthAt(0);
    }

    public org.w3c.dom.Node addwidth(uint value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "width", value.toString());
    }

    public org.w3c.dom.Node addwidth(String value) throws Exception {
        return addwidth(new uint(value));
    }

    public void insertwidthAt(uint value, int index) {
        insertDomChildAt(Attribute, null, "width", index, value.toString());
    }

    public void insertwidthAt(String value, int index) throws Exception {
        insertwidthAt(new uint(value), index);
    }

    public void replacewidthAt(uint value, int index) {
        replaceDomChildAt(Attribute, null, "width", index, value.toString());
    }

    public void replacewidthAt(String value, int index) throws Exception {
        replacewidthAt(new uint(value), index);
    }

    public static int getdepthMinCount() {
        return 0;
    }

    public static int getdepthMaxCount() {
        return 1;
    }

    public int getdepthCount() {
        return getDomChildCount(Attribute, null, "depth");
    }

    public boolean hasdepth() {
        return hasDomChild(Attribute, null, "depth");
    }

    public uint newdepth() {
        return new uint();
    }

    public uint getdepthAt(int index) throws Exception {
        return new uint(getDomNodeValue(getDomChildAt(Attribute, null, "depth", index)));
    }

    public org.w3c.dom.Node getStartingdepthCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "depth");
    }

    public org.w3c.dom.Node getAdvanceddepthCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "depth", curNode);
    }

    public uint getdepthValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new uint(getDomNodeValue(curNode));
    }

    public uint getdepth() throws Exception {
        return getdepthAt(0);
    }

    public void removedepthAt(int index) {
        removeDomChildAt(Attribute, null, "depth", index);
    }

    public void removedepth() {
        removedepthAt(0);
    }

    public org.w3c.dom.Node adddepth(uint value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "depth", value.toString());
    }

    public org.w3c.dom.Node adddepth(String value) throws Exception {
        return adddepth(new uint(value));
    }

    public void insertdepthAt(uint value, int index) {
        insertDomChildAt(Attribute, null, "depth", index, value.toString());
    }

    public void insertdepthAt(String value, int index) throws Exception {
        insertdepthAt(new uint(value), index);
    }

    public void replacedepthAt(uint value, int index) {
        replaceDomChildAt(Attribute, null, "depth", index, value.toString());
    }

    public void replacedepthAt(String value, int index) throws Exception {
        replacedepthAt(new uint(value), index);
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

    public static int getdataMinCount() {
        return 1;
    }

    public static int getdataMaxCount() {
        return 1;
    }

    public int getdataCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "data");
    }

    public boolean hasdata() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "data");
    }

    public ListOfHexBinary newdata() {
        return new ListOfHexBinary();
    }

    public ListOfHexBinary getdataAt(int index) throws Exception {
        return new ListOfHexBinary(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "data", index)));
    }

    public org.w3c.dom.Node getStartingdataCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "data");
    }

    public org.w3c.dom.Node getAdvanceddataCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "data", curNode);
    }

    public ListOfHexBinary getdataValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new ListOfHexBinary(getDomNodeValue(curNode));
    }

    public ListOfHexBinary getdata() throws Exception {
        return getdataAt(0);
    }

    public void removedataAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "data", index);
    }

    public void removedata() {
        removedataAt(0);
    }

    public org.w3c.dom.Node adddata(ListOfHexBinary value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "data", value.toString());
    }

    public org.w3c.dom.Node adddata(String value) throws Exception {
        return adddata(new ListOfHexBinary(value));
    }

    public void insertdataAt(ListOfHexBinary value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "data", index, value.toString());
    }

    public void insertdataAt(String value, int index) throws Exception {
        insertdataAt(new ListOfHexBinary(value), index);
    }

    public void replacedataAt(ListOfHexBinary value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "data", index, value.toString());
    }

    public void replacedataAt(String value, int index) throws Exception {
        replacedataAt(new ListOfHexBinary(value), index);
    }

    public static int getinit_fromMinCount() {
        return 1;
    }

    public static int getinit_fromMaxCount() {
        return 1;
    }

    public int getinit_fromCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from");
    }

    public boolean hasinit_from() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from");
    }

    public SchemaString newinit_from() {
        return new SchemaString();
    }

    public SchemaString getinit_fromAt(int index) throws Exception {
        return new SchemaString(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from", index)));
    }

    public org.w3c.dom.Node getStartinginit_fromCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from");
    }

    public org.w3c.dom.Node getAdvancedinit_fromCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from", curNode);
    }

    public SchemaString getinit_fromValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new SchemaString(getDomNodeValue(curNode));
    }

    public SchemaString getinit_from() throws Exception {
        return getinit_fromAt(0);
    }

    public void removeinit_fromAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from", index);
    }

    public void removeinit_from() {
        removeinit_fromAt(0);
    }

    public org.w3c.dom.Node addinit_from(SchemaString value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from", value.toString());
    }

    public org.w3c.dom.Node addinit_from(String value) throws Exception {
        return addinit_from(new SchemaString(value));
    }

    public void insertinit_fromAt(SchemaString value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from", index, value.toString());
    }

    public void insertinit_fromAt(String value, int index) throws Exception {
        insertinit_fromAt(new SchemaString(value), index);
    }

    public void replaceinit_fromAt(SchemaString value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from", index, value.toString());
    }

    public void replaceinit_fromAt(String value, int index) throws Exception {
        replaceinit_fromAt(new SchemaString(value), index);
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
