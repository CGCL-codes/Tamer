package org.codegallery.javagal.xml.parse;

import org.codegallery.javagal.Constants;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.*;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: gongwenwei
 * Date: 11-10-19
 * Time: 上午10:00
 * To change this template use File | Settings | File Templates.
 */
public class DomParser {

    private StringBuffer indent = new StringBuffer("");

    public static void main(String[] args) {
        DomParser p = new DomParser();
        try {
            p.dtdValidation();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void parse() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setExpandEntityReferences(false);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(Constants.TEST_RES_DIR + "\\xml\\note.xml"));
        NodeList nodes = doc.getElementsByTagName("msg");
        Element element = (Element) nodes.item(0);
        CDATASection cdataNode = doc.createCDATASection("");
        element.appendChild(cdataNode);
        cdataNode.setData("Hello World");
        System.out.println(convert(doc));
    }

    public StringBuilder convert(Document doc) throws Exception {
        StringBuilder stringBuilder = null;
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            OutputFormat outputformat = new OutputFormat();
            outputformat.setIndent(4);
            outputformat.setIndenting(true);
            outputformat.setPreserveSpace(false);
            XMLSerializer serializer = new XMLSerializer();
            serializer.setOutputFormat(outputformat);
            serializer.setOutputByteStream(stream);
            serializer.asDOMSerializer();
            serializer.serialize(doc.getDocumentElement());
            stringBuilder = new StringBuilder(stream.toString());
        } catch (Exception except) {
            except.getMessage();
        }
        return stringBuilder;
    }

    public void listNodes(Node node) {
        String nodeName = node.getNodeName();
        if (node instanceof Element) {
            if (node.hasAttributes()) {
                NamedNodeMap attrs = node.getAttributes();
                for (int i = 0; i < attrs.getLength(); i++) {
                    Attr attribute = (Attr) attrs.item(i);
                    System.out.println(" " + attribute.getName() + "=" + attribute.getValue());
                }
            }
            System.out.println(indent + "<" + nodeName + ">");
        } else if (node instanceof Text) {
            System.out.println(((Text) node).getData());
        } else if (node instanceof DocumentType) {
            System.out.println(getDoctypeString((DocumentType) node));
        }
        indent.append(' ');
        NodeList list = node.getChildNodes();
        if (list.getLength() > 0) {
            for (int i = 0; i < list.getLength(); i++) {
                listNodes(list.item(i));
            }
        }
        System.out.println("</" + nodeName + ">");
    }

    private String getDoctypeString(DocumentType doctype) {
        String str = doctype.getName();
        StringBuffer doctypeStr = new StringBuffer("<!DOCTYPE ").append(str);
        if ((str = doctype.getSystemId()) != null) {
            doctypeStr.append(" SYSTEM ").append('\"').append(str).append('\"');
        }
        if ((str = doctype.getPublicId()) != null) {
            doctypeStr.append(" PUBLIC ").append('\"').append(str).append('\"');
        }
        if ((str = doctype.getInternalSubset()) != null) {
            doctypeStr.append('[').append(str).append(']');
        }
        return doctypeStr.append('>').toString();
    }

    public void xsdValidation() throws Exception {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        File schemaLocation = new File(Constants.TEST_RES_DIR + "\\xml\\note.xsd");
        Schema schema = sf.newSchema(schemaLocation);
        Source source = new StreamSource(Constants.TEST_RES_DIR + "\\xml\\note.xml");
        Validator validator = schema.newValidator();
        validator.validate(source);
    }

    public void dtdValidation() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document xmlDocument = db.parse(new FileInputStream(Constants.TEST_RES_DIR + "\\xml\\newspaper.xml"));
        DOMSource source = new DOMSource(xmlDocument);
        StreamResult result = new StreamResult(System.out);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, Constants.TEST_RES_DIR + "\\xml\\newspaper.dtd");
        transformer.transform(source, result);
    }

    public void convert1(Document doc) {
        DOMImplementationLS DOMiLS = (DOMImplementationLS) (doc.getImplementation()).getFeature("LS", "3.0");
        LSSerializer LSS = DOMiLS.createLSSerializer();
    }
}
