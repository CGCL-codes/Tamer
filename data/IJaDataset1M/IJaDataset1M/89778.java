package com.jmex.model.collada.schema;

public class glsl_setparam_simple extends com.jmex.xml.xml.Node {

    public glsl_setparam_simple(glsl_setparam_simple node) {
        super(node);
    }

    public glsl_setparam_simple(org.w3c.dom.Node node) {
        super(node);
    }

    public glsl_setparam_simple(org.w3c.dom.Document doc) {
        super(doc);
    }

    public glsl_setparam_simple(com.jmex.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "ref"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "ref", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "annotate"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "annotate", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new fx_annotate_common(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool2"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool2", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool3"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool3", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool4"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool4", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2x2"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2x2", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3x3"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3x3", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4x4"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4x4", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int2"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int2", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int3"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int3", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int4"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int4", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new glsl_surface_type(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler1D"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler1D", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new gl_sampler1D(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler2D"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler2D", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new gl_sampler2D(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler3D"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler3D", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new gl_sampler3D(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerCUBE"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerCUBE", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new gl_samplerCUBE(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerRECT"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerRECT", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new gl_samplerRECT(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerDEPTH"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerDEPTH", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new gl_samplerDEPTH(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "enum"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "enum", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "glsl_setparam_simple");
    }

    public static int getrefMinCount() {
        return 1;
    }

    public static int getrefMaxCount() {
        return 1;
    }

    public int getrefCount() {
        return getDomChildCount(Attribute, null, "ref");
    }

    public boolean hasref() {
        return hasDomChild(Attribute, null, "ref");
    }

    public glsl_identifier newref() {
        return new glsl_identifier();
    }

    public glsl_identifier getrefAt(int index) throws Exception {
        return new glsl_identifier(getDomNodeValue(getDomChildAt(Attribute, null, "ref", index)));
    }

    public org.w3c.dom.Node getStartingrefCursor() throws Exception {
        return getDomFirstChild(Attribute, null, "ref");
    }

    public org.w3c.dom.Node getAdvancedrefCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Attribute, null, "ref", curNode);
    }

    public glsl_identifier getrefValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new glsl_identifier(getDomNodeValue(curNode));
    }

    public glsl_identifier getref() throws Exception {
        return getrefAt(0);
    }

    public void removerefAt(int index) {
        removeDomChildAt(Attribute, null, "ref", index);
    }

    public void removeref() {
        removerefAt(0);
    }

    public org.w3c.dom.Node addref(glsl_identifier value) {
        if (value.isNull()) return null;
        return appendDomChild(Attribute, null, "ref", value.toString());
    }

    public org.w3c.dom.Node addref(String value) throws Exception {
        return addref(new glsl_identifier(value));
    }

    public void insertrefAt(glsl_identifier value, int index) {
        insertDomChildAt(Attribute, null, "ref", index, value.toString());
    }

    public void insertrefAt(String value, int index) throws Exception {
        insertrefAt(new glsl_identifier(value), index);
    }

    public void replacerefAt(glsl_identifier value, int index) {
        replaceDomChildAt(Attribute, null, "ref", index, value.toString());
    }

    public void replacerefAt(String value, int index) throws Exception {
        replacerefAt(new glsl_identifier(value), index);
    }

    public static int getannotateMinCount() {
        return 0;
    }

    public static int getannotateMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int getannotateCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "annotate");
    }

    public boolean hasannotate() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "annotate");
    }

    public fx_annotate_common newannotate() {
        return new fx_annotate_common(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "annotate"));
    }

    public fx_annotate_common getannotateAt(int index) throws Exception {
        return new fx_annotate_common(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "annotate", index));
    }

    public org.w3c.dom.Node getStartingannotateCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "annotate");
    }

    public org.w3c.dom.Node getAdvancedannotateCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "annotate", curNode);
    }

    public fx_annotate_common getannotateValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new fx_annotate_common(curNode);
    }

    public fx_annotate_common getannotate() throws Exception {
        return getannotateAt(0);
    }

    public void removeannotateAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "annotate", index);
    }

    public void removeannotate() {
        while (hasannotate()) removeannotateAt(0);
    }

    public org.w3c.dom.Node addannotate(fx_annotate_common value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "annotate", value);
    }

    public void insertannotateAt(fx_annotate_common value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "annotate", index, value);
    }

    public void replaceannotateAt(fx_annotate_common value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "annotate", index, value);
    }

    public static int getboolMinCount() {
        return 1;
    }

    public static int getboolMaxCount() {
        return 1;
    }

    public int getboolCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool");
    }

    public boolean hasbool() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool");
    }

    public glsl_bool newbool() {
        return new glsl_bool();
    }

    public glsl_bool getboolAt(int index) throws Exception {
        return new glsl_bool(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool", index)));
    }

    public org.w3c.dom.Node getStartingboolCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool");
    }

    public org.w3c.dom.Node getAdvancedboolCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool", curNode);
    }

    public glsl_bool getboolValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new glsl_bool(getDomNodeValue(curNode));
    }

    public glsl_bool getbool() throws Exception {
        return getboolAt(0);
    }

    public void removeboolAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool", index);
    }

    public void removebool() {
        removeboolAt(0);
    }

    public org.w3c.dom.Node addbool(glsl_bool value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool", value.toString());
    }

    public org.w3c.dom.Node addbool(String value) throws Exception {
        return addbool(new glsl_bool(value));
    }

    public void insertboolAt(glsl_bool value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool", index, value.toString());
    }

    public void insertboolAt(String value, int index) throws Exception {
        insertboolAt(new glsl_bool(value), index);
    }

    public void replaceboolAt(glsl_bool value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool", index, value.toString());
    }

    public void replaceboolAt(String value, int index) throws Exception {
        replaceboolAt(new glsl_bool(value), index);
    }

    public static int getbool2MinCount() {
        return 1;
    }

    public static int getbool2MaxCount() {
        return 1;
    }

    public int getbool2Count() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool2");
    }

    public boolean hasbool2() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool2");
    }

    public glsl_bool2 newbool2() {
        return new glsl_bool2();
    }

    public glsl_bool2 getbool2At(int index) throws Exception {
        return new glsl_bool2(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool2", index)));
    }

    public org.w3c.dom.Node getStartingbool2Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool2");
    }

    public org.w3c.dom.Node getAdvancedbool2Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool2", curNode);
    }

    public glsl_bool2 getbool2ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new glsl_bool2(getDomNodeValue(curNode));
    }

    public glsl_bool2 getbool2() throws Exception {
        return getbool2At(0);
    }

    public void removebool2At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool2", index);
    }

    public void removebool2() {
        removebool2At(0);
    }

    public org.w3c.dom.Node addbool2(glsl_bool2 value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool2", value.toString());
    }

    public org.w3c.dom.Node addbool2(String value) throws Exception {
        return addbool2(new glsl_bool2(value));
    }

    public void insertbool2At(glsl_bool2 value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool2", index, value.toString());
    }

    public void insertbool2At(String value, int index) throws Exception {
        insertbool2At(new glsl_bool2(value), index);
    }

    public void replacebool2At(glsl_bool2 value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool2", index, value.toString());
    }

    public void replacebool2At(String value, int index) throws Exception {
        replacebool2At(new glsl_bool2(value), index);
    }

    public static int getbool3MinCount() {
        return 1;
    }

    public static int getbool3MaxCount() {
        return 1;
    }

    public int getbool3Count() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool3");
    }

    public boolean hasbool3() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool3");
    }

    public glsl_bool3 newbool3() {
        return new glsl_bool3();
    }

    public glsl_bool3 getbool3At(int index) throws Exception {
        return new glsl_bool3(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool3", index)));
    }

    public org.w3c.dom.Node getStartingbool3Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool3");
    }

    public org.w3c.dom.Node getAdvancedbool3Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool3", curNode);
    }

    public glsl_bool3 getbool3ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new glsl_bool3(getDomNodeValue(curNode));
    }

    public glsl_bool3 getbool3() throws Exception {
        return getbool3At(0);
    }

    public void removebool3At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool3", index);
    }

    public void removebool3() {
        removebool3At(0);
    }

    public org.w3c.dom.Node addbool3(glsl_bool3 value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool3", value.toString());
    }

    public org.w3c.dom.Node addbool3(String value) throws Exception {
        return addbool3(new glsl_bool3(value));
    }

    public void insertbool3At(glsl_bool3 value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool3", index, value.toString());
    }

    public void insertbool3At(String value, int index) throws Exception {
        insertbool3At(new glsl_bool3(value), index);
    }

    public void replacebool3At(glsl_bool3 value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool3", index, value.toString());
    }

    public void replacebool3At(String value, int index) throws Exception {
        replacebool3At(new glsl_bool3(value), index);
    }

    public static int getbool4MinCount() {
        return 1;
    }

    public static int getbool4MaxCount() {
        return 1;
    }

    public int getbool4Count() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool4");
    }

    public boolean hasbool4() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool4");
    }

    public glsl_bool4 newbool4() {
        return new glsl_bool4();
    }

    public glsl_bool4 getbool4At(int index) throws Exception {
        return new glsl_bool4(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool4", index)));
    }

    public org.w3c.dom.Node getStartingbool4Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool4");
    }

    public org.w3c.dom.Node getAdvancedbool4Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool4", curNode);
    }

    public glsl_bool4 getbool4ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new glsl_bool4(getDomNodeValue(curNode));
    }

    public glsl_bool4 getbool4() throws Exception {
        return getbool4At(0);
    }

    public void removebool4At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool4", index);
    }

    public void removebool4() {
        removebool4At(0);
    }

    public org.w3c.dom.Node addbool4(glsl_bool4 value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool4", value.toString());
    }

    public org.w3c.dom.Node addbool4(String value) throws Exception {
        return addbool4(new glsl_bool4(value));
    }

    public void insertbool4At(glsl_bool4 value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool4", index, value.toString());
    }

    public void insertbool4At(String value, int index) throws Exception {
        insertbool4At(new glsl_bool4(value), index);
    }

    public void replacebool4At(glsl_bool4 value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "bool4", index, value.toString());
    }

    public void replacebool4At(String value, int index) throws Exception {
        replacebool4At(new glsl_bool4(value), index);
    }

    public static int getfloat2MinCount() {
        return 1;
    }

    public static int getfloat2MaxCount() {
        return 1;
    }

    public int getfloat2Count() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "float");
    }

    public boolean hasfloat2() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float");
    }

    public glsl_float newfloat2() {
        return new glsl_float();
    }

    public glsl_float getfloat2At(int index) throws Exception {
        return new glsl_float(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float", index)));
    }

    public org.w3c.dom.Node getStartingfloat2Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float");
    }

    public org.w3c.dom.Node getAdvancedfloat2Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float", curNode);
    }

    public glsl_float getfloat2ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new glsl_float(getDomNodeValue(curNode));
    }

    public glsl_float getfloat2() throws Exception {
        return getfloat2At(0);
    }

    public void removefloat2At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float", index);
    }

    public void removefloat2() {
        removefloat2At(0);
    }

    public org.w3c.dom.Node addfloat2(glsl_float value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float", value.toString());
    }

    public org.w3c.dom.Node addfloat2(String value) throws Exception {
        return addfloat2(new glsl_float(value));
    }

    public void insertfloat2At(glsl_float value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float", index, value.toString());
    }

    public void insertfloat2At(String value, int index) throws Exception {
        insertfloat2At(new glsl_float(value), index);
    }

    public void replacefloat2At(glsl_float value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float", index, value.toString());
    }

    public void replacefloat2At(String value, int index) throws Exception {
        replacefloat2At(new glsl_float(value), index);
    }

    public static int getfloat22MinCount() {
        return 1;
    }

    public static int getfloat22MaxCount() {
        return 1;
    }

    public int getfloat22Count() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2");
    }

    public boolean hasfloat22() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2");
    }

    public glsl_float2 newfloat22() {
        return new glsl_float2();
    }

    public glsl_float2 getfloat22At(int index) throws Exception {
        return new glsl_float2(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2", index)));
    }

    public org.w3c.dom.Node getStartingfloat22Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2");
    }

    public org.w3c.dom.Node getAdvancedfloat22Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2", curNode);
    }

    public glsl_float2 getfloat22ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new glsl_float2(getDomNodeValue(curNode));
    }

    public glsl_float2 getfloat22() throws Exception {
        return getfloat22At(0);
    }

    public void removefloat22At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2", index);
    }

    public void removefloat22() {
        removefloat22At(0);
    }

    public org.w3c.dom.Node addfloat22(glsl_float2 value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2", value.toString());
    }

    public org.w3c.dom.Node addfloat22(String value) throws Exception {
        return addfloat22(new glsl_float2(value));
    }

    public void insertfloat22At(glsl_float2 value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2", index, value.toString());
    }

    public void insertfloat22At(String value, int index) throws Exception {
        insertfloat22At(new glsl_float2(value), index);
    }

    public void replacefloat22At(glsl_float2 value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2", index, value.toString());
    }

    public void replacefloat22At(String value, int index) throws Exception {
        replacefloat22At(new glsl_float2(value), index);
    }

    public static int getfloat3MinCount() {
        return 1;
    }

    public static int getfloat3MaxCount() {
        return 1;
    }

    public int getfloat3Count() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3");
    }

    public boolean hasfloat3() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3");
    }

    public glsl_float3 newfloat3() {
        return new glsl_float3();
    }

    public glsl_float3 getfloat3At(int index) throws Exception {
        return new glsl_float3(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3", index)));
    }

    public org.w3c.dom.Node getStartingfloat3Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3");
    }

    public org.w3c.dom.Node getAdvancedfloat3Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3", curNode);
    }

    public glsl_float3 getfloat3ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new glsl_float3(getDomNodeValue(curNode));
    }

    public glsl_float3 getfloat3() throws Exception {
        return getfloat3At(0);
    }

    public void removefloat3At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3", index);
    }

    public void removefloat3() {
        removefloat3At(0);
    }

    public org.w3c.dom.Node addfloat3(glsl_float3 value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3", value.toString());
    }

    public org.w3c.dom.Node addfloat3(String value) throws Exception {
        return addfloat3(new glsl_float3(value));
    }

    public void insertfloat3At(glsl_float3 value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3", index, value.toString());
    }

    public void insertfloat3At(String value, int index) throws Exception {
        insertfloat3At(new glsl_float3(value), index);
    }

    public void replacefloat3At(glsl_float3 value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3", index, value.toString());
    }

    public void replacefloat3At(String value, int index) throws Exception {
        replacefloat3At(new glsl_float3(value), index);
    }

    public static int getfloat4MinCount() {
        return 1;
    }

    public static int getfloat4MaxCount() {
        return 1;
    }

    public int getfloat4Count() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4");
    }

    public boolean hasfloat4() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4");
    }

    public glsl_float4 newfloat4() {
        return new glsl_float4();
    }

    public glsl_float4 getfloat4At(int index) throws Exception {
        return new glsl_float4(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4", index)));
    }

    public org.w3c.dom.Node getStartingfloat4Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4");
    }

    public org.w3c.dom.Node getAdvancedfloat4Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4", curNode);
    }

    public glsl_float4 getfloat4ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new glsl_float4(getDomNodeValue(curNode));
    }

    public glsl_float4 getfloat4() throws Exception {
        return getfloat4At(0);
    }

    public void removefloat4At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4", index);
    }

    public void removefloat4() {
        removefloat4At(0);
    }

    public org.w3c.dom.Node addfloat4(glsl_float4 value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4", value.toString());
    }

    public org.w3c.dom.Node addfloat4(String value) throws Exception {
        return addfloat4(new glsl_float4(value));
    }

    public void insertfloat4At(glsl_float4 value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4", index, value.toString());
    }

    public void insertfloat4At(String value, int index) throws Exception {
        insertfloat4At(new glsl_float4(value), index);
    }

    public void replacefloat4At(glsl_float4 value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4", index, value.toString());
    }

    public void replacefloat4At(String value, int index) throws Exception {
        replacefloat4At(new glsl_float4(value), index);
    }

    public static int getfloat2x2MinCount() {
        return 1;
    }

    public static int getfloat2x2MaxCount() {
        return 1;
    }

    public int getfloat2x2Count() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2x2");
    }

    public boolean hasfloat2x2() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2x2");
    }

    public glsl_float2x2 newfloat2x2() {
        return new glsl_float2x2();
    }

    public glsl_float2x2 getfloat2x2At(int index) throws Exception {
        return new glsl_float2x2(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2x2", index)));
    }

    public org.w3c.dom.Node getStartingfloat2x2Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2x2");
    }

    public org.w3c.dom.Node getAdvancedfloat2x2Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2x2", curNode);
    }

    public glsl_float2x2 getfloat2x2ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new glsl_float2x2(getDomNodeValue(curNode));
    }

    public glsl_float2x2 getfloat2x2() throws Exception {
        return getfloat2x2At(0);
    }

    public void removefloat2x2At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2x2", index);
    }

    public void removefloat2x2() {
        removefloat2x2At(0);
    }

    public org.w3c.dom.Node addfloat2x2(glsl_float2x2 value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2x2", value.toString());
    }

    public org.w3c.dom.Node addfloat2x2(String value) throws Exception {
        return addfloat2x2(new glsl_float2x2(value));
    }

    public void insertfloat2x2At(glsl_float2x2 value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2x2", index, value.toString());
    }

    public void insertfloat2x2At(String value, int index) throws Exception {
        insertfloat2x2At(new glsl_float2x2(value), index);
    }

    public void replacefloat2x2At(glsl_float2x2 value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2x2", index, value.toString());
    }

    public void replacefloat2x2At(String value, int index) throws Exception {
        replacefloat2x2At(new glsl_float2x2(value), index);
    }

    public static int getfloat3x3MinCount() {
        return 1;
    }

    public static int getfloat3x3MaxCount() {
        return 1;
    }

    public int getfloat3x3Count() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3x3");
    }

    public boolean hasfloat3x3() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3x3");
    }

    public glsl_float3x3 newfloat3x3() {
        return new glsl_float3x3();
    }

    public glsl_float3x3 getfloat3x3At(int index) throws Exception {
        return new glsl_float3x3(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3x3", index)));
    }

    public org.w3c.dom.Node getStartingfloat3x3Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3x3");
    }

    public org.w3c.dom.Node getAdvancedfloat3x3Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3x3", curNode);
    }

    public glsl_float3x3 getfloat3x3ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new glsl_float3x3(getDomNodeValue(curNode));
    }

    public glsl_float3x3 getfloat3x3() throws Exception {
        return getfloat3x3At(0);
    }

    public void removefloat3x3At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3x3", index);
    }

    public void removefloat3x3() {
        removefloat3x3At(0);
    }

    public org.w3c.dom.Node addfloat3x3(glsl_float3x3 value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3x3", value.toString());
    }

    public org.w3c.dom.Node addfloat3x3(String value) throws Exception {
        return addfloat3x3(new glsl_float3x3(value));
    }

    public void insertfloat3x3At(glsl_float3x3 value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3x3", index, value.toString());
    }

    public void insertfloat3x3At(String value, int index) throws Exception {
        insertfloat3x3At(new glsl_float3x3(value), index);
    }

    public void replacefloat3x3At(glsl_float3x3 value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3x3", index, value.toString());
    }

    public void replacefloat3x3At(String value, int index) throws Exception {
        replacefloat3x3At(new glsl_float3x3(value), index);
    }

    public static int getfloat4x4MinCount() {
        return 1;
    }

    public static int getfloat4x4MaxCount() {
        return 1;
    }

    public int getfloat4x4Count() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4x4");
    }

    public boolean hasfloat4x4() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4x4");
    }

    public glsl_float4x4 newfloat4x4() {
        return new glsl_float4x4();
    }

    public glsl_float4x4 getfloat4x4At(int index) throws Exception {
        return new glsl_float4x4(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4x4", index)));
    }

    public org.w3c.dom.Node getStartingfloat4x4Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4x4");
    }

    public org.w3c.dom.Node getAdvancedfloat4x4Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4x4", curNode);
    }

    public glsl_float4x4 getfloat4x4ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new glsl_float4x4(getDomNodeValue(curNode));
    }

    public glsl_float4x4 getfloat4x4() throws Exception {
        return getfloat4x4At(0);
    }

    public void removefloat4x4At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4x4", index);
    }

    public void removefloat4x4() {
        removefloat4x4At(0);
    }

    public org.w3c.dom.Node addfloat4x4(glsl_float4x4 value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4x4", value.toString());
    }

    public org.w3c.dom.Node addfloat4x4(String value) throws Exception {
        return addfloat4x4(new glsl_float4x4(value));
    }

    public void insertfloat4x4At(glsl_float4x4 value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4x4", index, value.toString());
    }

    public void insertfloat4x4At(String value, int index) throws Exception {
        insertfloat4x4At(new glsl_float4x4(value), index);
    }

    public void replacefloat4x4At(glsl_float4x4 value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4x4", index, value.toString());
    }

    public void replacefloat4x4At(String value, int index) throws Exception {
        replacefloat4x4At(new glsl_float4x4(value), index);
    }

    public static int getint2MinCount() {
        return 1;
    }

    public static int getint2MaxCount() {
        return 1;
    }

    public int getint2Count() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "int");
    }

    public boolean hasint2() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int");
    }

    public glsl_int newint2() {
        return new glsl_int();
    }

    public glsl_int getint2At(int index) throws Exception {
        return new glsl_int(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "int", index)));
    }

    public org.w3c.dom.Node getStartingint2Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int");
    }

    public org.w3c.dom.Node getAdvancedint2Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int", curNode);
    }

    public glsl_int getint2ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new glsl_int(getDomNodeValue(curNode));
    }

    public glsl_int getint2() throws Exception {
        return getint2At(0);
    }

    public void removeint2At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "int", index);
    }

    public void removeint2() {
        removeint2At(0);
    }

    public org.w3c.dom.Node addint2(glsl_int value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int", value.toString());
    }

    public org.w3c.dom.Node addint2(String value) throws Exception {
        return addint2(new glsl_int(value));
    }

    public void insertint2At(glsl_int value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "int", index, value.toString());
    }

    public void insertint2At(String value, int index) throws Exception {
        insertint2At(new glsl_int(value), index);
    }

    public void replaceint2At(glsl_int value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "int", index, value.toString());
    }

    public void replaceint2At(String value, int index) throws Exception {
        replaceint2At(new glsl_int(value), index);
    }

    public static int getint22MinCount() {
        return 1;
    }

    public static int getint22MaxCount() {
        return 1;
    }

    public int getint22Count() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "int2");
    }

    public boolean hasint22() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int2");
    }

    public glsl_int2 newint22() {
        return new glsl_int2();
    }

    public glsl_int2 getint22At(int index) throws Exception {
        return new glsl_int2(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "int2", index)));
    }

    public org.w3c.dom.Node getStartingint22Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int2");
    }

    public org.w3c.dom.Node getAdvancedint22Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int2", curNode);
    }

    public glsl_int2 getint22ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new glsl_int2(getDomNodeValue(curNode));
    }

    public glsl_int2 getint22() throws Exception {
        return getint22At(0);
    }

    public void removeint22At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "int2", index);
    }

    public void removeint22() {
        removeint22At(0);
    }

    public org.w3c.dom.Node addint22(glsl_int2 value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int2", value.toString());
    }

    public org.w3c.dom.Node addint22(String value) throws Exception {
        return addint22(new glsl_int2(value));
    }

    public void insertint22At(glsl_int2 value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "int2", index, value.toString());
    }

    public void insertint22At(String value, int index) throws Exception {
        insertint22At(new glsl_int2(value), index);
    }

    public void replaceint22At(glsl_int2 value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "int2", index, value.toString());
    }

    public void replaceint22At(String value, int index) throws Exception {
        replaceint22At(new glsl_int2(value), index);
    }

    public static int getint3MinCount() {
        return 1;
    }

    public static int getint3MaxCount() {
        return 1;
    }

    public int getint3Count() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "int3");
    }

    public boolean hasint3() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int3");
    }

    public glsl_int3 newint3() {
        return new glsl_int3();
    }

    public glsl_int3 getint3At(int index) throws Exception {
        return new glsl_int3(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "int3", index)));
    }

    public org.w3c.dom.Node getStartingint3Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int3");
    }

    public org.w3c.dom.Node getAdvancedint3Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int3", curNode);
    }

    public glsl_int3 getint3ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new glsl_int3(getDomNodeValue(curNode));
    }

    public glsl_int3 getint3() throws Exception {
        return getint3At(0);
    }

    public void removeint3At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "int3", index);
    }

    public void removeint3() {
        removeint3At(0);
    }

    public org.w3c.dom.Node addint3(glsl_int3 value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int3", value.toString());
    }

    public org.w3c.dom.Node addint3(String value) throws Exception {
        return addint3(new glsl_int3(value));
    }

    public void insertint3At(glsl_int3 value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "int3", index, value.toString());
    }

    public void insertint3At(String value, int index) throws Exception {
        insertint3At(new glsl_int3(value), index);
    }

    public void replaceint3At(glsl_int3 value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "int3", index, value.toString());
    }

    public void replaceint3At(String value, int index) throws Exception {
        replaceint3At(new glsl_int3(value), index);
    }

    public static int getint4MinCount() {
        return 1;
    }

    public static int getint4MaxCount() {
        return 1;
    }

    public int getint4Count() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "int4");
    }

    public boolean hasint4() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int4");
    }

    public glsl_int4 newint4() {
        return new glsl_int4();
    }

    public glsl_int4 getint4At(int index) throws Exception {
        return new glsl_int4(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "int4", index)));
    }

    public org.w3c.dom.Node getStartingint4Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int4");
    }

    public org.w3c.dom.Node getAdvancedint4Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int4", curNode);
    }

    public glsl_int4 getint4ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new glsl_int4(getDomNodeValue(curNode));
    }

    public glsl_int4 getint4() throws Exception {
        return getint4At(0);
    }

    public void removeint4At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "int4", index);
    }

    public void removeint4() {
        removeint4At(0);
    }

    public org.w3c.dom.Node addint4(glsl_int4 value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "int4", value.toString());
    }

    public org.w3c.dom.Node addint4(String value) throws Exception {
        return addint4(new glsl_int4(value));
    }

    public void insertint4At(glsl_int4 value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "int4", index, value.toString());
    }

    public void insertint4At(String value, int index) throws Exception {
        insertint4At(new glsl_int4(value), index);
    }

    public void replaceint4At(glsl_int4 value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "int4", index, value.toString());
    }

    public void replaceint4At(String value, int index) throws Exception {
        replaceint4At(new glsl_int4(value), index);
    }

    public static int getsurfaceMinCount() {
        return 1;
    }

    public static int getsurfaceMaxCount() {
        return 1;
    }

    public int getsurfaceCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface");
    }

    public boolean hassurface() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface");
    }

    public glsl_surface_type newsurface() {
        return new glsl_surface_type(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "surface"));
    }

    public glsl_surface_type getsurfaceAt(int index) throws Exception {
        return new glsl_surface_type(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface", index));
    }

    public org.w3c.dom.Node getStartingsurfaceCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface");
    }

    public org.w3c.dom.Node getAdvancedsurfaceCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface", curNode);
    }

    public glsl_surface_type getsurfaceValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new glsl_surface_type(curNode);
    }

    public glsl_surface_type getsurface() throws Exception {
        return getsurfaceAt(0);
    }

    public void removesurfaceAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface", index);
    }

    public void removesurface() {
        removesurfaceAt(0);
    }

    public org.w3c.dom.Node addsurface(glsl_surface_type value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "surface", value);
    }

    public void insertsurfaceAt(glsl_surface_type value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "surface", index, value);
    }

    public void replacesurfaceAt(glsl_surface_type value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "surface", index, value);
    }

    public static int getsampler1DMinCount() {
        return 1;
    }

    public static int getsampler1DMaxCount() {
        return 1;
    }

    public int getsampler1DCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler1D");
    }

    public boolean hassampler1D() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler1D");
    }

    public gl_sampler1D newsampler1D() {
        return new gl_sampler1D(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "sampler1D"));
    }

    public gl_sampler1D getsampler1DAt(int index) throws Exception {
        return new gl_sampler1D(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler1D", index));
    }

    public org.w3c.dom.Node getStartingsampler1DCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler1D");
    }

    public org.w3c.dom.Node getAdvancedsampler1DCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler1D", curNode);
    }

    public gl_sampler1D getsampler1DValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new gl_sampler1D(curNode);
    }

    public gl_sampler1D getsampler1D() throws Exception {
        return getsampler1DAt(0);
    }

    public void removesampler1DAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler1D", index);
    }

    public void removesampler1D() {
        removesampler1DAt(0);
    }

    public org.w3c.dom.Node addsampler1D(gl_sampler1D value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "sampler1D", value);
    }

    public void insertsampler1DAt(gl_sampler1D value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "sampler1D", index, value);
    }

    public void replacesampler1DAt(gl_sampler1D value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "sampler1D", index, value);
    }

    public static int getsampler2DMinCount() {
        return 1;
    }

    public static int getsampler2DMaxCount() {
        return 1;
    }

    public int getsampler2DCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler2D");
    }

    public boolean hassampler2D() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler2D");
    }

    public gl_sampler2D newsampler2D() {
        return new gl_sampler2D(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "sampler2D"));
    }

    public gl_sampler2D getsampler2DAt(int index) throws Exception {
        return new gl_sampler2D(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler2D", index));
    }

    public org.w3c.dom.Node getStartingsampler2DCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler2D");
    }

    public org.w3c.dom.Node getAdvancedsampler2DCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler2D", curNode);
    }

    public gl_sampler2D getsampler2DValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new gl_sampler2D(curNode);
    }

    public gl_sampler2D getsampler2D() throws Exception {
        return getsampler2DAt(0);
    }

    public void removesampler2DAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler2D", index);
    }

    public void removesampler2D() {
        removesampler2DAt(0);
    }

    public org.w3c.dom.Node addsampler2D(gl_sampler2D value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "sampler2D", value);
    }

    public void insertsampler2DAt(gl_sampler2D value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "sampler2D", index, value);
    }

    public void replacesampler2DAt(gl_sampler2D value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "sampler2D", index, value);
    }

    public static int getsampler3DMinCount() {
        return 1;
    }

    public static int getsampler3DMaxCount() {
        return 1;
    }

    public int getsampler3DCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler3D");
    }

    public boolean hassampler3D() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler3D");
    }

    public gl_sampler3D newsampler3D() {
        return new gl_sampler3D(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "sampler3D"));
    }

    public gl_sampler3D getsampler3DAt(int index) throws Exception {
        return new gl_sampler3D(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler3D", index));
    }

    public org.w3c.dom.Node getStartingsampler3DCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler3D");
    }

    public org.w3c.dom.Node getAdvancedsampler3DCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler3D", curNode);
    }

    public gl_sampler3D getsampler3DValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new gl_sampler3D(curNode);
    }

    public gl_sampler3D getsampler3D() throws Exception {
        return getsampler3DAt(0);
    }

    public void removesampler3DAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler3D", index);
    }

    public void removesampler3D() {
        removesampler3DAt(0);
    }

    public org.w3c.dom.Node addsampler3D(gl_sampler3D value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "sampler3D", value);
    }

    public void insertsampler3DAt(gl_sampler3D value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "sampler3D", index, value);
    }

    public void replacesampler3DAt(gl_sampler3D value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "sampler3D", index, value);
    }

    public static int getsamplerCUBEMinCount() {
        return 1;
    }

    public static int getsamplerCUBEMaxCount() {
        return 1;
    }

    public int getsamplerCUBECount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerCUBE");
    }

    public boolean hassamplerCUBE() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerCUBE");
    }

    public gl_samplerCUBE newsamplerCUBE() {
        return new gl_samplerCUBE(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "samplerCUBE"));
    }

    public gl_samplerCUBE getsamplerCUBEAt(int index) throws Exception {
        return new gl_samplerCUBE(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerCUBE", index));
    }

    public org.w3c.dom.Node getStartingsamplerCUBECursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerCUBE");
    }

    public org.w3c.dom.Node getAdvancedsamplerCUBECursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerCUBE", curNode);
    }

    public gl_samplerCUBE getsamplerCUBEValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new gl_samplerCUBE(curNode);
    }

    public gl_samplerCUBE getsamplerCUBE() throws Exception {
        return getsamplerCUBEAt(0);
    }

    public void removesamplerCUBEAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerCUBE", index);
    }

    public void removesamplerCUBE() {
        removesamplerCUBEAt(0);
    }

    public org.w3c.dom.Node addsamplerCUBE(gl_samplerCUBE value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "samplerCUBE", value);
    }

    public void insertsamplerCUBEAt(gl_samplerCUBE value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "samplerCUBE", index, value);
    }

    public void replacesamplerCUBEAt(gl_samplerCUBE value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "samplerCUBE", index, value);
    }

    public static int getsamplerRECTMinCount() {
        return 1;
    }

    public static int getsamplerRECTMaxCount() {
        return 1;
    }

    public int getsamplerRECTCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerRECT");
    }

    public boolean hassamplerRECT() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerRECT");
    }

    public gl_samplerRECT newsamplerRECT() {
        return new gl_samplerRECT(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "samplerRECT"));
    }

    public gl_samplerRECT getsamplerRECTAt(int index) throws Exception {
        return new gl_samplerRECT(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerRECT", index));
    }

    public org.w3c.dom.Node getStartingsamplerRECTCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerRECT");
    }

    public org.w3c.dom.Node getAdvancedsamplerRECTCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerRECT", curNode);
    }

    public gl_samplerRECT getsamplerRECTValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new gl_samplerRECT(curNode);
    }

    public gl_samplerRECT getsamplerRECT() throws Exception {
        return getsamplerRECTAt(0);
    }

    public void removesamplerRECTAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerRECT", index);
    }

    public void removesamplerRECT() {
        removesamplerRECTAt(0);
    }

    public org.w3c.dom.Node addsamplerRECT(gl_samplerRECT value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "samplerRECT", value);
    }

    public void insertsamplerRECTAt(gl_samplerRECT value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "samplerRECT", index, value);
    }

    public void replacesamplerRECTAt(gl_samplerRECT value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "samplerRECT", index, value);
    }

    public static int getsamplerDEPTHMinCount() {
        return 1;
    }

    public static int getsamplerDEPTHMaxCount() {
        return 1;
    }

    public int getsamplerDEPTHCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerDEPTH");
    }

    public boolean hassamplerDEPTH() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerDEPTH");
    }

    public gl_samplerDEPTH newsamplerDEPTH() {
        return new gl_samplerDEPTH(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "samplerDEPTH"));
    }

    public gl_samplerDEPTH getsamplerDEPTHAt(int index) throws Exception {
        return new gl_samplerDEPTH(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerDEPTH", index));
    }

    public org.w3c.dom.Node getStartingsamplerDEPTHCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerDEPTH");
    }

    public org.w3c.dom.Node getAdvancedsamplerDEPTHCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerDEPTH", curNode);
    }

    public gl_samplerDEPTH getsamplerDEPTHValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new gl_samplerDEPTH(curNode);
    }

    public gl_samplerDEPTH getsamplerDEPTH() throws Exception {
        return getsamplerDEPTHAt(0);
    }

    public void removesamplerDEPTHAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "samplerDEPTH", index);
    }

    public void removesamplerDEPTH() {
        removesamplerDEPTHAt(0);
    }

    public org.w3c.dom.Node addsamplerDEPTH(gl_samplerDEPTH value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "samplerDEPTH", value);
    }

    public void insertsamplerDEPTHAt(gl_samplerDEPTH value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "samplerDEPTH", index, value);
    }

    public void replacesamplerDEPTHAt(gl_samplerDEPTH value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "samplerDEPTH", index, value);
    }

    public static int getenumMinCount() {
        return 1;
    }

    public static int getenumMaxCount() {
        return 1;
    }

    public int getenumCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "enum");
    }

    public boolean hasenum() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "enum");
    }

    public gl_enumeration newenum() {
        return new gl_enumeration();
    }

    public gl_enumeration getenumAt(int index) throws Exception {
        return new gl_enumeration(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "enum", index)));
    }

    public org.w3c.dom.Node getStartingenumCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "enum");
    }

    public org.w3c.dom.Node getAdvancedenumCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "enum", curNode);
    }

    public gl_enumeration getenumValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new com.jmex.xml.xml.XmlException("Out of range"); else return new gl_enumeration(getDomNodeValue(curNode));
    }

    public gl_enumeration getenum() throws Exception {
        return getenumAt(0);
    }

    public void removeenumAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "enum", index);
    }

    public void removeenum() {
        removeenumAt(0);
    }

    public org.w3c.dom.Node addenum(gl_enumeration value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "enum", value.toString());
    }

    public org.w3c.dom.Node addenum(String value) throws Exception {
        return addenum(new gl_enumeration(value));
    }

    public void insertenumAt(gl_enumeration value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "enum", index, value.toString());
    }

    public void insertenumAt(String value, int index) throws Exception {
        insertenumAt(new gl_enumeration(value), index);
    }

    public void replaceenumAt(gl_enumeration value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "enum", index, value.toString());
    }

    public void replaceenumAt(String value, int index) throws Exception {
        replaceenumAt(new gl_enumeration(value), index);
    }
}
