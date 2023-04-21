package com.metanology.mde.core.xmlserializers;

import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.metanology.mde.utils.*;
import com.metanology.mde.core.codeFactory.ComponentMetaProgram;

/**
 * Serialize an instanceof ComponentMetaProgram into XML file
 */
public class ComponentMetaProgramSerializer implements Serializer {

    private ComponentMetaProgram obj;

    private String oid;

    private Element element;

    public ComponentMetaProgramSerializer(ComponentMetaProgram obj) {
        this.obj = obj;
        this.oid = null;
        this.element = null;
    }

    public ComponentMetaProgramSerializer(ComponentMetaProgram obj, Element element, String oid) {
        this.obj = obj;
        this.element = element;
        this.oid = oid;
    }

    public void setObject(Object o) {
        if (o instanceof ComponentMetaProgram) {
            obj = (ComponentMetaProgram) o;
        }
    }

    public void addToElement(Element root, XmlWriteObjectRepository objRep) {
        MetaProgramSerializer sserializer = new MetaProgramSerializer(obj);
        sserializer.addToElement(root, objRep);
        root.setAttribute("oid", objRep.getOid(obj));
        root.setAttribute("classname", "ComponentMetaProgram");
        Element attr = null;
        Text val = null;
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
        MetaProgramSerializer sserializer = new MetaProgramSerializer(obj, element, oid);
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
            }
        }
    }

    public Object createFromObjectRepository(XmlReadObjectRepository objRep) {
        if (oid != null) obj = (ComponentMetaProgram) objRep.findObject(oid);
        if (obj != null) return obj;
        if (element == null) {
            if (oid == null || oid.length() == 0) {
                element = objRep.findClassElement("ComponentMetaProgram");
            } else {
                element = objRep.findElement(oid);
            }
        }
        if (element != null) {
            oid = (String) element.getAttribute("oid");
            obj = new ComponentMetaProgram();
            if (obj instanceof Identifiable) {
                Identifiable idObj = (Identifiable) obj;
                idObj.setObjId(oid);
            }
            objRep.addObject(oid, obj);
            ComponentMetaProgramSerializer serializer = new ComponentMetaProgramSerializer(obj, element, oid);
            objRep.getSerializers().add(serializer);
        } else {
            obj = new ComponentMetaProgram();
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
        Serializer objSer = new ComponentMetaProgramSerializer(null, null, "");
        Object o = objSer.createFromObjectRepository(objRep);
        while (objRep.getSerializers().size() > 0) {
            Serializer s = (Serializer) objRep.getSerializers().get(0);
            objRep.getSerializers().remove(s);
            s.loadFromElement(objRep);
        }
        return o;
    }
}
