package com.metanology.mde.core.xmlserializers;

import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.metanology.mde.core.metaModel.*;
import com.metanology.mde.utils.*;

/**
 * Serialize an instanceof AssociationRole into XML file
 */
public class AssociationRoleSerializer implements Serializer {

    private AssociationRole obj;

    private String oid;

    private Element element;

    public AssociationRoleSerializer(AssociationRole obj) {
        this.obj = obj;
        this.oid = null;
        this.element = null;
    }

    public AssociationRoleSerializer(AssociationRole obj, Element element, String oid) {
        this.obj = obj;
        this.element = element;
        this.oid = oid;
    }

    public void setObject(Object o) {
        if (o instanceof AssociationRole) {
            obj = (AssociationRole) o;
        }
    }

    public void addToElement(Element root, XmlWriteObjectRepository objRep) {
        ModelElementSerializer sserializer = new ModelElementSerializer(obj);
        sserializer.addToElement(root, objRep);
        root.setAttribute("oid", objRep.getOid(obj));
        root.setAttribute("classname", "AssociationRole");
        Element attr = null;
        Text val = null;
        if (obj.getConstraints() != null) {
            attr = objRep.createElement("attribute");
            attr.setAttribute("name", "constraints");
            attr.setAttribute("type", "String");
            val = objRep.createTextNode(XmlWriteObjectRepository.parseString(obj.getConstraints()));
            attr.appendChild(val);
            root.appendChild(attr);
        }
        attr = objRep.createElement("attribute");
        attr.setAttribute("name", "isAggregate");
        attr.setAttribute("type", "boolean");
        val = objRep.createTextNode(String.valueOf(obj.getIsAggregate()));
        attr.appendChild(val);
        root.appendChild(attr);
        attr = objRep.createElement("attribute");
        attr.setAttribute("name", "isNavigable");
        attr.setAttribute("type", "boolean");
        val = objRep.createTextNode(String.valueOf(obj.getIsNavigable()));
        attr.appendChild(val);
        root.appendChild(attr);
        attr = objRep.createElement("attribute");
        attr.setAttribute("name", "multiplicity");
        attr.setAttribute("type", "int");
        val = objRep.createTextNode(String.valueOf(obj.getMultiplicity()));
        attr.appendChild(val);
        root.appendChild(attr);
        attr = objRep.createElement("attribute");
        attr.setAttribute("name", "containment");
        attr.setAttribute("type", "int");
        val = objRep.createTextNode(String.valueOf(obj.getContainment()));
        attr.appendChild(val);
        root.appendChild(attr);
        attr = objRep.createElement("attribute");
        attr.setAttribute("name", "scope");
        attr.setAttribute("type", "int");
        val = objRep.createTextNode(String.valueOf(obj.getScope()));
        attr.appendChild(val);
        root.appendChild(attr);
        if (obj.getMetaClass() != null) {
            attr = objRep.createElement("attribute");
            attr.setAttribute("name", "metaClass");
            attr.setAttribute("type", "objectref");
            val = objRep.createTextNode(objRep.getOid(obj.getMetaClass()));
            attr.appendChild(val);
            root.appendChild(attr);
        }
        if (obj.getAssociation() != null) {
            attr = objRep.createElement("attribute");
            attr.setAttribute("name", "association");
            attr.setAttribute("type", "objectref");
            val = objRep.createTextNode(objRep.getOid(obj.getAssociation()));
            attr.appendChild(val);
            root.appendChild(attr);
            Association association = (Association) obj.getAssociation();
            AssociationSerializer associationSerializer = new AssociationSerializer(association);
            objRep.getSerializers().add(associationSerializer);
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
                if (name.equals("constraints")) {
                    obj.setConstraints(val);
                } else if (name.equals("isAggregate")) {
                    obj.setIsAggregate(Boolean.valueOf(val).booleanValue());
                } else if (name.equals("isNavigable")) {
                    obj.setIsNavigable(Boolean.valueOf(val).booleanValue());
                } else if (name.equals("multiplicity")) {
                    obj.setMultiplicity(Integer.valueOf(val).intValue());
                } else if (name.equals("containment")) {
                    obj.setContainment(Integer.valueOf(val).intValue());
                } else if (name.equals("scope")) {
                    obj.setScope(Integer.valueOf(val).intValue());
                } else if (name.equals("metaClass")) {
                    MetaClassSerializer s = new MetaClassSerializer(null, null, val);
                    if (obj.getMetaClass() == null) {
                        obj.setMetaClass((MetaClass) s.createFromObjectRepository(objRep));
                    }
                } else if (name.equals("association")) {
                    AssociationSerializer s = new AssociationSerializer(null, null, val);
                    if (obj.getAssociation() == null) {
                        obj.setAssociation((Association) s.createFromObjectRepository(objRep));
                    }
                }
            }
        }
    }

    public Object createFromObjectRepository(XmlReadObjectRepository objRep) {
        if (oid != null) obj = (AssociationRole) objRep.findObject(oid);
        if (obj != null) return obj;
        if (element == null) {
            if (oid == null || oid.length() == 0) {
                element = objRep.findClassElement("AssociationRole");
            } else {
                element = objRep.findElement(oid);
            }
        }
        if (element != null) {
            oid = (String) element.getAttribute("oid");
            obj = new AssociationRole();
            if (obj instanceof Identifiable) {
                Identifiable idObj = (Identifiable) obj;
                idObj.setObjId(oid);
            }
            objRep.addObject(oid, obj);
            AssociationRoleSerializer serializer = new AssociationRoleSerializer(obj, element, oid);
            objRep.getSerializers().add(serializer);
        } else {
            obj = new AssociationRole();
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
        Serializer objSer = new AssociationRoleSerializer(null, null, "");
        Object o = objSer.createFromObjectRepository(objRep);
        while (objRep.getSerializers().size() > 0) {
            Serializer s = (Serializer) objRep.getSerializers().get(0);
            objRep.getSerializers().remove(s);
            s.loadFromElement(objRep);
        }
        return o;
    }
}
