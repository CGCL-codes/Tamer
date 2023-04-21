package com.metanology.mde.core.xmlserializers;

import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.metanology.mde.core.metaModel.*;
import com.metanology.mde.utils.*;
import com.metanology.mde.core.metaModel.Package;

/**
 * Serialize an instanceof Package into XML file
 */
public class PackageSerializer implements Serializer {

    private Package obj;

    private String oid;

    private Element element;

    public PackageSerializer(Package obj) {
        this.obj = obj;
        this.oid = null;
        this.element = null;
    }

    public PackageSerializer(Package obj, Element element, String oid) {
        this.obj = obj;
        this.element = element;
        this.oid = oid;
    }

    public void setObject(Object o) {
        if (o instanceof Package) {
            obj = (Package) o;
        }
    }

    public void addToElement(Element root, XmlWriteObjectRepository objRep) {
        ModelElementSerializer sserializer = new ModelElementSerializer(obj);
        sserializer.addToElement(root, objRep);
        root.setAttribute("oid", objRep.getOid(obj));
        root.setAttribute("classname", "Package");
        Element attr = null;
        Text val = null;
        if (obj.getChildren() != null && obj.getChildren().size() > 0) {
            attr = objRep.createElement("attribute");
            attr.setAttribute("name", "children");
            attr.setAttribute("type", "objectref");
            val = objRep.createTextNode(objRep.getOid(obj.getChildren()));
            attr.appendChild(val);
            root.appendChild(attr);
        }
        if (obj.getParent() != null) {
            attr = objRep.createElement("attribute");
            attr.setAttribute("name", "parent");
            attr.setAttribute("type", "objectref");
            val = objRep.createTextNode(objRep.getOid(obj.getParent()));
            attr.appendChild(val);
            root.appendChild(attr);
        }
        if (obj.getMetaClasses() != null && obj.getMetaClasses().size() > 0) {
            attr = objRep.createElement("attribute");
            attr.setAttribute("name", "metaClasses");
            attr.setAttribute("type", "objectref");
            val = objRep.createTextNode(objRep.getOid(obj.getMetaClasses()));
            attr.appendChild(val);
            root.appendChild(attr);
        }
        if (obj.getModel() != null) {
            attr = objRep.createElement("attribute");
            attr.setAttribute("name", "model");
            attr.setAttribute("type", "objectref");
            val = objRep.createTextNode(objRep.getOid(obj.getModel()));
            attr.appendChild(val);
            root.appendChild(attr);
        }
    }

    public void addToObjectRepository(XmlWriteObjectRepository objRep) {
        if (objRep.hasObject(obj)) return;
        Element root = objRep.createElement("object");
        if (root != null) {
            objRep.addObject(obj, root);
            this.addToElement(root, objRep);
        }
    }

    public void writeXml(java.io.OutputStream out) throws java.io.IOException {
        XmlWriteObjectRepository objRep = new XmlWriteObjectRepository(out);
        this.addToObjectRepository(objRep);
        while (objRep.getSerializers().size() > 0) {
            Serializer s = (Serializer) objRep.getSerializers().get(0);
            objRep.getSerializers().remove(s);
            s.addToObjectRepository(objRep);
        }
        objRep.writeXmlDoc();
    }

    public void writeXml(java.io.OutputStream out, java.util.Map parameters) throws java.io.IOException {
        XmlWriteObjectRepository objRep = new XmlWriteObjectRepository(out);
        objRep.setParameters(parameters);
        this.addToObjectRepository(objRep);
        while (objRep.getSerializers().size() > 0) {
            Serializer s = (Serializer) objRep.getSerializers().get(0);
            objRep.getSerializers().remove(s);
            s.addToObjectRepository(objRep);
        }
        objRep.writeXmlDoc();
    }

    public void loadFromElement(XmlReadObjectRepository objRep) {
        ModelElementSerializer sserializer = new ModelElementSerializer(obj, element, oid);
        sserializer.loadFromElement(objRep);
        if (element == null && oid != null) {
            element = objRep.findElement(oid);
        }
        NodeList nodes = element.getElementsByTagName("attribute");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            if (!(n instanceof Element)) continue;
            Element el = (Element) n;
            if (el.getTagName() != null && el.getTagName().equalsIgnoreCase("attribute")) {
                Node valNode = el.getFirstChild();
                String val = "";
                if (valNode != null) val = valNode.getNodeValue();
                String name = (String) el.getAttribute("name");
                String type = (String) el.getAttribute("type");
                if (name == null) continue;
                if (name.equals("children")) {
                    if (obj.getChildren() != null && obj.getChildren().size() == 0) {
                        PackageCollectionSerializer s = new PackageCollectionSerializer(null, null, val);
                        Object col = s.createFromObjectRepository(objRep);
                        if (col != null) {
                            obj.setChildren((PackageCollection) col);
                        }
                    }
                } else if (name.equals("parent")) {
                    PackageSerializer s = new PackageSerializer(null, null, val);
                    if (obj.getParent() == null) {
                        obj.setParent((Package) s.createFromObjectRepository(objRep));
                    }
                } else if (name.equals("metaClasses")) {
                    if (obj.getMetaClasses() != null && obj.getMetaClasses().size() == 0) {
                        MetaClassCollectionSerializer s = new MetaClassCollectionSerializer(null, null, val);
                        Object col = s.createFromObjectRepository(objRep);
                        if (col != null) {
                            obj.setMetaClasses((MetaClassCollection) col);
                        }
                    }
                } else if (name.equals("model")) {
                    ModelSerializer s = new ModelSerializer(null, null, val);
                    if (obj.getModel() == null) {
                        obj.setModel((Model) s.createFromObjectRepository(objRep));
                    }
                }
            }
        }
    }

    public Object createFromObjectRepository(XmlReadObjectRepository objRep) {
        if (oid != null) obj = (Package) objRep.findObject(oid);
        if (obj != null) return obj;
        if (element == null) {
            if (oid == null || oid.length() == 0) {
                element = objRep.findClassElement("Package");
            } else {
                element = objRep.findElement(oid);
            }
        }
        if (element != null) {
            oid = (String) element.getAttribute("oid");
            obj = new Package();
            if (obj instanceof Identifiable) {
                Identifiable idObj = (Identifiable) obj;
                idObj.setObjId(oid);
            }
            objRep.addObject(oid, obj);
            PackageSerializer serializer = new PackageSerializer(obj, element, oid);
            objRep.getSerializers().add(serializer);
        } else {
            obj = new Package();
            if (obj instanceof Identifiable) {
                Identifiable idObj = (Identifiable) obj;
                idObj.setObjId(oid);
            }
        }
        return obj;
    }

    public static Object readXml(java.io.InputStream in) {
        return readXml(in, null);
    }

    public static Object readXml(java.io.InputStream in, java.util.Map parameters) {
        XmlReadObjectRepository objRep = new XmlReadObjectRepository(in);
        if (parameters != null) {
            objRep.setParameters(parameters);
        }
        Serializer objSer = new PackageSerializer(null, null, "");
        Object o = objSer.createFromObjectRepository(objRep);
        while (objRep.getSerializers().size() > 0) {
            Serializer s = (Serializer) objRep.getSerializers().get(0);
            objRep.getSerializers().remove(s);
            s.loadFromElement(objRep);
        }
        return o;
    }
}
