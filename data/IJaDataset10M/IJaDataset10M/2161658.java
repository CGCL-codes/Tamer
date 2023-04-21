package net.sf.sanity4j.util;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

/**
 * StartElementListener - listener interface called by {@link JaxbMarshaller} 
 * when iterating through an XML document. This allows readers to unmarshall
 * sections of the large documents that would otherwise consume excess amounts
 * of memory.
 * 
 * @author Yiannis Paschalidis
 * @since Sanity4J 1.0
 */
public interface StartElementListener {

    /**
     * Called whenever a start element is encountered during traversal of an XML document.
     * 
     * @param element the start element which was found
     * @param reader the XML event reader that is in use
     * @param unmarshaller the current JAXB unmarshaller in use.
     * @throws JAXBException if there is an error during unmarshalling.
     */
    void foundElement(StartElement element, XMLEventReader reader, Unmarshaller unmarshaller) throws JAXBException;
}
