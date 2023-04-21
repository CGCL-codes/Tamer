package org.apache.ws.jaxme.impl;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import dfdl.exception.DFDLException;

/** Interface of an object, which is able to convert another
 * object into SAX events.
 */
public interface JMSAXDriver {

    /** Returns the objects attributes.
	 */
    public AttributesImpl getAttributes(DriverController pController, Object pObject) throws SAXException, DFDLException;

    /** Creates the objects content into SAX events, which
	 * are being fired into the given content handler.
	 */
    public void marshalChilds(DriverController pController, ContentHandler pHandler, Object pObject) throws SAXException, DFDLException;

    /** Returns a suggested prefix for the namespace
	 * <code>pNamespaceURI</code>, or null, if no suggestion
	 * is available.
	 */
    public String getPreferredPrefix(String pNamespaceURI);
}
