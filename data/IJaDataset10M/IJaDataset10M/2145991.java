package net.metasimian.examples.xml.validation;

import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import junit.framework.TestCase;
import org.w3c.dom.Document;

public class ValidateXMLTest extends TestCase {

    public ValidateXMLTest() {
    }

    public void testValidate() throws Exception {
        InputStream isXML = this.getClass().getResourceAsStream("/myXMLDocument.xml");
        DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = parser.parse(isXML);
        InputStream isXSL = this.getClass().getResourceAsStream("/schema.xsd");
        Source schemaFile = new StreamSource(isXSL);
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(schemaFile);
        javax.xml.validation.Validator validator = schema.newValidator();
        validator.validate(new DOMSource(document));
    }

    public void testInValidate() throws Exception {
        InputStream isXML = this.getClass().getResourceAsStream("/myBadXMLDocument.xml");
        DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = parser.parse(isXML);
        InputStream isXSL = this.getClass().getResourceAsStream("/schema.xsd");
        Source schemaFile = new StreamSource(isXSL);
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(schemaFile);
        javax.xml.validation.Validator validator = schema.newValidator();
        try {
            validator.validate(new DOMSource(document));
            assertTrue(false);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
