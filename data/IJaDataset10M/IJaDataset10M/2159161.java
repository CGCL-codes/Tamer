package org.xmi.xml.reader.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmi.utils.ArrayUtils;

public class Event {

    public int depth;

    public int linenumer;

    public int type;

    public String name;

    public String namespace;

    public String prefix;

    public String text;

    public Map<List<String>, List<String>> properties = new HashMap<List<String>, List<String>>();

    public List<String> getProperty(String namespace, String name) {
        return properties.get(ArrayUtils.getStringCollection(new String[] { namespace, name }));
    }

    public void addProperty(String namespace, String name, String[] value) {
        properties.put(ArrayUtils.getStringCollection(new String[] { namespace, name }), ArrayUtils.getStringCollection(value));
    }

    public void addProperty(String namespace, String name, String value) {
        properties.put(ArrayUtils.getStringCollection(new String[] { namespace, name }), ArrayUtils.getStringCollection(value));
    }

    public void addProperty(String namespace, String name, List<String> value) {
        properties.put(ArrayUtils.getStringCollection(new String[] { namespace, name }), value);
    }

    public Map<String, String> namespaces = new HashMap<String, String>();

    public String getNSPrefix(String namespace) {
        return namespaces.get(namespace);
    }

    public void addNamespace(String uri, String prefix) {
        namespaces.put(uri, prefix);
    }
}
