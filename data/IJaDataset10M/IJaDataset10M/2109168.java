package org.orbeon.oxf.processor;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;
import org.orbeon.oxf.common.OXFException;
import org.orbeon.oxf.pipeline.api.PipelineContext;
import org.orbeon.oxf.processor.xforms.XFormsUtils;
import org.orbeon.oxf.processor.xforms.Constants;
import org.orbeon.oxf.util.PooledXPathExpression;
import org.orbeon.oxf.util.XPathCache;
import org.orbeon.oxf.util.SecureUtils;
import org.orbeon.oxf.xml.XMLUtils;
import org.orbeon.oxf.xml.dom4j.LocationData;
import org.orbeon.oxf.resources.OXFProperties;
import org.orbeon.saxon.dom4j.DocumentWrapper;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Convert an XForms instance into a list of parameters.
 *
 * A filter can be provided. It contains XPath references to nodes that should not be included in
 * the result. The format of the filter comes directly from the native document created in the WAC,
 * for example:
 *
 * <params xmlns="http://www.orbeon.com/oxf/controller">
 *    <param ref="/form/x"/>
 *    <param ref="/form/y"/>
 *    <param ref="/form/z"/>
 * </params>
 */
public class InstanceToParametersProcessor extends ProcessorImpl {

    public static final String PARAMETERS_ELEMENT = "parameters";

    public static final String PARAMETER_ELEMENT = "parameter";

    public static final String NAME_ELEMENT = "name";

    public static final String VALUE_ELEMENT = "value";

    private static final String INPUT_INSTANCE = "instance";

    private static final String INPUT_FILTER = "filter";

    public InstanceToParametersProcessor() {
        addInputInfo(new ProcessorInputOutputInfo(INPUT_INSTANCE));
        addInputInfo(new ProcessorInputOutputInfo(INPUT_FILTER));
        addOutputInfo(new ProcessorInputOutputInfo(OUTPUT_DATA));
    }

    public ProcessorOutput createOutput(String name) {
        ProcessorOutput output = new ProcessorImpl.ProcessorOutputImpl(getClass(), name) {

            public void readImpl(PipelineContext pipelineContext, final ContentHandler contentHandler) {
                try {
                    Element filterElement = readInputAsDOM4J(pipelineContext, INPUT_FILTER).getRootElement();
                    Document instance = DocumentHelper.createDocument(readInputAsDOM4J(pipelineContext, INPUT_INSTANCE).getRootElement().createCopy());
                    DocumentWrapper instanceWrapper = new DocumentWrapper(instance, ((LocationData) instance.getRootElement().getData()).getSystemID());
                    final Set markedNodes = new HashSet();
                    for (Iterator i = filterElement.elements().iterator(); i.hasNext(); ) {
                        Element paramElement = (Element) i.next();
                        Attribute refAttribute = paramElement.attribute("ref");
                        String excludeRef = refAttribute.getValue();
                        PooledXPathExpression xpath = XPathCache.getXPathExpression(pipelineContext, instanceWrapper.wrap(instance), excludeRef, XMLUtils.getNamespaceContext(paramElement));
                        try {
                            markedNodes.add(xpath.evaluateSingle());
                        } finally {
                            if (xpath != null) xpath.returnToPool();
                        }
                    }
                    final boolean[] allMarked = { true };
                    instance.accept(new VisitorSupport() {

                        public void visit(Element node) {
                            super.visit(node);
                            if (node.elements().size() == 0 && !markedNodes.contains(node)) allMarked[0] = false;
                        }

                        public void visit(Attribute node) {
                            super.visit(node);
                            if (!markedNodes.contains(node)) allMarked[0] = false;
                        }
                    });
                    contentHandler.startDocument();
                    contentHandler.startElement("", PARAMETERS_ELEMENT, PARAMETERS_ELEMENT, XMLUtils.EMPTY_ATTRIBUTES);
                    if (!allMarked[0]) {
                        String key = null;
                        if (XFormsUtils.isHiddenEncryptionEnabled() || XFormsUtils.isNameEncryptionEnabled()) {
                            key = SecureUtils.generateRandomPassword();
                            String serverPassword = OXFProperties.instance().getPropertySet().getString(Constants.XFORMS_PASSWORD);
                            outputParameter("$key", SecureUtils.encrypt(pipelineContext, serverPassword, key), contentHandler);
                        }
                        outputParameter("$instance", XFormsUtils.instanceToString(pipelineContext, key, instance), contentHandler);
                    }
                    contentHandler.endElement("", PARAMETERS_ELEMENT, PARAMETERS_ELEMENT);
                    contentHandler.endDocument();
                } catch (Exception e) {
                    throw new OXFException(e);
                }
            }
        };
        addOutput(OUTPUT_DATA, output);
        return output;
    }

    private static void outputParameter(String name, String value, ContentHandler contentHandler) throws SAXException {
        contentHandler.startElement("", PARAMETER_ELEMENT, PARAMETER_ELEMENT, XMLUtils.EMPTY_ATTRIBUTES);
        outputElement(NAME_ELEMENT, name, contentHandler);
        outputElement(VALUE_ELEMENT, value, contentHandler);
        contentHandler.endElement("", PARAMETER_ELEMENT, PARAMETER_ELEMENT);
    }

    private static void outputElement(String name, String content, ContentHandler contentHandler) throws SAXException {
        contentHandler.startElement("", name, name, XMLUtils.EMPTY_ATTRIBUTES);
        contentHandler.characters(content.toCharArray(), 0, content.length());
        contentHandler.endElement("", name, name);
    }
}
