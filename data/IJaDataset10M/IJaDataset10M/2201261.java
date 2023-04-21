package com.hp.hpl.jena.rdf.arp.impl;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import com.hp.hpl.jena.rdf.model.RDFErrorHandler;

/**
 * This class is not part of the API.
 * It is public merely for test purposes.
 * @author Jeremy Carroll
 *
 * 
 */
public class ARPSaxErrorHandler extends Object implements org.xml.sax.ErrorHandler {

    protected RDFErrorHandler errorHandler;

    public ARPSaxErrorHandler(RDFErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void error(SAXParseException e) throws SAXException {
        errorHandler.error(e);
    }

    public void warning(SAXParseException e) throws SAXException {
        errorHandler.warning(e);
    }

    public void fatalError(SAXParseException e) throws SAXException {
        errorHandler.fatalError(e);
    }

    /**
	 * @param errorHandler The errorHandler to set.
	 */
    public void setErrorHandler(RDFErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
}
