package com.ontotext.ordi.sar.remote;

import gate.TextualDocument;
import gate.corpora.DocumentXmlUtils;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.encoding.SerializationContext;
import org.apache.axis.encoding.ser.DocumentSerializer;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.ontotext.ordi.sar.core.requests.SparqlQuery;
import com.ontotext.ordi.sar.remote.core.NodeNames;

public class MultiRefSerializer extends DocumentSerializer {

    static final long serialVersionUID = 1l;

    @Override
    public void serialize(QName name, Attributes attributes, Object value, SerializationContext context) throws IOException {
        if (value instanceof TextualDocument) {
            TextualDocument doc = (TextualDocument) value;
            serializeDocument(doc, name, attributes, value, context);
        } else if (value instanceof SparqlQuery) {
            SparqlQuery query = (SparqlQuery) value;
            serializeSparqlQuery(query, name, attributes, value, context);
        } else {
            super.serialize(name, attributes, value, context);
        }
    }

    private void serializeDocument(TextualDocument doc, QName name, Attributes attributes, Object value, SerializationContext context) throws IOException {
        String xml = DocumentXmlUtils.toXml(doc);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            StringReader reader = new StringReader(xml);
            org.w3c.dom.Document xmlDoc = builder.parse(new InputSource(reader));
            super.serialize(name, attributes, xmlDoc, context);
        } catch (ParserConfigurationException e) {
            throw new IOException(String.valueOf(e));
        } catch (SAXException e) {
            throw new IOException(String.valueOf(e));
        }
    }

    private void serializeSparqlQuery(SparqlQuery query, QName name, Attributes attributes, Object value, SerializationContext context) throws IOException {
        Element queryElement;
        try {
            org.w3c.dom.Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            xml.setXmlVersion("1.0");
            queryElement = xml.createElementNS(NodeNames.NS_SAR, NodeNames.TYPE_SPARQL_QUERY);
            xml.appendChild(queryElement);
            Element textElement = xml.createElement(NodeNames.TAG_TEXT);
            textElement.setTextContent(query.getText());
            queryElement.appendChild(textElement);
            super.serialize(name, attributes, xml, context);
        } catch (DOMException e) {
            throw new IOException(String.valueOf(e));
        } catch (ParserConfigurationException e) {
            throw new IOException(String.valueOf(e));
        }
    }
}
