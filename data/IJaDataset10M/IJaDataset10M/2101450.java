package org.custommonkey.xmlunit;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.Properties;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Handy wrapper for an XSLT transformation performed using JAXP/Trax.
 * Note that transformation is not actually performed until a call to
 * <code>getResultXXX</code> method, and Templates are not used.
 * <br />Examples and more at <a href="http://xmlunit.sourceforge.net"/>xmlunit.sourceforge.net</a>
 */
public class Transform {

    private static final File PWD = new File(".");

    private final Source inputSource;

    private final Transformer transformer;

    /**
     * Create a transformation using String input XML and String stylesheet
     * @param input
     * @param stylesheet
     * @throws TransformerConfigurationException
     */
    public Transform(String input, String stylesheet) throws TransformerConfigurationException {
        this(new StreamSource(new StringReader(input)), new StreamSource(new StringReader(stylesheet)));
    }

    /**
     * Create a transformation using String input XML and stylesheet in a File
     * @param input
     * @param stylesheet
     * @throws TransformerConfigurationException
     */
    public Transform(String input, File stylesheet) throws TransformerConfigurationException {
        this(new StreamSource(new StringReader(input)), new StreamSource(stylesheet));
    }

    /**
     * Create a transformation that allows us to serialize a DOM Node
     * @param source
     * @throws TransformerConfigurationException
     */
    public Transform(Node sourceNode) throws TransformerConfigurationException {
        this(sourceNode, (Source) null);
    }

    /**
     * Create a transformation from an input Node and stylesheet in a String
     * @param sourceNode
     * @param stylesheet
     * @throws TransformerConfigurationException
     */
    public Transform(Node sourceNode, String stylesheet) throws TransformerConfigurationException {
        this(sourceNode, new StreamSource(new StringReader(stylesheet)));
    }

    /**
     * Create a transformation from an input Node and stylesheet in a File
     * @param sourceNode
     * @param stylesheet
     * @throws TransformerConfigurationException
     */
    public Transform(Node sourceNode, File stylesheet) throws TransformerConfigurationException {
        this(sourceNode, new StreamSource(stylesheet));
    }

    /**
     * Create a transformation from an input Node and stylesheet in a Source
     * @param sourceNode
     * @param stylesheetSource
     */
    private Transform(Node sourceNode, Source stylesheetSource) throws TransformerConfigurationException {
        this(new DOMSource(sourceNode), stylesheetSource);
    }

    /**
     * Create a transformation using Source input XML and Source stylesheet
     * @param inputReader
     * @param stylesheetReader
     * @throws TransformerConfigurationException
     */
    public Transform(Source inputSource, Source stylesheetSource) throws TransformerConfigurationException {
        this.inputSource = inputSource;
        provideSystemIdIfRequired(inputSource);
        provideSystemIdIfRequired(stylesheetSource);
        this.transformer = getTransformer(stylesheetSource);
    }

    /**
     * Ensure that the source has a systemId
     * @param source
     */
    private void provideSystemIdIfRequired(Source source) {
        if (source != null && (source.getSystemId() == null || source.getSystemId().length() == 0)) {
            source.setSystemId(getDefaultSystemId());
        }
    }

    /**
     * @return the current working directory as an URL-form string
     */
    private String getDefaultSystemId() {
        try {
            return PWD.toURL().toExternalForm();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Unable to determine current working directory!");
        }
    }

    /**
     * Factory method
     * @param stylesheetSource
     * @return
     * @throws TransformerConfigurationException
     */
    private Transformer getTransformer(Source stylesheetSource) throws TransformerConfigurationException {
        TransformerFactory factory = XMLUnit.getTransformerFactory();
        if (stylesheetSource == null) {
            return factory.newTransformer();
        }
        return factory.newTransformer(stylesheetSource);
    }

    /**
     * Perform the actual transformation
     * @param result
     * @throws TransformerException
     */
    protected void transformTo(Result result) throws TransformerException {
        transformer.transform(inputSource, result);
    }

    /**
     * Perform the XSLT transformation specified in the constructor
     * @return the result as a String
     * @throws TransformerException
     */
    public String getResultString() throws TransformerException {
        StringWriter outputWriter = new StringWriter();
        transformTo(new StreamResult(outputWriter));
        return outputWriter.toString();
    }

    /**
     * Perform the XSLT transformation specified in the constructor
     * @return the result as a DOM Document
     * @throws TransformerException
     */
    public Document getResultDocument() throws TransformerException {
        DOMResult result = new DOMResult();
        transformTo(result);
        return (Document) result.getNode();
    }

    /**
     * Override an output property specified in the transformation stylesheet
     * @param name
     * @param value
     */
    public void setOutputProperty(String name, String value) {
        Properties properties = new Properties();
        properties.setProperty(name, value);
        setOutputProperties(properties);
    }

    /**
     * Override output properties specified in the transformation stylesheet
     * @param outputProperties
     * @see Transformer#setOutputProperties(java.util.Properties)
     */
    public void setOutputProperties(Properties outputProperties) {
        transformer.setOutputProperties(outputProperties);
    }

    /**
     * Add a parameter for the transformation
     * @param name
     * @param value
     * @see Transformer#setParameter(java.lang.String, java.lang.Object)
     */
    public void setParameter(String name, Object value) {
        transformer.setParameter(name, value);
    }

    /**
     * See a parameter used for the transformation
     * @param name
     * @return the parameter value
     * @see Transformer#getParameter(java.lang.String)
     */
    public Object getParameter(String name) {
        return transformer.getParameter(name);
    }

    /**
     * Clear parameters used for the transformation 
     * @see Transformer#clearParameters()
     */
    public void clearParameters() {
        transformer.clearParameters();
    }

    /**
     * Set the URIResolver for the transformation
     * @see Transformer#setURIResolver(javax.xml.transform.URIResolver)
     */
    public void setURIResolver(URIResolver uriResolver) {
        transformer.setURIResolver(uriResolver);
    }

    /**
     * Set the ErrorListener for the transformation
     * @see Transformer#setErrorListener(javax.xml.transform.ErrorListener)
     */
    public void setErrorListener(ErrorListener errorListener) {
        transformer.setErrorListener(errorListener);
    }
}
