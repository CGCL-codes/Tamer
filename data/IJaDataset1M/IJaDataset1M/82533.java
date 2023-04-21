package org.toobsframework.tags;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xalan.extensions.XSLProcessorContext;
import org.apache.xalan.templates.ElemExtensionCall;
import org.apache.xalan.templates.OutputProperties;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.toobsframework.pres.util.IComponentRequest;
import org.toobsframework.pres.xsl.ComponentTransformerHelper;
import org.toobsframework.transformpipeline.transformer.IXMLTransformer;
import org.toobsframework.util.Configuration;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class TagBase {

    protected final Log log = LogFactory.getLog(getClass());

    /**
   * Obtain the helper (which is stored as a property in the xsl)
   * @param processorContext is the context call for th transformation
   * @return the context
   * @throws TransformerException if not foud
   */
    protected ComponentTransformerHelper getTransformerHelper(XSLProcessorContext processorContext) throws TransformerException {
        TransformerImpl transformer = processorContext.getTransformer();
        Object th = transformer.getParameter(IXMLTransformer.TRANSFORMER_HELPER);
        if (th == null || !(th instanceof ComponentTransformerHelper)) {
            TransformerException ex = new TransformerException("Internal error: the property " + IXMLTransformer.TRANSFORMER_HELPER + " needs to be properly initialized prior to calling the transformation.");
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        ComponentTransformerHelper transformerHelper = (ComponentTransformerHelper) th;
        return transformerHelper;
    }

    protected IComponentRequest getComponentRequest(XSLProcessorContext processorContext) throws TransformerException {
        TransformerImpl transformer = processorContext.getTransformer();
        Object th = transformer.getParameter(IXMLTransformer.COMPONENT_REQUEST);
        if (th == null || !(th instanceof IComponentRequest)) {
            TransformerException ex = new TransformerException("Internal error: the property " + IXMLTransformer.COMPONENT_REQUEST + " needs to be properly initialized prior to calling the transformation.");
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        IComponentRequest request = (IComponentRequest) th;
        return request;
    }

    protected Configuration getConfiguration(XSLProcessorContext processorContext) throws TransformerException {
        TransformerImpl transformer = processorContext.getTransformer();
        Object th = transformer.getParameter(IXMLTransformer.TRANSFORMER_HELPER);
        if (th == null || !(th instanceof ComponentTransformerHelper)) {
            TransformerException ex = new TransformerException("Internal error: the property " + IXMLTransformer.TRANSFORMER_HELPER + " needs to be properly initialized prior to calling the transformation.");
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        ComponentTransformerHelper transformerHelper = (ComponentTransformerHelper) th;
        return transformerHelper.getConfiguration();
    }

    /**
   * Write a string to the xml output stream, controlling escaping of XML characters
   * @param processorContext - passed to the tags
   * @param extensionElement - passed to the tags
   * @param result is the object to serialize out
   * @param escape - if true, XML characters are converted to XML equivalents, otherwise they are streamed straight
   */
    protected void serialize(XSLProcessorContext processorContext, ElemExtensionCall extensionElement, Object result, boolean escape) throws SAXException, MalformedURLException, FileNotFoundException, TransformerException, IOException {
        TransformerImpl transformer = processorContext.getTransformer();
        SerializationHandler handler = transformer.getResultTreeHandler();
        boolean previousEscaping = handler.setEscaping(escape);
        processorContext.outputToResultTree(extensionElement.getStylesheet(), result);
        handler.setEscaping(previousEscaping);
    }

    protected Object getOutputObject(Object input) throws UnsupportedEncodingException {
        if (input instanceof DOMResult) {
            return ((DOMResult) input).getNode();
        }
        if (input instanceof StreamResult) {
            return new String(((ByteArrayOutputStream) ((StreamResult) input).getOutputStream()).toByteArray(), "UTF-8");
        }
        return input.toString();
    }

    /**
   * execute child templates and collect the text as a result of this execution
   * and return it to the calling function
   * 
   * @param processorContext - provided by xalan, pass through to this function
   * @param extensionElement - provided by xalan, pass through to this function
   * @return the string result
   * @throws TransformerException 
   */
    protected String executeChildTemplatesAsText(XSLProcessorContext processorContext, ElemExtensionCall extensionElement) throws TransformerException {
        TransformerImpl transformer = processorContext.getTransformer();
        OutputProperties format = transformer.getOutputFormat();
        Writer writer = new StringWriter();
        ContentHandler handler = transformer.createSerializationHandler(new StreamResult(writer), format);
        transformer.executeChildTemplates(extensionElement, processorContext.getContextNode(), processorContext.getMode(), handler);
        return writer.toString();
    }

    /**
   * Get a property for a tag from the context passed
   * @param name is the name of the attribute
   * @param processorContext passed to the tag
   * @param extensionElement passed to the tag
   * @return the value or null if the tag is not there
   * @throws TransformerException on error
   */
    protected String getStringProperty(String name, org.apache.xalan.extensions.XSLProcessorContext processorContext, org.apache.xalan.templates.ElemExtensionCall extensionElement) throws TransformerException {
        return extensionElement.getAttribute(name, processorContext.getContextNode(), processorContext.getTransformer());
    }

    /**
   * Get a property for a tag from the context passed, or the default if it does not exist
   * @param name is the name of the attribute
   * @param defaultValue is the value to be used in case it is not there
   * @param processorContext passed to the tag
   * @param extensionElement passed to the tag
   * @return the value or the default if the tag is not there
   * @throws TransformerException on error
   */
    protected String getStringProperty(String name, String defaultValue, org.apache.xalan.extensions.XSLProcessorContext processorContext, org.apache.xalan.templates.ElemExtensionCall extensionElement) throws TransformerException {
        String value = extensionElement.getAttribute(name, processorContext.getContextNode(), processorContext.getTransformer());
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    /**
   * Get a property for a tag from the context passed, and err if it is not there
   * @param name is the name of the attribute
   * @param processorContext passed to the tag
   * @param extensionElement passed to the tag
   * @return the value 
   * @throws TransformerException on error or the tag missing
   */
    protected String getRequiredStringProperty(String name, String message, org.apache.xalan.extensions.XSLProcessorContext processorContext, org.apache.xalan.templates.ElemExtensionCall extensionElement) throws TransformerException {
        String value = extensionElement.getAttribute(name, processorContext.getContextNode(), processorContext.getTransformer());
        if (value == null || value.length() == 0) {
            TransformerException ex = new TransformerException(message);
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        return value;
    }

    /**
   * Get a property for a tag from the context passed
   * @param name is the name of the attribute
   * @param processorContext passed to the tag
   * @param extensionElement passed to the tag
   * @return the value or null if the tag is not there
   * @throws TransformerException on error
   */
    protected boolean getBooleanProperty(String name, org.apache.xalan.extensions.XSLProcessorContext processorContext, org.apache.xalan.templates.ElemExtensionCall extensionElement) throws TransformerException {
        String value = getStringProperty(name, processorContext, extensionElement);
        if (value == null) {
            return false;
        }
        value = value.trim();
        return value.equals("true") || value.equals("yes") || value.equals("1");
    }

    /**
   * Get a property for a tag from the context passed, or the default if it does not exist
   * @param name is the name of the attribute
   * @param defaultValue is the value to be used in case it is not there
   * @param processorContext passed to the tag
   * @param extensionElement passed to the tag
   * @return the value or the default if the tag is not there
   * @throws TransformerException on error
   */
    protected boolean getBooleanProperty(String name, boolean defaultValue, org.apache.xalan.extensions.XSLProcessorContext processorContext, org.apache.xalan.templates.ElemExtensionCall extensionElement) throws TransformerException {
        String value = getStringProperty(name, processorContext, extensionElement);
        if (value == null) {
            return defaultValue;
        }
        value = value.trim();
        return value.equals("true") || value.equals("yes") || value.equals("1");
    }

    /**
   * Get a property for a tag from the context passed, and err if it is not there
   * @param name is the name of the attribute
   * @param processorContext passed to the tag
   * @param extensionElement passed to the tag
   * @return the value 
   * @throws TransformerException on error or the tag missing
   */
    protected boolean getRequiredBooleanProperty(String name, String message, org.apache.xalan.extensions.XSLProcessorContext processorContext, org.apache.xalan.templates.ElemExtensionCall extensionElement) throws TransformerException {
        String value = getStringProperty(name, processorContext, extensionElement);
        if (value == null || value.length() == 0) {
            TransformerException ex = new TransformerException(message);
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        value = value.trim();
        return value.equals("true") || value.equals("yes") || value.equals("1");
    }

    /**
   * Get a property for a tag from the context passed
   * @param name is the name of the attribute
   * @param processorContext passed to the tag
   * @param extensionElement passed to the tag
   * @return the value or null if the tag is not there
   * @throws TransformerException on error
   */
    protected int getIntegerProperty(String name, org.apache.xalan.extensions.XSLProcessorContext processorContext, org.apache.xalan.templates.ElemExtensionCall extensionElement) throws TransformerException {
        String value = getStringProperty(name, processorContext, extensionElement);
        if (value == null) {
            return 0;
        }
        value = value.trim();
        return Integer.parseInt(value);
    }

    /**
   * Get a property for a tag from the context passed, or the default if it does not exist
   * @param name is the name of the attribute
   * @param defaultValue is the value to be used in case it is not there
   * @param processorContext passed to the tag
   * @param extensionElement passed to the tag
   * @return the value or the default if the tag is not there
   * @throws TransformerException on error
   */
    protected int getIntegerProperty(String name, int defaultValue, org.apache.xalan.extensions.XSLProcessorContext processorContext, org.apache.xalan.templates.ElemExtensionCall extensionElement) throws TransformerException {
        String value = getStringProperty(name, processorContext, extensionElement);
        if (value == null) {
            return defaultValue;
        }
        value = value.trim();
        return Integer.parseInt(value);
    }

    /**
   * Get a property for a tag from the context passed, and err if it is not there
   * @param name is the name of the attribute
   * @param processorContext passed to the tag
   * @param extensionElement passed to the tag
   * @return the value 
   * @throws TransformerException on error or the tag missing
   */
    protected int getRequiredIntegerProperty(String name, String message, org.apache.xalan.extensions.XSLProcessorContext processorContext, org.apache.xalan.templates.ElemExtensionCall extensionElement) throws TransformerException {
        String value = getStringProperty(name, processorContext, extensionElement);
        if (value == null || value.length() == 0) {
            TransformerException ex = new TransformerException(message);
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        value = value.trim();
        return Integer.parseInt(value);
    }
}
