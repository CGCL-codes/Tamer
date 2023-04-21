package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaNCName;

public class alphaType extends homura.hde.util.xml.xml.Node {

    public alphaType(alphaType node) {
        super(node);
    }

    public alphaType(org.w3c.dom.Node node) {
        super(node);
    }

    public alphaType(org.w3c.dom.Document doc) {
        super(doc);
    }

    public alphaType(homura.hde.util.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "value"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "value", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "param"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "param", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "alpha");
    }

    public static int getvalue2MinCount() {
        return 0;
    }

    public static int getvalue2MaxCount() {
        return 1;
    }

    public int getvalue2Count() {
        return getDomChildCount(Attribute, null, "value");
    }

    public boolean hasvalue2() {
        return hasDomChild(Attribute, null, "value");
    }

    public gl_blend_equation_type newvalue2() {
        return new gl_blend_equation_type();
    }

    public gl_blend_equation_type getvalue2At(int index) throws Exception {
        return new gl_blend_equation_type(getDomNodeValue(getDomChildAt(Attribute, null, "value", index)));
    }

    public org.w3c.dom.Node getStartingvalue2Cursor() throws Exception {
        return getDomFirstChild(Attribute, null, "value");
    }

    public org.w3c.dom.Node getAdvancedvalue2Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "value", curNode);
    }

    public gl_blend_equation_type getvalue2ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new gl_blend_equation_type(getDomNodeValue(curNode));
    }

    public gl_blend_equation_type getvalue2() throws Exception {
        return getvalue2At(0);
    }

    public void removevalue2At(int index) {
        removeDomChildAt(Attribute, null, "value", index);
    }

    public void removevalue2() {
        removevalue2At(0);
    }

    public org.w3c.dom.Node addvalue2(gl_blend_equation_type value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "value", value.toString());
    }

    public org.w3c.dom.Node addvalue2(String value) throws Exception {
        return addvalue2(new gl_blend_equation_type(value));
    }

    public void insertvalue2At(gl_blend_equation_type value, int index) {
        insertDomChildAt(Attribute, null, "value", index, value.toString());
    }

    public void insertvalue2At(String value, int index) throws Exception {
        insertvalue2At(new gl_blend_equation_type(value), index);
    }

    public void replacevalue2At(gl_blend_equation_type value, int index) {
        replaceDomChildAt(Attribute, null, "value", index, value.toString());
    }

    public void replacevalue2At(String value, int index) throws Exception {
        replacevalue2At(new gl_blend_equation_type(value), index);
    }

    public static int getparamMinCount() {
        return 0;
    }

    public static int getparamMaxCount() {
        return 1;
    }

    public int getparamCount() {
        return getDomChildCount(Attribute, null, "param");
    }

    public boolean hasparam() {
        return hasDomChild(Attribute, null, "param");
    }

    public SchemaNCName newparam() {
        return new SchemaNCName();
    }

    public SchemaNCName getparamAt(int index) throws Exception {
        return new SchemaNCName(getDomNodeValue(getDomChildAt(Attribute, null, "param", index)));
    }

    public org.w3c.dom.Node getStartingparamCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "param");
    }

    public org.w3c.dom.Node getAdvancedparamCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "param", curNode);
    }

    public SchemaNCName getparamValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new SchemaNCName(getDomNodeValue(curNode));
    }

    public SchemaNCName getparam() throws Exception {
        return getparamAt(0);
    }

    public void removeparamAt(int index) {
        removeDomChildAt(Attribute, null, "param", index);
    }

    public void removeparam() {
        removeparamAt(0);
    }

    public org.w3c.dom.Node addparam(SchemaNCName value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "param", value.toString());
    }

    public org.w3c.dom.Node addparam(String value) throws Exception {
        return addparam(new SchemaNCName(value));
    }

    public void insertparamAt(SchemaNCName value, int index) {
        insertDomChildAt(Attribute, null, "param", index, value.toString());
    }

    public void insertparamAt(String value, int index) throws Exception {
        insertparamAt(new SchemaNCName(value), index);
    }

    public void replaceparamAt(SchemaNCName value, int index) {
        replaceDomChildAt(Attribute, null, "param", index, value.toString());
    }

    public void replaceparamAt(String value, int index) throws Exception {
        replaceparamAt(new SchemaNCName(value), index);
    }
}
