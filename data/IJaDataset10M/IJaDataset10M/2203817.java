package annone.util.xml;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import annone.util.Checks;
import annone.util.Const;
import annone.util.Tools;

public class XmlNode extends XmlParent {

    private final String name;

    private final Map<String, String> attributes;

    public XmlNode(String name) {
        Checks.notEmpty("name", name);
        this.name = name;
        this.attributes = new TreeMap<String, String>();
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public String getAttribute(String name) {
        Checks.notEmpty("name", name);
        return attributes.get(name);
    }

    public void setAttribute(String name, String value) {
        Checks.notEmpty("name", name);
        if (value == null) attributes.remove(name); else attributes.put(name, value);
    }

    @Override
    protected String toString(int level) {
        StringBuilder b = new StringBuilder();
        b.append(Tools.Strings.of(' ', level * 2)).append(name);
        for (Entry<String, String> e : attributes.entrySet()) b.append(';').append(e.getKey()).append('=').append(e.getValue());
        b.append(Const.LINE_SEPARATOR).append(super.toString(level));
        return b.toString();
    }

    @Override
    public String toXml() {
        StringBuilder b = new StringBuilder();
        b.append('<').append(name);
        for (Entry<String, String> e : attributes.entrySet()) b.append(' ').append(e.getKey()).append('=').append('"').append(e.getValue()).append('"');
        String childrenXml = super.toXml();
        if (childrenXml.isEmpty()) b.append(" />"); else b.append('>').append(childrenXml).append("</").append(name).append('>');
        return b.toString();
    }
}
