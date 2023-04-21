package org.ws4d.java.io.xml.cache;

import java.io.IOException;
import org.ws4d.java.io.xml.XmlSerializer;
import org.ws4d.java.io.xml.canonicalization.CanonicalSerializer;
import org.xmlpull.v1.IllegalStateException;

public class XmlText implements XmlStructure {

    String text;

    public XmlText(String txt) {
        text = txt;
    }

    public void flush(CanonicalSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        serializer.texto(text);
    }

    public void flush(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        serializer.text(text);
    }

    public int getType() {
        return XmlStructure.XML_TEXT;
    }

    public String getName() {
        return null;
    }

    public String getNamespace() {
        return null;
    }

    public String getValue() {
        return null;
    }

    public void setNameSpace(String ns) {
    }
}
