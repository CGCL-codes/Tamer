package org.biocatalogue.api;

import java.io.InputStream;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;

public class ResponseParser<N> {

    /**
	 * Given an InputStream to an element, returns an object of type <code>N</code> which
	 *  will be the root element of the returned object.
	 * @param is An {@link InputStream} to an XML element
	 * @return An object of type N
	 * @throws JAXBException if XML cannot be unmarshalled.
	 * @throws {@link ClassCastException} if the XML element root element is not of type N.
	 * 
	 */
    public N getObjectFor(InputStream is, Class<N> clazz) throws JAXBException {
        N service = JAXB.unmarshal(is, clazz);
        return service;
    }
}
