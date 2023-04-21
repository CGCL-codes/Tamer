package com.volantis.xml.pipeline.sax.drivers.web;

/**
 * This interface is for use in a command pattern that allows entities to
 * be added to both Get and Post methods in a generic fashion.
 */
public interface DerivableHTTPMessageEntityAdder {

    public void addDerivableHTTPMessageEntity(DerivableHTTPMessageEntity requestProperty) throws HTTPException;
}
