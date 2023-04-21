package com.volantis.xml.pipeline.sax.impl.operations.diselect;

import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionParser;
import com.volantis.xml.expression.PipelineExpressionHelper;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableQName;
import com.volantis.xml.namespace.QName;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.impl.dynamic.PassThroughProcess;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import java.util.Stack;

/**
 * Process used within the preprocessing part of the pipeline to implement the
 * DISelect conditional processing functionality.
 *
 * <p>Supports 'if', 'select', 'when', 'otherwise', elements along with
 * 'diselect:expr', 'diselect:selidname', and 'diselect:selid' attributes.</p>
 *
 * <p>The selidname is maintained in the State as an {@link ExpandedName}
 * because the namespace prefix mappings may change between the point it is
 * specified and the point at which it is used. As there is no way of adding
 * new namespace prefix mappings if at the point it is used there is no non
 * empty prefix associated with the namespace then it is an error.</p>
 */
public final class DISelectConditionalProcess extends PassThroughProcess {

    /**
     * The parser used to parse the expressions, this could be shared with
     * another process within the same pipeline, but will not be used
     * concurrently.
     */
    private ExpressionParser parser;

    /**
     * The stack of states used by this to manage the current selidname,
     * remember whether the body of an element was processed, and the
     * select / when / otherwise interactions.
     */
    private Stack<State> states;

    /**
     * Initialise.
     *
     * @param parser The expression parser.
     */
    public DISelectConditionalProcess(ExpressionParser parser) {
        this.parser = parser;
        states = new Stack<State>();
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException {
        Element diSelectElement;
        if (inPassThroughMode || (diSelectElement = getDISelectElement(namespaceURI, localName)) == null) {
            super.startElement(namespaceURI, localName, qName, attributes);
            return;
        }
        XMLPipelineContext pipelineContext = getPipelineContext();
        State containingState = peekState();
        State state = diSelectElement.createState(containingState, attributes);
        states.push(state);
        int index;
        AttributesImpl output = null;
        if (!state.canProcessBody()) {
            pipelineContext.getFlowControlManager().exitCurrentElement();
            state.setProcessBody(false);
            return;
        }
        boolean forwardEvent = diSelectElement.getForwardEvent();
        String exprAttributeURI = diSelectElement.getExprAttributeURI();
        if (exprAttributeURI != null) {
            index = attributes.getIndex(exprAttributeURI, "expr");
            if (index != -1) {
                String value = attributes.getValue(index);
                try {
                    Expression expression = parser.parse(value);
                    ExpressionContext expressionContext = pipelineContext.getExpressionContext();
                    boolean result = PipelineExpressionHelper.fnBoolean(expression.evaluate(expressionContext).getSequence()).asJavaBoolean();
                    if (!result) {
                        pipelineContext.getFlowControlManager().exitCurrentElement();
                        state.setProcessBody(false);
                        return;
                    }
                    if (forwardEvent) {
                        if (output == null) {
                            output = new AttributesImpl(attributes);
                            attributes = output;
                        }
                        output.removeAttribute(index);
                    }
                } catch (ExpressionException e) {
                    Locator locator = pipelineContext.getCurrentLocator();
                    XMLPipelineException se = new XMLPipelineException("Could not evaluate the expression " + value, locator, e);
                    fatalError(se);
                }
            }
        }
        state.setProcessBody(true);
        ExpandedName selidName = state.getSelidName();
        String selidNameAttributeURI = diSelectElement.getSelidNameAttributeURI();
        if (selidNameAttributeURI != null) {
            index = attributes.getIndex(selidNameAttributeURI, "selidname");
            if (index != -1) {
                String value = attributes.getValue(index);
                if (forwardEvent) {
                    if (output == null) {
                        output = new AttributesImpl(attributes);
                        attributes = output;
                    }
                    output.removeAttribute(index);
                }
                QName qname = new ImmutableQName(value);
                selidName = pipelineContext.getNamespacePrefixTracker().resolveQName(qname, null);
                state.setSelidName(selidName);
            }
        }
        if (diSelectElement.canHaveSelidAttribute()) {
            index = attributes.getIndex(Constants.DISELECT_NAMESPACE, "selid");
            if (index != -1) {
                String idURI = selidName.getNamespaceURI();
                String idLocalName = selidName.getLocalName();
                String idQName;
                if (idURI.equals("")) {
                    idQName = idLocalName;
                } else {
                    String prefix = null;
                    String[] prefixes = pipelineContext.getNamespacePrefixTracker().getNamespacePrefixes(idURI);
                    for (String p : prefixes) {
                        if (!p.equals("")) {
                            prefix = p;
                            break;
                        }
                    }
                    if (prefix == null) {
                        throw new IllegalStateException("Could not find a non empty prefix declared for namespace: " + idURI);
                    }
                    idQName = prefix + ":" + idLocalName;
                }
                if (output == null) {
                    output = new AttributesImpl(attributes);
                    attributes = output;
                }
                output.setAttribute(index, idURI, idLocalName, idQName, "xs:ID", attributes.getValue(index));
            }
        }
        if (forwardEvent) {
            super.startElement(namespaceURI, localName, qName, attributes);
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        Element diSelectElement;
        if (inPassThroughMode || (diSelectElement = getDISelectElement(namespaceURI, localName)) == null) {
            super.endElement(namespaceURI, localName, qName);
            return;
        }
        State state = popState();
        if (state.processedBody() && diSelectElement.getForwardEvent()) {
            super.endElement(namespaceURI, localName, qName);
        }
    }

    /**
     * Get the DISelect element enumeration instance for the specified element.
     *
     * @param namespaceURI The namespace of the element.
     * @param localName    The local name of the element.
     * @return The DISelect element enumeration instance, or null if this is
     *         not a conditional element.
     */
    private Element getDISelectElement(String namespaceURI, String localName) {
        if (namespaceURI.equals(Constants.DISELECT_NAMESPACE)) {
            if (localName.equals("if")) {
                return Element.IF;
            }
            if (localName.equals("select")) {
                return Element.SELECT;
            }
            if (localName.equals("when")) {
                return Element.WHEN;
            }
            if (localName.equals("otherwise")) {
                return Element.OTHERWISE;
            }
            return null;
        } else {
            return Element.OTHER;
        }
    }

    /**
     * Take a look at the last state pushed onto the stack.
     *
     * @return The last state pushed onto the stack, or null if the stack is
     *         empty.
     */
    private State peekState() {
        return states.empty() ? null : states.peek();
    }

    /**
     * Remove the last state pushed onto the stack.
     *
     * @return The last state pushed onto the stack.
     */
    private State popState() {
        return states.pop();
    }
}
