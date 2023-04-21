package homura.hde.ext.model.collada.schema;

public class fx_surface_format_hint_common extends homura.hde.util.xml.xml.Node {

    public fx_surface_format_hint_common(fx_surface_format_hint_common node) {
        super(node);
    }

    public fx_surface_format_hint_common(org.w3c.dom.Node node) {
        super(node);
    }

    public fx_surface_format_hint_common(org.w3c.dom.Document doc) {
        super(doc);
    }

    public fx_surface_format_hint_common(homura.hde.util.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "channels"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "channels", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "range"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "range", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "precision"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "precision", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "option"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "option", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new extraType(tmpNode).adjustPrefix();
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "fx_surface_format_hint_common");
    }

    public static int getchannelsMinCount() {
        return 1;
    }

    public static int getchannelsMaxCount() {
        return 1;
    }

    public int getchannelsCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "channels");
    }

    public boolean haschannels() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "channels");
    }

    public fx_surface_format_hint_channels_enum newchannels() {
        return new fx_surface_format_hint_channels_enum();
    }

    public fx_surface_format_hint_channels_enum getchannelsAt(int index) throws Exception {
        return new fx_surface_format_hint_channels_enum(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "channels", index)));
    }

    public org.w3c.dom.Node getStartingchannelsCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "channels");
    }

    public org.w3c.dom.Node getAdvancedchannelsCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "channels", curNode);
    }

    public fx_surface_format_hint_channels_enum getchannelsValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_surface_format_hint_channels_enum(getDomNodeValue(curNode));
    }

    public fx_surface_format_hint_channels_enum getchannels() throws Exception {
        return getchannelsAt(0);
    }

    public void removechannelsAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "channels", index);
    }

    public void removechannels() {
        removechannelsAt(0);
    }

    public org.w3c.dom.Node addchannels(fx_surface_format_hint_channels_enum value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "channels", value.toString());
    }

    public org.w3c.dom.Node addchannels(String value) throws Exception {
        return addchannels(new fx_surface_format_hint_channels_enum(value));
    }

    public void insertchannelsAt(fx_surface_format_hint_channels_enum value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "channels", index, value.toString());
    }

    public void insertchannelsAt(String value, int index) throws Exception {
        insertchannelsAt(new fx_surface_format_hint_channels_enum(value), index);
    }

    public void replacechannelsAt(fx_surface_format_hint_channels_enum value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "channels", index, value.toString());
    }

    public void replacechannelsAt(String value, int index) throws Exception {
        replacechannelsAt(new fx_surface_format_hint_channels_enum(value), index);
    }

    public static int getrangeMinCount() {
        return 1;
    }

    public static int getrangeMaxCount() {
        return 1;
    }

    public int getrangeCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "range");
    }

    public boolean hasrange() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "range");
    }

    public fx_surface_format_hint_range_enum newrange() {
        return new fx_surface_format_hint_range_enum();
    }

    public fx_surface_format_hint_range_enum getrangeAt(int index) throws Exception {
        return new fx_surface_format_hint_range_enum(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "range", index)));
    }

    public org.w3c.dom.Node getStartingrangeCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "range");
    }

    public org.w3c.dom.Node getAdvancedrangeCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "range", curNode);
    }

    public fx_surface_format_hint_range_enum getrangeValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_surface_format_hint_range_enum(getDomNodeValue(curNode));
    }

    public fx_surface_format_hint_range_enum getrange() throws Exception {
        return getrangeAt(0);
    }

    public void removerangeAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "range", index);
    }

    public void removerange() {
        removerangeAt(0);
    }

    public org.w3c.dom.Node addrange(fx_surface_format_hint_range_enum value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "range", value.toString());
    }

    public org.w3c.dom.Node addrange(String value) throws Exception {
        return addrange(new fx_surface_format_hint_range_enum(value));
    }

    public void insertrangeAt(fx_surface_format_hint_range_enum value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "range", index, value.toString());
    }

    public void insertrangeAt(String value, int index) throws Exception {
        insertrangeAt(new fx_surface_format_hint_range_enum(value), index);
    }

    public void replacerangeAt(fx_surface_format_hint_range_enum value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "range", index, value.toString());
    }

    public void replacerangeAt(String value, int index) throws Exception {
        replacerangeAt(new fx_surface_format_hint_range_enum(value), index);
    }

    public static int getprecisionMinCount() {
        return 0;
    }

    public static int getprecisionMaxCount() {
        return 1;
    }

    public int getprecisionCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "precision");
    }

    public boolean hasprecision() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "precision");
    }

    public fx_surface_format_hint_precision_enum newprecision() {
        return new fx_surface_format_hint_precision_enum();
    }

    public fx_surface_format_hint_precision_enum getprecisionAt(int index) throws Exception {
        return new fx_surface_format_hint_precision_enum(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "precision", index)));
    }

    public org.w3c.dom.Node getStartingprecisionCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "precision");
    }

    public org.w3c.dom.Node getAdvancedprecisionCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "precision", curNode);
    }

    public fx_surface_format_hint_precision_enum getprecisionValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_surface_format_hint_precision_enum(getDomNodeValue(curNode));
    }

    public fx_surface_format_hint_precision_enum getprecision() throws Exception {
        return getprecisionAt(0);
    }

    public void removeprecisionAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "precision", index);
    }

    public void removeprecision() {
        removeprecisionAt(0);
    }

    public org.w3c.dom.Node addprecision(fx_surface_format_hint_precision_enum value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "precision", value.toString());
    }

    public org.w3c.dom.Node addprecision(String value) throws Exception {
        return addprecision(new fx_surface_format_hint_precision_enum(value));
    }

    public void insertprecisionAt(fx_surface_format_hint_precision_enum value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "precision", index, value.toString());
    }

    public void insertprecisionAt(String value, int index) throws Exception {
        insertprecisionAt(new fx_surface_format_hint_precision_enum(value), index);
    }

    public void replaceprecisionAt(fx_surface_format_hint_precision_enum value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "precision", index, value.toString());
    }

    public void replaceprecisionAt(String value, int index) throws Exception {
        replaceprecisionAt(new fx_surface_format_hint_precision_enum(value), index);
    }

    public static int getoptionMinCount() {
        return 0;
    }

    public static int getoptionMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int getoptionCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "option");
    }

    public boolean hasoption() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "option");
    }

    public fx_surface_format_hint_option_enum newoption() {
        return new fx_surface_format_hint_option_enum();
    }

    public fx_surface_format_hint_option_enum getoptionAt(int index) throws Exception {
        return new fx_surface_format_hint_option_enum(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "option", index)));
    }

    public org.w3c.dom.Node getStartingoptionCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "option");
    }

    public org.w3c.dom.Node getAdvancedoptionCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "option", curNode);
    }

    public fx_surface_format_hint_option_enum getoptionValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_surface_format_hint_option_enum(getDomNodeValue(curNode));
    }

    public fx_surface_format_hint_option_enum getoption() throws Exception {
        return getoptionAt(0);
    }

    public void removeoptionAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "option", index);
    }

    public void removeoption() {
        while (hasoption()) removeoptionAt(0);
    }

    public org.w3c.dom.Node addoption(fx_surface_format_hint_option_enum value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "option", value.toString());
    }

    public org.w3c.dom.Node addoption(String value) throws Exception {
        return addoption(new fx_surface_format_hint_option_enum(value));
    }

    public void insertoptionAt(fx_surface_format_hint_option_enum value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "option", index, value.toString());
    }

    public void insertoptionAt(String value, int index) throws Exception {
        insertoptionAt(new fx_surface_format_hint_option_enum(value), index);
    }

    public void replaceoptionAt(fx_surface_format_hint_option_enum value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "option", index, value.toString());
    }

    public void replaceoptionAt(String value, int index) throws Exception {
        replaceoptionAt(new fx_surface_format_hint_option_enum(value), index);
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
