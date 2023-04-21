package org.openremote.irbuilder.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.openremote.irbuilder.exception.XmlParserException;
import org.xml.sax.InputSource;

/**
 * @author Tomsky
 *
 */
public class IphoneXmlParser {

    private static final Logger logger = Logger.getLogger(IphoneXmlParser.class);

    private IphoneXmlParser() {
    }

    /**
    * Modify xmlString and download icons from beehive
    * 
    * @param xmlString
    * @param folder
    * @return modified iphoneXML
    */
    @SuppressWarnings("unchecked")
    public static String parserXML(File xsdfile, String xmlString, File folder) {
        SAXBuilder sb = new SAXBuilder(true);
        sb.setValidation(true);
        final String SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
        final String XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
        final String SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
        sb.setProperty(SCHEMA_LANGUAGE, XML_SCHEMA);
        sb.setProperty(SCHEMA_SOURCE, xsdfile);
        String iphoneXml = "";
        try {
            Document doc = sb.build(new InputSource(new StringReader(xmlString)));
            XPath xpath = XPath.newInstance("//or:button[@icon]");
            xpath.addNamespace("or", "http://www.openremote.org");
            List<Element> elements = xpath.selectNodes(doc);
            for (Element element : elements) {
                String iconVal = element.getAttributeValue("icon");
                String iconName = iconVal.substring(iconVal.lastIndexOf("/") + 1);
                ;
                element.setAttribute("icon", iconName);
                File iphoneIconFile = new File(folder, iconName);
                if (iconVal.startsWith("http")) {
                    downloadFile(iconVal, iphoneIconFile);
                }
            }
            Format format = Format.getPrettyFormat();
            format.setIndent("  ");
            format.setEncoding("UTF-8");
            XMLOutputter outp = new XMLOutputter(format);
            iphoneXml = outp.outputString(doc);
        } catch (JDOMException e) {
            logger.error("Parser XML occur JDOMException", e);
            throw new XmlParserException("Parser XML occur JDOMException", e);
        } catch (IOException e) {
            logger.error("Parser XML occur IOException", e);
            throw new XmlParserException("Parser XML occur IOException", e);
        }
        return iphoneXml;
    }

    private static void downloadFile(String srcUrl, File destFile) throws HttpException, IOException {
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod(srcUrl);
        client.executeMethod(get);
        FileOutputStream output = new FileOutputStream(destFile);
        output.write(get.getResponseBody());
        output.close();
    }

    /**
    * Check xml schema.
    * 
    * @param xsdPath the xsd path
    * @param xmlString the xml string
    * 
    * @return true, if successful
    */
    public static boolean checkXmlSchema(String xsdPath, String xmlString) {
        SAXBuilder sb = new SAXBuilder(true);
        sb.setValidation(true);
        File xsdfile = new File(xsdPath);
        final String SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
        final String XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
        final String SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
        sb.setProperty(SCHEMA_LANGUAGE, XML_SCHEMA);
        sb.setProperty(SCHEMA_SOURCE, xsdfile);
        try {
            sb.build(new InputSource(new StringReader(xmlString)));
        } catch (JDOMException e) {
            logger.error("Check the schema " + xsdfile.getName() + " occur JDOMException", e);
            return false;
        } catch (IOException e) {
            logger.error("Check the schema " + xsdfile.getName() + " occur IOException", e);
            return false;
        }
        return true;
    }
}
