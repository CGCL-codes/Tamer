package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaNCName;
import homura.hde.util.xml.types.SchemaNMToken;
import homura.hde.util.xml.types.SchemaString;
import homura.hde.util.xml.types.SchemaType;

public class paramType3 extends homura.hde.util.xml.xml.Node {

    public paramType3(paramType3 node) {
        super(node);
    }

    public paramType3(org.w3c.dom.Node node) {
        super(node);
    }

    public paramType3(org.w3c.dom.Document doc) {
        super(doc);
    }

    public paramType3(homura.hde.util.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public SchemaString getValue() {
        return new SchemaString(getDomNodeValue(domNode));
    }

    public void setValue(SchemaType value) {
        setDomNodeValue(domNode, value.toString());
    }

    public void assign(SchemaType value) {
        setValue(value);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "name"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "name", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "sid"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "sid", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "semantic"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "semantic", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "type"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "type", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "param");
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

    public static int getsemanticMinCount() {
        return 0;
    }

    public static int getsemanticMaxCount() {
        return 1;
    }

    public int getsemanticCount() {
        return getDomChildCount(Attribute, null, "semantic");
    }

    public boolean hassemantic() {
        return hasDomChild(Attribute, null, "semantic");
    }

    public SchemaNMToken newsemantic() {
        return new SchemaNMToken();
    }

    public SchemaNMToken getsemanticAt(int index) throws Exception {
        return new SchemaNMToken(getDomNodeValue(getDomChildAt(Attribute, null, "semantic", index)));
    }

    public org.w3c.dom.Node getStartingsemanticCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "semantic");
    }

    public org.w3c.dom.Node getAdvancedsemanticCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "semantic", curNode);
    }

    public SchemaNMToken getsemanticValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new SchemaNMToken(getDomNodeValue(curNode));
    }

    public SchemaNMToken getsemantic() throws Exception {
        return getsemanticAt(0);
    }

    public void removesemanticAt(int index) {
        removeDomChildAt(Attribute, null, "semantic", index);
    }

    public void removesemantic() {
        removesemanticAt(0);
    }

    public org.w3c.dom.Node addsemantic(SchemaNMToken value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "semantic", value.toString());
    }

    public org.w3c.dom.Node addsemantic(String value) throws Exception {
        return addsemantic(new SchemaNMToken(value));
    }

    public void insertsemanticAt(SchemaNMToken value, int index) {
        insertDomChildAt(Attribute, null, "semantic", index, value.toString());
    }

    public void insertsemanticAt(String value, int index) throws Exception {
        insertsemanticAt(new SchemaNMToken(value), index);
    }

    public void replacesemanticAt(SchemaNMToken value, int index) {
        replaceDomChildAt(Attribute, null, "semantic", index, value.toString());
    }

    public void replacesemanticAt(String value, int index) throws Exception {
        replacesemanticAt(new SchemaNMToken(value), index);
    }

    public static int gettypeMinCount() {
        return 1;
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

    public SchemaNMToken newtype() {
        return new SchemaNMToken();
    }

    public SchemaNMToken gettypeAt(int index) throws Exception {
        return new SchemaNMToken(getDomNodeValue(getDomChildAt(Attribute, null, "type", index)));
    }

    public org.w3c.dom.Node getStartingtypeCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "type");
    }

    public org.w3c.dom.Node getAdvancedtypeCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "type", curNode);
    }

    public SchemaNMToken gettypeValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new SchemaNMToken(getDomNodeValue(curNode));
    }

    public SchemaNMToken gettype() throws Exception {
        return gettypeAt(0);
    }

    public void removetypeAt(int index) {
        removeDomChildAt(Attribute, null, "type", index);
    }

    public void removetype() {
        removetypeAt(0);
    }

    public org.w3c.dom.Node addtype(SchemaNMToken value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "type", value.toString());
    }

    public org.w3c.dom.Node addtype(String value) throws Exception {
        return addtype(new SchemaNMToken(value));
    }

    public void inserttypeAt(SchemaNMToken value, int index) {
        insertDomChildAt(Attribute, null, "type", index, value.toString());
    }

    public void inserttypeAt(String value, int index) throws Exception {
        inserttypeAt(new SchemaNMToken(value), index);
    }

    public void replacetypeAt(SchemaNMToken value, int index) {
        replaceDomChildAt(Attribute, null, "type", index, value.toString());
    }

    public void replacetypeAt(String value, int index) throws Exception {
        replacetypeAt(new SchemaNMToken(value), index);
    }
}
