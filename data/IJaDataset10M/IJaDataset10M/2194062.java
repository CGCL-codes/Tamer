package homura.hde.ext.model.collada.schema;

import homura.hde.util.xml.types.SchemaID;
import homura.hde.util.xml.types.SchemaNCName;

public class effectType extends homura.hde.util.xml.xml.Node {

    public effectType(effectType node) {
        super(node);
    }

    public effectType(org.w3c.dom.Node node) {
        super(node);
    }

    public effectType(org.w3c.dom.Document doc) {
        super(doc);
    }

    public effectType(homura.hde.util.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "id"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "id", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Attribute, null, "name"); tmpNode != null; tmpNode = getDomNextChild(Attribute, null, "name", tmpNode)) {
            internalAdjustPrefix(tmpNode, false);
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "asset"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "asset", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new assetType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "annotate"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "annotate", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new fx_annotate_common(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "image"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "image", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new imageType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "newparam"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "newparam", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new fx_newparam_common(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_GLES"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_GLES", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new profile_GLESType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_COMMON"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_COMMON", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new profile_COMMONType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_CG"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_CG", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new profile_CGType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_GLSL"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_GLSL", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new profile_GLSLType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new extraType(tmpNode).adjustPrefix();
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "effect");
    }

    public static int getidMinCount() {
        return 1;
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
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_annotate_common(curNode);
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

    public static int getimageMinCount() {
        return 0;
    }

    public static int getimageMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int getimageCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "image");
    }

    public boolean hasimage() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "image");
    }

    public imageType newimage() {
        return new imageType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "image"));
    }

    public imageType getimageAt(int index) throws Exception {
        return new imageType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "image", index));
    }

    public org.w3c.dom.Node getStartingimageCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "image");
    }

    public org.w3c.dom.Node getAdvancedimageCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "image", curNode);
    }

    public imageType getimageValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new imageType(curNode);
    }

    public imageType getimage() throws Exception {
        return getimageAt(0);
    }

    public void removeimageAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "image", index);
    }

    public void removeimage() {
        while (hasimage()) removeimageAt(0);
    }

    public org.w3c.dom.Node addimage(imageType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "image", value);
    }

    public void insertimageAt(imageType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "image", index, value);
    }

    public void replaceimageAt(imageType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "image", index, value);
    }

    public static int getnewparamMinCount() {
        return 0;
    }

    public static int getnewparamMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int getnewparamCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "newparam");
    }

    public boolean hasnewparam() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "newparam");
    }

    public fx_newparam_common newnewparam() {
        return new fx_newparam_common(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "newparam"));
    }

    public fx_newparam_common getnewparamAt(int index) throws Exception {
        return new fx_newparam_common(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "newparam", index));
    }

    public org.w3c.dom.Node getStartingnewparamCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "newparam");
    }

    public org.w3c.dom.Node getAdvancednewparamCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "newparam", curNode);
    }

    public fx_newparam_common getnewparamValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new fx_newparam_common(curNode);
    }

    public fx_newparam_common getnewparam() throws Exception {
        return getnewparamAt(0);
    }

    public void removenewparamAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "newparam", index);
    }

    public void removenewparam() {
        while (hasnewparam()) removenewparamAt(0);
    }

    public org.w3c.dom.Node addnewparam(fx_newparam_common value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "newparam", value);
    }

    public void insertnewparamAt(fx_newparam_common value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "newparam", index, value);
    }

    public void replacenewparamAt(fx_newparam_common value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "newparam", index, value);
    }

    public static int getprofile_GLESMinCount() {
        return 1;
    }

    public static int getprofile_GLESMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int getprofile_GLESCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_GLES");
    }

    public boolean hasprofile_GLES() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_GLES");
    }

    public profile_GLESType newprofile_GLES() {
        return new profile_GLESType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "profile_GLES"));
    }

    public profile_GLESType getprofile_GLESAt(int index) throws Exception {
        return new profile_GLESType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_GLES", index));
    }

    public org.w3c.dom.Node getStartingprofile_GLESCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_GLES");
    }

    public org.w3c.dom.Node getAdvancedprofile_GLESCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_GLES", curNode);
    }

    public profile_GLESType getprofile_GLESValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new profile_GLESType(curNode);
    }

    public profile_GLESType getprofile_GLES() throws Exception {
        return getprofile_GLESAt(0);
    }

    public void removeprofile_GLESAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_GLES", index);
    }

    public void removeprofile_GLES() {
        while (hasprofile_GLES()) removeprofile_GLESAt(0);
    }

    public org.w3c.dom.Node addprofile_GLES(profile_GLESType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "profile_GLES", value);
    }

    public void insertprofile_GLESAt(profile_GLESType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "profile_GLES", index, value);
    }

    public void replaceprofile_GLESAt(profile_GLESType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "profile_GLES", index, value);
    }

    public static int getprofile_COMMONMinCount() {
        return 1;
    }

    public static int getprofile_COMMONMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int getprofile_COMMONCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_COMMON");
    }

    public boolean hasprofile_COMMON() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_COMMON");
    }

    public profile_COMMONType newprofile_COMMON() {
        return new profile_COMMONType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "profile_COMMON"));
    }

    public profile_COMMONType getprofile_COMMONAt(int index) throws Exception {
        return new profile_COMMONType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_COMMON", index));
    }

    public org.w3c.dom.Node getStartingprofile_COMMONCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_COMMON");
    }

    public org.w3c.dom.Node getAdvancedprofile_COMMONCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_COMMON", curNode);
    }

    public profile_COMMONType getprofile_COMMONValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new profile_COMMONType(curNode);
    }

    public profile_COMMONType getprofile_COMMON() throws Exception {
        return getprofile_COMMONAt(0);
    }

    public void removeprofile_COMMONAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_COMMON", index);
    }

    public void removeprofile_COMMON() {
        while (hasprofile_COMMON()) removeprofile_COMMONAt(0);
    }

    public org.w3c.dom.Node addprofile_COMMON(profile_COMMONType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "profile_COMMON", value);
    }

    public void insertprofile_COMMONAt(profile_COMMONType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "profile_COMMON", index, value);
    }

    public void replaceprofile_COMMONAt(profile_COMMONType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "profile_COMMON", index, value);
    }

    public static int getprofile_CGMinCount() {
        return 1;
    }

    public static int getprofile_CGMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int getprofile_CGCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_CG");
    }

    public boolean hasprofile_CG() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_CG");
    }

    public profile_CGType newprofile_CG() {
        return new profile_CGType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "profile_CG"));
    }

    public profile_CGType getprofile_CGAt(int index) throws Exception {
        return new profile_CGType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_CG", index));
    }

    public org.w3c.dom.Node getStartingprofile_CGCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_CG");
    }

    public org.w3c.dom.Node getAdvancedprofile_CGCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_CG", curNode);
    }

    public profile_CGType getprofile_CGValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new profile_CGType(curNode);
    }

    public profile_CGType getprofile_CG() throws Exception {
        return getprofile_CGAt(0);
    }

    public void removeprofile_CGAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_CG", index);
    }

    public void removeprofile_CG() {
        while (hasprofile_CG()) removeprofile_CGAt(0);
    }

    public org.w3c.dom.Node addprofile_CG(profile_CGType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "profile_CG", value);
    }

    public void insertprofile_CGAt(profile_CGType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "profile_CG", index, value);
    }

    public void replaceprofile_CGAt(profile_CGType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "profile_CG", index, value);
    }

    public static int getprofile_GLSLMinCount() {
        return 1;
    }

    public static int getprofile_GLSLMaxCount() {
        return Integer.MAX_VALUE;
    }

    public int getprofile_GLSLCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_GLSL");
    }

    public boolean hasprofile_GLSL() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_GLSL");
    }

    public profile_GLSLType newprofile_GLSL() {
        return new profile_GLSLType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "profile_GLSL"));
    }

    public profile_GLSLType getprofile_GLSLAt(int index) throws Exception {
        return new profile_GLSLType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_GLSL", index));
    }

    public org.w3c.dom.Node getStartingprofile_GLSLCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_GLSL");
    }

    public org.w3c.dom.Node getAdvancedprofile_GLSLCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_GLSL", curNode);
    }

    public profile_GLSLType getprofile_GLSLValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new profile_GLSLType(curNode);
    }

    public profile_GLSLType getprofile_GLSL() throws Exception {
        return getprofile_GLSLAt(0);
    }

    public void removeprofile_GLSLAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "profile_GLSL", index);
    }

    public void removeprofile_GLSL() {
        while (hasprofile_GLSL()) removeprofile_GLSLAt(0);
    }

    public org.w3c.dom.Node addprofile_GLSL(profile_GLSLType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "profile_GLSL", value);
    }

    public void insertprofile_GLSLAt(profile_GLSLType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "profile_GLSL", index, value);
    }

    public void replaceprofile_GLSLAt(profile_GLSLType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "profile_GLSL", index, value);
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
