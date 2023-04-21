package xbrowser.util;

import xbrowser.plugin.io.XPluginSerializer;

public class XPluginSerializerObject extends XProxyObject {

    public XPluginSerializerObject(String name, String package_name, String resource, String format) {
        super(name, package_name, resource);
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public String toString() {
        return getName() + " (." + format + ")";
    }

    public XPluginSerializer getSerializer() throws Exception {
        if (serializerInstance == null) serializerInstance = (XPluginSerializer) buildObjectImpl();
        return serializerInstance;
    }

    private String format;

    private XPluginSerializer serializerInstance = null;
}
