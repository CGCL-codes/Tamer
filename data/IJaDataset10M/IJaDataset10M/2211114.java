package com.sun.xacml.ctx;

import java.util.logging.Logger;
import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * XACML context utillities and constants for internal use only.
 *
 * @since 2.0
 * @author Pascal S. de Kloe
 */
class Context {

    /** The namespace for XACML 1.0. */
    public static final String NAMESPACE10 = "urn:oasis:names:tc:xacml:1.0:context";

    /** The namespace for XACML 2.0. */
    public static final String NAMESPACE20 = "urn:oasis:names:tc:xacml:2.0:context:schema:os";

    public static final Schema SCHEMA;

    static {
        try {
            String prefix = "/com/sun/xacml/xml/schema/";
            InputStream policy10 = Context.class.getResourceAsStream(prefix + "access_control-xacml-2.0-policy-schema-os.xsd");
            InputStream context10 = Context.class.getResourceAsStream(prefix + "access_control-xacml-2.0-context-schema-os.xsd");
            InputStream policy20 = Context.class.getResourceAsStream(prefix + "cs-xacml-schema-policy-01.xsd");
            InputStream context20 = Context.class.getResourceAsStream(prefix + "cs-xacml-schema-context-01.xsd");
            Source[] source = new Source[] { new StreamSource(policy10), new StreamSource(context10), new StreamSource(policy20), new StreamSource(context20) };
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            SCHEMA = factory.newSchema(source);
        } catch (Exception e) {
            String message = "Failed to initialize XACML schemas.";
            throw new Error(message, e);
        }
    }

    private static final Logger log = Logger.getLogger(Context.class.getName());

    /**
     * Creates a new document.
     *
     * @param namespace the namespace for the root element.
     * @param rootNode the name for the root element.
     */
    static Document createDocument(String namespace, String rootNode) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation dom = builder.getDOMImplementation();
            return dom.createDocument(namespace, rootNode, null);
        } catch (ParserConfigurationException e) {
            String message = "Could not create XACML context DOM";
            log.severe(message + ": " + e.getMessage());
            throw new Error(message, e);
        }
    }

    /**
     * Checks whether the {@code uri} is a supported XACML context namespace.
     */
    static boolean isSupportedNamespace(String uri) {
        return NAMESPACE10.equals(uri) || NAMESPACE20.equals(uri);
    }

    /**
     * Converts a context namespace to a policy namespace with the same version.
     */
    static String toPolicyNamespace(String uri) {
        if (uri.equals(NAMESPACE10)) return "";
        if (uri.equals(NAMESPACE20)) return "";
        throw new IllegalArgumentException("Unsupported context namespace");
    }

    /**
     * Checks whether node {@code x} is a element with the specified
     * {@code name}.
     *
     * @param name the element name without prefix.
     */
    static boolean isElement(Node x, String name) {
        if (x.getNodeType() != Node.ELEMENT_NODE) return false;
        return checkName(x, name);
    }

    /**
     * Checks whether node {@code x} is a attribute with the specified
     * {@code name}.
     *
     * @param name the attribute name without prefix.
     */
    static boolean isAttribute(Node x, String name) {
        if (x.getNodeType() != Node.ATTRIBUTE_NODE) return false;
        return checkName(x, name);
    }

    private static boolean checkName(Node x, String name) {
        assert name.indexOf(':') < 0;
        String localName = x.getLocalName();
        if (localName != null) return localName.equals(name);
        String fullName = x.getNodeName();
        return fullName.equals(name) || fullName.endsWith(":" + name);
    }
}
