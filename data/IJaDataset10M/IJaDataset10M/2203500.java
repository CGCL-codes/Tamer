package net.woodstock.rockapi.xml.dom;

import java.io.IOException;
import java.io.Writer;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;

class ApacheXmlWriter extends XmlWriter {

    public ApacheXmlWriter() {
        super();
    }

    @Override
    public void write(Document document, Writer writer) throws IOException {
        OutputFormat format = new OutputFormat(document, XmlWriter.XML_ENCODING, true);
        XMLSerializer serializer = new XMLSerializer(writer, format);
        format.setIndent(1);
        format.setIndenting(true);
        serializer.asDOMSerializer();
        serializer.serialize(document);
    }
}
