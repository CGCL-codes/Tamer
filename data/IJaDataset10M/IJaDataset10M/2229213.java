package net.sourceforge.poi.cocoon.serialization.elementprocessor;

/**
 * Trivial extension of POIFSSerializer
 *
 * @author Marc Johnson (marc_johnson27591@hotmail.com)
 */
class EPS extends ElementProcessorSerializer {

    private ElementProcessorFactory _factory;

    /**
     * Constructor
     */
    EPS() {
        super();
        _factory = new EPF();
    }

    /**
     * get the mime type
     *
     * @return an empty string
     */
    public String getMimeType() {
        return "";
    }

    /**
     * end document
     */
    public void endDocument() {
    }

    /**
     * get the ElementProcessorFactory
     *
     * @return the ElementProcessorFactory
     */
    protected ElementProcessorFactory getElementProcessorFactory() {
        return _factory;
    }

    /**
     * pre-initialize the ElementProcessor
     *
     * @param processor
     */
    protected void doPreInitialization(ElementProcessor processor) {
    }
}
