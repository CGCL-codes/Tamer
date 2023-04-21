package homura.hde.ext.model.collada.schema;

public class gles_texcombiner_command_type extends homura.hde.util.xml.xml.Node {

    public gles_texcombiner_command_type(gles_texcombiner_command_type node) {
        super(node);
    }

    public gles_texcombiner_command_type(org.w3c.dom.Node node) {
        super(node);
    }

    public gles_texcombiner_command_type(org.w3c.dom.Document doc) {
        super(doc);
    }

    public gles_texcombiner_command_type(homura.hde.util.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "constant"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "constant", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new gles_texture_constant_type(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "RGB"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "RGB", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new gles_texcombiner_commandRGB_type(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "alpha"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "alpha", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new gles_texcombiner_commandAlpha_type(tmpNode).adjustPrefix();
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "gles_texcombiner_command_type");
    }

    public static int getconstantMinCount() {
        return 0;
    }

    public static int getconstantMaxCount() {
        return 1;
    }

    public int getconstantCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "constant");
    }

    public boolean hasconstant() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "constant");
    }

    public gles_texture_constant_type newconstant() {
        return new gles_texture_constant_type(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "constant"));
    }

    public gles_texture_constant_type getconstantAt(int index) throws Exception {
        return new gles_texture_constant_type(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "constant", index));
    }

    public org.w3c.dom.Node getStartingconstantCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "constant");
    }

    public org.w3c.dom.Node getAdvancedconstantCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "constant", curNode);
    }

    public gles_texture_constant_type getconstantValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new gles_texture_constant_type(curNode);
    }

    public gles_texture_constant_type getconstant() throws Exception {
        return getconstantAt(0);
    }

    public void removeconstantAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "constant", index);
    }

    public void removeconstant() {
        removeconstantAt(0);
    }

    public org.w3c.dom.Node addconstant(gles_texture_constant_type value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "constant", value);
    }

    public void insertconstantAt(gles_texture_constant_type value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "constant", index, value);
    }

    public void replaceconstantAt(gles_texture_constant_type value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "constant", index, value);
    }

    public static int getRGBMinCount() {
        return 0;
    }

    public static int getRGBMaxCount() {
        return 1;
    }

    public int getRGBCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "RGB");
    }

    public boolean hasRGB() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "RGB");
    }

    public gles_texcombiner_commandRGB_type newRGB() {
        return new gles_texcombiner_commandRGB_type(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "RGB"));
    }

    public gles_texcombiner_commandRGB_type getRGBAt(int index) throws Exception {
        return new gles_texcombiner_commandRGB_type(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "RGB", index));
    }

    public org.w3c.dom.Node getStartingRGBCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "RGB");
    }

    public org.w3c.dom.Node getAdvancedRGBCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "RGB", curNode);
    }

    public gles_texcombiner_commandRGB_type getRGBValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new gles_texcombiner_commandRGB_type(curNode);
    }

    public gles_texcombiner_commandRGB_type getRGB() throws Exception {
        return getRGBAt(0);
    }

    public void removeRGBAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "RGB", index);
    }

    public void removeRGB() {
        removeRGBAt(0);
    }

    public org.w3c.dom.Node addRGB(gles_texcombiner_commandRGB_type value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "RGB", value);
    }

    public void insertRGBAt(gles_texcombiner_commandRGB_type value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "RGB", index, value);
    }

    public void replaceRGBAt(gles_texcombiner_commandRGB_type value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "RGB", index, value);
    }

    public static int getalphaMinCount() {
        return 0;
    }

    public static int getalphaMaxCount() {
        return 1;
    }

    public int getalphaCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "alpha");
    }

    public boolean hasalpha() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "alpha");
    }

    public gles_texcombiner_commandAlpha_type newalpha() {
        return new gles_texcombiner_commandAlpha_type(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "alpha"));
    }

    public gles_texcombiner_commandAlpha_type getalphaAt(int index) throws Exception {
        return new gles_texcombiner_commandAlpha_type(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "alpha", index));
    }

    public org.w3c.dom.Node getStartingalphaCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "alpha");
    }

    public org.w3c.dom.Node getAdvancedalphaCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "alpha", curNode);
    }

    public gles_texcombiner_commandAlpha_type getalphaValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new gles_texcombiner_commandAlpha_type(curNode);
    }

    public gles_texcombiner_commandAlpha_type getalpha() throws Exception {
        return getalphaAt(0);
    }

    public void removealphaAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "alpha", index);
    }

    public void removealpha() {
        removealphaAt(0);
    }

    public org.w3c.dom.Node addalpha(gles_texcombiner_commandAlpha_type value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "alpha", value);
    }

    public void insertalphaAt(gles_texcombiner_commandAlpha_type value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "alpha", index, value);
    }

    public void replacealphaAt(gles_texcombiner_commandAlpha_type value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "alpha", index, value);
    }
}
