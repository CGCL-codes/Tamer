package br.ufes.xpflow.util;

import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * Utilidades para trabalhar com documentos xml
 */
public class XMLUtil {

    /**
     * Class-level logging provider object
     */
    private static final Logger logger = Logger.getLogger(XMLUtil.class.getName());

    /**
     * reference to default parser (non-validating and namespace-aware).
     */
    private static final DocumentBuilder defaultParser;

    /**
     * initialization during class-loading
     *
     */
    static {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setCoalescing(true);
            dbf.setNamespaceAware(false);
            dbf.setValidating(false);
            defaultParser = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.fatal("Erro de configuracao de xml parser", e);
            throw new FactoryConfigurationError(e);
        } catch (FactoryConfigurationError e) {
            logger.fatal("Erro de configuracao de xml parser factory", e);
            throw e;
        }
    }

    /**
     * creates a new and empty document using the default document builder
     * factory.
     *
     * @return empty DOM document or null if exception occured.
     */
    public static Document newDocument() {
        Document doc;
        synchronized (XMLUtil.defaultParser) {
            doc = XMLUtil.defaultParser.newDocument();
            XMLUtil.defaultParser.notifyAll();
        }
        return doc;
    }

    /**
     * Parse the contents of the given String, and return the DOM Document
     * object it describes. This method uses the default XML parser.
     *
     * @param str string to be parsed
     * @return DOM document
     * @throws IllegalArgumentException if string is null
     */
    public static Document parse(String str) throws IllegalArgumentException {
        return XMLUtil.parse(str, defaultParser, null);
    }

    /**
     * Parse the contents of the given String, and return the DOM Document
     * object it describes. This method uses the default XML parser.
     *
     * @param str string to be parsed
     * @param lp  logging provider. If null then the default logging provider
     *            will be used.
     * @return DOM document
     * @throws IllegalArgumentException if string is null
     */
    public static Document parse(String str, Logger lp) throws IllegalArgumentException {
        return XMLUtil.parse(str, defaultParser, lp);
    }

    /**
     * Parse the contents of the given String, and return the DOM Document
     * object it describes. This method uses a custom XML parser for parsing or
     * if <code>nsDomParser</code> is null, it uses the default parser
     *
     * @param str string to be parsed
     * @param db  XML Parser.
     * @param lp  logging provider. If null then the default logging provider
     *            will be used.
     * @return DOM document
     * @throws IllegalArgumentException if string is null
     */
    public static Document parse(String str, DocumentBuilder db, Logger lp) throws IllegalArgumentException {
        assert str != null;
        assert db != null;
        if (lp == null) {
            lp = XMLUtil.logger;
        }
        InputSource is = new InputSource(new StringReader(str));
        is.setEncoding("UTF-8");
        Document doc = null;
        try {
            synchronized (db) {
                doc = db.parse(is);
                db.notifyAll();
            }
        } catch (SAXException e) {
            lp.error("Error while parsing string:\n" + str, e);
        } catch (IOException e) {
            lp.error("Error parsing string:\n" + str, e);
        }
        return doc;
    }

    /**
     * Parse the contents of the given byte array, and return the Document
     * object it describes. This method uses the default XML parser.
     *
     * @param bytes byte array to be parsed
     * @return DOM document describing byte array
     * @throws IllegalArgumentException if byte array is null
     */
    public static Document parse(byte[] bytes) throws IllegalArgumentException {
        return XMLUtil.parse(bytes, defaultParser, null);
    }

    /**
     * Parse the contents of the given byte array, and return the Document
     * object it describes. This method uses the default XML parser.
     *
     * @param bytes byte array to be parsed
     * @param lp    logging provider. If null then the default logging provider
     *              will be used.
     * @return DOM document describing byte array
     * @throws IllegalArgumentException if byte array is null
     */
    public static Document parse(byte[] bytes, Logger lp) throws IllegalArgumentException {
        return XMLUtil.parse(bytes, defaultParser, lp);
    }

    /**
     * Parse the contents of the given byte array, and return the Document
     * object it describes. This method uses a custom XML parser for parsing or
     * if <code>nsDomParser</code> is null, it uses the default parser
     *
     * @param bytes byte array to be parsed
     * @param db    XML Parser.
     * @param lp    logging provider. If null then the default logging provider
     *              will be used.
     * @return DOM document describing byte array
     * @throws IllegalArgumentException if byte array is null
     */
    public static Document parse(byte[] bytes, DocumentBuilder db, Logger lp) throws IllegalArgumentException {
        assert bytes != null;
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return XMLUtil.parse(bais, db, lp);
    }

    /**
     * Parse the contents arriving from the given InputStream, and return the
     * Document object it describes. This method uses the default XML parser.
     *
     * @param is input stream to be parsed
     * @return DOM document describing input stream
     * @throws IllegalArgumentException if input stream is null
     */
    public static Document parse(InputStream is) throws IllegalArgumentException {
        return XMLUtil.parse(is, defaultParser, null);
    }

    /**
     * Parse the contents arriving from the given InputStream, and return the
     * Document object it describes. This method uses the default XML parser.
     *
     * @param is input stream to be parsed
     * @param lp logging provider. If null then the default logging provider
     *           will be used.
     * @return DOM document describing input stream
     * @throws IllegalArgumentException if input stream is null
     */
    public static Document parse(InputStream is, Logger lp) throws IllegalArgumentException {
        return XMLUtil.parse(is, defaultParser, lp);
    }

    /**
     * Parse the contents arriving from the given InputStream, and return the
     * Document object it describes. This method uses a custom XML parser for
     * parsing or if <code>nsDomParser</code> is null, it uses the default
     * parser
     *
     * @param is input stream to be parsed
     * @param db XML Parser.
     * @param lp logging provider. If null then the default logging provider
     *           will be used.
     * @return DOM document describing input stream
     * @throws IllegalArgumentException if input stream is null
     */
    public static Document parse(InputStream is, DocumentBuilder db, Logger lp) throws IllegalArgumentException {
        assert is != null;
        assert db != null;
        if (lp == null) {
            lp = XMLUtil.logger;
        }
        Document doc = null;
        try {
            synchronized (db) {
                doc = db.parse(is);
                db.notifyAll();
            }
        } catch (SAXException e) {
            lp.error("Error while parsing input stream", e);
        } catch (IOException e) {
            lp.error("Error while parsing input stream", e);
        }
        return doc;
    }

    /**
     * Parse the contents arriving from the given InputSource, and return the
     * Document object it describes. This method uses the default XML parser.
     *
     * @param is input source to be parsed
     * @return DOM document describing input source
     * @throws IllegalArgumentException if input source is null
     */
    public static Document parse(InputSource is) throws IllegalArgumentException {
        return XMLUtil.parse(is, defaultParser, null);
    }

    /**
     * Parse the contents arriving from the given InputSource, and return the
     * Document object it describes
     *
     * @param is input source to be parsed
     * @param lp logging provider. If null then the default logging provider
     *           will be used
     * @return DOM document describing input source
     * @throws IllegalArgumentException if input source is null
     */
    public static Document parse(InputSource is, Logger lp) throws IllegalArgumentException {
        return XMLUtil.parse(is, defaultParser, lp);
    }

    /**
     * Parse the contents arriving from the given InputSource, and return the
     * Document object it describes. This method uses a custom XML parser for
     * parsing or if <code>nsDomParser</code> is null, it uses the default
     * parser
     *
     * @param is input source to be parsed
     * @param db XML Parser. If null then the default XML parser will be used.
     * @param lp logging provider. If null then the default logging provider
     *           will be used
     * @return DOM document describing input source
     * @throws IllegalArgumentException if input source is null
     */
    public static Document parse(InputSource is, DocumentBuilder db, Logger lp) throws IllegalArgumentException {
        assert is != null;
        assert db != null;
        if (lp == null) {
            lp = XMLUtil.logger;
        }
        Document doc = null;
        is.setEncoding("UTF-8");
        try {
            synchronized (db) {
                doc = db.parse(is);
                db.notifyAll();
            }
        } catch (SAXException e) {
            lp.error("Error while parsing input source", e);
        } catch (IOException e) {
            lp.error("Error while parsing input source", e);
        }
        return doc;
    }

    /**
     * Serializes an XML element into a string using the default output format.
     *
     * @param el the DOM element to be converted
     * @return string representing the DOM document or null if problem
     *         encountered during serialization
     * @throws IllegalArgumentException if uri is null
     */
    public static String toString(Element el) throws IllegalArgumentException {
        return XMLUtil.toString(el, null, null);
    }

    /**
     * Serializes an XML element into a string using a custom output format, if
     * specified.
     *
     * @param el the DOM Document to be converted
     * @param of Output format. If null, the default output format is being
     *           used. if null, then the default output format is used.
     * @param lp logging provider. If null then the default logging provider
     *           will be used
     * @return string representing the DOM document or null if problem
     *         encountered during serialization
     * @throws IllegalArgumentException if the document is null
     */
    public static String toString(Element el, OutputFormat of, Logger lp) throws IllegalArgumentException {
        assert el != null;
        if (of == null) {
            of = outputFormat;
        }
        if (lp == null) {
            lp = XMLUtil.logger;
        }
        StringWriter sw = new StringWriter();
        XMLSerializer xmls = new XMLSerializer(sw, of);
        try {
            synchronized (xmls) {
                xmls.serialize(el);
            }
        } catch (IOException e) {
            lp.error("Error while serializing DOM element", e);
            return null;
        }
        return sw.toString();
    }

    /**
     * Serializes an XML document into a string using the default output
     * format.
     *
     * @param doc the DOM Document to be converted
     * @return string representing the DOM document or null if problem
     *         encountered during serialization
     * @throws IllegalArgumentException if the document is null
     */
    public static String toString(Document doc) throws IllegalArgumentException {
        return XMLUtil.toString(doc, null, null);
    }

    /**
     * Serializes an XML document into a string using document
     * format
     *
     * @param doc the DOM Document to be converted
     * @return string representing the DOM document or null if problem
     *         encountered during serialization
     * @throws IllegalArgumentException if the document is null
     */
    public static String serialize(Document doc) throws IllegalArgumentException {
        return XMLUtil.toString(doc, new OutputFormat(doc), null);
    }

    /**
     * Serializes an XML document into a string using a custom output format, if
     * specified.
     *
     * @param doc the DOM Document to be converted.
     * @param of  output format. If null, the default output format is being
     *            used.
     * @param lp  logging provider. If null then the default logging provider
     *            will be used.
     * @return string representing the DOM document or null if problem
     *         encountered during serialization
     * @throws IllegalArgumentException if the document is null
     */
    public static String toString(Document doc, OutputFormat of, Logger lp) throws IllegalArgumentException {
        if (lp == null) {
            lp = XMLUtil.logger;
        }
        StringWriter sw = new StringWriter();
        try {
            write(doc, sw, of);
        } catch (IOException e) {
            lp.error("Error while serializing DOM document", e);
        }
        return sw.toString();
    }

    /**
     * Writes an XML document into a string using a custom output format, if
     * specified.
     *
     * @param doc the DOM Document to be converted.
     * @param out output
     * @param of  output format. If null, the default output format is being
     *            used.
     * @throws IllegalArgumentException if the document is null
     * @throws java.io.IOException if failed
     */
    public static void write(Document doc, Writer out, OutputFormat of) throws IllegalArgumentException, IOException {
        assert doc != null;
        assert out != null;
        if (of == null) {
            of = outputFormat;
        }
        XMLSerializer xmls = new XMLSerializer(out, of);
        synchronized (doc) {
            xmls.serialize(doc);
        }
    }

    /**
     * Output format
     */
    private static final OutputFormat outputFormat;

    static {
        outputFormat = new OutputFormat("xml", "UTF-8", true);
        outputFormat.setIndent(2);
        outputFormat.setOmitDocumentType(false);
        outputFormat.setOmitXMLDeclaration(false);
    }
}
