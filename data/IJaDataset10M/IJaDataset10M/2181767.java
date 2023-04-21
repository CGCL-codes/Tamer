package homura.hde.ext.model.collada.schema;

public class lambertType extends homura.hde.util.xml.xml.Node {

    public lambertType(lambertType node) {
        super(node);
    }

    public lambertType(org.w3c.dom.Node node) {
        super(node);
    }

    public lambertType(org.w3c.dom.Document doc) {
        super(doc);
    }

    public lambertType(homura.hde.util.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "emission"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "emission", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new common_color_or_texture_type(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "ambient"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "ambient", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new common_color_or_texture_type(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "diffuse"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "diffuse", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new common_color_or_texture_type(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "reflective"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "reflective", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new common_color_or_texture_type(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "reflectivity"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "reflectivity", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new common_float_or_param_type(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "transparent"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "transparent", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new common_transparent_type(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "transparency"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "transparency", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new common_float_or_param_type(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "index_of_refraction"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "index_of_refraction", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new common_float_or_param_type(tmpNode).adjustPrefix();
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "lambert");
    }

    public static int getemissionMinCount() {
        return 0;
    }

    public static int getemissionMaxCount() {
        return 1;
    }

    public int getemissionCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "emission");
    }

    public boolean hasemission() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "emission");
    }

    public common_color_or_texture_type newemission() {
        return new common_color_or_texture_type(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "emission"));
    }

    public common_color_or_texture_type getemissionAt(int index) throws Exception {
        return new common_color_or_texture_type(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "emission", index));
    }

    public org.w3c.dom.Node getStartingemissionCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "emission");
    }

    public org.w3c.dom.Node getAdvancedemissionCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "emission", curNode);
    }

    public common_color_or_texture_type getemissionValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new common_color_or_texture_type(curNode);
    }

    public common_color_or_texture_type getemission() throws Exception {
        return getemissionAt(0);
    }

    public void removeemissionAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "emission", index);
    }

    public void removeemission() {
        removeemissionAt(0);
    }

    public org.w3c.dom.Node addemission(common_color_or_texture_type value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "emission", value);
    }

    public void insertemissionAt(common_color_or_texture_type value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "emission", index, value);
    }

    public void replaceemissionAt(common_color_or_texture_type value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "emission", index, value);
    }

    public static int getambientMinCount() {
        return 0;
    }

    public static int getambientMaxCount() {
        return 1;
    }

    public int getambientCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "ambient");
    }

    public boolean hasambient() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "ambient");
    }

    public common_color_or_texture_type newambient() {
        return new common_color_or_texture_type(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "ambient"));
    }

    public common_color_or_texture_type getambientAt(int index) throws Exception {
        return new common_color_or_texture_type(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "ambient", index));
    }

    public org.w3c.dom.Node getStartingambientCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "ambient");
    }

    public org.w3c.dom.Node getAdvancedambientCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "ambient", curNode);
    }

    public common_color_or_texture_type getambientValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new common_color_or_texture_type(curNode);
    }

    public common_color_or_texture_type getambient() throws Exception {
        return getambientAt(0);
    }

    public void removeambientAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "ambient", index);
    }

    public void removeambient() {
        removeambientAt(0);
    }

    public org.w3c.dom.Node addambient(common_color_or_texture_type value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "ambient", value);
    }

    public void insertambientAt(common_color_or_texture_type value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "ambient", index, value);
    }

    public void replaceambientAt(common_color_or_texture_type value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "ambient", index, value);
    }

    public static int getdiffuseMinCount() {
        return 0;
    }

    public static int getdiffuseMaxCount() {
        return 1;
    }

    public int getdiffuseCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "diffuse");
    }

    public boolean hasdiffuse() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "diffuse");
    }

    public common_color_or_texture_type newdiffuse() {
        return new common_color_or_texture_type(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "diffuse"));
    }

    public common_color_or_texture_type getdiffuseAt(int index) throws Exception {
        return new common_color_or_texture_type(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "diffuse", index));
    }

    public org.w3c.dom.Node getStartingdiffuseCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "diffuse");
    }

    public org.w3c.dom.Node getAdvanceddiffuseCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "diffuse", curNode);
    }

    public common_color_or_texture_type getdiffuseValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new common_color_or_texture_type(curNode);
    }

    public common_color_or_texture_type getdiffuse() throws Exception {
        return getdiffuseAt(0);
    }

    public void removediffuseAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "diffuse", index);
    }

    public void removediffuse() {
        removediffuseAt(0);
    }

    public org.w3c.dom.Node adddiffuse(common_color_or_texture_type value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "diffuse", value);
    }

    public void insertdiffuseAt(common_color_or_texture_type value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "diffuse", index, value);
    }

    public void replacediffuseAt(common_color_or_texture_type value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "diffuse", index, value);
    }

    public static int getreflectiveMinCount() {
        return 0;
    }

    public static int getreflectiveMaxCount() {
        return 1;
    }

    public int getreflectiveCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "reflective");
    }

    public boolean hasreflective() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "reflective");
    }

    public common_color_or_texture_type newreflective() {
        return new common_color_or_texture_type(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "reflective"));
    }

    public common_color_or_texture_type getreflectiveAt(int index) throws Exception {
        return new common_color_or_texture_type(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "reflective", index));
    }

    public org.w3c.dom.Node getStartingreflectiveCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "reflective");
    }

    public org.w3c.dom.Node getAdvancedreflectiveCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "reflective", curNode);
    }

    public common_color_or_texture_type getreflectiveValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new common_color_or_texture_type(curNode);
    }

    public common_color_or_texture_type getreflective() throws Exception {
        return getreflectiveAt(0);
    }

    public void removereflectiveAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "reflective", index);
    }

    public void removereflective() {
        removereflectiveAt(0);
    }

    public org.w3c.dom.Node addreflective(common_color_or_texture_type value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "reflective", value);
    }

    public void insertreflectiveAt(common_color_or_texture_type value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "reflective", index, value);
    }

    public void replacereflectiveAt(common_color_or_texture_type value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "reflective", index, value);
    }

    public static int getreflectivityMinCount() {
        return 0;
    }

    public static int getreflectivityMaxCount() {
        return 1;
    }

    public int getreflectivityCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "reflectivity");
    }

    public boolean hasreflectivity() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "reflectivity");
    }

    public common_float_or_param_type newreflectivity() {
        return new common_float_or_param_type(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "reflectivity"));
    }

    public common_float_or_param_type getreflectivityAt(int index) throws Exception {
        return new common_float_or_param_type(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "reflectivity", index));
    }

    public org.w3c.dom.Node getStartingreflectivityCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "reflectivity");
    }

    public org.w3c.dom.Node getAdvancedreflectivityCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "reflectivity", curNode);
    }

    public common_float_or_param_type getreflectivityValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new common_float_or_param_type(curNode);
    }

    public common_float_or_param_type getreflectivity() throws Exception {
        return getreflectivityAt(0);
    }

    public void removereflectivityAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "reflectivity", index);
    }

    public void removereflectivity() {
        removereflectivityAt(0);
    }

    public org.w3c.dom.Node addreflectivity(common_float_or_param_type value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "reflectivity", value);
    }

    public void insertreflectivityAt(common_float_or_param_type value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "reflectivity", index, value);
    }

    public void replacereflectivityAt(common_float_or_param_type value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "reflectivity", index, value);
    }

    public static int gettransparentMinCount() {
        return 0;
    }

    public static int gettransparentMaxCount() {
        return 1;
    }

    public int gettransparentCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "transparent");
    }

    public boolean hastransparent() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "transparent");
    }

    public common_transparent_type newtransparent() {
        return new common_transparent_type(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "transparent"));
    }

    public common_transparent_type gettransparentAt(int index) throws Exception {
        return new common_transparent_type(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "transparent", index));
    }

    public org.w3c.dom.Node getStartingtransparentCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "transparent");
    }

    public org.w3c.dom.Node getAdvancedtransparentCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "transparent", curNode);
    }

    public common_transparent_type gettransparentValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new common_transparent_type(curNode);
    }

    public common_transparent_type gettransparent() throws Exception {
        return gettransparentAt(0);
    }

    public void removetransparentAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "transparent", index);
    }

    public void removetransparent() {
        removetransparentAt(0);
    }

    public org.w3c.dom.Node addtransparent(common_transparent_type value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "transparent", value);
    }

    public void inserttransparentAt(common_transparent_type value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "transparent", index, value);
    }

    public void replacetransparentAt(common_transparent_type value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "transparent", index, value);
    }

    public static int gettransparencyMinCount() {
        return 0;
    }

    public static int gettransparencyMaxCount() {
        return 1;
    }

    public int gettransparencyCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "transparency");
    }

    public boolean hastransparency() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "transparency");
    }

    public common_float_or_param_type newtransparency() {
        return new common_float_or_param_type(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "transparency"));
    }

    public common_float_or_param_type gettransparencyAt(int index) throws Exception {
        return new common_float_or_param_type(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "transparency", index));
    }

    public org.w3c.dom.Node getStartingtransparencyCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "transparency");
    }

    public org.w3c.dom.Node getAdvancedtransparencyCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "transparency", curNode);
    }

    public common_float_or_param_type gettransparencyValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new common_float_or_param_type(curNode);
    }

    public common_float_or_param_type gettransparency() throws Exception {
        return gettransparencyAt(0);
    }

    public void removetransparencyAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "transparency", index);
    }

    public void removetransparency() {
        removetransparencyAt(0);
    }

    public org.w3c.dom.Node addtransparency(common_float_or_param_type value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "transparency", value);
    }

    public void inserttransparencyAt(common_float_or_param_type value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "transparency", index, value);
    }

    public void replacetransparencyAt(common_float_or_param_type value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "transparency", index, value);
    }

    public static int getindex_of_refractionMinCount() {
        return 0;
    }

    public static int getindex_of_refractionMaxCount() {
        return 1;
    }

    public int getindex_of_refractionCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "index_of_refraction");
    }

    public boolean hasindex_of_refraction() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "index_of_refraction");
    }

    public common_float_or_param_type newindex_of_refraction() {
        return new common_float_or_param_type(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "index_of_refraction"));
    }

    public common_float_or_param_type getindex_of_refractionAt(int index) throws Exception {
        return new common_float_or_param_type(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "index_of_refraction", index));
    }

    public org.w3c.dom.Node getStartingindex_of_refractionCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "index_of_refraction");
    }

    public org.w3c.dom.Node getAdvancedindex_of_refractionCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "index_of_refraction", curNode);
    }

    public common_float_or_param_type getindex_of_refractionValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new common_float_or_param_type(curNode);
    }

    public common_float_or_param_type getindex_of_refraction() throws Exception {
        return getindex_of_refractionAt(0);
    }

    public void removeindex_of_refractionAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "index_of_refraction", index);
    }

    public void removeindex_of_refraction() {
        removeindex_of_refractionAt(0);
    }

    public org.w3c.dom.Node addindex_of_refraction(common_float_or_param_type value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "index_of_refraction", value);
    }

    public void insertindex_of_refractionAt(common_float_or_param_type value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "index_of_refraction", index, value);
    }

    public void replaceindex_of_refractionAt(common_float_or_param_type value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "index_of_refraction", index, value);
    }
}
