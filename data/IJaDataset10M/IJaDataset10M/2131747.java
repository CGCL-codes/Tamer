package com.manning.junitbook.ch13;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import junit.framework.Assert;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class TestXmlService {

    @Test
    public void testGetXmlBasicCheck() throws IOException {
        HttpClient httpClient = new HttpClient();
        GetMethod get = new GetMethod("http://localhost/ch13personal/personal.xml");
        String responseString;
        try {
            httpClient.executeMethod(get);
            InputStream input = get.getResponseBodyAsStream();
            responseString = IOUtils.toString(input, "UTF-8");
        } finally {
            get.releaseConnection();
        }
        Assert.assertTrue(responseString, responseString.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
    }

    @Test
    public void testGetXmlAndValidateXmlSchema() throws IOException, ParserConfigurationException, SAXException {
        HttpClient httpClient = new HttpClient();
        GetMethod get = new GetMethod("http://localhost/ch13personal/personal.xml");
        Document document;
        try {
            httpClient.executeMethod(get);
            InputStream input = get.getResponseBodyAsStream();
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = parser.parse(input);
        } finally {
            get.releaseConnection();
        }
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Source schemaFile = new StreamSource(new File("src/main/webapp/personal.xsd"));
        Schema schema = factory.newSchema(schemaFile);
        Validator validator = schema.newValidator();
        validator.validate(new DOMSource(document));
    }
}
