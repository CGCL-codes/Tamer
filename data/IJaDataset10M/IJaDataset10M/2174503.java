package gqtiv2;

import java.util.*;
import java.io.*;
import javax.xml.parsers.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import gqtiexcept.validateManifestException;

public class XMLParser {

    DocumentBuilderFactory factory;

    DocumentBuilder builder;

    /** Constants used for JAXP 1.2 */
    String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    boolean externalXsd;

    String[] schemaSources;

    Document document;

    private DocumentDataStore thedataStore;

    public XMLParser() {
        thedataStore = null;
        factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
    }

    public XMLParser(DocumentDataStore ds) {
        thedataStore = ds;
        factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
    }

    public void setValidation(String validationMethod) {
        String v2p0SchemaName = "";
        String v2p1SchemaName = "";
        String XMLFileDirectory = "";
        String debugString = validationMethod;
        if (validationMethod.equals("NONE")) factory.setValidating(false); else {
            factory.setValidating(true);
            if (validationMethod.equals("ASINXML")) {
                externalXsd = false;
            }
            if (validationMethod.equals("STD")) {
                v2p0SchemaName = "v2p0MathSchemaName";
                v2p1SchemaName = "v2p1MathSchemaName";
                externalXsd = true;
            }
            if (validationMethod.equals("NOMATH")) {
                v2p0SchemaName = "v2p0NoMathSchemaName";
                v2p1SchemaName = "v2p1NoMathSchemaName";
                externalXsd = true;
            }
            if (externalXsd) {
                schemaSources = new String[2];
                schemaSources[0] = thedataStore.getProperty("XMLfileDirectory") + File.separator + thedataStore.getProperty(v2p0SchemaName);
                schemaSources[1] = thedataStore.getProperty("XMLfileDirectory") + File.separator + thedataStore.getProperty(v2p1SchemaName);
                debugString = debugString + "SCHEMASOURCE0: " + schemaSources[0] + "<br />";
                debugString = debugString + "SCHEMASOURCE1: " + schemaSources[1] + "<br />";
                debugString = debugString + "XMLFILEDIRECTORY" + XMLFileDirectory + "<br />";
            }
        }
    }

    public Document parseQTIXml(File XMLFile, String validationMethod, String validatingData) throws SAXException, ParserConfigurationException, IOException {
        if (!validationMethod.equals("NONE")) {
            setSchemas(factory, validatingData, schemaSources, externalXsd);
        }
        builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new ParseErrorHandler());
        document = builder.parse(XMLFile);
        return document;
    }

    public void validateManifest(File manifestFile) throws validateManifestException {
        String output;
        try {
            String Directory = manifestFile.getParent();
            schemaSources = new String[3];
            schemaSources[0] = Directory + File.separator + "imscp_v1p2.xsd";
            schemaSources[1] = Directory + File.separator + "imsqti_v2p1.xsd";
            schemaSources[2] = Directory + File.separator + "imsmd_v1p2p4.xsd";
            boolean externalXsd = true;
            String validatingData = "XSD";
            setSchemas(factory, validatingData, schemaSources, externalXsd);
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ParseErrorHandler());
            document = builder.parse(manifestFile);
            output = "OK";
        } catch (IOException ioe) {
            output = "Cannot read " + manifestFile.getPath() + " " + ioe.getMessage();
            throw new validateManifestException(output);
        } catch (org.xml.sax.SAXException pe) {
            output = SAXErrorHandler(pe);
            throw new validateManifestException(output);
        } catch (ParserConfigurationException pce) {
            String info = "PARSE CONFIGURATION EXCEPTION IN RESPONSE TEMPLATE PROCESSING \n";
            info = info + pce.getMessage() + "\n";
            output = info;
            throw new validateManifestException(output);
        }
    }

    private void setSchemas(DocumentBuilderFactory thefactory, String validatingData, String[] schemaSources, boolean externalXsd) {
        thefactory.setValidating(true);
        if (validatingData.equals("XSD")) {
            try {
                thefactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            } catch (IllegalArgumentException x) {
                String message;
                message = "Error: IllegalArgumentException : JAXP DocumentBuilderFactory attribute not recognized:\n " + JAXP_SCHEMA_LANGUAGE + "\n Check to see if parser conforms to JAXP 1.2 spec.\n";
            }
            if (externalXsd) {
                File[] schemas = new File[schemaSources.length];
                for (int i = 0; i < schemaSources.length; i++) {
                    File f = new File(schemaSources[i]);
                    schemas[i] = f;
                }
                thefactory.setAttribute(JAXP_SCHEMA_SOURCE, schemas);
            }
        }
    }

    private static String SAXErrorHandler(Exception e) {
        String errstr;
        StringWriter errout = new StringWriter();
        errstr = "<html><body>PARSE EXCEPTION,<br />";
        if (e.getMessage() != null) errstr = errstr + "<br />\n" + e.getMessage() + "<br />\n" + "</body></html>"; else errstr = errstr + "<br />\n" + e.getClass().getName() + "<br />\n" + e.toString();
        errstr = errstr + "</body></html>";
        return errstr;
    }

    private static class ParseErrorHandler implements org.xml.sax.ErrorHandler {

        private String getParseExceptionInfo(SAXParseException spe) {
            String systemId = spe.getSystemId();
            if (systemId == null) systemId = "null";
            String info = "\n URI = " + systemId + " \n Line = " + spe.getLineNumber() + ",\n Column = " + spe.getColumnNumber() + ":\n" + spe.getMessage() + "\n ";
            return info;
        }

        public void warning(SAXParseException spe) throws SAXException {
            String message = "Warning: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }

        public void error(SAXParseException spe) throws SAXException {
            String message = "Parse Exception: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }

        public void fatalError(SAXParseException spe) throws SAXException {
            String message = "Fatal Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }
    }
}
