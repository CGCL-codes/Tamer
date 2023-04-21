package com.google.checkout.schema._2.impl.runtime;

import org.xml.sax.SAXException;
import com.sun.xml.bind.JAXBObject;

/**
 * For a generated class to be serializable, it has to
 * implement this interface.
 * 
 * @author Kohsuke Kawaguchi
 */
public interface XMLSerializable extends JAXBObject {

    /**
     * Serializes child elements and texts into the specified target.
     */
    void serializeBody(XMLSerializer target) throws SAXException;

    /**
     * Serializes attributes into the specified target.
     */
    void serializeAttributes(XMLSerializer target) throws SAXException;

    /**
     * Declares all the namespace URIs this object is using at
     * its top-level scope into the specified target.
     */
    void serializeURIs(XMLSerializer target) throws SAXException;
}
