package com.volantis.xml.pipeline.sax.tryop;

import com.volantis.xml.pipeline.sax.dynamic.rules.AbstractAddProcessRule;
import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.namespace.ExpandedName;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class generates a fatal error
 */
public class GenerateErrorRule extends DynamicElementRuleImpl {

    protected XMLProcess createProcess(DynamicProcess dynamicProcess, ExpandedName elementName, Attributes attributes) throws SAXException {
        return null;
    }

    public Object startElement(DynamicProcess dynamicProcess, ExpandedName elementName, Attributes attributes) throws SAXException {
        XMLPipelineException exception = new XMLPipelineException("Deliberately generated exception.", dynamicProcess.getPipeline().getPipelineContext().getCurrentLocator());
        dynamicProcess.fatalError(exception);
        return null;
    }

    public void endElement(DynamicProcess dynamicProcess, ExpandedName element, Object object) throws SAXException {
    }
}
