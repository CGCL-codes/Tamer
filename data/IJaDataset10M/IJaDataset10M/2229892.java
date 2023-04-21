package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaNCName;

public class common_newparam_type extends homura.hde.util.xml.xml.Node {

    public common_newparam_type(common_newparam_type node) {
        super(node);
    }

    public common_newparam_type(org.w3c.dom.Node node) {
        super(node);
    }

    public common_newparam_type(org.w3c.dom.Document doc) {
        super(doc);
    }

    public common_newparam_type(homura.hde.util.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "sid"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "sid", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "semantic"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "semantic", tmpNode)) {
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
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new fx_surface_common(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler2D"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler2D", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new fx_sampler2D_common(tmpNode).adjustPrefix();
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "common_newparam_type");
    }

    public static int getsidMinCount() {
        return 1;
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
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "semantic");
    }

    public boolean hassemantic() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "semantic");
    }

    public SchemaNCName newsemantic() {
        return new SchemaNCName();
    }

    public SchemaNCName getsemanticAt(int index) throws Exception {
        return new SchemaNCName(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "semantic", index)));
    }

    public org.w3c.dom.Node getStartingsemanticCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "semantic");
    }

    public org.w3c.dom.Node getAdvancedsemanticCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "semantic", curNode);
    }

    public SchemaNCName getsemanticValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new SchemaNCName(getDomNodeValue(curNode));
    }

    public SchemaNCName getsemantic() throws Exception {
        return getsemanticAt(0);
    }

    public void removesemanticAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "semantic", index);
    }

    public void removesemantic() {
        removesemanticAt(0);
    }

    public org.w3c.dom.Node addsemantic(SchemaNCName value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "semantic", value.toString());
    }

    public org.w3c.dom.Node addsemantic(String value) throws Exception {
        return addsemantic(new SchemaNCName(value));
    }

    public void insertsemanticAt(SchemaNCName value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "semantic", index, value.toString());
    }

    public void insertsemanticAt(String value, int index) throws Exception {
        insertsemanticAt(new SchemaNCName(value), index);
    }

    public void replacesemanticAt(SchemaNCName value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "semantic", index, value.toString());
    }

    public void replacesemanticAt(String value, int index) throws Exception {
        replacesemanticAt(new SchemaNCName(value), index);
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

    public float2 newfloat2() {
        return new float2();
    }

    public float2 getfloat2At(int index) throws Exception {
        return new float2(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float", index)));
    }

    public org.w3c.dom.Node getStartingfloat2Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float");
    }

    public org.w3c.dom.Node getAdvancedfloat2Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float", curNode);
    }

    public float2 getfloat2ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new float2(getDomNodeValue(curNode));
    }

    public float2 getfloat2() throws Exception {
        return getfloat2At(0);
    }

    public void removefloat2At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float", index);
    }

    public void removefloat2() {
        removefloat2At(0);
    }

    public org.w3c.dom.Node addfloat2(float2 value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float", value.toString());
    }

    public org.w3c.dom.Node addfloat2(String value) throws Exception {
        return addfloat2(new float2(value));
    }

    public void insertfloat2At(float2 value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float", index, value.toString());
    }

    public void insertfloat2At(String value, int index) throws Exception {
        insertfloat2At(new float2(value), index);
    }

    public void replacefloat2At(float2 value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float", index, value.toString());
    }

    public void replacefloat2At(String value, int index) throws Exception {
        replacefloat2At(new float2(value), index);
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

    public float22 newfloat22() {
        return new float22();
    }

    public float22 getfloat22At(int index) throws Exception {
        return new float22(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2", index)));
    }

    public org.w3c.dom.Node getStartingfloat22Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2");
    }

    public org.w3c.dom.Node getAdvancedfloat22Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2", curNode);
    }

    public float22 getfloat22ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new float22(getDomNodeValue(curNode));
    }

    public float22 getfloat22() throws Exception {
        return getfloat22At(0);
    }

    public void removefloat22At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2", index);
    }

    public void removefloat22() {
        removefloat22At(0);
    }

    public org.w3c.dom.Node addfloat22(float22 value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2", value.toString());
    }

    public org.w3c.dom.Node addfloat22(String value) throws Exception {
        return addfloat22(new float22(value));
    }

    public void insertfloat22At(float22 value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2", index, value.toString());
    }

    public void insertfloat22At(String value, int index) throws Exception {
        insertfloat22At(new float22(value), index);
    }

    public void replacefloat22At(float22 value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float2", index, value.toString());
    }

    public void replacefloat22At(String value, int index) throws Exception {
        replacefloat22At(new float22(value), index);
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

    public float3 newfloat3() {
        return new float3();
    }

    public float3 getfloat3At(int index) throws Exception {
        return new float3(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3", index)));
    }

    public org.w3c.dom.Node getStartingfloat3Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3");
    }

    public org.w3c.dom.Node getAdvancedfloat3Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3", curNode);
    }

    public float3 getfloat3ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new float3(getDomNodeValue(curNode));
    }

    public float3 getfloat3() throws Exception {
        return getfloat3At(0);
    }

    public void removefloat3At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3", index);
    }

    public void removefloat3() {
        removefloat3At(0);
    }

    public org.w3c.dom.Node addfloat3(float3 value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3", value.toString());
    }

    public org.w3c.dom.Node addfloat3(String value) throws Exception {
        return addfloat3(new float3(value));
    }

    public void insertfloat3At(float3 value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3", index, value.toString());
    }

    public void insertfloat3At(String value, int index) throws Exception {
        insertfloat3At(new float3(value), index);
    }

    public void replacefloat3At(float3 value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float3", index, value.toString());
    }

    public void replacefloat3At(String value, int index) throws Exception {
        replacefloat3At(new float3(value), index);
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

    public float4 newfloat4() {
        return new float4();
    }

    public float4 getfloat4At(int index) throws Exception {
        return new float4(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4", index)));
    }

    public org.w3c.dom.Node getStartingfloat4Cursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4");
    }

    public org.w3c.dom.Node getAdvancedfloat4Cursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4", curNode);
    }

    public float4 getfloat4ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new float4(getDomNodeValue(curNode));
    }

    public float4 getfloat4() throws Exception {
        return getfloat4At(0);
    }

    public void removefloat4At(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4", index);
    }

    public void removefloat4() {
        removefloat4At(0);
    }

    public org.w3c.dom.Node addfloat4(float4 value) {
        if (value.isNull()) return null;
        return appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4", value.toString());
    }

    public org.w3c.dom.Node addfloat4(String value) throws Exception {
        return addfloat4(new float4(value));
    }

    public void insertfloat4At(float4 value, int index) {
        insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4", index, value.toString());
    }

    public void insertfloat4At(String value, int index) throws Exception {
        insertfloat4At(new float4(value), index);
    }

    public void replacefloat4At(float4 value, int index) {
        replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "float4", index, value.toString());
    }

    public void replacefloat4At(String value, int index) throws Exception {
        replacefloat4At(new float4(value), index);
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

    public fx_surface_common newsurface() {
        return new fx_surface_common(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "surface"));
    }

    public fx_surface_common getsurfaceAt(int index) throws Exception {
        return new fx_surface_common(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface", index));
    }

    public org.w3c.dom.Node getStartingsurfaceCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface");
    }

    public org.w3c.dom.Node getAdvancedsurfaceCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface", curNode);
    }

    public fx_surface_common getsurfaceValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_surface_common(curNode);
    }

    public fx_surface_common getsurface() throws Exception {
        return getsurfaceAt(0);
    }

    public void removesurfaceAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface", index);
    }

    public void removesurface() {
        removesurfaceAt(0);
    }

    public org.w3c.dom.Node addsurface(fx_surface_common value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "surface", value);
    }

    public void insertsurfaceAt(fx_surface_common value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "surface", index, value);
    }

    public void replacesurfaceAt(fx_surface_common value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "surface", index, value);
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

    public fx_sampler2D_common newsampler2D() {
        return new fx_sampler2D_common(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "sampler2D"));
    }

    public fx_sampler2D_common getsampler2DAt(int index) throws Exception {
        return new fx_sampler2D_common(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler2D", index));
    }

    public org.w3c.dom.Node getStartingsampler2DCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler2D");
    }

    public org.w3c.dom.Node getAdvancedsampler2DCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler2D", curNode);
    }

    public fx_sampler2D_common getsampler2DValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_sampler2D_common(curNode);
    }

    public fx_sampler2D_common getsampler2D() throws Exception {
        return getsampler2DAt(0);
    }

    public void removesampler2DAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler2D", index);
    }

    public void removesampler2D() {
        removesampler2DAt(0);
    }

    public org.w3c.dom.Node addsampler2D(fx_sampler2D_common value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "sampler2D", value);
    }

    public void insertsampler2DAt(fx_sampler2D_common value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "sampler2D", index, value);
    }

    public void replacesampler2DAt(fx_sampler2D_common value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "sampler2D", index, value);
    }
}
