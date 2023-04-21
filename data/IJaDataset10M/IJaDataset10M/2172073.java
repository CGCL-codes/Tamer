package org.dctmvfs.vfs.provider.dctm.client.serializing.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.dctmvfs.vfs.provider.dctm.client.DctmFile;
import org.dctmvfs.vfs.provider.dctm.client.serializing.AttributeSerializer;
import org.dctmvfs.vfs.provider.dctm.client.serializing.SerializerException;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class SimpleXmlSaxSerializer implements AttributeSerializer {

    public InputStream serialize(DctmFile file) throws SerializerException {
        try {
            Map attributes = file.getAttributes();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            StreamResult streamResult = new StreamResult(out);
            SAXTransformerFactory transformerFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            TransformerHandler transformerHandler = transformerFactory.newTransformerHandler();
            Transformer serializer = transformerHandler.getTransformer();
            serializer.setOutputProperty(OutputKeys.METHOD, "xml");
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformerHandler.setResult(streamResult);
            this.writeEvents(attributes, transformerHandler);
            out.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new SerializerException("Error serializing file", e);
        }
    }

    protected void writeEvents(Map attributes, TransformerHandler handler) throws SAXException {
        AttributesImpl noAtts = new AttributesImpl();
        handler.startDocument();
        handler.startElement("", "", "meta", noAtts);
        Iterator iterator = attributes.keySet().iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            Object value = attributes.get(key);
            char[] characters;
            if (value != null) {
                characters = value.toString().toCharArray();
            } else {
                characters = new char[0];
            }
            handler.startElement("", "", key.toString(), noAtts);
            handler.characters(characters, 0, characters.length);
            handler.endElement("", "", key.toString());
        }
        handler.endElement("", "", "meta");
        handler.endDocument();
    }
}
