package org.jato.transform;

import java.io.Writer;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.URIResolver;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.LocatorImpl;
import org.jdom.Document;
import org.jdom.DocType;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;
import org.jdom.input.SAXHandler;
import org.jdom.output.DOMOutputter;
import org.jdom.output.SAXOutputter;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMSource;
import org.jdom.transform.JDOMResult;
import org.jato.ScriptBuilder;
import org.jato.JatoScript;
import org.jato.JatoFormat;
import org.jato.JatoFunction;
import org.jato.JatoException;
import org.jato.format.FormatFactory;

final class TransformUtilities implements TransformConstants {

    /**
    * The XSLT standard output properties with, for each of them,
    * whether it is managed by this implementation and its default
    * value.
    * <p>
    * This array is parsed at class initialization time to fill in
    * the {@link #managedOutputProperties} and
    * {@link #defaultOutputProperties} tables.
    */
    private static final String[][] OUTPUT_PROPERTIES = { { OutputKeys.METHOD, "false", "xml" }, { OutputKeys.VERSION, "false", "1.0" }, { OutputKeys.ENCODING, "true", "UTF-8" }, { OutputKeys.OMIT_XML_DECLARATION, "true", "no" }, { OutputKeys.STANDALONE, "false", "no" }, { OutputKeys.DOCTYPE_PUBLIC, "true", null }, { OutputKeys.DOCTYPE_SYSTEM, "true", null }, { OutputKeys.CDATA_SECTION_ELEMENTS, "false", null }, { OutputKeys.INDENT, "true", "no" }, { OutputKeys.MEDIA_TYPE, "false", null }, { JATO_INDENT, "true", "  " } };

    /**
    * The list of managed output properties, i.e. output properties
    * for which this implementation will accept setting the value.
    */
    private static final Set managedOutputProperties = new HashSet();

    /**
    * The default values for all known output properties.
    */
    private static final Properties defaultOutputProperties = new Properties();

    static {
        for (int i = 0, max = OUTPUT_PROPERTIES.length; i < max; i++) {
            String[] entry = OUTPUT_PROPERTIES[i];
            if ("true".equals(entry[1])) {
                managedOutputProperties.add(entry[0]);
            }
            if (entry[2] != null) {
                defaultOutputProperties.put(entry[0], entry[2]);
            }
        }
    }

    /**
    * Private empty constructor to prevent the Java compiler from
    * generating the default public constructor.
    * <p>
    * The <tt>TransformUtilities</tt> class does not support being
    * instanciated.</p>
    */
    private TransformUtilities() {
        throw (new UnsupportedOperationException());
    }

    /**
    * Reads an XML document from a TrAX source.
    *
    * @param  source     the TrAX source to read the document from.
    * @param  resolver   the TrAX URIResolver to resolve the URIs
    *                    found in the document or <code>null</code>.
    *
    * @return the XML document pointed to by <code>source</code> as
    *         a JDOM document or <code>null</code> if
    *         <code>source</code> is <code>null</code>.  If the
    *         source is a subclass of {@link AbstractJatoSource},
    *         the actual XML source is retrieved by calling
    *         {@link AbstractJatoSource#getXmlSource()}.
    *
    * @throws TransformerException   if the type of source is not
    *                                supported or the parsing of
    *                                the XML document failed.
    */
    public static Document readDocument(Source source, URIResolver resolver) throws TransformerException {
        Document doc = null;
        if (source instanceof AbstractJatoSource) {
            source = ((AbstractJatoSource) source).getXmlSource();
        }
        if (source != null) {
            if (source instanceof JDOMSource) {
                doc = ((JDOMSource) source).getDocument();
            } else {
                if (source instanceof StreamSource) {
                    doc = readDocument(SAXSource.sourceToInputSource(source), getEntityResolver(resolver, source), null);
                } else {
                    if (source instanceof SAXSource) {
                        SAXSource sSource = (SAXSource) source;
                        doc = readDocument(sSource.getInputSource(), getEntityResolver(resolver, source), sSource.getXMLReader());
                    } else {
                        if (source instanceof DOMSource) {
                            doc = readDocument((DOMSource) source);
                        } else {
                            throw (new TransformerException("Unsupported source type: " + source.getClass().getName()));
                        }
                    }
                }
            }
        }
        return (doc);
    }

    /**
    * Reads an XML document from a SAX InputSource using the
    * pre-configured SAX2 parser or allocating one if none is
    * provided.
    *
    * @param  source     the SAX InputSource to read the XML document
    *                    from.
    * @param  resolver   the SAX EntityResolver to resolve the URIs
    *                    found in the document or <code>null</code>.
    * @param  reader     a pre-configured SAX2 parser or
    *                    <code>null</code> to use the default parser
    *                    configured for JAXP.
    *
    * @throws TransformerException   if any error occurred while
    *                                allocating the parser or
    *                                reading the XML document.
    */
    public static Document readDocument(InputSource source, EntityResolver resolver, XMLReader reader) throws TransformerException {
        DocumentReader builder = new DocumentReader();
        if (resolver != null) {
            builder.setEntityResolver(resolver);
        }
        return (builder.build(source, reader));
    }

    /**
    * Creates a SAX EntityResolver from a TrAX URIResolver object.
    *
    * @param  resolver   the application-provided URIResolver object
    *                    or <code>null</code>.
    * @param  source     the TrAX Source from which the URIs to
    *                    resolve will originate.
    *
    * @return a SAX EntityResolver object or <code>null</code> if
    *         no URIResolver object was provided.
    */
    private static EntityResolver getEntityResolver(URIResolver resolver, Source source) {
        EntityResolver saxResolver = null;
        if (resolver != null) {
            saxResolver = new SaxUriResolver(resolver, source.getSystemId());
        }
        return (saxResolver);
    }

    /**
    * Converts a DOM document into a JDOM one.
    *
    * @param  source   the DOMSource containing the root of the
    *                  XML document as a Node.
    *
    * @throws TransformerException   if any error occurred.
    */
    private static Document readDocument(DOMSource source) throws TransformerException {
        Document doc = null;
        try {
            org.w3c.dom.Node node = source.getNode();
            if (node instanceof org.w3c.dom.Document) {
                doc = new DOMBuilder().build((org.w3c.dom.Document) node);
            } else {
                if (node instanceof org.w3c.dom.Element) {
                    Element root = new DOMBuilder().build((org.w3c.dom.Element) node);
                    doc = new Document(root);
                } else {
                    throw (new JDOMException("Can't parse from " + node.getClass().getName()));
                }
            }
        } catch (JDOMException ex1) {
            throw (new TransformerException("Failed to parse source: " + ex1.getMessage(), ex1));
        }
        return (doc);
    }

    /**
    * Outputs a document to the specified sink.
    * <p>
    * This implementation simply ignores any call targeting a TrAX
    * result that is either <code>null</code> or a subclass of
    * {@link AbstractJatoResult}.
    *
    * @param  doc           the document to output.
    * @param  result        the TrAX Result where to save the
    *                       document to.
    * @param  outputProps   the output properties to configure the
    *                       XML outputter when outputting the XML
    *                       document as XML text; ignored otherwise.
    *
    * @throws TransformerException   if the type of result is not
    *                                supported or an error occurred
    *                                while saving the XML document.
    */
    public static void saveDocument(Document doc, Result result, Properties outputProps) throws TransformerException {
        if (result instanceof AbstractJatoResult) {
            result = ((AbstractJatoResult) result).getXmlResult();
        }
        if ((doc != null) && (result != null)) {
            try {
                if (result instanceof JDOMResult) {
                    ((JDOMResult) result).setDocument(doc);
                } else {
                    if (result instanceof StreamResult) {
                        saveDocument(doc, (StreamResult) result, outputProps);
                    } else {
                        if (result instanceof SAXResult) {
                            SAXResult saxResult = (SAXResult) result;
                            SAXOutputter outputter = new SAXOutputter(saxResult.getHandler());
                            LexicalHandler lh = saxResult.getLexicalHandler();
                            if (lh != null) {
                                outputter.setLexicalHandler(lh);
                            }
                            outputter.output(doc);
                        } else {
                            if (result instanceof DOMResult) {
                                DOMOutputter outputter = new DOMOutputter();
                                ((DOMResult) result).setNode(outputter.output(doc));
                            } else {
                                throw (new TransformerException("Unsupported result type: " + result.getClass().getName()));
                            }
                        }
                    }
                }
            } catch (TransformerException ex1) {
                throw (ex1);
            } catch (Exception ex2) {
                throw (new TransformerException("Failed to write result: " + ex2.getMessage(), ex2));
            }
        }
        return;
    }

    /**
    * Outputs a JDOM document to the specified stream.
    *
    * @param  doc           the document to output.
    * @param  result        the StreamResult where to save the
    *                       document to.
    * @param  outputProps   the output properties to configure the
    *                       XML outputter.
    *
    * @throws TransformerException   if any error occurred while
    *                                saving the XML document.
    */
    private static void saveDocument(Document doc, StreamResult result, Properties outputProps) throws TransformerException {
        OutputStream os = null;
        boolean closeStreamOnExit = false;
        try {
            XMLOutputter outputter = getSerializer(doc, outputProps);
            if (result.getWriter() != null) {
                Writer writer = result.getWriter();
                outputter.output(doc, writer);
                writer.flush();
            } else {
                os = result.getOutputStream();
                if ((os == null) && (result.getSystemId() != null)) {
                    String fileURL = result.getSystemId();
                    if (fileURL.startsWith("file:///")) {
                        fileURL = fileURL.substring(8);
                    }
                    closeStreamOnExit = true;
                    os = new FileOutputStream(fileURL);
                }
                if (os != null) {
                    outputter.output(doc, os);
                    os.flush();
                } else {
                    throw (new TransformerException("No output specified!"));
                }
            }
        } catch (TransformerException ex1) {
            throw (ex1);
        } catch (Exception ex2) {
            throw (new TransformerException("Failed to write result: " + ex2.getMessage(), ex2));
        } finally {
            if ((closeStreamOnExit == true) && (os != null)) {
                try {
                    os.close();
                } catch (IOException ex1) {
                }
            }
        }
        return;
    }

    /**
    * Returns a configured XML outputter.
    *
    * @param  doc      the document to output.
    * @param  config   the output properties to configure the
    *                  outputter.
    *
    * @return a configured XML outputter.
    *
    * @throws TRansformerException   if any error occurred.
    */
    private static XMLOutputter getSerializer(Document doc, Properties config) throws TransformerException {
        XMLOutputter outputter = new XMLOutputter();
        if ((config != null) && (config.size() > 0)) {
            for (Iterator i = managedOutputProperties.iterator(); i.hasNext(); ) {
                String name = i.next().toString();
                String value = config.getProperty(name);
                if (value != null) {
                    if (OutputKeys.ENCODING.equals(name)) {
                        outputter.setEncoding(value);
                        continue;
                    }
                    if (OutputKeys.OMIT_XML_DECLARATION.equals(name)) {
                        outputter.setOmitDeclaration("yes".equals(value));
                        continue;
                    }
                    if (OutputKeys.DOCTYPE_PUBLIC.equals(name)) {
                        DocType dtd = doc.getDocType();
                        if (dtd == null) {
                            dtd = new DocType(doc.getRootElement().getName());
                            doc.setDocType(dtd);
                        }
                        dtd.setPublicID(value);
                        continue;
                    }
                    if (OutputKeys.DOCTYPE_SYSTEM.equals(name)) {
                        DocType dtd = doc.getDocType();
                        if (dtd == null) {
                            dtd = new DocType(doc.getRootElement().getName());
                            doc.setDocType(dtd);
                        }
                        dtd.setSystemID(value);
                        continue;
                    }
                    if (OutputKeys.INDENT.equals(name)) {
                        if ("yes".equals(value)) {
                            String indent = config.getProperty(JATO_INDENT);
                            try {
                                outputter.setIndentSize(Integer.parseInt(indent));
                            } catch (Exception ex1) {
                                outputter.setIndent(indent);
                            }
                            outputter.setTextNormalize(true);
                            outputter.setNewlines(true);
                        }
                        continue;
                    }
                }
            }
        }
        return (outputter);
    }

    /**
    * Returns the default output properties for this implementation.
    * <p>
    * <strong>Warning</strong>: The return Properties reference
    * is not a copy: it directly points to the default property list
    * shared by all Transformers.</p>
    *
    * @return the default output properties for this implementation.
    */
    public static Properties getDefaultOutputProperties() {
        return (defaultOutputProperties);
    }

    /**
    * Checks whether an output property is supported by this
    * implementation.
    *
    * @param  name   the name of the property to check.
    *
    * @return <code>true</code> if the property is supported;
    *         <code>false</code> otherwise.
    */
    public static boolean isOutputPropertySupported(String name) {
        return (managedOutputProperties.contains(name));
    }

    /**
    * Creates and returns a JDOM Element object from a string
    * definition.
    * <p>
    * The string definition <code>elementDesc</code> can be any valid
    * XML element definition, possibly including attributes and
    * namespace declarations.  The leading "<code>&lt;</code>" and
    * trailing "<code>/&gt;</code>" characters can be ommitted.</p>
    *
    * @param  elementDesc   the string definition for the element.
    *
    * @return an element corresponding to <code>elementDesc</code>.
    *
    * @throws IllegalArgumentException   if <code>elementDesc</code>
    *                                    cannot be parsed.
    */
    public static Element createElement(String elementDesc) throws IllegalArgumentException {
        Element result = null;
        if (elementDesc == null) {
            throw (new IllegalArgumentException("element description: null"));
        }
        elementDesc = elementDesc.trim();
        boolean startOk = elementDesc.startsWith("<");
        boolean endOk = elementDesc.endsWith("/>");
        if ((startOk == false) || (endOk == false)) {
            StringBuffer buf = new StringBuffer(elementDesc.length() + 4);
            if (startOk == false) {
                buf.append('<');
            }
            buf.append(elementDesc);
            if (endOk == false) {
                buf.append("/>");
            }
            elementDesc = buf.toString();
        }
        Reader in = null;
        try {
            in = new StringReader(elementDesc);
            result = new SAXBuilder().build(in).getRootElement();
            result.detach();
        } catch (JDOMException ex1) {
            throw (new IllegalArgumentException("Failed to parse root element description: " + ex1.getMessage()));
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex1) {
                }
            }
        }
        return (result);
    }

    /**
    * Adds Jato functions to the list of known functions managed by
    * ScriptBuilder.
    *
    * @param  functions   the {@link JatoFunction Jato functions}
    *                     to add.  This argument can be either a
    *                     {@link Collection}, an {@link Iterator},
    *                     an {@link Enumeration} or an array of
    *                     JatoFunctions or a single JatoFunction.
    * @param  script      the script that is to contain the
    *                     function(s). If <code>null</code>,
    *                     functions are added to
    *                     {@link org.jato.ScriptBuilder}.
    *
    * @throws IllegalArgumentException   if <code>functions</code>
    *         contains anything but JatoFunctions or if a function
    *         is already registered with <code>script</code>.
    */
    public static void addFunctions(Object functions, JatoScript script) throws IllegalArgumentException {
        if (functions instanceof Collection) {
            functions = ((Collection) functions).iterator();
        }
        if (functions instanceof Iterator) {
            for (Iterator i = (Iterator) functions; i.hasNext(); ) {
                addFunction(i.next(), script);
            }
        } else {
            if (functions instanceof Enumeration) {
                for (Enumeration i = (Enumeration) functions; i.hasMoreElements(); ) {
                    addFunction(i.nextElement(), script);
                }
            } else {
                if (functions.getClass().isArray()) {
                    int length = Array.getLength(functions);
                    for (int i = 0; i < length; i++) {
                        addFunction(Array.get(functions, i), script);
                    }
                } else {
                    addFunction(functions, script);
                }
            }
        }
    }

    /**
    * Adds a single Jato function to the list of known functions
    * managed by ScriptBuilder.
    *
    * @param  function   the {@link JatoFunction} to add.
    * @param  script     the script that is to contain the
    *                    function(s). If <code>null</code>,
    *                    functions are added to
    *                    {@link org.jato.ScriptBuilder}.
    *
    * @throws IllegalArgumentException   if <code>function</code>
    *         is not a JatoFunction or if it is already registered
    *         with <code>script</code>.
    */
    private static void addFunction(Object function, JatoScript script) throws IllegalArgumentException {
        try {
            if (function instanceof JatoFunction) {
                JatoFunction f = (JatoFunction) function;
                if (script != null) {
                    script.removeFunction(f.getName());
                    script.addFunction(f);
                } else {
                    ScriptBuilder.removeFunction(f.getName());
                    ScriptBuilder.addFunction(f);
                }
            } else {
                throw (new IllegalArgumentException("Not a Jato Function"));
            }
        } catch (JatoException ex1) {
            throw (new IllegalArgumentException(ex1.getMessage()));
        }
    }

    /**
    * Adds Jato formats to the list of known formats managed by
    * FormatFactory.
    *
    * @param  formats   the {@link JatoFormat Jato formats}
    *                   to add.  This argument can be either a
    *                   {@link Collection}, an {@link Iterator},
    *                   an {@link Enumeration} or an array of
    *                   JatoFormat instances or classes or a
    *                   single JatoFormat instance or class.
    * @param  script    the script on behalf of which the method
    *                   call is being performed. (<i>Ignored</i>)
    *
    * @throws IllegalArgumentException   if <code>formats</code>
    *                                    contains anything but
    *                                    JatoFormats.
    */
    public static void addFormats(Object formats, JatoScript script) throws IllegalArgumentException {
        if (formats instanceof Collection) {
            formats = ((Collection) formats).iterator();
        }
        if (formats instanceof Iterator) {
            for (Iterator i = (Iterator) formats; i.hasNext(); ) {
                addFormat(i.next());
            }
        } else {
            if (formats instanceof Enumeration) {
                for (Enumeration i = (Enumeration) formats; i.hasMoreElements(); ) {
                    addFormat(i.nextElement());
                }
            } else {
                if (formats.getClass().isArray()) {
                    int length = Array.getLength(formats);
                    for (int i = 0; i < length; i++) {
                        addFormat(Array.get(formats, i));
                    }
                } else {
                    addFormat(formats);
                }
            }
        }
    }

    /**
    * Adds a single Jato format to the list of known formats
    * managed by FormatFactory.
    *
    * @param  format   the {@link JatoFormat} to add either as an
    *                  instance or as a class object.
    *
    * @throws IllegalArgumentException   if <code>format</code>
    *                                    is not a JatoFormat.
    */
    private static void addFormat(Object format) throws IllegalArgumentException {
        try {
            if (format instanceof JatoFormat) {
                FormatFactory.addJatoFormat((JatoFormat) format);
            } else {
                if (format instanceof Class) {
                    FormatFactory.addJatoFormat((Class) format);
                } else {
                    throw (new IllegalArgumentException("Not a Jato Format"));
                }
            }
        } catch (JatoException ex1) {
            throw (new IllegalArgumentException(ex1.getMessage()));
        }
    }

    /**
    * A subclass of JDOM's SAXBuilder that supports the SAX parser
    * instance to be provided by the caller application.
    */
    private static class DocumentReader extends SAXBuilder {

        /**
       * Default constructor.
       */
        public DocumentReader() {
            super(false);
        }

        /**
       * Builds a document from the supplied input source using
       * the provided pre-configured parser or allocating one if
       * none is provided.
       * <p>
       * The parser configuration will be altered to match JDOM's
       * requirements:
       * <ul>
       *  <li>Activation of namespaces and namespace-prefixes
       *      features</li>
       *  <li>Setting of validation flag according to DocumentReader
       *      {@link #setValidation configuration}</li>
       *  <li>Registration of JDOM-specific ContentHandler,
       *      DTDHandler, ErrorHandler and LexicalHandler</li>
       * </ul></p>
       *
       * @param  in       the SAX InputSource to read from.
       * @param  parser   an optional pre-configured parser instance.
       *
       * @return a JDOM document.
       *
       * @throws TransformerException   when errors occurred in
       *                                parsing.
       */
        public Document build(InputSource in, XMLReader parser) throws TransformerException {
            SAXHandler contentHandler = null;
            try {
                contentHandler = createContentHandler();
                configureContentHandler(contentHandler);
                if (parser == null) {
                    parser = createParser();
                }
                configureParser(parser, contentHandler);
                parser.parse(in);
                return (contentHandler.getDocument());
            } catch (SAXParseException ex1) {
                StringBuffer message = new StringBuffer();
                message.append("Error on line ").append(ex1.getLineNumber());
                String systemId = ex1.getSystemId();
                if (systemId != null) {
                    message.append(" of document ").append(systemId);
                }
                message.append(": ").append(ex1.getMessage());
                throw (new TransformerException(message.toString(), new ErrorLocator((SAXParseException) ex1), ex1));
            } catch (SAXException ex2) {
                Exception cause = ex2.getException();
                if (cause == null) {
                    cause = ex2;
                }
                throw (new TransformerException("Error reading document: " + cause.getMessage(), cause));
            } catch (Exception ex3) {
                throw (new TransformerException("Error reading document: " + ex3.getMessage(), ex3));
            } finally {
                contentHandler = null;
            }
        }
    }

    /**
    * An implementation of the TrAX SourceLocator interface that
    * wraps a SAX Locator object.
    */
    private static class ErrorLocator implements SourceLocator {

        /**
       * The SAX Locator wrapped by this object.
       */
        private final Locator locator;

        /**
       * Creates an error locator from a SAX Locator object.
       *
       * @param  locator   the source SAX Locator.
       */
        public ErrorLocator(Locator locator) {
            this.locator = locator;
        }

        /**
       * Creates an error locator from a SAX parse exception.
       *
       * @param  error   the source SAX parse exception.
       */
        public ErrorLocator(SAXParseException error) {
            LocatorImpl aLocator = new LocatorImpl();
            aLocator.setPublicId(error.getPublicId());
            aLocator.setSystemId(error.getSystemId());
            aLocator.setLineNumber(error.getLineNumber());
            aLocator.setColumnNumber(error.getColumnNumber());
            this.locator = aLocator;
        }

        /**
       * Return the public identifier for the current document event.
       *
       * @return A string containing the public identifier, or
       *         <code>null</code> if none is available.
       */
        public String getPublicId() {
            return (this.locator.getPublicId());
        }

        /**
       * Return the system identifier for the current document event.
       *
       * @return A string containing the system identifier, or
       *         <code>null</code> if none is available.
       */
        public String getSystemId() {
            return (this.locator.getSystemId());
        }

        /**
       * Return the line number where the current document event
       * ends.
       *
       * @return the line number, or <code>-1</code> if none is
       * available.
       */
        public int getLineNumber() {
            return (this.locator.getLineNumber());
        }

        /**
       * Return the column number where the current document event
       * ends.
       *
       * @return the column number, or <code>-1</code> if none is
       * available.
       */
        public int getColumnNumber() {
            return (this.locator.getColumnNumber());
        }
    }

    /**
    * An implementation of the SAX EntityResolver interface that
    * wraps a TrAx URIResolver object.
    */
    private static class SaxUriResolver implements EntityResolver {

        /**
       * The TrAX URIResolver wrapped by this object.
       */
        private final URIResolver resolver;

        /**
       * The base URI to use when resolving relative URIs.
       */
        private final String baseUri;

        /**
       * Creates a SAX EntityResolver wrapping the specified TrAX
       * URIResolver
       *
       * @param  resolver   the TrAX URIResolver object.
       * @param  baseUri    the base URI to use when resolving
       *                    relative URIs.
       */
        public SaxUriResolver(URIResolver resolver, String baseUri) {
            this.resolver = resolver;
            this.baseUri = baseUri;
        }

        /**
       * Resolves external entities.
       *
       * @param  publicId   the public identifier of the external
       *                    entity being referenced, or
       *                    <code>null</code> if none was supplied.
       * @param  systemId   the system identifier of the external
       *                    entity being referenced.
       *
       * @return an InputSource object describing the new input
       *         source, or <code>null</code> to request that the
       *         parser open a regular URI connection to the system
       *         identifier.
       *
       * @throws SAXException   any SAX exception, possibly wrapping
       *                        another exception.
       *         IOException    a Java-specific IO exception, possibly
       *        the result of creating a new InputStream or Reader for the InputSource.See Also:
       */
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            InputSource resolvedInput = null;
            try {
                Source resolvedSource = this.resolver.resolve(systemId, this.baseUri);
                if (resolvedSource != null) {
                    resolvedInput = SAXSource.sourceToInputSource(resolvedSource);
                }
            } catch (TransformerException ex1) {
                throw (new SAXException("URIResolver error: " + ex1.getMessage(), ex1));
            }
            return (resolvedInput);
        }
    }
}
