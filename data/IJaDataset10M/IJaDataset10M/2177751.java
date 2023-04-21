package com.javector.soaj.util;

import com.javector.soaj.SoajException;
import com.javector.soaj.SoajRuntimeException;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public abstract class XmlUtil {

    public static final String NL = System.getProperty("line.separator");

    public static final String XSLT_RAW = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"><xsl:output method=\"xml\" encoding=\"UTF-8\" indent=\"no\"/><xsl:strip-space elements=\"*\"/><xsl:template match=\"*\"><xsl:copy><xsl:copy-of select=\"@*\"/><xsl:apply-templates/></xsl:copy></xsl:template></xsl:stylesheet>";

    public static final String XSLT_READABLE = "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"><xsl:output method=\"xml\"/><xsl:param name=\"indent-increment\" select=\"'  '\" /><xsl:template match=\"*\"><xsl:param name=\"indent\" select=\"'&#xA;'\"/><xsl:value-of select=\"$indent\"/><xsl:copy><xsl:copy-of select=\"@*\" /><xsl:apply-templates><xsl:with-param name=\"indent\" select=\"concat($indent, $indent-increment)\"/></xsl:apply-templates><xsl:if test=\"*\"><xsl:value-of select=\"$indent\"/></xsl:if></xsl:copy></xsl:template><xsl:template match=\"comment()|processing-instruction()\"><xsl:copy /></xsl:template><xsl:template match=\"text()[normalize-space(.)='']\"/></xsl:stylesheet>";

    public static final String URL_XSD_WSIBP11_WSDL11 = "http://ws-i.org/profiles/basic/1.1/wsdl-2004-08-24.xsd";

    public static final String URL_XSD_WSIBP11_WSDL11SOAP11 = "http://ws-i.org/profiles/basic/1.1/wsdlsoap-2004-08-24.xsd";

    public static final String URL_XSD_WSIBP11_SOAP11 = "http://ws-i.org/profiles/basic/1.1/soap-envelope-2004-01-21.xsd";

    private static Schema wsdl11soap11;

    private static SoajLogger log = LoggingFactory.getLogger(XmlUtil.class.getName());

    private static Transformer rawXformer;

    private static Transformer readableXformer;

    private static Transformer simpleXformer;

    static {
        try {
            TransformerFactory transFac = TransformerFactory.newInstance();
            StreamSource rawSS = new StreamSource(new ByteArrayInputStream(XSLT_RAW.getBytes()));
            StreamSource readableSS = new StreamSource(new ByteArrayInputStream(XSLT_READABLE.getBytes()));
            rawXformer = transFac.newTransformer(rawSS);
            readableXformer = transFac.newTransformer(readableSS);
            simpleXformer = transFac.newTransformer();
        } catch (Exception e) {
            System.out.println(IOUtil.stackTrace(e));
        }
    }

    public static synchronized String toString(Source source) throws SoajRuntimeException {
        if (source == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StreamResult res = new StreamResult(out);
        try {
            simpleXformer.transform(source, res);
        } catch (TransformerException te) {
            throw new SoajRuntimeException("Error transforming Source to String.", te);
        }
        if (StreamSource.class.isInstance(source)) {
            ((StreamSource) source).setInputStream(new ByteArrayInputStream(out.toByteArray()));
        }
        return out.toString();
    }

    public static synchronized String toString(Node n, boolean formatted) throws SoajRuntimeException {
        if (n == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StreamResult res = new StreamResult(out);
        try {
            if (formatted) {
                readableXformer.transform(new DOMSource(n), res);
            } else {
                rawXformer.transform(new DOMSource(n), res);
            }
        } catch (TransformerException te) {
            throw new SoajRuntimeException("Error transforming DOM to String.", te);
        }
        return out.toString();
    }

    public static synchronized String toFormattedString(Node n) throws SoajRuntimeException {
        return toString(n, true);
    }

    public static synchronized String toString(Node n) throws SoajRuntimeException {
        return toString(n, false);
    }

    public static Schema getSchemaWSDL11SOAP11() {
        if (wsdl11soap11 == null) {
            SchemaFactory fac = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            StreamSource[] schemas = { new StreamSource(URL_XSD_WSIBP11_WSDL11), new StreamSource(URL_XSD_WSIBP11_WSDL11SOAP11) };
            try {
                wsdl11soap11 = fac.newSchema(schemas);
            } catch (SAXException e) {
                throw new SoajRuntimeException("Failed to create a validating schema for WSDL 1.1 / SOAP 1.1", e);
            }
        }
        return wsdl11soap11;
    }

    public static synchronized String formatXml(String xml) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            readableXformer.transform(new StreamSource(new StringReader(xml)), new StreamResult(output));
        } catch (TransformerException e) {
            throw new SoajRuntimeException("Failed to format XML", e);
        }
        return output.toString();
    }
}
