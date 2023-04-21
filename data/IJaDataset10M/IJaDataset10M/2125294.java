package ch.olsen.products.util.serialize;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.xml.serialize.OutputFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import ch.olsen.products.util.serialize.GenericObject.MapEntry;
import ch.olsen.products.util.serialize.Reflector.Serializer;

@SuppressWarnings("deprecation")
public class XmlSerializer {

    public static final void writeToXML(OutputStream os, Object obj) throws XmlException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            fill(document, obj);
            OutputFormat format = new OutputFormat(document);
            org.apache.xml.serialize.XMLSerializer serializer = new org.apache.xml.serialize.XMLSerializer(os, format);
            serializer.asDOMSerializer();
            serializer.serialize(document.getDocumentElement());
        } catch (Exception e) {
            throw new XmlException("Could not write to XML: " + e.getMessage(), e);
        }
    }

    public static final void writeToXML(String fileName, Object obj) throws XmlException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            fill(document, obj);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource newDocSource = new DOMSource(document);
            StreamResult newResult = new StreamResult(fileName);
            transformer.transform(newDocSource, newResult);
        } catch (Exception e) {
            throw new XmlException("Could not write to XML: " + e.getMessage(), e);
        }
    }

    private static class IndexedGenericClass {

        int id;

        GenericClass gc;

        public IndexedGenericClass(int id, GenericClass gc) {
            this.id = id;
            this.gc = gc;
        }
    }

    private static void fill(Document document, Object obj) {
        Serializer serializer = new Serializer();
        GenericObject toSerialize = (GenericObject) serializer.getGenericObject(obj);
        Map<String, IndexedGenericClass> classLookup = new HashMap<String, IndexedGenericClass>();
        int id = 0;
        for (GenericClass gc : serializer.knownClasses.values()) {
            classLookup.put(gc.toString(), new IndexedGenericClass(id++, gc));
        }
        int mainId = 0;
        Map<GenericObject, Integer> objectLookup = new HashMap<GenericObject, Integer>();
        for (GenericObject go : serializer.knownObjects.values()) {
            if (go == toSerialize) mainId = id;
            objectLookup.put(go, id++);
        }
        Element root = document.createElement("xmlSerialized");
        document.appendChild(root);
        root.setAttribute("mainId", String.valueOf(mainId));
        Node classes = document.createElement("classes");
        root.appendChild(classes);
        Node objects = document.createElement("objects");
        root.appendChild(objects);
        for (Map.Entry<String, IndexedGenericClass> me : classLookup.entrySet()) {
            Element eClass = document.createElement("class");
            classes.appendChild(eClass);
            IndexedGenericClass indexed = me.getValue();
            GenericClass gc = indexed.gc;
            eClass.setAttribute("id", String.valueOf(indexed.id));
            eClass.setAttribute("name", gc.getName());
            int sgc = -1;
            if (gc.getSuperClass() != null) sgc = classLookup.get(gc.getSuperClass().toString()).id;
            eClass.setAttribute("superClass", String.valueOf(sgc));
            eClass.setAttribute("isPrimitive", String.valueOf(gc.isPrimitive()));
            eClass.setAttribute("isArray", String.valueOf(gc.isArray()));
            eClass.setAttribute("isEnum", String.valueOf(gc.isEnum()));
            eClass.setAttribute("isCollection", String.valueOf(gc.isCollection));
            eClass.setAttribute("isMap", String.valueOf(gc.isMap));
            eClass.setAttribute("isClass", String.valueOf(gc.isClass));
            eClass.setAttribute("hasSpecializedSerialization", String.valueOf(gc.hasSpecializedSerialization));
            eClass.setAttribute("hasWriteReplace", String.valueOf(gc.hasWriteReplace));
            eClass.setAttribute("hasReadResolve", String.valueOf(gc.hasReadResolve));
            eClass.setAttribute("fieldCount", String.valueOf(gc.getFieldCount()));
            eClass.setAttribute("declaredFieldCount", String.valueOf(gc.getDeclaredFields().length));
            for (GenericField f : gc.getDeclaredFields()) {
                Element eField = document.createElement("field");
                eClass.appendChild(eField);
                eField.setAttribute("name", f.getName());
                int fgc = classLookup.get(f.getType().toString()).id;
                eField.setAttribute("type", String.valueOf(fgc));
                eField.setAttribute("index", String.valueOf(f.getIndex()));
            }
        }
        for (Map.Entry<GenericObject, Integer> me : objectLookup.entrySet()) {
            Element eObj = document.createElement("object");
            objects.appendChild(eObj);
            GenericObject go = me.getKey();
            eObj.setAttribute("id", String.valueOf(me.getValue()));
            int cgc = classLookup.get(go.getGenericClass().toString()).id;
            eObj.setAttribute("type", String.valueOf(cgc));
            eObj.setAttribute("toString", go.toString());
            Object valuesArray = go.getAllFields();
            if (go.type.hasSpecializedSerialization) {
                Element eValue = document.createElement("value");
                eObj.appendChild(eValue);
                fillValue(eValue, valuesArray, document, objectLookup);
            } else {
                int len = Array.getLength(valuesArray);
                for (int n = 0; n < len; n++) {
                    Object o = Array.get(valuesArray, n);
                    Element eValue = document.createElement("value");
                    eObj.appendChild(eValue);
                    fillValue(eValue, o, document, objectLookup);
                }
            }
        }
    }

    private static void fillValue(Element e, Object o, Document document, Map<GenericObject, Integer> objectLookup) {
        if (o instanceof GenericObject) {
            GenericObject go2 = (GenericObject) o;
            int ngo2 = objectLookup.get(go2);
            e.setAttribute("ref", String.valueOf(ngo2));
        } else if (o instanceof MapEntry) {
            MapEntry sme = (MapEntry) o;
            Element key = document.createElement("key");
            e.appendChild(key);
            fillValue(key, sme.key, document, objectLookup);
            Element value = document.createElement("value");
            e.appendChild(value);
            fillValue(value, sme.value, document, objectLookup);
        } else if (o instanceof Integer) {
            e.setAttribute("int", o.toString());
        } else if (o instanceof Long) {
            e.setAttribute("long", o.toString());
        } else if (o instanceof Double) {
            e.setAttribute("double", o.toString());
        } else if (o instanceof Float) {
            e.setAttribute("float", o.toString());
        } else if (o instanceof String) {
            e.setAttribute("string", o.toString());
        } else if (o instanceof Boolean) {
            e.setAttribute("boolean", o.toString());
        } else if (o instanceof Byte) {
            e.setAttribute("byte", o.toString());
        } else if (o instanceof byte[]) {
            String hex = "";
            for (byte b : (byte[]) o) {
                hex += String.format("%02x", b);
            }
            e.setAttribute("bytes", hex);
        } else {
            if (o != null) System.out.print("");
        }
    }

    public static Object readFromXml(InputStream is) throws XmlException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(is);
            Node root = document.getFirstChild();
            return parse(root);
        } catch (XmlException e) {
            throw e;
        } catch (Exception e) {
            throw new XmlException("Could not write to XML: " + e.getMessage(), e);
        }
    }

    public static Object readFromXml(String fileName) throws XmlException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(fileName));
            Node root = document.getFirstChild();
            return parse(root);
        } catch (XmlException e) {
            throw e;
        } catch (Exception e) {
            throw new XmlException("Could not write to XML: " + e.getMessage(), e);
        }
    }

    public static Object parse(Node root) throws XmlException {
        if (!root.getNodeName().equals("xmlSerialized")) throw new XmlException("Root node name mismatch");
        int mainId;
        try {
            mainId = Integer.parseInt(root.getAttributes().getNamedItem("mainId").getNodeValue());
        } catch (Exception e) {
            throw new XmlException("Could not parte mainId: " + e.getMessage(), e);
        }
        GenericObject ret = null;
        Map<Integer, GenericClass> allClasses = new HashMap<Integer, GenericClass>();
        Map<Integer, GenericObject> allObjects = new HashMap<Integer, GenericObject>();
        for (Node rChild = root.getFirstChild(); rChild != null; rChild = rChild.getNextSibling()) {
            if (rChild.getNodeType() != Node.ELEMENT_NODE) continue;
            if (rChild.getNodeName().equals("classes")) {
                for (Node cChild = rChild.getFirstChild(); cChild != null; cChild = cChild.getNextSibling()) {
                    if (cChild.getNodeType() != Node.ELEMENT_NODE) continue;
                    if (!cChild.getNodeName().equals("class")) throw new XmlException("Non class element in classes");
                    int id = parseInt(cChild, "id");
                    GenericClass gc = allClasses.get(id);
                    if (gc == null) {
                        gc = new GenericClass();
                        allClasses.put(id, gc);
                    }
                    gc.name = parseString(cChild, "name");
                    int superClassId = parseInt(cChild, "superClass");
                    if (superClassId != -1) {
                        gc.superClass = allClasses.get(superClassId);
                        if (gc.superClass == null) {
                            gc.superClass = new GenericClass();
                            allClasses.put(superClassId, gc.superClass);
                        }
                    }
                    gc.isPrimitive = parseBoolean(cChild, "isPrimitive");
                    gc.isArray = parseBoolean(cChild, "isArray");
                    gc.isEnum = parseBoolean(cChild, "isEnum");
                    gc.isCollection = parseBoolean(cChild, "isCollection");
                    gc.isMap = parseBoolean(cChild, "isMap");
                    gc.isClass = parseBoolean(cChild, "isClass");
                    gc.hasSpecializedSerialization = parseBoolean(cChild, "hasSpecializedSerialization");
                    gc.hasWriteReplace = parseBoolean(cChild, "hasWriteReplace");
                    gc.hasReadResolve = parseBoolean(cChild, "hasReadResolve");
                    gc.fieldCount = parseInt(cChild, "fieldCount");
                    gc.declaredFieldCount = parseInt(cChild, "declaredFieldCount");
                    int n = 0;
                    gc.fields = new GenericField[gc.fieldCount];
                    for (Node fChild = cChild.getFirstChild(); fChild != null; fChild = fChild.getNextSibling()) {
                        if (fChild.getNodeType() != Node.ELEMENT_NODE) continue;
                        if (!fChild.getNodeName().equals("field")) throw new XmlException("Non field values in class");
                        GenericField gf = new GenericField();
                        gf.name = parseString(fChild, "name");
                        int fTypeId = parseInt(fChild, "type");
                        gf.type = allClasses.get(fTypeId);
                        if (gf.type == null) {
                            gf.type = new GenericClass();
                            allClasses.put(fTypeId, gf.type);
                        }
                        gf.index = parseInt(fChild, "index");
                        gc.fields[n++] = gf;
                    }
                }
            } else if (rChild.getNodeName().equals("objects")) {
                for (Node oChild = rChild.getFirstChild(); oChild != null; oChild = oChild.getNextSibling()) {
                    if (oChild.getNodeType() != Node.ELEMENT_NODE) continue;
                    if (!oChild.getNodeName().equals("object")) throw new XmlException("Non object element in objects");
                    int id = parseInt(oChild, "id");
                    GenericObject go = allObjects.get(id);
                    if (go == null) {
                        go = new GenericObject();
                        allObjects.put(id, go);
                    }
                    if (id == mainId) ret = go;
                    int typeId = parseInt(oChild, "type");
                    go.type = allClasses.get(typeId);
                    go.toString = parseString(oChild, "toString");
                    List<Object> values = new LinkedList<Object>();
                    for (Node vChild = oChild.getFirstChild(); vChild != null; vChild = vChild.getNextSibling()) {
                        if (vChild.getNodeType() != Node.ELEMENT_NODE) continue;
                        if (!vChild.getNodeName().equals("value")) throw new XmlException("Non field values in class");
                        if (go.type.hasSpecializedSerialization) go.values = parseValue(vChild, allObjects); else values.add(parseValue(vChild, allObjects));
                    }
                    if (!go.type.hasSpecializedSerialization) go.values = values.toArray();
                }
            }
        }
        return Reflector.fromGeneric(ret, null);
    }

    private static Object parseValue(Node child, Map<Integer, GenericObject> allObjects) throws XmlException {
        if (child.getAttributes().getNamedItem("ref") != null) {
            int refId = parseInt(child, "ref");
            GenericObject ref = allObjects.get(refId);
            if (ref == null) {
                ref = new GenericObject();
                allObjects.put(refId, ref);
            }
            return ref;
        } else if (child.getAttributes().getNamedItem("int") != null) {
            return parseInt(child, "int");
        } else if (child.getAttributes().getNamedItem("double") != null) {
            return Double.parseDouble(parseString(child, "double"));
        } else if (child.getAttributes().getNamedItem("float") != null) {
            return Float.parseFloat(parseString(child, "float"));
        } else if (child.getAttributes().getNamedItem("byte") != null) {
            return Byte.parseByte(parseString(child, "byte"));
        } else if (child.getAttributes().getNamedItem("long") != null) {
            return Long.parseLong(parseString(child, "long"));
        } else if (child.getAttributes().getNamedItem("boolean") != null) {
            return Boolean.parseBoolean(parseString(child, "boolean"));
        } else if (child.getAttributes().getNamedItem("string") != null) {
            return parseString(child, "string");
        } else if (child.getAttributes().getNamedItem("bytes") != null) {
            String s = parseString(child, "bytes");
            byte ret[] = new byte[s.length() / 2];
            for (int n = 0; n + 1 < s.length(); n += 2) {
                String sb = s.substring(n, n + 2);
                ret[n / 2] = (byte) Integer.parseInt(sb, 16);
            }
            return ret;
        } else {
            Object key = null;
            Object value = null;
            for (Node mChild = child.getFirstChild(); mChild != null; mChild = mChild.getNextSibling()) {
                if (mChild.getNodeType() != Node.ELEMENT_NODE) continue;
                if (mChild.getNodeName().equals("key")) {
                    key = parseValue(mChild, allObjects);
                } else if (mChild.getNodeName().equals("value")) {
                    value = parseValue(mChild, allObjects);
                }
            }
            if (key == null || value == null) throw new XmlException("MapEntry key or value missing");
            return new MapEntry(key, value);
        }
    }

    /**
	 * parses a single xml attribute
	 * @param child
	 * @param string
	 * @return
	 * @throws XmlException 
	 */
    private static int parseInt(Node node, String attrName) throws XmlException {
        try {
            return Integer.parseInt(node.getAttributes().getNamedItem(attrName).getNodeValue());
        } catch (Exception e) {
            throw new XmlException("Could not parse " + attrName + ": " + e.getMessage(), e);
        }
    }

    private static boolean parseBoolean(Node node, String attrName) throws XmlException {
        try {
            return Boolean.parseBoolean(node.getAttributes().getNamedItem(attrName).getNodeValue());
        } catch (Exception e) {
            throw new XmlException("Could not parse " + attrName + ": " + e.getMessage(), e);
        }
    }

    private static String parseString(Node node, String attrName) throws XmlException {
        try {
            return node.getAttributes().getNamedItem(attrName).getNodeValue();
        } catch (Exception e) {
            throw new XmlException("Could not parse " + attrName + ": " + e.getMessage(), e);
        }
    }

    public static class XmlException extends Exception {

        private static final long serialVersionUID = 1L;

        public XmlException(String message) {
            super(message);
        }

        public XmlException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
