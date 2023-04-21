package org.apache.batik.util;

/**
 * Very simple abstract base class for ParsedURLProtocolHandlers.
 * Just handles the 'what protocol part'.
 */
public abstract class AbstractParsedURLProtocolHandler implements ParsedURLProtocolHandler {

    protected String protocol;

    /**
     * Constrcut a ProtocolHandler for <tt>protocol</tt>
     */
    public AbstractParsedURLProtocolHandler(String protocol) {
        this.protocol = protocol;
    }

    /**
     * Returns the protocol to be handled by this class.
     * The protocol must _always_ be the part of the URL before the
     * first ':'.
     */
    public String getProtocolHandled() {
        return protocol;
    }
}
