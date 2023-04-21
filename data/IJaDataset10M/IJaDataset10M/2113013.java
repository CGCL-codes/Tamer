package org.jfree.xml.writer;

/**
 * A base class for implementing a handler that writes the XML for an object of a particular
 * class.
 */
public abstract class AbstractXmlWriteHandler implements XmlWriteHandler {

    /** The root handler. */
    private RootXmlWriteHandler rootHandler;

    /**
     * Creates a new handler.
     */
    public AbstractXmlWriteHandler() {
        super();
    }

    /**
     * Returns the root handler.
     * 
     * @return the root handler.
     */
    public RootXmlWriteHandler getRootHandler() {
        return this.rootHandler;
    }

    /**
     * Sets the root handler.
     * 
     * @param rootHandler  the root handler.
     */
    public void setRootHandler(final RootXmlWriteHandler rootHandler) {
        this.rootHandler = rootHandler;
    }
}
