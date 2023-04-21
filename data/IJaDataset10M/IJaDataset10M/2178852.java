package net.sf.practicalxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import net.sf.practicalxml.util.ExceptionErrorHandler;

/**
 *  A collection of static methods for parsing XML into a DOM, with or without
 *  validation.
 */
public class ParseUtil {

    /**
     *  Parses the supplied source with a namespace-aware, non-validating
     *  parser, using a caller-supplied error handler.
     *
     *  @throws XmlException for any configuration or fatal execution error.
     */
    public static Document parse(InputSource source, ErrorHandler errHandler) {
        DocumentBuilder db = newNVDocumentBuilder();
        if (errHandler != null) db.setErrorHandler(errHandler);
        try {
            return db.parse(source);
        } catch (IOException e) {
            throw new XmlException("unable to parse", e);
        } catch (SAXException e) {
            throw new XmlException("unable to parse", e);
        }
    }

    /**
     *  Parses the supplied source with a namespace-aware, non-validating
     *  parser, using the built-in error handler that throws on parse errors
     *  and ignores warnings.
     *
     *  @throws XmlException for any configuration or execution error.
     */
    public static Document parse(InputSource source) {
        return parse(source, new ExceptionErrorHandler());
    }

    /**
     *  Parses a string containing XML, using a namespace-aware, non-validating
     *  parser. Note that this ignores any encoding specification in the
     *  prologue; if you read the string from a file, make sure that you used
     *  the correct encoding.
     *
     *  @throws XmlException for any configuration or execution error.
     */
    public static Document parse(String xml) {
        return parse(new InputSource(new StringReader(xml)));
    }

    /**
     *  Parses a file using a namespace-aware, non-validating parser. Ensures that
     *  there are no dangling <code>FileInputStream</code>s for the finalizer to
     *  close.
     *
     *  @since 1.1.11
     */
    public static Document parse(File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            return parse(new InputSource(in));
        } catch (IOException ex) {
            throw new XmlException("unable to parse: " + file, ex);
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     *  Parses the supplied source with a namespace-aware, DTD-validating
     *  parser, using a caller-supplied error handler and entity resolver.
     *  Both of these objects may be <code>null</code>, to use the built-in
     *  defaults.
     *
     *  @throws XmlException for any configuration or fatal execution error.
     */
    public static Document validatingParse(InputSource source, EntityResolver resolver, ErrorHandler errHandler) {
        DocumentBuilder db = newDTDDocumentBuilder();
        if (resolver != null) db.setEntityResolver(resolver);
        if (errHandler != null) db.setErrorHandler(errHandler);
        try {
            return db.parse(source);
        } catch (IOException e) {
            throw new XmlException("unable to parse", e);
        } catch (SAXException e) {
            throw new XmlException("unable to parse", e);
        }
    }

    /**
     *  Parses the supplied source with a namespace-aware, DTD-validating
     *  parser, using a caller-supplied error handler and default entity
     *  resolver. This is useful when the DTD is publicly accessible.
     *
     *  @throws XmlException for any configuration or fatal execution error.
     */
    public static Document validatingParse(InputSource source, ErrorHandler errHandler) {
        return validatingParse(source, (EntityResolver) null, errHandler);
    }

    /**
     *  Parses the supplied source with a namespace-aware, XSD-validating
     *  parser, using a caller-supplied error handler and entity resolver.
     *  Both of these objects may be <code>null</code>, to use the built-in
     *  defaults.
     *
     *  @throws XmlException for any configuration or fatal execution error.
     */
    public static Document validatingParse(InputSource source, Schema schema, ErrorHandler errHandler) {
        DocumentBuilder db = newXSDDocumentBuilder(schema);
        if (errHandler != null) db.setErrorHandler(errHandler);
        try {
            return db.parse(source);
        } catch (IOException e) {
            throw new XmlException("unable to parse", e);
        } catch (SAXException e) {
            throw new XmlException("unable to parse", e);
        }
    }

    /**
     *  Returns a namespace-aware, non-validating parser.
     */
    private static synchronized DocumentBuilder newNVDocumentBuilder() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setCoalescing(true);
        dbf.setValidating(false);
        try {
            return dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new XmlException("unable to confiure parser", e);
        }
    }

    /**
     *  Returns a namespace-aware, DTD-validating parser.
     */
    private static synchronized DocumentBuilder newDTDDocumentBuilder() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(true);
        dbf.setCoalescing(true);
        try {
            return dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new XmlException("unable to confiure parser", e);
        }
    }

    /**
     *  Returns a namespace-aware, XSD-validating parser using the supplied
     *  schema. Note that we don't use a singleton factory, because the schema
     *  gets applied to the factory, not the parser.
     */
    private static synchronized DocumentBuilder newXSDDocumentBuilder(Schema schema) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setCoalescing(true);
        dbf.setValidating(false);
        dbf.setSchema(schema);
        try {
            return dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new XmlException("unable to configure parser", e);
        }
    }
}
