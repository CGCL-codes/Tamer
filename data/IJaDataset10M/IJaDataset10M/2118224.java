package nu.staldal.lagoon.core;

import org.xml.sax.ContentHandler;

/**
 * Produces an XML stream.
 */
public interface XMLStreamProducer extends ProducerInterface {

    /**
     * Start the generation of the XML stream.
     * Synchrounos, does not return until the whole stream is finished.
     * Must invoke startDocument() and endDocument() on the stream.
     *
     * @param sax  the SAX ContentHandler to write to
     * @param target  the target
     */
    public void start(ContentHandler sax, Target target) throws org.xml.sax.SAXException, java.io.IOException;
}
