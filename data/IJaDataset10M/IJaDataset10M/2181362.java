package com.volantis.xml.pipeline.sax.impl;

import org.xml.sax.SAXException;
import org.xml.sax.Locator;
import com.volantis.xml.pipeline.sax.XMLPipelineProcessImpl;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineProcess;
import com.volantis.xml.pipeline.sax.XMLProcess;

/**
 * An extension of XMLPipelineProcessImpl which processes the
 * {@link org.xml.sax.ContentHandler} methods:
 * <ul>
 * <li> {@link org.xml.sax.ContentHandler#startDocument}
 * <li> {@link org.xml.sax.ContentHandler#endDocument}
 * <li> {@link org.xml.sax.ContentHandler#setDocumentLocator}
 * <li> {@link org.xml.sax.ContentHandler#startPrefixMapping}
 * <li> {@link org.xml.sax.ContentHandler#endPrefixMapping}
 * </ul>
 *
 * All {@link org.xml.sax.ContentHandler} methods are processed such that the
 * events are forwarded to the consumer process of this pipeline.  It is
 * intended that the head process (and therefore the consumer process) will be
 * an instance of
 * {@link com.volantis.xml.pipeline.sax.impl.dynamic.ContextManagerProcess}
 *
 * The ContextManagerProcess will update the {@link com.volantis.xml.pipeline.sax.XMLPipelineContext} as
 * document in the javadoc for that class.
 *
 * It is further intended that this XMLPipelineProcess should have a
 * {@link com.volantis.xml.pipeline.sax.impl.dynamic.ContextAnnotatingProcess} as
 * the tail process.  This will restore the contextual information as
 * SAXEvents.  This behaviour is as per the documentation for
 * {@link com.volantis.xml.pipeline.sax.impl.dynamic.ContextAnnotatingProcess}
 */
public class DefaultXMLPipelineProcess extends XMLPipelineProcessImpl {

    /**
     * Create a new DefaultXMLPipelineProcess instance. This default pipeline
     * will forward "nested" startDocument() and endDocument() events
     * to any process that has been set as the head/consumer process.
     * @param pipelineContext the associated XMLPipelineContext
     */
    public DefaultXMLPipelineProcess(XMLPipelineContext pipelineContext) {
        super(pipelineContext);
    }

    public void addTailXMLPipelineProcess(XMLPipelineProcess process) throws SAXException {
        if (null == process) {
            throw new NullPointerException("Cannot add a null process");
        }
        addTailProcessImpl(process);
    }

    public void startProcess() throws SAXException {
        throw new UnsupportedOperationException("As the top level " + "XMLPipelineProcess the startProcess() method should " + "never be called.");
    }

    public void stopProcess() throws SAXException {
        throw new UnsupportedOperationException("As the top level " + "XMLPipelineProcess the stopProcess() method should " + "never be called.");
    }

    public void startDocument() throws SAXException {
        XMLProcess consumer = getConsumerProcess();
        if (null != consumer) {
            consumer.startDocument();
        }
    }

    public void endDocument() throws SAXException {
        XMLProcess consumer = getConsumerProcess();
        if (null != consumer) {
            consumer.endDocument();
        }
    }

    public void setDocumentLocator(Locator locator) {
        XMLProcess consumer = getConsumerProcess();
        if (null != consumer) {
            consumer.setDocumentLocator(locator);
        }
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        XMLProcess consumer = getConsumerProcess();
        if (null != consumer) {
            consumer.startPrefixMapping(prefix, uri);
        }
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        XMLProcess consumer = getConsumerProcess();
        if (null != consumer) {
            consumer.endPrefixMapping(prefix);
        }
    }
}
