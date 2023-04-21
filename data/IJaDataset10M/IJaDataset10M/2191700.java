package edu.yale.csgp.vitapad;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import edu.berkeley.guir.prefuse.graph.DefaultNode;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.yale.csgp.prefuse.graph.DecoratedEdge;
import edu.yale.csgp.prefuse.graph.DefaultDecoration;
import edu.yale.csgp.prefuse.graph.VPGraph;
import edu.yale.csgp.vitapad.resources.VPResources;

public class TypeManager {

    public static class TypeSorter implements Comparator {

        public int compare(Object o1, Object o2) {
            System.out.println("Compare Types");
            if (!(o1 instanceof Map)) return 0;
            if (!(o2 instanceof Map)) return 0;
            Map t1 = (Map) o1;
            Map t2 = (Map) o2;
            return ((String) t1.get("name")).compareToIgnoreCase((String) t2.get("name"));
        }
    }

    private static Hashtable types;

    public static Map DEFAULT_VERTEX_TYPE;

    public static Map DEFAULT_EDGE_TYPE;

    public static Map DEFAULT_DEC_TYPE;

    private static List sysTypeEntries;

    private static List userTypeEntries;

    private static File userTypesFile;

    private static Document sysDoc;

    private static Document userDoc;

    static {
        types = new Hashtable();
    }

    public static void init() {
        String propertyPath = VitaPad.getResources().getPropertyPath();
        URL systemTypesFile = VPResources.class.getResource(propertyPath + "types.xml");
        userTypesFile = new File(VPUtilities.constructPath(VitaPad.getSettingsDir(), "types.xml"));
        SAXReader sr = new SAXReader();
        sr.setEntityResolver(new EntityResolver() {

            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                if (systemId.contains("types.dtd")) return new InputSource(VPResources.class.getResourceAsStream(VitaPad.getResources().getPropertyPath() + "types.dtd")); else return null;
            }
        });
        sysDoc = null;
        try {
            sysDoc = sr.read(systemTypesFile.openStream());
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element root = sysDoc.getRootElement();
        sysTypeEntries = root.elements("type");
        userDoc = null;
        if (userTypesFile.exists()) {
            try {
                userDoc = sr.read(userTypesFile);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
        if (userDoc != null) {
            root = userDoc.getRootElement();
            userTypeEntries = root.elements("type");
        }
        load();
    }

    private static void load() {
        String defaultVertex = VitaPad.getProperty("typeManager.defaultVertexType");
        String defaultEdge = VitaPad.getProperty("typeManager.defaultEdgeType");
        String defaultDec = VitaPad.getProperty("typeManager.defaultDecType");
        for (int i = 0; i < sysTypeEntries.size(); i++) {
            Element typeEntry = (Element) sysTypeEntries.get(i);
            String name = typeEntry.getText().trim();
            Map type = getType(typeEntry);
            if (name.equals(defaultVertex)) DEFAULT_VERTEX_TYPE = type; else if (name.equals(defaultEdge)) DEFAULT_EDGE_TYPE = type; else if (name.equals(defaultDec)) DEFAULT_DEC_TYPE = type;
            types.put(name, type);
        }
        if (userTypeEntries != null) {
            for (int i = 0; i < userTypeEntries.size(); i++) {
                Element typeEntry = (Element) userTypeEntries.get(i);
                String name = typeEntry.getText();
                Map type = getType(typeEntry);
                types.put(name, type);
            }
        }
    }

    public static Map getType(Element typeEntry) {
        Map type = new HashMap();
        String id = typeEntry.attributeValue("id");
        int kind = Integer.parseInt(typeEntry.attributeValue("kind"));
        String name = typeEntry.getText();
        String fillColor = typeEntry.attributeValue("fillColor");
        String outlineColor = typeEntry.attributeValue("outlineColor");
        String fontColor = typeEntry.attributeValue("fontColor");
        if (kind != VPGraph.EDGE) {
            String fontFamily = typeEntry.attributeValue("fontFamily");
            String fontStyle = typeEntry.attributeValue("fontStyle");
            String fontSize = typeEntry.attributeValue("fontSize");
            type.put("fontFamily", fontFamily);
            type.put("fontStyle", fontStyle);
            type.put("fontSize", fontSize);
        }
        String shape = typeEntry.attributeValue("shape");
        boolean tooltip = Boolean.parseBoolean(typeEntry.attributeValue("tooltip"));
        float curvature = Float.parseFloat(typeEntry.attributeValue("curvature"));
        type.put("type", kind);
        type.put("outlineColor", outlineColor);
        type.put("fillColor", fillColor);
        type.put("fontColor", fontColor);
        type.put("shape", shape);
        type.put("tooltip", tooltip);
        type.put("curvature", curvature);
        type.put("name", name);
        type.put("shortName", id);
        return type;
    }

    public static List getAvailableTypes() {
        return new ArrayList(types.values());
    }

    public static List getAvailableTypes(int typeKind) {
        List rawTypes = getAvailableTypes();
        List filterTypes = new ArrayList();
        for (int i = 0; i < rawTypes.size(); i++) {
            Map t = (Map) rawTypes.get(i);
            if (t.get("type").equals(typeKind)) filterTypes.add(t);
        }
        Collections.sort(filterTypes, new TypeSorter());
        return filterTypes;
    }

    public static List getDrawableTypes() {
        List rawTypes = getAvailableTypes();
        List drawableTypes = new ArrayList();
        for (int i = 0; i < rawTypes.size(); i++) {
            Map type = (Map) rawTypes.get(i);
            Entity pe = typeToElement(type);
            drawableTypes.add(pe);
        }
        return drawableTypes;
    }

    public static List getDrawableTypes(int typeKind) {
        List rawTypes = getAvailableTypes(typeKind);
        List drawableTypes = new ArrayList();
        for (int i = 0; i < rawTypes.size(); i++) {
            Map type = (Map) rawTypes.get(i);
            int kind = Integer.parseInt((String) type.get("type"));
            Entity pe = null;
            switch(kind) {
                case VPGraph.VERTEX:
                    if (kind == typeKind) {
                        pe = new DefaultNode();
                        pe.setAttribute("name", "Type" + type.get("shortName"));
                    }
                    break;
                case VPGraph.EDGE:
                    if (kind == typeKind) pe = new DecoratedEdge(null, null);
                    break;
                case VPGraph.DECORATION:
                    if (kind == typeKind) pe = new DefaultDecoration(null);
                    break;
            }
            if (kind == typeKind) drawableTypes.add(pe);
        }
        return drawableTypes;
    }

    public static Entity typeToElement(Map type) {
        int kind = Integer.parseInt((String) type.get("type"));
        Entity pe = null;
        switch(kind) {
            case VPGraph.VERTEX:
                pe = new DefaultNode();
                pe.setAttribute("name", "Type" + type.get("shortName"));
                break;
            case VPGraph.EDGE:
                pe = new DecoratedEdge(null, null);
                break;
            case VPGraph.DECORATION:
                pe = new DefaultDecoration(null);
                break;
        }
        return pe;
    }

    public static boolean containsType(Map currentType) {
        List types = getAvailableTypes(Integer.parseInt((String) currentType.get("type")));
        if (types.contains(currentType)) return true; else return false;
    }

    public static void saveType(Map type) {
        Element element = typeToXML(type);
        if (userTypeEntries == null) userTypeEntries = new ArrayList();
        userTypeEntries.add(element);
        propertiesChanged();
    }

    private static Element typeToXML(Map type) {
        Element element = DocumentHelper.createElement("type");
        element.setText((String) type.get("name"));
        element.addAttribute("id", (String) type.get("shortName"));
        element.addAttribute("kind", (String) type.get("type"));
        if (type.get("fillColor") != null) element.addAttribute("fillColor", (String) type.get("fillColor"));
        if (type.get("outlineColor") != null) element.addAttribute("outlineColor", (String) type.get("outlineColor"));
        if (type.get("fontColor") != null) element.addAttribute("fontColor", (String) type.get("fontColor"));
        if (type.get("fontFamily") != null) {
            element.addAttribute("fontFamily", (String) type.get("fontFamily"));
            element.addAttribute("fontStyle", (String) type.get("fontStyle"));
            element.addAttribute("fontSize", (String) type.get("fontSize"));
        }
        element.addAttribute("shape", (String) type.get("shape"));
        element.addAttribute("tooltip", "false");
        element.addAttribute("curvature", (String) type.get("curvature"));
        return element;
    }

    private static void propertiesChanged() {
        types.clear();
        load();
    }

    public static void saveTypeFile() {
        XMLWriter output;
        try {
            output = new XMLWriter(new FileWriter(userTypesFile), new OutputFormat("   ", true));
            output.write(userDoc);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map getDefaultType(int kind) {
        switch(kind) {
            case VPGraph.VERTEX:
                return DEFAULT_VERTEX_TYPE;
            case VPGraph.EDGE:
                return DEFAULT_EDGE_TYPE;
            case VPGraph.DECORATION:
                return DEFAULT_DEC_TYPE;
            default:
                throw new InternalError();
        }
    }
}
