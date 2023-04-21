package com.metanology.mde.core.xmlserializers;

import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.metanology.mde.utils.*;
import com.metanology.mde.core.codeFactory.ClassMetaProgram;
import com.metanology.mde.core.codeFactory.ComponentMetaProgram;
import com.metanology.mde.core.codeFactory.MetaProgram;
import com.metanology.mde.core.codeFactory.MetaProject;
import com.metanology.mde.core.codeFactory.UtilityMetaProgram;

/**
 * Serialize an instanceof MetaProgram into XML file
 */
public class MetaProgramSerializer implements Serializer {

    private MetaProgram obj;

    private String oid;

    private Element element;

    public MetaProgramSerializer(MetaProgram obj) {
        this.obj = obj;
        this.oid = null;
        this.element = null;
    }

    public MetaProgramSerializer(MetaProgram obj, Element element, String oid) {
        this.obj = obj;
        this.element = element;
        this.oid = oid;
    }

    public void setObject(Object o) {
        if (o instanceof MetaProgram) {
            obj = (MetaProgram) o;
        }
    }

    public void addToElement(Element root, XmlWriteObjectRepository objRep) {
        root.setAttribute("oid", objRep.getOid(obj));
        root.setAttribute("classname", "MetaProgram");
        Element attr = null;
        Text val = null;
        if (obj.getName() != null) {
            attr = objRep.createElement("attribute");
            attr.setAttribute("name", "name");
            attr.setAttribute("type", "String");
            val = objRep.createTextNode(XmlWriteObjectRepository.parseString(obj.getName()));
            attr.appendChild(val);
            root.appendChild(attr);
        }
        attr = objRep.createElement("attribute");
        attr.setAttribute("name", "enableLifeCycle");
        attr.setAttribute("type", "boolean");
        val = objRep.createTextNode(String.valueOf(obj.getEnableLifeCycle()));
        attr.appendChild(val);
        root.appendChild(attr);
        if (obj.getOutputFileNameFormat() != null) {
            attr = objRep.createElement("attribute");
            attr.setAttribute("name", "outputFileNameFormat");
            attr.setAttribute("type", "String");
            val = objRep.createTextNode(XmlWriteObjectRepository.parseString(obj.getOutputFileNameFormat()));
            attr.appendChild(val);
            root.appendChild(attr);
        }
        attr = objRep.createElement("attribute");
        attr.setAttribute("name", "lifeCycleAlgorithm");
        attr.setAttribute("type", "int");
        val = objRep.createTextNode(String.valueOf(obj.getLifeCycleAlgorithm()));
        attr.appendChild(val);
        root.appendChild(attr);
        if (obj.getSrcCodeFileName() != null) {
            attr = objRep.createElement("attribute");
            attr.setAttribute("name", "srcCodeFileName");
            attr.setAttribute("type", "String");
            val = objRep.createTextNode(XmlWriteObjectRepository.parseString(obj.getSrcCodeFileName()));
            attr.appendChild(val);
            root.appendChild(attr);
        }
        if (obj.getUdsPrefix() != null) {
            attr = objRep.createElement("attribute");
            attr.setAttribute("name", "udsPrefix");
            attr.setAttribute("type", "String");
            val = objRep.createTextNode(XmlWriteObjectRepository.parseString(obj.getUdsPrefix()));
            attr.appendChild(val);
            root.appendChild(attr);
        }
        if (obj.getUdsSuffix() != null) {
            attr = objRep.createElement("attribute");
            attr.setAttribute("name", "udsSuffix");
            attr.setAttribute("type", "String");
            val = objRep.createTextNode(XmlWriteObjectRepository.parseString(obj.getUdsSuffix()));
            attr.appendChild(val);
            root.appendChild(attr);
        }
        if (obj.getMetaProject() != null) {
            attr = objRep.createElement("attribute");
            attr.setAttribute("name", "metaProject");
            attr.setAttribute("type", "objectref");
            val = objRep.createTextNode(objRep.getOid(obj.getMetaProject()));
            attr.appendChild(val);
            root.appendChild(attr);
            MetaProject metaProject = (MetaProject) obj.getMetaProject();
            MetaProjectSerializer metaProjectSerializer = new MetaProjectSerializer(metaProject);
            objRep.getSerializers().add(metaProjectSerializer);
        }
    }

    public void addToObjectRepository(XmlWriteObjectRepository objRep) {
        if (objRep.hasObject(obj)) return;
        Element root = objRep.createElement("object");
        if (root != null) {
            objRep.addObject(obj, root);
            if (obj instanceof ClassMetaProgram) {
                ClassMetaProgramSerializer subClassSerializer = new ClassMetaProgramSerializer((ClassMetaProgram) obj);
                subClassSerializer.addToElement(root, objRep);
            } else if (obj instanceof UtilityMetaProgram) {
                UtilityMetaProgramSerializer subClassSerializer = new UtilityMetaProgramSerializer((UtilityMetaProgram) obj);
                subClassSerializer.addToElement(root, objRep);
            } else if (obj instanceof ComponentMetaProgram) {
                ComponentMetaProgramSerializer subClassSerializer = new ComponentMetaProgramSerializer((ComponentMetaProgram) obj);
                subClassSerializer.addToElement(root, objRep);
            } else {
                this.addToElement(root, objRep);
            }
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
                if (name.equals("name")) {
                    obj.setName(val);
                } else if (name.equals("enableLifeCycle")) {
                    obj.setEnableLifeCycle(Boolean.valueOf(val).booleanValue());
                } else if (name.equals("outputFileNameFormat")) {
                    obj.setOutputFileNameFormat(val);
                } else if (name.equals("lifeCycleAlgorithm")) {
                    obj.setLifeCycleAlgorithm(Integer.valueOf(val).intValue());
                } else if (name.equals("srcCodeFileName")) {
                    obj.setSrcCodeFileName(val);
                } else if (name.equals("udsPrefix")) {
                    obj.setUdsPrefix(val);
                } else if (name.equals("udsSuffix")) {
                    obj.setUdsSuffix(val);
                } else if (name.equals("metaProject")) {
                    MetaProjectSerializer s = new MetaProjectSerializer(null, null, val);
                    if (obj.getMetaProject() == null) {
                        obj.setMetaProject((MetaProject) s.createFromObjectRepository(objRep));
                    }
                }
            }
        }
    }

    public Object createFromObjectRepository(XmlReadObjectRepository objRep) {
        if (oid != null) obj = (MetaProgram) objRep.findObject(oid);
        if (obj != null) return obj;
        if (element == null) {
            if (oid == null || oid.length() == 0) {
                element = objRep.findClassElement("MetaProgram");
            } else {
                element = objRep.findElement(oid);
            }
        }
        if (element != null) {
            oid = (String) element.getAttribute("oid");
            String className = (String) element.getAttribute("classname");
            if (className != null && className.equals("ClassMetaProgram")) {
                obj = new ClassMetaProgram();
                if (obj instanceof Identifiable) {
                    Identifiable idObj = (Identifiable) obj;
                    idObj.setObjId(oid);
                }
                objRep.addObject(oid, obj);
                ClassMetaProgramSerializer serializer = new ClassMetaProgramSerializer((ClassMetaProgram) obj, element, oid);
                objRep.getSerializers().add(serializer);
                return obj;
            } else if (className != null && className.equals("UtilityMetaProgram")) {
                obj = new UtilityMetaProgram();
                if (obj instanceof Identifiable) {
                    Identifiable idObj = (Identifiable) obj;
                    idObj.setObjId(oid);
                }
                objRep.addObject(oid, obj);
                UtilityMetaProgramSerializer serializer = new UtilityMetaProgramSerializer((UtilityMetaProgram) obj, element, oid);
                objRep.getSerializers().add(serializer);
                return obj;
            } else if (className != null && className.equals("ComponentMetaProgram")) {
                obj = new ComponentMetaProgram();
                if (obj instanceof Identifiable) {
                    Identifiable idObj = (Identifiable) obj;
                    idObj.setObjId(oid);
                }
                objRep.addObject(oid, obj);
                ComponentMetaProgramSerializer serializer = new ComponentMetaProgramSerializer((ComponentMetaProgram) obj, element, oid);
                objRep.getSerializers().add(serializer);
                return obj;
            }
            if (obj == null) {
                obj = new MetaProgram();
                if (obj instanceof Identifiable) {
                    Identifiable idObj = (Identifiable) obj;
                    idObj.setObjId(oid);
                }
                objRep.addObject(oid, obj);
                MetaProgramSerializer serializer = new MetaProgramSerializer(obj, element, oid);
                objRep.getSerializers().add(serializer);
                return obj;
            }
        } else {
            obj = new MetaProgram();
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
        Serializer objSer = new MetaProgramSerializer(null, null, "");
        Object o = objSer.createFromObjectRepository(objRep);
        while (objRep.getSerializers().size() > 0) {
            Serializer s = (Serializer) objRep.getSerializers().get(0);
            objRep.getSerializers().remove(s);
            s.loadFromElement(objRep);
        }
        return o;
    }
}
