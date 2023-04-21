package org.xmappr;

import org.xmappr.annotation.Attribute;
import javax.xml.XMLConstants;

public class ConfigNamespace {

    @Attribute
    public String uri;

    @Attribute
    public String prefix;

    public ConfigNamespace(String namespace) {
        int index = namespace.indexOf('=');
        if (index > 0) {
            prefix = namespace.substring(0, index);
            uri = namespace.substring(index + 1, namespace.length());
        } else if (index == 0) {
            prefix = XMLConstants.DEFAULT_NS_PREFIX;
            uri = namespace.substring(1, namespace.length());
        } else {
            prefix = XMLConstants.DEFAULT_NS_PREFIX;
            uri = namespace;
        }
    }

    public ConfigNamespace() {
    }

    public String toString(String space) {
        return space + "<namespace " + uri + "=" + prefix + " />";
    }

    @Override
    public String toString() {
        return this.toString("");
    }
}
