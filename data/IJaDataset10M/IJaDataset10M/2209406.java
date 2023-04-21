package com.volantis.xml.pipeline.sax.impl.dynamic;

import com.volantis.xml.pipeline.sax.XMLProcessImpl;
import com.volantis.xml.pipeline.sax.flow.FlowController;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

/**
 * An <code>XMLProcess</code> that implements the <code>FlowController</code>
 * interface so that the flow of SAX events through this process can be
 * controlled
 */
public class FlowControlProcess extends XMLProcessImpl implements FlowController {

    /**
     * The flow control manager that is managing this FlowController
     */
    private FlowControlManager flowControlManager;

    /**
     * Flag that indicates wether this process is performing flow control
     */
    private boolean inFlowControlMode;

    /**
     * Creates a new <code>FlowControlProcess</code> instance
     */
    public FlowControlProcess() {
        this.flowControlManager = null;
        this.inFlowControlMode = false;
    }

    public void startProcess() throws SAXException {
        flowControlManager = getPipelineContext().getFlowControlManager();
        flowControlManager.addFlowController(this);
    }

    public void stopProcess() throws SAXException {
        flowControlManager.removeFlowController(this);
    }

    public void release() {
        super.release();
        this.inFlowControlMode = false;
    }

    public void beginFlowControl() {
        this.inFlowControlMode = true;
    }

    public void endFlowControl() {
        this.inFlowControlMode = false;
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        if (!inFlowControlMode) {
            super.characters(ch, start, length);
        }
    }

    public void endDocument() throws SAXException {
        if (!inFlowControlMode) {
            super.endDocument();
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (!inFlowControlMode || flowControlManager.handleEndElementEvent()) {
            super.endElement(namespaceURI, localName, qName);
        }
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        if (!inFlowControlMode || flowControlManager.forwardEndPrefixMappingEvent()) {
            super.endPrefixMapping(prefix);
        }
    }

    public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
        if (!inFlowControlMode) {
            super.ignorableWhitespace(ch, start, length);
        }
    }

    public void processingInstruction(String target, String data) throws SAXException {
        if (!inFlowControlMode) {
            super.processingInstruction(target, data);
        }
    }

    public void setDocumentLocator(Locator locator) {
        if (!inFlowControlMode) {
            super.setDocumentLocator(locator);
        }
    }

    public void skippedEntity(String name) throws SAXException {
        if (!inFlowControlMode) {
            super.skippedEntity(name);
        }
    }

    public void startDocument() throws SAXException {
        if (!inFlowControlMode) {
            super.startDocument();
        }
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (!inFlowControlMode) {
            super.startElement(namespaceURI, localName, qName, atts);
        } else {
            flowControlManager.handleStartElementEvent();
        }
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (!inFlowControlMode) {
            super.startPrefixMapping(prefix, uri);
        }
    }
}
