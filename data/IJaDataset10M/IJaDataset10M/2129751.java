package net.sf.xmlprocessor.test;

import java.io.File;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;

public class ValidationTestApp {

    public static void main(String[] args) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(args[0]));
            Validator validator = schema.newValidator();
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            DocumentBuilder parser = docFactory.newDocumentBuilder();
            Document document = parser.parse(new File(args[1]));
            validator.validate(new DOMSource(document));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
