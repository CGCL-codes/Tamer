package org.ws4d.java.io.xml.cache;

import java.io.IOException;
import org.ws4d.java.io.xml.XmlSerializer;
import org.ws4d.java.io.xml.canonicalization.CanonicalSerializer;
import org.xmlpull.v1.IllegalStateException;

public class XmlTag implements XmlStructure {

    int type;

    String namespace;

    String name;

    public XmlTag(boolean start, String namespace, String name) {
        if (start) type = XmlStructure.XML_START_TAG; else type = XmlStructure.XML_END_TAG;
        this.namespace = namespace;
        this.name = name;
    }

    public void flush(CanonicalSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        if (type == XmlStructure.XML_START_TAG) serializer.startTago(namespace, name); else serializer.endTago(namespace, name);
    }

    public void flush(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        if (type == XmlStructure.XML_START_TAG) serializer.startTag(namespace, name); else serializer.endTag(namespace, name);
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNamespace(String ns) {
        this.namespace = ns;
    }

    public String getValue() {
        return null;
    }

    public void setNameSpace(String ns) {
        namespace = ns;
    }
}
