package org.webthree.store.util;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DOMHelper {

    /**
     * Die Zeichenkette stream muss ein XML im ISO-8859-1 Format enthalten.
     */
    public static synchronized Document build(String stream) throws Exception {
        if (null != stream) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new ByteArrayInputStream(stream.getBytes("ISO-8859-1")));
        } else {
            return null;
        }
    }

    /**
     * Serialisiert den DOM im ISO-8859-1 Format.
     */
    public static synchronized String stream(Document doc) throws Exception {
        if (null != doc) {
            OutputFormat format = new OutputFormat(doc, "ISO-8859-1", true);
            StringWriter sw = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(sw, format);
            serializer.asDOMSerializer().serialize(doc.getDocumentElement());
            sw.flush();
            return sw.toString();
        } else {
            return null;
        }
    }

    /**
     * Erzeugt ein neues Dokument, welches dasselbe Wurzelelement und
     * deren Nachfolger enth�lt. Knoten au�erhalb des Wurzelelementes
     * werden nicht ber�cksichtigt.
     */
    public static synchronized Document cloneDocument(Document doc) throws Exception {
        if (null != doc) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document newDoc = builder.newDocument();
            Node node = doc.getDocumentElement();
            if (null != node) {
                Node newNode = newDoc.importNode(node, true);
                if (null != newNode) {
                    newDoc.appendChild(newNode);
                }
            }
            return newDoc;
        } else {
            return null;
        }
    }
}
