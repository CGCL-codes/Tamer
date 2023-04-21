package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaID;
import homura.hde.util.xml.types.SchemaNCName;

public class nodeType2 extends homura.hde.util.xml.xml.Node {

    public nodeType2(nodeType2 node) {
        super(node);
    }

    public nodeType2(org.w3c.dom.Node node) {
        super(node);
    }

    public nodeType2(org.w3c.dom.Document doc) {
        super(doc);
    }

    public nodeType2(homura.hde.util.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "id"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "id", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "name"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "name", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "sid"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "sid", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "type"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "type", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "layer"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "layer", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "asset"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "asset", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new assetType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "lookat"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "lookat", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new lookatType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "matrix"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "matrix", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new matrixType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "rotate"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "rotate", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new rotateType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "scale"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "scale", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new TargetableFloat3(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "skew"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "skew", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new skewType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "translate"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "translate", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new TargetableFloat3(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_camera"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_camera", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new InstanceWithExtra(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_controller"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_controller", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new instance_controllerType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_geometry"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_geometry", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new instance_geometryType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_light"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_light", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new InstanceWithExtra(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_node"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_node", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new InstanceWithExtra(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "node"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "node", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new nodeType2(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new extraType(tmpNode).adjustPrefix();
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "node");
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

    public static int getsidMinCount() {
        return 0;
    }

    public static int getsidMaxCount() {
        return 1;
    }

    public int getsidCount() {
        return getDomChildCount(Attribute, null, "sid");
    }

    public boolean hassid() {
        return hasDomChild(Attribute, null, "sid");
    }

    public SchemaNCName newsid() {
        return new SchemaNCName();
    }

    public SchemaNCName getsidAt(int index) throws Exception {
        return new SchemaNCName(getDomNodeValue(getDomChildAt(Attribute, null, "sid", index)));
    }

    public org.w3c.dom.Node getStartingsidCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "sid");
    }

    public org.w3c.dom.Node getAdvancedsidCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "sid", curNode);
    }

    public SchemaNCName getsidValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new SchemaNCName(getDomNodeValue(curNode));
    }

    public SchemaNCName getsid() throws Exception {
        return getsidAt(0);
    }

    public void removesidAt(int index) {
        removeDomChildAt(Attribute, null, "sid", index);
    }

    public void removesid() {
        removesidAt(0);
    }

    public org.w3c.dom.Node addsid(SchemaNCName value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "sid", value.toString());
    }

    public org.w3c.dom.Node addsid(String value) throws Exception {
        return addsid(new SchemaNCName(value));
    }

    public void insertsidAt(SchemaNCName value, int index) {
        insertDomChildAt(Attribute, null, "sid", index, value.toString());
    }

    public void insertsidAt(String value, int index) throws Exception {
        insertsidAt(new SchemaNCName(value), index);
    }

    public void replacesidAt(SchemaNCName value, int index) {
        replaceDomChildAt(Attribute, null, "sid", index, value.toString());
    }

    public void replacesidAt(String value, int index) throws Exception {
        replacesidAt(new SchemaNCName(value), index);
    }

    public static int gettypeMinCount() {
        return 0;
    }

    public static int gettypeMaxCount() {
        return 1;
    }

    public int gettypeCount() {
        return getDomChildCount(Attribute, null, "type");
    }

    public boolean hastype() {
        return hasDomChild(Attribute, null, "type");
    }

    public NodeType newtype() {
        return new NodeType();
    }

    public NodeType gettypeAt(int index) throws Exception {
        return new NodeType(getDomNodeValue(getDomChildAt(Attribute, null, "type", index)));
    }

    public org.w3c.dom.Node getStartingtypeCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "type");
    }

    public org.w3c.dom.Node getAdvancedtypeCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "type", curNode);
    }

    public NodeType gettypeValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new NodeType(getDomNodeValue(curNode));
    }

    public NodeType gettype() throws Exception {
        return gettypeAt(0);
    }

    public void removetypeAt(int index) {
        removeDomChildAt(Attribute, null, "type", index);
    }

    public void removetype() {
        removetypeAt(0);
    }

    public org.w3c.dom.Node addtype(NodeType value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "type", value.toString());
    }

    public org.w3c.dom.Node addtype(String value) throws Exception {
        return addtype(new NodeType(value));
    }

    public void inserttypeAt(NodeType value, int index) {
        insertDomChildAt(Attribute, null, "type", index, value.toString());
    }

    public void inserttypeAt(String value, int index) throws Exception {
        inserttypeAt(new NodeType(value), index);
    }

    public void replacetypeAt(NodeType value, int index) {
        replaceDomChildAt(Attribute, null, "type", index, value.toString());
    }

    public void replacetypeAt(String value, int index) throws Exception {
        replacetypeAt(new NodeType(value), index);
    }

    public static int getlayerMinCount() {
        return 0;
    }

    public static int getlayerMaxCount() {
        return 1;
    }

    public int getlayerCount() {
        return getDomChildCount(Attribute, null, "layer");
    }

    public boolean haslayer() {
        return hasDomChild(Attribute, null, "layer");
    }

    public ListOfNames newlayer() {
        return new ListOfNames();
    }

    public ListOfNames getlayerAt(int index) throws Exception {
        return new ListOfNames(getDomNodeValue(getDomChildAt(Attribute, null, "layer", index)));
    }

    public org.w3c.dom.Node getStartinglayerCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "layer");
    }

    public org.w3c.dom.Node getAdvancedlayerCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "layer", curNode);
    }

    public ListOfNames getlayerValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new ListOfNames(getDomNodeValue(curNode));
    }

    public ListOfNames getlayer() throws Exception {
        return getlayerAt(0);
    }

    public void removelayerAt(int index) {
        removeDomChildAt(Attribute, null, "layer", index);
    }

    public void removelayer() {
        removelayerAt(0);
    }

    public org.w3c.dom.Node addlayer(ListOfNames value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "layer", value.toString());
    }

    public org.w3c.dom.Node addlayer(String value) throws Exception {
        return addlayer(new ListOfNames(value));
    }

    public void insertlayerAt(ListOfNames value, int index) {
        insertDomChildAt(Attribute, null, "layer", index, value.toString());
    }

    public void insertlayerAt(String value, int index) throws Exception {
        insertlayerAt(new ListOfNames(value), index);
    }

    public void replacelayerAt(ListOfNames value, int index) {
        replaceDomChildAt(Attribute, null, "layer", index, value.toString());
    }

    public void replacelayerAt(String value, int index) throws Exception {
        replacelayerAt(new ListOfNames(value), index);
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

    public static int getlookatMinCount() {
        return 1;
    }

    public static int getlookatMaxCount() {
        return 1;
    }

    public int getlookatCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "lookat");
    }

    public boolean haslookat() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "lookat");
    }

    public lookatType newlookat() {
        return new lookatType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "lookat"));
    }

    public lookatType getlookatAt(int index) throws Exception {
        return new lookatType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "lookat", index));
    }

    public org.w3c.dom.Node getStartinglookatCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "lookat");
    }

    public org.w3c.dom.Node getAdvancedlookatCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "lookat", curNode);
    }

    public lookatType getlookatValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new lookatType(curNode);
    }

    public lookatType getlookat() throws Exception {
        return getlookatAt(0);
    }

    public void removelookatAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "lookat", index);
    }

    public void removelookat() {
        removelookatAt(0);
    }

    public org.w3c.dom.Node addlookat(lookatType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "lookat", value);
    }

    public void insertlookatAt(lookatType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "lookat", index, value);
    }

    public void replacelookatAt(lookatType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "lookat", index, value);
    }

    public static int getmatrixMinCount() {
        return 1;
    }

    public static int getmatrixMaxCount() {
        return 1;
    }

    public int getmatrixCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "matrix");
    }

    public boolean hasmatrix() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "matrix");
    }

    public matrixType newmatrix() {
        return new matrixType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "matrix"));
    }

    public matrixType getmatrixAt(int index) throws Exception {
        return new matrixType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "matrix", index));
    }

    public org.w3c.dom.Node getStartingmatrixCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "matrix");
    }

    public org.w3c.dom.Node getAdvancedmatrixCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "matrix", curNode);
    }

    public matrixType getmatrixValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new matrixType(curNode);
    }

    public matrixType getmatrix() throws Exception {
        return getmatrixAt(0);
    }

    public void removematrixAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "matrix", index);
    }

    public void removematrix() {
        removematrixAt(0);
    }

    public org.w3c.dom.Node addmatrix(matrixType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "matrix", value);
    }

    public void insertmatrixAt(matrixType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "matrix", index, value);
    }

    public void replacematrixAt(matrixType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "matrix", index, value);
    }

    public static int getrotateMinCount() {
        return 1;
    }

    public static int getrotateMaxCount() {
        return 1;
    }

    public int getrotateCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "rotate");
    }

    public boolean hasrotate() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "rotate");
    }

    public rotateType newrotate() {
        return new rotateType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "rotate"));
    }

    public rotateType getrotateAt(int index) throws Exception {
        return new rotateType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "rotate", index));
    }

    public org.w3c.dom.Node getStartingrotateCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "rotate");
    }

    public org.w3c.dom.Node getAdvancedrotateCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "rotate", curNode);
    }

    public rotateType getrotateValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new rotateType(curNode);
    }

    public rotateType getrotate() throws Exception {
        return getrotateAt(0);
    }

    public void removerotateAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "rotate", index);
    }

    public void removerotate() {
        removerotateAt(0);
    }

    public org.w3c.dom.Node addrotate(rotateType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "rotate", value);
    }

    public void insertrotateAt(rotateType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "rotate", index, value);
    }

    public void replacerotateAt(rotateType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "rotate", index, value);
    }

    public static int getscaleMinCount() {
        return 1;
    }

    public static int getscaleMaxCount() {
        return 1;
    }

    public int getscaleCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "scale");
    }

    public boolean hasscale() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "scale");
    }

    public TargetableFloat3 newscale() {
        return new TargetableFloat3(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "scale"));
    }

    public TargetableFloat3 getscaleAt(int index) throws Exception {
        return new TargetableFloat3(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "scale", index));
    }

    public org.w3c.dom.Node getStartingscaleCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "scale");
    }

    public org.w3c.dom.Node getAdvancedscaleCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "scale", curNode);
    }

    public TargetableFloat3 getscaleValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new TargetableFloat3(curNode);
    }

    public TargetableFloat3 getscale() throws Exception {
        return getscaleAt(0);
    }

    public void removescaleAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "scale", index);
    }

    public void removescale() {
        removescaleAt(0);
    }

    public org.w3c.dom.Node addscale(TargetableFloat3 value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "scale", value);
    }

    public void insertscaleAt(TargetableFloat3 value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "scale", index, value);
    }

    public void replacescaleAt(TargetableFloat3 value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "scale", index, value);
    }

    public static int getskewMinCount() {
        return 1;
    }

    public static int getskewMaxCount() {
        return 1;
    }

    public int getskewCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "skew");
    }

    public boolean hasskew() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "skew");
    }

    public skewType newskew() {
        return new skewType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "skew"));
    }

    public skewType getskewAt(int index) throws Exception {
        return new skewType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "skew", index));
    }

    public org.w3c.dom.Node getStartingskewCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "skew");
    }

    public org.w3c.dom.Node getAdvancedskewCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "skew", curNode);
    }

    public skewType getskewValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new skewType(curNode);
    }

    public skewType getskew() throws Exception {
        return getskewAt(0);
    }

    public void removeskewAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "skew", index);
    }

    public void removeskew() {
        removeskewAt(0);
    }

    public org.w3c.dom.Node addskew(skewType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "skew", value);
    }

    public void insertskewAt(skewType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "skew", index, value);
    }

    public void replaceskewAt(skewType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "skew", index, value);
    }

    public static int gettranslateMinCount() {
        return 1;
    }

    public static int gettranslateMaxCount() {
        return 1;
    }

    public int gettranslateCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "translate");
    }

    public boolean hastranslate() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "translate");
    }

    public TargetableFloat3 newtranslate() {
        return new TargetableFloat3(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "translate"));
    }

    public TargetableFloat3 gettranslateAt(int index) throws Exception {
        return new TargetableFloat3(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "translate", index));
    }

    public org.w3c.dom.Node getStartingtranslateCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "translate");
    }

    public org.w3c.dom.Node getAdvancedtranslateCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "translate", curNode);
    }

    public TargetableFloat3 gettranslateValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new TargetableFloat3(curNode);
    }

    public TargetableFloat3 gettranslate() throws Exception {
        return gettranslateAt(0);
    }

    public void removetranslateAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "translate", index);
    }

    public void removetranslate() {
        removetranslateAt(0);
    }

    public org.w3c.dom.Node addtranslate(TargetableFloat3 value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "translate", value);
    }

    public void inserttranslateAt(TargetableFloat3 value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "translate", index, value);
    }

    public void replacetranslateAt(TargetableFloat3 value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "translate", index, value);
    }

    public static int getinstance_cameraMinCount() {
        return 0;
    }

    public static int getinstance_cameraMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int getinstance_cameraCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_camera");
    }

    public boolean hasinstance_camera() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_camera");
    }

    public InstanceWithExtra newinstance_camera() {
        return new InstanceWithExtra(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "instance_camera"));
    }

    public InstanceWithExtra getinstance_cameraAt(int index) throws Exception {
        return new InstanceWithExtra(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_camera", index));
    }

    public org.w3c.dom.Node getStartinginstance_cameraCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_camera");
    }

    public org.w3c.dom.Node getAdvancedinstance_cameraCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_camera", curNode);
    }

    public InstanceWithExtra getinstance_cameraValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new InstanceWithExtra(curNode);
    }

    public InstanceWithExtra getinstance_camera() throws Exception {
        return getinstance_cameraAt(0);
    }

    public void removeinstance_cameraAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_camera", index);
    }

    public void removeinstance_camera() {
        while (hasinstance_camera()) removeinstance_cameraAt(0);
    }

    public org.w3c.dom.Node addinstance_camera(InstanceWithExtra value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "instance_camera", value);
    }

    public void insertinstance_cameraAt(InstanceWithExtra value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "instance_camera", index, value);
    }

    public void replaceinstance_cameraAt(InstanceWithExtra value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "instance_camera", index, value);
    }

    public static int getinstance_controllerMinCount() {
        return 0;
    }

    public static int getinstance_controllerMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int getinstance_controllerCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_controller");
    }

    public boolean hasinstance_controller() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_controller");
    }

    public instance_controllerType newinstance_controller() {
        return new instance_controllerType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "instance_controller"));
    }

    public instance_controllerType getinstance_controllerAt(int index) throws Exception {
        return new instance_controllerType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_controller", index));
    }

    public org.w3c.dom.Node getStartinginstance_controllerCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_controller");
    }

    public org.w3c.dom.Node getAdvancedinstance_controllerCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_controller", curNode);
    }

    public instance_controllerType getinstance_controllerValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new instance_controllerType(curNode);
    }

    public instance_controllerType getinstance_controller() throws Exception {
        return getinstance_controllerAt(0);
    }

    public void removeinstance_controllerAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_controller", index);
    }

    public void removeinstance_controller() {
        while (hasinstance_controller()) removeinstance_controllerAt(0);
    }

    public org.w3c.dom.Node addinstance_controller(instance_controllerType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "instance_controller", value);
    }

    public void insertinstance_controllerAt(instance_controllerType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "instance_controller", index, value);
    }

    public void replaceinstance_controllerAt(instance_controllerType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "instance_controller", index, value);
    }

    public static int getinstance_geometryMinCount() {
        return 0;
    }

    public static int getinstance_geometryMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int getinstance_geometryCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_geometry");
    }

    public boolean hasinstance_geometry() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_geometry");
    }

    public instance_geometryType newinstance_geometry() {
        return new instance_geometryType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "instance_geometry"));
    }

    public instance_geometryType getinstance_geometryAt(int index) throws Exception {
        return new instance_geometryType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_geometry", index));
    }

    public org.w3c.dom.Node getStartinginstance_geometryCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_geometry");
    }

    public org.w3c.dom.Node getAdvancedinstance_geometryCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_geometry", curNode);
    }

    public instance_geometryType getinstance_geometryValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new instance_geometryType(curNode);
    }

    public instance_geometryType getinstance_geometry() throws Exception {
        return getinstance_geometryAt(0);
    }

    public void removeinstance_geometryAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_geometry", index);
    }

    public void removeinstance_geometry() {
        while (hasinstance_geometry()) removeinstance_geometryAt(0);
    }

    public org.w3c.dom.Node addinstance_geometry(instance_geometryType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "instance_geometry", value);
    }

    public void insertinstance_geometryAt(instance_geometryType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "instance_geometry", index, value);
    }

    public void replaceinstance_geometryAt(instance_geometryType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "instance_geometry", index, value);
    }

    public static int getinstance_lightMinCount() {
        return 0;
    }

    public static int getinstance_lightMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int getinstance_lightCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_light");
    }

    public boolean hasinstance_light() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_light");
    }

    public InstanceWithExtra newinstance_light() {
        return new InstanceWithExtra(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "instance_light"));
    }

    public InstanceWithExtra getinstance_lightAt(int index) throws Exception {
        return new InstanceWithExtra(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_light", index));
    }

    public org.w3c.dom.Node getStartinginstance_lightCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_light");
    }

    public org.w3c.dom.Node getAdvancedinstance_lightCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_light", curNode);
    }

    public InstanceWithExtra getinstance_lightValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new InstanceWithExtra(curNode);
    }

    public InstanceWithExtra getinstance_light() throws Exception {
        return getinstance_lightAt(0);
    }

    public void removeinstance_lightAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_light", index);
    }

    public void removeinstance_light() {
        while (hasinstance_light()) removeinstance_lightAt(0);
    }

    public org.w3c.dom.Node addinstance_light(InstanceWithExtra value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "instance_light", value);
    }

    public void insertinstance_lightAt(InstanceWithExtra value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "instance_light", index, value);
    }

    public void replaceinstance_lightAt(InstanceWithExtra value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "instance_light", index, value);
    }

    public static int getinstance_nodeMinCount() {
        return 0;
    }

    public static int getinstance_nodeMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int getinstance_nodeCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_node");
    }

    public boolean hasinstance_node() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_node");
    }

    public InstanceWithExtra newinstance_node() {
        return new InstanceWithExtra(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "instance_node"));
    }

    public InstanceWithExtra getinstance_nodeAt(int index) throws Exception {
        return new InstanceWithExtra(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_node", index));
    }

    public org.w3c.dom.Node getStartinginstance_nodeCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_node");
    }

    public org.w3c.dom.Node getAdvancedinstance_nodeCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_node", curNode);
    }

    public InstanceWithExtra getinstance_nodeValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new InstanceWithExtra(curNode);
    }

    public InstanceWithExtra getinstance_node() throws Exception {
        return getinstance_nodeAt(0);
    }

    public void removeinstance_nodeAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_node", index);
    }

    public void removeinstance_node() {
        while (hasinstance_node()) removeinstance_nodeAt(0);
    }

    public org.w3c.dom.Node addinstance_node(InstanceWithExtra value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "instance_node", value);
    }

    public void insertinstance_nodeAt(InstanceWithExtra value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "instance_node", index, value);
    }

    public void replaceinstance_nodeAt(InstanceWithExtra value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "instance_node", index, value);
    }

    public static int getnodeMinCount() {
        return 0;
    }

    public static int getnodeMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int getnodeCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "node");
    }

    public boolean hasnode() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "node");
    }

    public nodeType2 newnode() {
        return new nodeType2(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "node"));
    }

    public nodeType2 getnodeAt(int index) throws Exception {
        return new nodeType2(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "node", index));
    }

    public org.w3c.dom.Node getStartingnodeCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "node");
    }

    public org.w3c.dom.Node getAdvancednodeCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "node", curNode);
    }

    public nodeType2 getnodeValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new nodeType2(curNode);
    }

    public nodeType2 getnode() throws Exception {
        return getnodeAt(0);
    }

    public void removenodeAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "node", index);
    }

    public void removenode() {
        while (hasnode()) removenodeAt(0);
    }

    public org.w3c.dom.Node addnode(nodeType2 value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "node", value);
    }

    public void insertnodeAt(nodeType2 value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "node", index, value);
    }

    public void replacenodeAt(nodeType2 value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "node", index, value);
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
