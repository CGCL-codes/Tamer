package com.volantis.xml.pipeline.sax.impl.operations.tryop;

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import com.volantis.xml.pipeline.sax.impl.validation.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This DynamicElementRule starts and end a block of recoverable markup.
 */
public abstract class AttemptRule extends DynamicElementRuleImpl {

    private final Element element;

    protected AttemptRule(Element element) {
        this.element = element;
    }

    public Object startElement(DynamicProcess dynamicProcess, ExpandedName elementName, Attributes attributes) throws SAXException {
        XMLPipelineContext context = dynamicProcess.getPipelineContext();
        TryModel model = (TryModel) context.findObject(TryProcess.class);
        model.startBlock(element);
        dynamicProcess.addErrorRecoveryPoint();
        return model;
    }

    public void endElement(DynamicProcess dynamicProcess, ExpandedName element, Object object) throws SAXException {
        XMLPipelineContext context = dynamicProcess.getPipelineContext();
        boolean inErrorRecoveryMode = context.inErrorRecoveryMode();
        TryModel model = (TryModel) object;
        model.endBlock(this.element);
        if (!inErrorRecoveryMode) {
            context.getFlowControlManager().exitCurrentElement();
        }
    }
}
