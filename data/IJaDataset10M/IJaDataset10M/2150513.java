package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaBoolean;
import homura.hde.util.xml.types.SchemaLong;
import homura.hde.util.xml.types.SchemaToken;

public class fx_surface_common extends homura.hde.util.xml.xml.Node {

    public fx_surface_common(fx_surface_common node) {
        super(node);
    }

    public fx_surface_common(org.w3c.dom.Node node) {
        super(node);
    }

    public fx_surface_common(org.w3c.dom.Document doc) {
        super(doc);
    }

    public fx_surface_common(homura.hde.util.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "type"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "type", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_as_null"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_as_null", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_as_target"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_as_target", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_cube"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_cube", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new fx_surface_init_cube_common(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_volume"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_volume", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new fx_surface_init_volume_common(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_planar"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_planar", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new fx_surface_init_planar_common(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new fx_surface_init_from_common(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "format"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "format", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "format_hint"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "format_hint", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new fx_surface_format_hint_common(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "size"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "size", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "viewport_ratio"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "viewport_ratio", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mip_levels"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mip_levels", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_generate"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_generate", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new extraType(tmpNode).adjustPrefix();
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "fx_surface_common");
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

    public fx_surface_type_enum newtype() {
        return new fx_surface_type_enum();
    }

    public fx_surface_type_enum gettypeAt(int index) throws Exception {
        return new fx_surface_type_enum(getDomNodeValue(getDomChildAt(Attribute, null, "type", index)));
    }

    public org.w3c.dom.Node getStartingtypeCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "type");
    }

    public org.w3c.dom.Node getAdvancedtypeCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "type", curNode);
    }

    public fx_surface_type_enum gettypeValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_surface_type_enum(getDomNodeValue(curNode));
    }

    public fx_surface_type_enum gettype() throws Exception {
        return gettypeAt(0);
    }

    public void removetypeAt(int index) {
        removeDomChildAt(Attribute, null, "type", index);
    }

    public void removetype() {
        removetypeAt(0);
    }

    public org.w3c.dom.Node addtype(fx_surface_type_enum value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "type", value.toString());
    }

    public org.w3c.dom.Node addtype(String value) throws Exception {
        return addtype(new fx_surface_type_enum(value));
    }

    public void inserttypeAt(fx_surface_type_enum value, int index) {
        insertDomChildAt(Attribute, null, "type", index, value.toString());
    }

    public void inserttypeAt(String value, int index) throws Exception {
        inserttypeAt(new fx_surface_type_enum(value), index);
    }

    public void replacetypeAt(fx_surface_type_enum value, int index) {
        replaceDomChildAt(Attribute, null, "type", index, value.toString());
    }

    public void replacetypeAt(String value, int index) throws Exception {
        replacetypeAt(new fx_surface_type_enum(value), index);
    }

    public static int getinit_as_nullMinCount() {
        return 1;
    }

    public static int getinit_as_nullMaxCount() {
        return 1;
    }

    public int getinit_as_nullCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_as_null");
    }

    public boolean hasinit_as_null() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_as_null");
    }

    public homura.hde.util.xml.xml.AnyTypeNode newinit_as_null() {
        return new homura.hde.util.xml.xml.AnyTypeNode(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "init_as_null"));
    }

    public homura.hde.util.xml.xml.AnyTypeNode getinit_as_nullAt(int index) throws Exception {
        return new homura.hde.util.xml.xml.AnyTypeNode(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_as_null", index));
    }

    public org.w3c.dom.Node getStartinginit_as_nullCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_as_null");
    }

    public org.w3c.dom.Node getAdvancedinit_as_nullCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_as_null", curNode);
    }

    public homura.hde.util.xml.xml.AnyTypeNode getinit_as_nullValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new homura.hde.util.xml.xml.AnyTypeNode(curNode);
    }

    public homura.hde.util.xml.xml.AnyTypeNode getinit_as_null() throws Exception {
        return getinit_as_nullAt(0);
    }

    public void removeinit_as_nullAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_as_null", index);
    }

    public void removeinit_as_null() {
        removeinit_as_nullAt(0);
    }

    public org.w3c.dom.Node addinit_as_null(homura.hde.util.xml.xml.AnyTypeNode value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "init_as_null", value);
    }

    public void insertinit_as_nullAt(homura.hde.util.xml.xml.AnyTypeNode value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "init_as_null", index, value);
    }

    public void replaceinit_as_nullAt(homura.hde.util.xml.xml.AnyTypeNode value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "init_as_null", index, value);
    }

    public static int getinit_as_targetMinCount() {
        return 1;
    }

    public static int getinit_as_targetMaxCount() {
        return 1;
    }

    public int getinit_as_targetCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_as_target");
    }

    public boolean hasinit_as_target() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_as_target");
    }

    public homura.hde.util.xml.xml.AnyTypeNode newinit_as_target() {
        return new homura.hde.util.xml.xml.AnyTypeNode(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "init_as_target"));
    }

    public homura.hde.util.xml.xml.AnyTypeNode getinit_as_targetAt(int index) throws Exception {
        return new homura.hde.util.xml.xml.AnyTypeNode(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_as_target", index));
    }

    public org.w3c.dom.Node getStartinginit_as_targetCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_as_target");
    }

    public org.w3c.dom.Node getAdvancedinit_as_targetCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_as_target", curNode);
    }

    public homura.hde.util.xml.xml.AnyTypeNode getinit_as_targetValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new homura.hde.util.xml.xml.AnyTypeNode(curNode);
    }

    public homura.hde.util.xml.xml.AnyTypeNode getinit_as_target() throws Exception {
        return getinit_as_targetAt(0);
    }

    public void removeinit_as_targetAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_as_target", index);
    }

    public void removeinit_as_target() {
        removeinit_as_targetAt(0);
    }

    public org.w3c.dom.Node addinit_as_target(homura.hde.util.xml.xml.AnyTypeNode value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "init_as_target", value);
    }

    public void insertinit_as_targetAt(homura.hde.util.xml.xml.AnyTypeNode value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "init_as_target", index, value);
    }

    public void replaceinit_as_targetAt(homura.hde.util.xml.xml.AnyTypeNode value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "init_as_target", index, value);
    }

    public static int getinit_cubeMinCount() {
        return 1;
    }

    public static int getinit_cubeMaxCount() {
        return 1;
    }

    public int getinit_cubeCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_cube");
    }

    public boolean hasinit_cube() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_cube");
    }

    public fx_surface_init_cube_common newinit_cube() {
        return new fx_surface_init_cube_common(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "init_cube"));
    }

    public fx_surface_init_cube_common getinit_cubeAt(int index) throws Exception {
        return new fx_surface_init_cube_common(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_cube", index));
    }

    public org.w3c.dom.Node getStartinginit_cubeCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_cube");
    }

    public org.w3c.dom.Node getAdvancedinit_cubeCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_cube", curNode);
    }

    public fx_surface_init_cube_common getinit_cubeValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_surface_init_cube_common(curNode);
    }

    public fx_surface_init_cube_common getinit_cube() throws Exception {
        return getinit_cubeAt(0);
    }

    public void removeinit_cubeAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_cube", index);
    }

    public void removeinit_cube() {
        removeinit_cubeAt(0);
    }

    public org.w3c.dom.Node addinit_cube(fx_surface_init_cube_common value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "init_cube", value);
    }

    public void insertinit_cubeAt(fx_surface_init_cube_common value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "init_cube", index, value);
    }

    public void replaceinit_cubeAt(fx_surface_init_cube_common value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "init_cube", index, value);
    }

    public static int getinit_volumeMinCount() {
        return 1;
    }

    public static int getinit_volumeMaxCount() {
        return 1;
    }

    public int getinit_volumeCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_volume");
    }

    public boolean hasinit_volume() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_volume");
    }

    public fx_surface_init_volume_common newinit_volume() {
        return new fx_surface_init_volume_common(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "init_volume"));
    }

    public fx_surface_init_volume_common getinit_volumeAt(int index) throws Exception {
        return new fx_surface_init_volume_common(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_volume", index));
    }

    public org.w3c.dom.Node getStartinginit_volumeCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_volume");
    }

    public org.w3c.dom.Node getAdvancedinit_volumeCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_volume", curNode);
    }

    public fx_surface_init_volume_common getinit_volumeValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_surface_init_volume_common(curNode);
    }

    public fx_surface_init_volume_common getinit_volume() throws Exception {
        return getinit_volumeAt(0);
    }

    public void removeinit_volumeAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_volume", index);
    }

    public void removeinit_volume() {
        removeinit_volumeAt(0);
    }

    public org.w3c.dom.Node addinit_volume(fx_surface_init_volume_common value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "init_volume", value);
    }

    public void insertinit_volumeAt(fx_surface_init_volume_common value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "init_volume", index, value);
    }

    public void replaceinit_volumeAt(fx_surface_init_volume_common value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "init_volume", index, value);
    }

    public static int getinit_planarMinCount() {
        return 1;
    }

    public static int getinit_planarMaxCount() {
        return 1;
    }

    public int getinit_planarCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_planar");
    }

    public boolean hasinit_planar() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_planar");
    }

    public fx_surface_init_planar_common newinit_planar() {
        return new fx_surface_init_planar_common(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "init_planar"));
    }

    public fx_surface_init_planar_common getinit_planarAt(int index) throws Exception {
        return new fx_surface_init_planar_common(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_planar", index));
    }

    public org.w3c.dom.Node getStartinginit_planarCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_planar");
    }

    public org.w3c.dom.Node getAdvancedinit_planarCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_planar", curNode);
    }

    public fx_surface_init_planar_common getinit_planarValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_surface_init_planar_common(curNode);
    }

    public fx_surface_init_planar_common getinit_planar() throws Exception {
        return getinit_planarAt(0);
    }

    public void removeinit_planarAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_planar", index);
    }

    public void removeinit_planar() {
        removeinit_planarAt(0);
    }

    public org.w3c.dom.Node addinit_planar(fx_surface_init_planar_common value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "init_planar", value);
    }

    public void insertinit_planarAt(fx_surface_init_planar_common value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "init_planar", index, value);
    }

    public void replaceinit_planarAt(fx_surface_init_planar_common value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "init_planar", index, value);
    }

    public static int getinit_fromMinCount() {
        return 1;
    }

    public static int getinit_fromMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int getinit_fromCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from");
    }

    public boolean hasinit_from() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from");
    }

    public fx_surface_init_from_common newinit_from() {
        return new fx_surface_init_from_common(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "init_from"));
    }

    public fx_surface_init_from_common getinit_fromAt(int index) throws Exception {
        return new fx_surface_init_from_common(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from", index));
    }

    public org.w3c.dom.Node getStartinginit_fromCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from");
    }

    public org.w3c.dom.Node getAdvancedinit_fromCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from", curNode);
    }

    public fx_surface_init_from_common getinit_fromValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_surface_init_from_common(curNode);
    }

    public fx_surface_init_from_common getinit_from() throws Exception {
        return getinit_fromAt(0);
    }

    public void removeinit_fromAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "init_from", index);
    }

    public void removeinit_from() {
        while (hasinit_from()) removeinit_fromAt(0);
    }

    public org.w3c.dom.Node addinit_from(fx_surface_init_from_common value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "init_from", value);
    }

    public void insertinit_fromAt(fx_surface_init_from_common value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "init_from", index, value);
    }

    public void replaceinit_fromAt(fx_surface_init_from_common value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "init_from", index, value);
    }

    public static int getformatMinCount() {
        return 0;
    }

    public static int getformatMaxCount() {
        return 1;
    }

    public int getformatCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "format");
    }

    public boolean hasformat() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "format");
    }

    public SchemaToken newformat() {
        return new SchemaToken();
    }

    public SchemaToken getformatAt(int index) throws Exception {
        return new SchemaToken(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "format", index)));
    }

    public org.w3c.dom.Node getStartingformatCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "format");
    }

    public org.w3c.dom.Node getAdvancedformatCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "format", curNode);
    }

    public SchemaToken getformatValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new SchemaToken(getDomNodeValue(curNode));
    }

    public SchemaToken getformat() throws Exception {
        return getformatAt(0);
    }

    public void removeformatAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "format", index);
    }

    public void removeformat() {
        removeformatAt(0);
    }

    public org.w3c.dom.Node addformat(SchemaToken value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "format", value.toString());
    }

    public org.w3c.dom.Node addformat(String value) throws Exception {
        return addformat(new SchemaToken(value));
    }

    public void insertformatAt(SchemaToken value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "format", index, value.toString());
    }

    public void insertformatAt(String value, int index) throws Exception {
        insertformatAt(new SchemaToken(value), index);
    }

    public void replaceformatAt(SchemaToken value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "format", index, value.toString());
    }

    public void replaceformatAt(String value, int index) throws Exception {
        replaceformatAt(new SchemaToken(value), index);
    }

    public static int getformat_hintMinCount() {
        return 0;
    }

    public static int getformat_hintMaxCount() {
        return 1;
    }

    public int getformat_hintCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "format_hint");
    }

    public boolean hasformat_hint() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "format_hint");
    }

    public fx_surface_format_hint_common newformat_hint() {
        return new fx_surface_format_hint_common(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "format_hint"));
    }

    public fx_surface_format_hint_common getformat_hintAt(int index) throws Exception {
        return new fx_surface_format_hint_common(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "format_hint", index));
    }

    public org.w3c.dom.Node getStartingformat_hintCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "format_hint");
    }

    public org.w3c.dom.Node getAdvancedformat_hintCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "format_hint", curNode);
    }

    public fx_surface_format_hint_common getformat_hintValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_surface_format_hint_common(curNode);
    }

    public fx_surface_format_hint_common getformat_hint() throws Exception {
        return getformat_hintAt(0);
    }

    public void removeformat_hintAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "format_hint", index);
    }

    public void removeformat_hint() {
        removeformat_hintAt(0);
    }

    public org.w3c.dom.Node addformat_hint(fx_surface_format_hint_common value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "format_hint", value);
    }

    public void insertformat_hintAt(fx_surface_format_hint_common value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "format_hint", index, value);
    }

    public void replaceformat_hintAt(fx_surface_format_hint_common value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "format_hint", index, value);
    }

    public static int getsizeMinCount() {
        return 1;
    }

    public static int getsizeMaxCount() {
        return 1;
    }

    public int getsizeCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "size");
    }

    public boolean hassize() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "size");
    }

    public int3 newsize() {
        return new int3();
    }

    public int3 getsizeAt(int index) throws Exception {
        return new int3(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "size", index)));
    }

    public org.w3c.dom.Node getStartingsizeCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "size");
    }

    public org.w3c.dom.Node getAdvancedsizeCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "size", curNode);
    }

    public int3 getsizeValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new int3(getDomNodeValue(curNode));
    }

    public int3 getsize() throws Exception {
        return getsizeAt(0);
    }

    public void removesizeAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "size", index);
    }

    public void removesize() {
        removesizeAt(0);
    }

    public org.w3c.dom.Node addsize(int3 value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "size", value.toString());
    }

    public org.w3c.dom.Node addsize(String value) throws Exception {
        return addsize(new int3(value));
    }

    public void insertsizeAt(int3 value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "size", index, value.toString());
    }

    public void insertsizeAt(String value, int index) throws Exception {
        insertsizeAt(new int3(value), index);
    }

    public void replacesizeAt(int3 value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "size", index, value.toString());
    }

    public void replacesizeAt(String value, int index) throws Exception {
        replacesizeAt(new int3(value), index);
    }

    public static int getviewport_ratioMinCount() {
        return 1;
    }

    public static int getviewport_ratioMaxCount() {
        return 1;
    }

    public int getviewport_ratioCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "viewport_ratio");
    }

    public boolean hasviewport_ratio() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "viewport_ratio");
    }

    public float22 newviewport_ratio() {
        return new float22();
    }

    public float22 getviewport_ratioAt(int index) throws Exception {
        return new float22(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "viewport_ratio", index)));
    }

    public org.w3c.dom.Node getStartingviewport_ratioCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "viewport_ratio");
    }

    public org.w3c.dom.Node getAdvancedviewport_ratioCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "viewport_ratio", curNode);
    }

    public float22 getviewport_ratioValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new float22(getDomNodeValue(curNode));
    }

    public float22 getviewport_ratio() throws Exception {
        return getviewport_ratioAt(0);
    }

    public void removeviewport_ratioAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "viewport_ratio", index);
    }

    public void removeviewport_ratio() {
        removeviewport_ratioAt(0);
    }

    public org.w3c.dom.Node addviewport_ratio(float22 value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "viewport_ratio", value.toString());
    }

    public org.w3c.dom.Node addviewport_ratio(String value) throws Exception {
        return addviewport_ratio(new float22(value));
    }

    public void insertviewport_ratioAt(float22 value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "viewport_ratio", index, value.toString());
    }

    public void insertviewport_ratioAt(String value, int index) throws Exception {
        insertviewport_ratioAt(new float22(value), index);
    }

    public void replaceviewport_ratioAt(float22 value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "viewport_ratio", index, value.toString());
    }

    public void replaceviewport_ratioAt(String value, int index) throws Exception {
        replaceviewport_ratioAt(new float22(value), index);
    }

    public static int getmip_levelsMinCount() {
        return 0;
    }

    public static int getmip_levelsMaxCount() {
        return 1;
    }

    public int getmip_levelsCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "mip_levels");
    }

    public boolean hasmip_levels() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mip_levels");
    }

    public SchemaLong newmip_levels() {
        return new SchemaLong();
    }

    public SchemaLong getmip_levelsAt(int index) throws Exception {
        return new SchemaLong(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mip_levels", index)));
    }

    public org.w3c.dom.Node getStartingmip_levelsCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mip_levels");
    }

    public org.w3c.dom.Node getAdvancedmip_levelsCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mip_levels", curNode);
    }

    public SchemaLong getmip_levelsValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new SchemaLong(getDomNodeValue(curNode));
    }

    public SchemaLong getmip_levels() throws Exception {
        return getmip_levelsAt(0);
    }

    public void removemip_levelsAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mip_levels", index);
    }

    public void removemip_levels() {
        removemip_levelsAt(0);
    }

    public org.w3c.dom.Node addmip_levels(SchemaLong value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mip_levels", value.toString());
    }

    public org.w3c.dom.Node addmip_levels(String value) throws Exception {
        return addmip_levels(new SchemaLong(value));
    }

    public void insertmip_levelsAt(SchemaLong value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mip_levels", index, value.toString());
    }

    public void insertmip_levelsAt(String value, int index) throws Exception {
        insertmip_levelsAt(new SchemaLong(value), index);
    }

    public void replacemip_levelsAt(SchemaLong value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mip_levels", index, value.toString());
    }

    public void replacemip_levelsAt(String value, int index) throws Exception {
        replacemip_levelsAt(new SchemaLong(value), index);
    }

    public static int getmipmap_generateMinCount() {
        return 0;
    }

    public static int getmipmap_generateMaxCount() {
        return 1;
    }

    public int getmipmap_generateCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_generate");
    }

    public boolean hasmipmap_generate() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_generate");
    }

    public SchemaBoolean newmipmap_generate() {
        return new SchemaBoolean();
    }

    public SchemaBoolean getmipmap_generateAt(int index) throws Exception {
        return new SchemaBoolean(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_generate", index)));
    }

    public org.w3c.dom.Node getStartingmipmap_generateCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_generate");
    }

    public org.w3c.dom.Node getAdvancedmipmap_generateCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_generate", curNode);
    }

    public SchemaBoolean getmipmap_generateValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new SchemaBoolean(getDomNodeValue(curNode));
    }

    public SchemaBoolean getmipmap_generate() throws Exception {
        return getmipmap_generateAt(0);
    }

    public void removemipmap_generateAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_generate", index);
    }

    public void removemipmap_generate() {
        removemipmap_generateAt(0);
    }

    public org.w3c.dom.Node addmipmap_generate(SchemaBoolean value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_generate", value.toString());
    }

    public org.w3c.dom.Node addmipmap_generate(String value) throws Exception {
        return addmipmap_generate(new SchemaBoolean(value));
    }

    public void insertmipmap_generateAt(SchemaBoolean value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_generate", index, value.toString());
    }

    public void insertmipmap_generateAt(String value, int index) throws Exception {
        insertmipmap_generateAt(new SchemaBoolean(value), index);
    }

    public void replacemipmap_generateAt(SchemaBoolean value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_generate", index, value.toString());
    }

    public void replacemipmap_generateAt(String value, int index) throws Exception {
        replacemipmap_generateAt(new SchemaBoolean(value), index);
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
