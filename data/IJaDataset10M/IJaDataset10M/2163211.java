package com.indigen.victor.actions;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.w3c.dom.Node;

public class GetOntologyAction extends VictorAction {

    public Map process() throws Exception {
        List classes = new Vector();
        setFlowData("classes", classes);
        List classNodes = getNodesFromXPath("/project/ontology/class");
        Iterator i = classNodes.iterator();
        while (i.hasNext()) {
            Node classNode = (Node) i.next();
            String name = getStringFromXPath(classNode, "name");
            String id = getStringFromXPath(classNode, "id");
            Map clazz = new Hashtable();
            classes.add(clazz);
            clazz.put("id", id);
            clazz.put("name", name);
            String builtin = getStringFromXPath(classNode, "builtin");
            clazz.put("builtin", "" + ("true".equals(builtin)));
            List props = new Vector();
            clazz.put("properties", props);
            List propNodes = getNodesFromXPath(classNode, "property");
            Iterator j = propNodes.iterator();
            while (j.hasNext()) {
                Node propNode = (Node) j.next();
                Map prop = new Hashtable();
                props.add(prop);
                String propId = getStringFromXPath(propNode, "id");
                prop.put("id", propId);
                String propName = getStringFromXPath(propNode, "name");
                prop.put("name", propName);
                String contentType = getStringFromXPath(propNode, "content-type");
                prop.put("contenttype", contentType);
                String contentId = getStringFromXPath(propNode, "content-id");
                if (contentId != null && contentId.length() > 0) prop.put("contentid", contentId);
                String key = getStringFromXPath(propNode, "key");
                if (key == null || key.length() == 0) key = "false";
                prop.put("key", key);
                String display = getStringFromXPath(propNode, "display");
                if (display == null || display.length() == 0) display = "false";
                prop.put("display", display);
                String i18n = getStringFromXPath(propNode, "i18n");
                if (i18n == null || i18n.length() == 0) i18n = "false";
                prop.put("i18n", i18n);
                List entries = new Vector();
                prop.put("entries", entries);
                String color = getStringFromXPath(propNode, "color");
                if (color == null || color.length() == 0) color = "yellow";
                prop.put("color", color);
                builtin = getStringFromXPath(propNode, "builtin");
                prop.put("builtin", "" + ("true".equals(builtin)));
                List entryNodes = getNodesFromXPath(propNode, "entry");
                Iterator k = entryNodes.iterator();
                while (k.hasNext()) {
                    Node entryNode = (Node) k.next();
                    Map entry = new Hashtable();
                    entries.add(entry);
                    String entryId = getStringFromXPath(entryNode, "@id");
                    entry.put("id", entryId);
                    String entryType = getStringFromXPath(entryNode, "@type");
                    entry.put("type", entryType);
                    String entryName = getStringFromXPath(entryNode, "text()");
                    entry.put("name", entryName);
                }
            }
        }
        Map map = new Hashtable();
        return map;
    }
}
