package homura.hde.ext.model.collada.schema;

public class shapeType2 extends homura.hde.util.xml.xml.Node {

    public shapeType2(shapeType2 node) {
        super(node);
    }

    public shapeType2(org.w3c.dom.Node node) {
        super(node);
    }

    public shapeType2(org.w3c.dom.Document doc) {
        super(doc);
    }

    public shapeType2(homura.hde.util.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "hollow"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "hollow", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new hollowType2(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mass"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mass", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new TargetableFloat(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "density"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "density", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new TargetableFloat(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new InstanceWithExtra(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "physics_material"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "physics_material", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new physics_materialType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_geometry"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_geometry", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new instance_geometryType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "plane"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "plane", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new planeType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "box"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "box", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new boxType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sphere"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sphere", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new sphereType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "cylinder"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "cylinder", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new cylinderType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "tapered_cylinder"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "tapered_cylinder", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new tapered_cylinderType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "capsule"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "capsule", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new capsuleType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "tapered_capsule"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "tapered_capsule", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new tapered_capsuleType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "translate"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "translate", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new TargetableFloat3(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "rotate"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "rotate", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new rotateType(tmpNode).adjustPrefix();
        }
        for (org.w3c.dom.Node tmpNode = getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra"); tmpNode != null; tmpNode = getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra", tmpNode)) {
            internalAdjustPrefix(tmpNode, true);
            new extraType(tmpNode).adjustPrefix();
        }
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "shape");
    }

    public static int gethollowMinCount() {
        return 0;
    }

    public static int gethollowMaxCount() {
        return 1;
    }

    public int gethollowCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "hollow");
    }

    public boolean hashollow() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "hollow");
    }

    public hollowType2 newhollow() {
        return new hollowType2(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "hollow"));
    }

    public hollowType2 gethollowAt(int index) throws Exception {
        return new hollowType2(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "hollow", index));
    }

    public org.w3c.dom.Node getStartinghollowCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "hollow");
    }

    public org.w3c.dom.Node getAdvancedhollowCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "hollow", curNode);
    }

    public hollowType2 gethollowValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new hollowType2(curNode);
    }

    public hollowType2 gethollow() throws Exception {
        return gethollowAt(0);
    }

    public void removehollowAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "hollow", index);
    }

    public void removehollow() {
        removehollowAt(0);
    }

    public org.w3c.dom.Node addhollow(hollowType2 value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "hollow", value);
    }

    public void inserthollowAt(hollowType2 value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "hollow", index, value);
    }

    public void replacehollowAt(hollowType2 value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "hollow", index, value);
    }

    public static int getmassMinCount() {
        return 0;
    }

    public static int getmassMaxCount() {
        return 1;
    }

    public int getmassCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "mass");
    }

    public boolean hasmass() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mass");
    }

    public TargetableFloat newmass() {
        return new TargetableFloat(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "mass"));
    }

    public TargetableFloat getmassAt(int index) throws Exception {
        return new TargetableFloat(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mass", index));
    }

    public org.w3c.dom.Node getStartingmassCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mass");
    }

    public org.w3c.dom.Node getAdvancedmassCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mass", curNode);
    }

    public TargetableFloat getmassValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new TargetableFloat(curNode);
    }

    public TargetableFloat getmass() throws Exception {
        return getmassAt(0);
    }

    public void removemassAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mass", index);
    }

    public void removemass() {
        removemassAt(0);
    }

    public org.w3c.dom.Node addmass(TargetableFloat value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "mass", value);
    }

    public void insertmassAt(TargetableFloat value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "mass", index, value);
    }

    public void replacemassAt(TargetableFloat value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "mass", index, value);
    }

    public static int getdensityMinCount() {
        return 0;
    }

    public static int getdensityMaxCount() {
        return 1;
    }

    public int getdensityCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "density");
    }

    public boolean hasdensity() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "density");
    }

    public TargetableFloat newdensity() {
        return new TargetableFloat(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "density"));
    }

    public TargetableFloat getdensityAt(int index) throws Exception {
        return new TargetableFloat(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "density", index));
    }

    public org.w3c.dom.Node getStartingdensityCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "density");
    }

    public org.w3c.dom.Node getAdvanceddensityCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "density", curNode);
    }

    public TargetableFloat getdensityValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new TargetableFloat(curNode);
    }

    public TargetableFloat getdensity() throws Exception {
        return getdensityAt(0);
    }

    public void removedensityAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "density", index);
    }

    public void removedensity() {
        removedensityAt(0);
    }

    public org.w3c.dom.Node adddensity(TargetableFloat value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "density", value);
    }

    public void insertdensityAt(TargetableFloat value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "density", index, value);
    }

    public void replacedensityAt(TargetableFloat value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "density", index, value);
    }

    public static int getinstance_physics_materialMinCount() {
        return 1;
    }

    public static int getinstance_physics_materialMaxCount() {
        return 1;
    }

    public int getinstance_physics_materialCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material");
    }

    public boolean hasinstance_physics_material() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material");
    }

    public InstanceWithExtra newinstance_physics_material() {
        return new InstanceWithExtra(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material"));
    }

    public InstanceWithExtra getinstance_physics_materialAt(int index) throws Exception {
        return new InstanceWithExtra(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material", index));
    }

    public org.w3c.dom.Node getStartinginstance_physics_materialCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material");
    }

    public org.w3c.dom.Node getAdvancedinstance_physics_materialCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material", curNode);
    }

    public InstanceWithExtra getinstance_physics_materialValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new InstanceWithExtra(curNode);
    }

    public InstanceWithExtra getinstance_physics_material() throws Exception {
        return getinstance_physics_materialAt(0);
    }

    public void removeinstance_physics_materialAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material", index);
    }

    public void removeinstance_physics_material() {
        removeinstance_physics_materialAt(0);
    }

    public org.w3c.dom.Node addinstance_physics_material(InstanceWithExtra value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material", value);
    }

    public void insertinstance_physics_materialAt(InstanceWithExtra value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material", index, value);
    }

    public void replaceinstance_physics_materialAt(InstanceWithExtra value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material", index, value);
    }

    public static int getphysics_materialMinCount() {
        return 1;
    }

    public static int getphysics_materialMaxCount() {
        return 1;
    }

    public int getphysics_materialCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "physics_material");
    }

    public boolean hasphysics_material() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "physics_material");
    }

    public physics_materialType newphysics_material() {
        return new physics_materialType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "physics_material"));
    }

    public physics_materialType getphysics_materialAt(int index) throws Exception {
        return new physics_materialType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "physics_material", index));
    }

    public org.w3c.dom.Node getStartingphysics_materialCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "physics_material");
    }

    public org.w3c.dom.Node getAdvancedphysics_materialCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "physics_material", curNode);
    }

    public physics_materialType getphysics_materialValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new physics_materialType(curNode);
    }

    public physics_materialType getphysics_material() throws Exception {
        return getphysics_materialAt(0);
    }

    public void removephysics_materialAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "physics_material", index);
    }

    public void removephysics_material() {
        removephysics_materialAt(0);
    }

    public org.w3c.dom.Node addphysics_material(physics_materialType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "physics_material", value);
    }

    public void insertphysics_materialAt(physics_materialType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "physics_material", index, value);
    }

    public void replacephysics_materialAt(physics_materialType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "physics_material", index, value);
    }

    public static int getinstance_geometryMinCount() {
        return 1;
    }

    public static int getinstance_geometryMaxCount() {
        return 1;
    }

    public int getinstance_geometryCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_geometry");
    }

    public boolean hasinstance_geometry() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_geometry");
    }

    public instance_geometryType newinstance_geometry() {
        return new instance_geometryType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "instance_geometry"));
    }

    public instance_geometryType getinstance_geometryAt(int index) throws Exception {
        return new instance_geometryType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_geometry", index));
    }

    public org.w3c.dom.Node getStartinginstance_geometryCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_geometry");
    }

    public org.w3c.dom.Node getAdvancedinstance_geometryCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_geometry", curNode);
    }

    public instance_geometryType getinstance_geometryValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new instance_geometryType(curNode);
    }

    public instance_geometryType getinstance_geometry() throws Exception {
        return getinstance_geometryAt(0);
    }

    public void removeinstance_geometryAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_geometry", index);
    }

    public void removeinstance_geometry() {
        removeinstance_geometryAt(0);
    }

    public org.w3c.dom.Node addinstance_geometry(instance_geometryType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "instance_geometry", value);
    }

    public void insertinstance_geometryAt(instance_geometryType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "instance_geometry", index, value);
    }

    public void replaceinstance_geometryAt(instance_geometryType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "instance_geometry", index, value);
    }

    public static int getplaneMinCount() {
        return 1;
    }

    public static int getplaneMaxCount() {
        return 1;
    }

    public int getplaneCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "plane");
    }

    public boolean hasplane() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "plane");
    }

    public planeType newplane() {
        return new planeType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "plane"));
    }

    public planeType getplaneAt(int index) throws Exception {
        return new planeType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "plane", index));
    }

    public org.w3c.dom.Node getStartingplaneCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "plane");
    }

    public org.w3c.dom.Node getAdvancedplaneCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "plane", curNode);
    }

    public planeType getplaneValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new planeType(curNode);
    }

    public planeType getplane() throws Exception {
        return getplaneAt(0);
    }

    public void removeplaneAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "plane", index);
    }

    public void removeplane() {
        removeplaneAt(0);
    }

    public org.w3c.dom.Node addplane(planeType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "plane", value);
    }

    public void insertplaneAt(planeType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "plane", index, value);
    }

    public void replaceplaneAt(planeType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "plane", index, value);
    }

    public static int getboxMinCount() {
        return 1;
    }

    public static int getboxMaxCount() {
        return 1;
    }

    public int getboxCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "box");
    }

    public boolean hasbox() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "box");
    }

    public boxType newbox() {
        return new boxType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "box"));
    }

    public boxType getboxAt(int index) throws Exception {
        return new boxType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "box", index));
    }

    public org.w3c.dom.Node getStartingboxCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "box");
    }

    public org.w3c.dom.Node getAdvancedboxCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "box", curNode);
    }

    public boxType getboxValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new boxType(curNode);
    }

    public boxType getbox() throws Exception {
        return getboxAt(0);
    }

    public void removeboxAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "box", index);
    }

    public void removebox() {
        removeboxAt(0);
    }

    public org.w3c.dom.Node addbox(boxType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "box", value);
    }

    public void insertboxAt(boxType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "box", index, value);
    }

    public void replaceboxAt(boxType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "box", index, value);
    }

    public static int getsphereMinCount() {
        return 1;
    }

    public static int getsphereMaxCount() {
        return 1;
    }

    public int getsphereCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "sphere");
    }

    public boolean hassphere() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sphere");
    }

    public sphereType newsphere() {
        return new sphereType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "sphere"));
    }

    public sphereType getsphereAt(int index) throws Exception {
        return new sphereType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "sphere", index));
    }

    public org.w3c.dom.Node getStartingsphereCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sphere");
    }

    public org.w3c.dom.Node getAdvancedsphereCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sphere", curNode);
    }

    public sphereType getsphereValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new sphereType(curNode);
    }

    public sphereType getsphere() throws Exception {
        return getsphereAt(0);
    }

    public void removesphereAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "sphere", index);
    }

    public void removesphere() {
        removesphereAt(0);
    }

    public org.w3c.dom.Node addsphere(sphereType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "sphere", value);
    }

    public void insertsphereAt(sphereType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "sphere", index, value);
    }

    public void replacesphereAt(sphereType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "sphere", index, value);
    }

    public static int getcylinderMinCount() {
        return 1;
    }

    public static int getcylinderMaxCount() {
        return 1;
    }

    public int getcylinderCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "cylinder");
    }

    public boolean hascylinder() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "cylinder");
    }

    public cylinderType newcylinder() {
        return new cylinderType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "cylinder"));
    }

    public cylinderType getcylinderAt(int index) throws Exception {
        return new cylinderType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "cylinder", index));
    }

    public org.w3c.dom.Node getStartingcylinderCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "cylinder");
    }

    public org.w3c.dom.Node getAdvancedcylinderCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "cylinder", curNode);
    }

    public cylinderType getcylinderValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new cylinderType(curNode);
    }

    public cylinderType getcylinder() throws Exception {
        return getcylinderAt(0);
    }

    public void removecylinderAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "cylinder", index);
    }

    public void removecylinder() {
        removecylinderAt(0);
    }

    public org.w3c.dom.Node addcylinder(cylinderType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "cylinder", value);
    }

    public void insertcylinderAt(cylinderType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "cylinder", index, value);
    }

    public void replacecylinderAt(cylinderType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "cylinder", index, value);
    }

    public static int gettapered_cylinderMinCount() {
        return 1;
    }

    public static int gettapered_cylinderMaxCount() {
        return 1;
    }

    public int gettapered_cylinderCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "tapered_cylinder");
    }

    public boolean hastapered_cylinder() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "tapered_cylinder");
    }

    public tapered_cylinderType newtapered_cylinder() {
        return new tapered_cylinderType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "tapered_cylinder"));
    }

    public tapered_cylinderType gettapered_cylinderAt(int index) throws Exception {
        return new tapered_cylinderType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "tapered_cylinder", index));
    }

    public org.w3c.dom.Node getStartingtapered_cylinderCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "tapered_cylinder");
    }

    public org.w3c.dom.Node getAdvancedtapered_cylinderCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "tapered_cylinder", curNode);
    }

    public tapered_cylinderType gettapered_cylinderValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new tapered_cylinderType(curNode);
    }

    public tapered_cylinderType gettapered_cylinder() throws Exception {
        return gettapered_cylinderAt(0);
    }

    public void removetapered_cylinderAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "tapered_cylinder", index);
    }

    public void removetapered_cylinder() {
        removetapered_cylinderAt(0);
    }

    public org.w3c.dom.Node addtapered_cylinder(tapered_cylinderType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "tapered_cylinder", value);
    }

    public void inserttapered_cylinderAt(tapered_cylinderType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "tapered_cylinder", index, value);
    }

    public void replacetapered_cylinderAt(tapered_cylinderType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "tapered_cylinder", index, value);
    }

    public static int getcapsuleMinCount() {
        return 1;
    }

    public static int getcapsuleMaxCount() {
        return 1;
    }

    public int getcapsuleCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "capsule");
    }

    public boolean hascapsule() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "capsule");
    }

    public capsuleType newcapsule() {
        return new capsuleType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "capsule"));
    }

    public capsuleType getcapsuleAt(int index) throws Exception {
        return new capsuleType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "capsule", index));
    }

    public org.w3c.dom.Node getStartingcapsuleCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "capsule");
    }

    public org.w3c.dom.Node getAdvancedcapsuleCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "capsule", curNode);
    }

    public capsuleType getcapsuleValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new capsuleType(curNode);
    }

    public capsuleType getcapsule() throws Exception {
        return getcapsuleAt(0);
    }

    public void removecapsuleAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "capsule", index);
    }

    public void removecapsule() {
        removecapsuleAt(0);
    }

    public org.w3c.dom.Node addcapsule(capsuleType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "capsule", value);
    }

    public void insertcapsuleAt(capsuleType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "capsule", index, value);
    }

    public void replacecapsuleAt(capsuleType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "capsule", index, value);
    }

    public static int gettapered_capsuleMinCount() {
        return 1;
    }

    public static int gettapered_capsuleMaxCount() {
        return 1;
    }

    public int gettapered_capsuleCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "tapered_capsule");
    }

    public boolean hastapered_capsule() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "tapered_capsule");
    }

    public tapered_capsuleType newtapered_capsule() {
        return new tapered_capsuleType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "tapered_capsule"));
    }

    public tapered_capsuleType gettapered_capsuleAt(int index) throws Exception {
        return new tapered_capsuleType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "tapered_capsule", index));
    }

    public org.w3c.dom.Node getStartingtapered_capsuleCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "tapered_capsule");
    }

    public org.w3c.dom.Node getAdvancedtapered_capsuleCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "tapered_capsule", curNode);
    }

    public tapered_capsuleType gettapered_capsuleValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new tapered_capsuleType(curNode);
    }

    public tapered_capsuleType gettapered_capsule() throws Exception {
        return gettapered_capsuleAt(0);
    }

    public void removetapered_capsuleAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "tapered_capsule", index);
    }

    public void removetapered_capsule() {
        removetapered_capsuleAt(0);
    }

    public org.w3c.dom.Node addtapered_capsule(tapered_capsuleType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "tapered_capsule", value);
    }

    public void inserttapered_capsuleAt(tapered_capsuleType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "tapered_capsule", index, value);
    }

    public void replacetapered_capsuleAt(tapered_capsuleType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "tapered_capsule", index, value);
    }

    public static int gettranslateMinCount() {
        return 1;
    }

    public static int gettranslateMaxCount() {
        return 1;
    }

    public int gettranslateCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "translate");
    }

    public boolean hastranslate() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "translate");
    }

    public TargetableFloat3 newtranslate() {
        return new TargetableFloat3(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "translate"));
    }

    public TargetableFloat3 gettranslateAt(int index) throws Exception {
        return new TargetableFloat3(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "translate", index));
    }

    public org.w3c.dom.Node getStartingtranslateCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "translate");
    }

    public org.w3c.dom.Node getAdvancedtranslateCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "translate", curNode);
    }

    public TargetableFloat3 gettranslateValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new TargetableFloat3(curNode);
    }

    public TargetableFloat3 gettranslate() throws Exception {
        return gettranslateAt(0);
    }

    public void removetranslateAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "translate", index);
    }

    public void removetranslate() {
        removetranslateAt(0);
    }

    public org.w3c.dom.Node addtranslate(TargetableFloat3 value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "translate", value);
    }

    public void inserttranslateAt(TargetableFloat3 value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "translate", index, value);
    }

    public void replacetranslateAt(TargetableFloat3 value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "translate", index, value);
    }

    public static int getrotateMinCount() {
        return 1;
    }

    public static int getrotateMaxCount() {
        return 1;
    }

    public int getrotateCount() {
        return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "rotate");
    }

    public boolean hasrotate() {
        return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "rotate");
    }

    public rotateType newrotate() {
        return new rotateType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "rotate"));
    }

    public rotateType getrotateAt(int index) throws Exception {
        return new rotateType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "rotate", index));
    }

    public org.w3c.dom.Node getStartingrotateCursor() throws Exception {
        return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "rotate");
    }

    public org.w3c.dom.Node getAdvancedrotateCursor(org.w3c.dom.Node curNode) throws Exception {
        return getDomNextChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "rotate", curNode);
    }

    public rotateType getrotateValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
        if (curNode == null) throw new homura.hde.util.xml.xml.XmlException("Out of range"); else return new rotateType(curNode);
    }

    public rotateType getrotate() throws Exception {
        return getrotateAt(0);
    }

    public void removerotateAt(int index) {
        removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "rotate", index);
    }

    public void removerotate() {
        removerotateAt(0);
    }

    public org.w3c.dom.Node addrotate(rotateType value) {
        return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "rotate", value);
    }

    public void insertrotateAt(rotateType value, int index) {
        insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "rotate", index, value);
    }

    public void replacerotateAt(rotateType value, int index) {
        replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "rotate", index, value);
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
