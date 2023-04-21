package com.volantis.xml.pipeline.sax.impl.operations.debug;

import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLWrappingProcess;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.Locator;
import org.xml.sax.Attributes;

/**
 * This {@link XMLProcess} manages a {@link TryCatchFinallyProcess} by
 * delegating to the TryCatchFinallyProcess ensuring that if an exception
 * occurs the {@link TryCatchFinallyProcess#doCatch} and
 * {@link TryCatchFinallyProcess#doFinally} methods are invoked. 
 */
public class TryCatchFinallyManagerProcess extends XMLWrappingProcess {

    /**
     * Will delegate to in order to perform any "catch" or "finally" processing
     * that may be required in the {@link org.xml.sax.ContentHandler}
     * implemetation.
     */
    private TryCatchFinallyProcess tryCatchFinallyProcess;

    /**
     * Constructor for <code>TryCatchFinallyManagerProcess</code>
     * @param tryCatchFinallyProcess the {@link TryCatchFinallyProcess} that
     * will be delegated to in order to provide the XML processing. In addition
     * the {@link TryCatchFinallyProcess#doCatch} and
     * {@link TryCatchFinallyProcess#doFinally} implementations will be used to
     * perform any processing for caught exceptions as well as the finally
     * block processing.
     */
    public TryCatchFinallyManagerProcess(TryCatchFinallyProcess tryCatchFinallyProcess) {
        super(tryCatchFinallyProcess);
        this.tryCatchFinallyProcess = tryCatchFinallyProcess;
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        try {
            super.characters(ch, start, length);
        } catch (Throwable throwble) {
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            tryCatchFinallyProcess.doFinally();
        }
    }

    public void endDocument() throws SAXException {
        try {
            super.endDocument();
        } catch (Throwable throwble) {
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            tryCatchFinallyProcess.doFinally();
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        try {
            super.endElement(namespaceURI, localName, qName);
        } catch (Throwable throwble) {
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            tryCatchFinallyProcess.doFinally();
        }
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        try {
            super.endPrefixMapping(prefix);
        } catch (Throwable throwble) {
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            tryCatchFinallyProcess.doFinally();
        }
    }

    public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
        try {
            super.ignorableWhitespace(ch, start, length);
        } catch (Throwable throwble) {
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            tryCatchFinallyProcess.doFinally();
        }
    }

    public void processingInstruction(String target, String data) throws SAXException {
        try {
            super.processingInstruction(target, data);
        } catch (Throwable throwble) {
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            tryCatchFinallyProcess.doFinally();
        }
    }

    public void setDocumentLocator(Locator locator) {
        try {
            super.setDocumentLocator(locator);
        } catch (Throwable throwble) {
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            tryCatchFinallyProcess.doFinally();
        }
    }

    public void skippedEntity(String s) throws SAXException {
        try {
            super.skippedEntity(s);
        } catch (Throwable throwble) {
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            tryCatchFinallyProcess.doFinally();
        }
    }

    public void startDocument() throws SAXException {
        try {
            super.startDocument();
        } catch (Throwable throwble) {
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            tryCatchFinallyProcess.doFinally();
        }
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        try {
            super.startElement(namespaceURI, localName, qName, atts);
        } catch (Throwable throwble) {
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            tryCatchFinallyProcess.doFinally();
        }
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        try {
            super.startPrefixMapping(prefix, uri);
        } catch (Throwable throwble) {
            tryCatchFinallyProcess.doCatch(throwble);
        } finally {
            tryCatchFinallyProcess.doFinally();
        }
    }

    public void setNextProcess(XMLProcess next) {
        super.setNextProcess(next);
    }

    public void setPipeline(XMLPipeline pipeline) {
        super.setPipeline(pipeline);
        tryCatchFinallyProcess.setPipeline(pipeline);
    }

    public void startProcess() throws SAXException {
        super.startProcess();
        tryCatchFinallyProcess.startProcess();
    }

    public void stopProcess() throws SAXException {
        super.stopProcess();
        tryCatchFinallyProcess.stopProcess();
    }

    protected XMLProcess getConsumerProcess() {
        return super.getConsumerProcess();
    }

    public XMLProcess getNextProcess() {
        return super.getNextProcess();
    }

    public XMLPipeline getPipeline() {
        return super.getPipeline();
    }

    public XMLPipelineContext getPipelineContext() {
        return super.getPipelineContext();
    }

    public void release() {
        super.release();
    }

    public void warning(SAXParseException exception) throws SAXException {
        super.warning(exception);
    }

    public void error(SAXParseException exception) throws SAXException {
        super.error(exception);
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        super.fatalError(exception);
    }
}
