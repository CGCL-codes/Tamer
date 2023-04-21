package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaFloat;
import homura.hde.util.xml.types.SchemaNCName;
import homura.hde.util.xml.types.SchemaShort;

public class fx_samplerRECT_common extends homura.hde.util.xml.xml.Node {

    public fx_samplerRECT_common(fx_samplerRECT_common node) {
        super(node);
    }

    public fx_samplerRECT_common(org.w3c.dom.Node node) {
        super(node);
    }

    public fx_samplerRECT_common(org.w3c.dom.Document doc) {
        super(doc);
    }

    public fx_samplerRECT_common(homura.hde.util.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "source"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "source", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_s"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_s", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_t"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_t", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "minfilter"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "minfilter", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "magfilter"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "magfilter", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipfilter"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipfilter", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "border_color"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "border_color", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_maxlevel"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_maxlevel", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_bias"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_bias", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new extraType(tmpNode).adjustPrefix();
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "fx_samplerRECT_common");
    }

    public static int getsourceMinCount() {
        return 1;
    }

    public static int getsourceMaxCount() {
        return 1;
    }

    public int getsourceCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "source");
    }

    public boolean hassource() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "source");
    }

    public SchemaNCName newsource() {
        return new SchemaNCName();
    }

    public SchemaNCName getsourceAt(int index) throws Exception {
        return new SchemaNCName(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "source", index)));
    }

    public org.w3c.dom.Node getStartingsourceCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "source");
    }

    public org.w3c.dom.Node getAdvancedsourceCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "source", curNode);
    }

    public SchemaNCName getsourceValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new SchemaNCName(getDomNodeValue(curNode));
    }

    public SchemaNCName getsource() throws Exception {
        return getsourceAt(0);
    }

    public void removesourceAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "source", index);
    }

    public void removesource() {
        removesourceAt(0);
    }

    public org.w3c.dom.Node addsource(SchemaNCName value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "source", value.toString());
    }

    public org.w3c.dom.Node addsource(String value) throws Exception {
        return addsource(new SchemaNCName(value));
    }

    public void insertsourceAt(SchemaNCName value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "source", index, value.toString());
    }

    public void insertsourceAt(String value, int index) throws Exception {
        insertsourceAt(new SchemaNCName(value), index);
    }

    public void replacesourceAt(SchemaNCName value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "source", index, value.toString());
    }

    public void replacesourceAt(String value, int index) throws Exception {
        replacesourceAt(new SchemaNCName(value), index);
    }

    public static int getwrap_sMinCount() {
        return 0;
    }

    public static int getwrap_sMaxCount() {
        return 1;
    }

    public int getwrap_sCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_s");
    }

    public boolean haswrap_s() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_s");
    }

    public fx_sampler_wrap_common newwrap_s() {
        return new fx_sampler_wrap_common();
    }

    public fx_sampler_wrap_common getwrap_sAt(int index) throws Exception {
        return new fx_sampler_wrap_common(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_s", index)));
    }

    public org.w3c.dom.Node getStartingwrap_sCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_s");
    }

    public org.w3c.dom.Node getAdvancedwrap_sCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_s", curNode);
    }

    public fx_sampler_wrap_common getwrap_sValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_sampler_wrap_common(getDomNodeValue(curNode));
    }

    public fx_sampler_wrap_common getwrap_s() throws Exception {
        return getwrap_sAt(0);
    }

    public void removewrap_sAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_s", index);
    }

    public void removewrap_s() {
        removewrap_sAt(0);
    }

    public org.w3c.dom.Node addwrap_s(fx_sampler_wrap_common value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_s", value.toString());
    }

    public org.w3c.dom.Node addwrap_s(String value) throws Exception {
        return addwrap_s(new fx_sampler_wrap_common(value));
    }

    public void insertwrap_sAt(fx_sampler_wrap_common value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_s", index, value.toString());
    }

    public void insertwrap_sAt(String value, int index) throws Exception {
        insertwrap_sAt(new fx_sampler_wrap_common(value), index);
    }

    public void replacewrap_sAt(fx_sampler_wrap_common value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_s", index, value.toString());
    }

    public void replacewrap_sAt(String value, int index) throws Exception {
        replacewrap_sAt(new fx_sampler_wrap_common(value), index);
    }

    public static int getwrap_tMinCount() {
        return 0;
    }

    public static int getwrap_tMaxCount() {
        return 1;
    }

    public int getwrap_tCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_t");
    }

    public boolean haswrap_t() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_t");
    }

    public fx_sampler_wrap_common newwrap_t() {
        return new fx_sampler_wrap_common();
    }

    public fx_sampler_wrap_common getwrap_tAt(int index) throws Exception {
        return new fx_sampler_wrap_common(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_t", index)));
    }

    public org.w3c.dom.Node getStartingwrap_tCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_t");
    }

    public org.w3c.dom.Node getAdvancedwrap_tCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_t", curNode);
    }

    public fx_sampler_wrap_common getwrap_tValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_sampler_wrap_common(getDomNodeValue(curNode));
    }

    public fx_sampler_wrap_common getwrap_t() throws Exception {
        return getwrap_tAt(0);
    }

    public void removewrap_tAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_t", index);
    }

    public void removewrap_t() {
        removewrap_tAt(0);
    }

    public org.w3c.dom.Node addwrap_t(fx_sampler_wrap_common value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_t", value.toString());
    }

    public org.w3c.dom.Node addwrap_t(String value) throws Exception {
        return addwrap_t(new fx_sampler_wrap_common(value));
    }

    public void insertwrap_tAt(fx_sampler_wrap_common value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_t", index, value.toString());
    }

    public void insertwrap_tAt(String value, int index) throws Exception {
        insertwrap_tAt(new fx_sampler_wrap_common(value), index);
    }

    public void replacewrap_tAt(fx_sampler_wrap_common value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "wrap_t", index, value.toString());
    }

    public void replacewrap_tAt(String value, int index) throws Exception {
        replacewrap_tAt(new fx_sampler_wrap_common(value), index);
    }

    public static int getminfilterMinCount() {
        return 0;
    }

    public static int getminfilterMaxCount() {
        return 1;
    }

    public int getminfilterCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "minfilter");
    }

    public boolean hasminfilter() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "minfilter");
    }

    public fx_sampler_filter_common newminfilter() {
        return new fx_sampler_filter_common();
    }

    public fx_sampler_filter_common getminfilterAt(int index) throws Exception {
        return new fx_sampler_filter_common(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "minfilter", index)));
    }

    public org.w3c.dom.Node getStartingminfilterCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "minfilter");
    }

    public org.w3c.dom.Node getAdvancedminfilterCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "minfilter", curNode);
    }

    public fx_sampler_filter_common getminfilterValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_sampler_filter_common(getDomNodeValue(curNode));
    }

    public fx_sampler_filter_common getminfilter() throws Exception {
        return getminfilterAt(0);
    }

    public void removeminfilterAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "minfilter", index);
    }

    public void removeminfilter() {
        removeminfilterAt(0);
    }

    public org.w3c.dom.Node addminfilter(fx_sampler_filter_common value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "minfilter", value.toString());
    }

    public org.w3c.dom.Node addminfilter(String value) throws Exception {
        return addminfilter(new fx_sampler_filter_common(value));
    }

    public void insertminfilterAt(fx_sampler_filter_common value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "minfilter", index, value.toString());
    }

    public void insertminfilterAt(String value, int index) throws Exception {
        insertminfilterAt(new fx_sampler_filter_common(value), index);
    }

    public void replaceminfilterAt(fx_sampler_filter_common value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "minfilter", index, value.toString());
    }

    public void replaceminfilterAt(String value, int index) throws Exception {
        replaceminfilterAt(new fx_sampler_filter_common(value), index);
    }

    public static int getmagfilterMinCount() {
        return 0;
    }

    public static int getmagfilterMaxCount() {
        return 1;
    }

    public int getmagfilterCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "magfilter");
    }

    public boolean hasmagfilter() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "magfilter");
    }

    public fx_sampler_filter_common newmagfilter() {
        return new fx_sampler_filter_common();
    }

    public fx_sampler_filter_common getmagfilterAt(int index) throws Exception {
        return new fx_sampler_filter_common(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "magfilter", index)));
    }

    public org.w3c.dom.Node getStartingmagfilterCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "magfilter");
    }

    public org.w3c.dom.Node getAdvancedmagfilterCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "magfilter", curNode);
    }

    public fx_sampler_filter_common getmagfilterValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_sampler_filter_common(getDomNodeValue(curNode));
    }

    public fx_sampler_filter_common getmagfilter() throws Exception {
        return getmagfilterAt(0);
    }

    public void removemagfilterAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "magfilter", index);
    }

    public void removemagfilter() {
        removemagfilterAt(0);
    }

    public org.w3c.dom.Node addmagfilter(fx_sampler_filter_common value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "magfilter", value.toString());
    }

    public org.w3c.dom.Node addmagfilter(String value) throws Exception {
        return addmagfilter(new fx_sampler_filter_common(value));
    }

    public void insertmagfilterAt(fx_sampler_filter_common value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "magfilter", index, value.toString());
    }

    public void insertmagfilterAt(String value, int index) throws Exception {
        insertmagfilterAt(new fx_sampler_filter_common(value), index);
    }

    public void replacemagfilterAt(fx_sampler_filter_common value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "magfilter", index, value.toString());
    }

    public void replacemagfilterAt(String value, int index) throws Exception {
        replacemagfilterAt(new fx_sampler_filter_common(value), index);
    }

    public static int getmipfilterMinCount() {
        return 0;
    }

    public static int getmipfilterMaxCount() {
        return 1;
    }

    public int getmipfilterCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipfilter");
    }

    public boolean hasmipfilter() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipfilter");
    }

    public fx_sampler_filter_common newmipfilter() {
        return new fx_sampler_filter_common();
    }

    public fx_sampler_filter_common getmipfilterAt(int index) throws Exception {
        return new fx_sampler_filter_common(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipfilter", index)));
    }

    public org.w3c.dom.Node getStartingmipfilterCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipfilter");
    }

    public org.w3c.dom.Node getAdvancedmipfilterCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipfilter", curNode);
    }

    public fx_sampler_filter_common getmipfilterValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_sampler_filter_common(getDomNodeValue(curNode));
    }

    public fx_sampler_filter_common getmipfilter() throws Exception {
        return getmipfilterAt(0);
    }

    public void removemipfilterAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipfilter", index);
    }

    public void removemipfilter() {
        removemipfilterAt(0);
    }

    public org.w3c.dom.Node addmipfilter(fx_sampler_filter_common value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipfilter", value.toString());
    }

    public org.w3c.dom.Node addmipfilter(String value) throws Exception {
        return addmipfilter(new fx_sampler_filter_common(value));
    }

    public void insertmipfilterAt(fx_sampler_filter_common value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipfilter", index, value.toString());
    }

    public void insertmipfilterAt(String value, int index) throws Exception {
        insertmipfilterAt(new fx_sampler_filter_common(value), index);
    }

    public void replacemipfilterAt(fx_sampler_filter_common value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipfilter", index, value.toString());
    }

    public void replacemipfilterAt(String value, int index) throws Exception {
        replacemipfilterAt(new fx_sampler_filter_common(value), index);
    }

    public static int getborder_colorMinCount() {
        return 0;
    }

    public static int getborder_colorMaxCount() {
        return 1;
    }

    public int getborder_colorCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "border_color");
    }

    public boolean hasborder_color() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "border_color");
    }

    public fx_color_common newborder_color() {
        return new fx_color_common();
    }

    public fx_color_common getborder_colorAt(int index) throws Exception {
        return new fx_color_common(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "border_color", index)));
    }

    public org.w3c.dom.Node getStartingborder_colorCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "border_color");
    }

    public org.w3c.dom.Node getAdvancedborder_colorCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "border_color", curNode);
    }

    public fx_color_common getborder_colorValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_color_common(getDomNodeValue(curNode));
    }

    public fx_color_common getborder_color() throws Exception {
        return getborder_colorAt(0);
    }

    public void removeborder_colorAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "border_color", index);
    }

    public void removeborder_color() {
        removeborder_colorAt(0);
    }

    public org.w3c.dom.Node addborder_color(fx_color_common value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "border_color", value.toString());
    }

    public org.w3c.dom.Node addborder_color(String value) throws Exception {
        return addborder_color(new fx_color_common(value));
    }

    public void insertborder_colorAt(fx_color_common value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "border_color", index, value.toString());
    }

    public void insertborder_colorAt(String value, int index) throws Exception {
        insertborder_colorAt(new fx_color_common(value), index);
    }

    public void replaceborder_colorAt(fx_color_common value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "border_color", index, value.toString());
    }

    public void replaceborder_colorAt(String value, int index) throws Exception {
        replaceborder_colorAt(new fx_color_common(value), index);
    }

    public static int getmipmap_maxlevelMinCount() {
        return 0;
    }

    public static int getmipmap_maxlevelMaxCount() {
        return 1;
    }

    public int getmipmap_maxlevelCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_maxlevel");
    }

    public boolean hasmipmap_maxlevel() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_maxlevel");
    }

    public SchemaShort newmipmap_maxlevel() {
        return new SchemaShort();
    }

    public SchemaShort getmipmap_maxlevelAt(int index) throws Exception {
        return new SchemaShort(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_maxlevel", index)));
    }

    public org.w3c.dom.Node getStartingmipmap_maxlevelCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_maxlevel");
    }

    public org.w3c.dom.Node getAdvancedmipmap_maxlevelCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_maxlevel", curNode);
    }

    public SchemaShort getmipmap_maxlevelValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new SchemaShort(getDomNodeValue(curNode));
    }

    public SchemaShort getmipmap_maxlevel() throws Exception {
        return getmipmap_maxlevelAt(0);
    }

    public void removemipmap_maxlevelAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_maxlevel", index);
    }

    public void removemipmap_maxlevel() {
        removemipmap_maxlevelAt(0);
    }

    public org.w3c.dom.Node addmipmap_maxlevel(SchemaShort value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_maxlevel", value.toString());
    }

    public org.w3c.dom.Node addmipmap_maxlevel(String value) throws Exception {
        return addmipmap_maxlevel(new SchemaShort(value));
    }

    public void insertmipmap_maxlevelAt(SchemaShort value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_maxlevel", index, value.toString());
    }

    public void insertmipmap_maxlevelAt(String value, int index) throws Exception {
        insertmipmap_maxlevelAt(new SchemaShort(value), index);
    }

    public void replacemipmap_maxlevelAt(SchemaShort value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_maxlevel", index, value.toString());
    }

    public void replacemipmap_maxlevelAt(String value, int index) throws Exception {
        replacemipmap_maxlevelAt(new SchemaShort(value), index);
    }

    public static int getmipmap_biasMinCount() {
        return 0;
    }

    public static int getmipmap_biasMaxCount() {
        return 1;
    }

    public int getmipmap_biasCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_bias");
    }

    public boolean hasmipmap_bias() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_bias");
    }

    public SchemaFloat newmipmap_bias() {
        return new SchemaFloat();
    }

    public SchemaFloat getmipmap_biasAt(int index) throws Exception {
        return new SchemaFloat(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_bias", index)));
    }

    public org.w3c.dom.Node getStartingmipmap_biasCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_bias");
    }

    public org.w3c.dom.Node getAdvancedmipmap_biasCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_bias", curNode);
    }

    public SchemaFloat getmipmap_biasValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new SchemaFloat(getDomNodeValue(curNode));
    }

    public SchemaFloat getmipmap_bias() throws Exception {
        return getmipmap_biasAt(0);
    }

    public void removemipmap_biasAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_bias", index);
    }

    public void removemipmap_bias() {
        removemipmap_biasAt(0);
    }

    public org.w3c.dom.Node addmipmap_bias(SchemaFloat value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_bias", value.toString());
    }

    public org.w3c.dom.Node addmipmap_bias(String value) throws Exception {
        return addmipmap_bias(new SchemaFloat(value));
    }

    public void insertmipmap_biasAt(SchemaFloat value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_bias", index, value.toString());
    }

    public void insertmipmap_biasAt(String value, int index) throws Exception {
        insertmipmap_biasAt(new SchemaFloat(value), index);
    }

    public void replacemipmap_biasAt(SchemaFloat value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mipmap_bias", index, value.toString());
    }

    public void replacemipmap_biasAt(String value, int index) throws Exception {
        replacemipmap_biasAt(new SchemaFloat(value), index);
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
