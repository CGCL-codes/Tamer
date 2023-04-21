package org.dataminx.dts.common.xml;

import static java.util.Collections.unmodifiableMap;
import static org.apache.commons.collections.MapUtils.putAll;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.collections.MapUtils;
import org.dataminx.dts.DtsException;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A utility class that deals with XML conversions.
 *
 * @author Alex Arana
 * @author Gerson Galang
 */
public final class XmlUtils {

    /**
     * The indent amount of characters to use when indenting is enabled.
     * <p>Defaults to "2".
     */
    public static final int DEFAULT_INDENT_AMOUNT = 2;

    /**
     * Default set of output properties applied when creating a new instance of a {@link Transformer}
     * object.
     */
    @SuppressWarnings("unchecked")
    public static final Map<String, String> DEFAULT_TRANSFORMER_OUTPUT_PROPERTIES = unmodifiableMap(putAll(new HashMap<String, String>(), new Object[] { new String[] { OutputKeys.INDENT, "yes" }, new String[] { "{http://xml.apache.org/xslt}indent-amount", String.valueOf(DEFAULT_INDENT_AMOUNT) } }));

    /**
     * Prevent public instantiation of this class.
     */
    protected XmlUtils() {
    }

    /**
     * Returns a String representation of the given XML document.
     * <p>
     * <em>NOTE</em>: Care must be taken not to use XMLBeans DOM documents as input to this method
     * while using DOM Level 3 implementations such as the default JAXP bundled with JDK 1.6.  XMLBeans
     * only support DOM Level 2.  Refer to this <a href="https://issues.apache.org/jira/browse/XMLBEANS-100">
     * online resource</a> for additional information.
     *
     * @param document An XML document as an instance of {@link Document}
     * @return a String representation of the given XML document
     * @throws DtsException if an error occurs serialising the input document
     */
    public static String documentToString(final Document document) throws DtsException {
        Assert.notNull(document, "Document is null in call to documentToString()");
        try {
            final ByteArrayResult result = new ByteArrayResult();
            newTransformer().transform(new DOMSource(document), result);
            return result.toString();
        } catch (final TransformerException ex) {
            throw new DtsXmlTransformationException("A transformation error occurred serialising the input Document to a String: " + ex, ex);
        }
    }

    /**
     * Returns the org.w3c.dom.Document equivalent of the given XML string.
     *
     * @param xmlString the XML String
     * @return the org.w3c.dom.Document equivalent of the given XML string
     * @throws DtsException if an error occurs while parsing the xmlString
     */
    public static Document stringToDocument(final String xmlString) throws DtsException {
        if (xmlString == null) {
            throw new NullPointerException("xmlString is null in call to stringToDocument()");
        }
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document = builder.parse(new InputSource(new StringReader(xmlString)));
            return document;
        } catch (final SAXException ex) {
            throw new DtsException("A transformation error occurred converting the XML string to a Document: " + ex, ex);
        } catch (final ParserConfigurationException ex) {
            throw new DtsException("A transformation error occurred converting the XML string to a Document: " + ex, ex);
        } catch (final IOException ex) {
            throw new DtsException("A transformation error occurred converting the XML string to a Document: " + ex, ex);
        }
    }

    /**
     * Creates a new instance of a {@link javax.xml.parsers.DocumentBuilder} using the currently configured
     * parameters.
     *
     * @param properties A Map of String -> Object pairs that can be used to configure properties on the
     *        underlying parser.  For a list of Xerces-specific configuration properties, refer to the
     *        following <a href="http://xerces.apache.org/xerces2-j/properties.html">online resource</a>.
     *
     * @return A new instance of a DocumentBuilder.
     * @throws ParserConfigurationException if a DocumentBuilder cannot be created which satisfies the
     *         configuration requested.
     */
    public static DocumentBuilder newDocumentBuilder(final Map<String, Object> properties) throws ParserConfigurationException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        if (MapUtils.isNotEmpty(properties)) {
            for (final String key : properties.keySet()) {
                factory.setAttribute(key, properties.get(key));
            }
        }
        return factory.newDocumentBuilder();
    }

    /**
     * Obtain a new instance of a DOM {@link Document} object to build a DOM tree with.
     *
     * @return A new instance of a DOM Document object.
     * @throws DtsXmlConfigurationException if a DocumentBuilder cannot be created to obtain the new Document
     *         instance.
     */
    public static Document newDocument() throws DtsXmlConfigurationException {
        try {
            return newDocumentBuilder(null).newDocument();
        } catch (final ParserConfigurationException ex) {
            throw new DtsXmlConfigurationException(ex);
        }
    }

    /**
     * Processes the specified stylesheet resource into an instance of a {@link Templates} object, which is a
     * compiled representation of the source.
     *
     * @param stylesheet Resource that references an XSLT document used to create <code>Transformer</code>
     * @return A new instance of a <code>Templates</code> object.
     * @throws DtsXmlConfigurationException if either the Transformer factory cannot be instantiated due to a
     *         configuration error or parsing the Source fails
     * @see #newTemplates(Source)
     */
    public static Templates newTemplates(final Resource stylesheet) throws DtsXmlConfigurationException {
        Assert.notNull(stylesheet);
        try {
            return newTemplates(new StreamSource(stylesheet.getInputStream()));
        } catch (final IOException ex) {
            throw new DtsException("An I/O error occurred loading the specified stylesheet: " + stylesheet, ex);
        }
    }

    /**
     * Processes the specified stylesheet into an instance of a {@link Templates} object, which is a compiled
     * representation of the source. This {@link Templates} object may then be used concurrently across multiple
     * threads to create {@link Transformer} instances.
     * <p>
     * The stylesheet {@link Source} is an XSLT document that conforms to <a href="http://www.w3.org/TR/xslt">
     * XSL Transformations (XSLT) Version 1.0</a>.
     *
     * @param stylesheet <code>Source</code> of XSLT document used to create <code>Transformer</code>
     * @return A new instance of a <code>Templates</code> object.
     * @throws DtsXmlConfigurationException if either the Transformer factory cannot be instantiated due to a
     *         configuration error or parsing the Source fails
     */
    public static Templates newTemplates(final Source stylesheet) throws DtsXmlConfigurationException {
        Assert.notNull(stylesheet);
        try {
            return TransformerFactory.newInstance().newTemplates(stylesheet);
        } catch (final TransformerConfigurationException ex) {
            throw new DtsXmlConfigurationException("A transformation configuration error has occurred: " + ex, ex);
        } catch (final TransformerFactoryConfigurationError ex) {
            throw new DtsXmlConfigurationException("A transformation factory configuration error has occurred: " + ex, ex);
        }
    }

    /**
     * Creates new instance of a {@link Transformer} using the specified stylesheet  that may then be used
     * to perform an XML transformation.
     * <p>
     * The stylesheet {@link Source} is an XSLT document that conforms to <a href="http://www.w3.org/TR/xslt">
     * XSL Transformations (XSLT) Version 1.0</a>.
     *
     * @param stylesheet <code>Source</code> of XSLT document used to create <code>Transformer</code>
     * @return A new instance of a Transformer object.
     * @throws DtsXmlConfigurationException if the Transformer factory cannot be instantiated due to a
     *         configuration error
     */
    public static Transformer newTransformer(final Source stylesheet) throws DtsXmlConfigurationException {
        Assert.notNull(stylesheet);
        try {
            final Transformer transformer = TransformerFactory.newInstance().newTransformer(stylesheet);
            for (final Map.Entry<String, String> entry : DEFAULT_TRANSFORMER_OUTPUT_PROPERTIES.entrySet()) {
                transformer.setOutputProperty(entry.getKey(), entry.getValue());
            }
            return transformer;
        } catch (final TransformerConfigurationException ex) {
            throw new DtsXmlConfigurationException("A transformation configuration error has occurred: " + ex, ex);
        } catch (final TransformerFactoryConfigurationError ex) {
            throw new DtsXmlConfigurationException("A transformation factory configuration error has occurred: " + ex, ex);
        }
    }

    /**
     * Obtain a new instance of a {@link Transformer} object that may be used to perform an XML transformation.
     *
     * @return A new instance of a Transformer object.
     * @throws DtsXmlConfigurationException if the Transformer factory cannot be instantiated due to a
     *         configuration error
     */
    public static Transformer newTransformer() throws DtsXmlConfigurationException {
        try {
            final Transformer transformer = TransformerFactory.newInstance().newTransformer();
            for (final Map.Entry<String, String> entry : DEFAULT_TRANSFORMER_OUTPUT_PROPERTIES.entrySet()) {
                transformer.setOutputProperty(entry.getKey(), entry.getValue());
            }
            return transformer;
        } catch (final TransformerConfigurationException ex) {
            throw new DtsXmlConfigurationException("A transformation configuration error has occurred: " + ex, ex);
        } catch (final TransformerFactoryConfigurationError ex) {
            throw new DtsXmlConfigurationException("A transformation factory configuration error has occurred: " + ex, ex);
        }
    }

    /**
     * Transform the XML {@link Source} input into a {@link Result} object.
     * <p>
     * <em>NOTE</em>: Care must be taken not to use XMLBeans DOM documents as input in a transformation
     * while using DOM Level 3 implementations such as the default JAXP bundled with JDK 1.6.  XMLBeans
     * only support DOM Level 2.  Refer to this <a href="https://issues.apache.org/jira/browse/XMLBEANS-100">
     * online resource</a> for additional details.
     *
     * @param source The XML input to transform.
     * @param result The <code>Result</code> of transforming the <code>source</code>
     *
     * @throws DtsXmlConfigurationException if the Transformer factory cannot be instantiated due to a
     *         configuration error
     * @throws DtsXmlTransformationException If an unrecoverable error occurs during the course of the
     *         transformation.
     */
    public static void transform(final Source source, final Result result) throws DtsXmlConfigurationException, DtsXmlTransformationException {
        try {
            newTransformer().transform(source, result);
        } catch (final TransformerException ex) {
            throw new DtsXmlTransformationException("An XML transformation error has occurred: " + ex, ex);
        }
    }

    /**
     * Transform the XML {@link Source} input into a {@link Result} object using the specified XSLT
     * stylesheet.
     * <p>
     * The {@link Source} stylesheet is an XSLT document that conforms to <a href="http://www.w3.org/TR/xslt">
     * XSL Transformations (XSLT) Version 1.0</a>.
     * <p>
     * <em>NOTE</em>: Care must be taken not to use XMLBeans DOM documents as input in a transformation
     * while using DOM Level 3 implementations such as the default JAXP bundled with JDK 1.6.  XMLBeans
     * only support DOM Level 2.  Refer to this <a href="https://issues.apache.org/jira/browse/XMLBEANS-100">
     * online resource</a> for additional details.
     *
     * @param stylesheet <code>Source</code> of XSLT document used to create <code>Transformer</code>
     * @param xml The XML input to transform.
     * @param result The <code>Result</code> of transforming the <code>source</code>
     *
     * @throws DtsXmlConfigurationException if the Transformer factory cannot be instantiated due to a
     *         configuration error
     * @throws DtsXmlTransformationException If an unrecoverable error occurs during the course of the
     *         transformation.
     */
    public static void transform(final Source stylesheet, final Source xml, final Result result) throws DtsXmlConfigurationException, DtsXmlTransformationException {
        try {
            newTransformer(stylesheet).transform(xml, result);
        } catch (final TransformerException ex) {
            throw new DtsXmlTransformationException("An XML transformation error has occurred: " + ex, ex);
        }
    }
}
