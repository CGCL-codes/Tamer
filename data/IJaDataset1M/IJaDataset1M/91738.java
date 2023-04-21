package org.quantumleaphealth.transform;

import java.io.InputStream;
import javax.xml.stream.XMLStreamException;

/**
 * Parses data from an XML input stream.
 * @author Tom Bechtold
 * @version 2008-08-15
 */
public interface XmlDataParser {

    /**
     * Parses data from an XML input stream
     * @param inputStream the input stream
     * @throws XMLStreamException if the XML stream cannot be read
     */
    void parse(InputStream inputStream) throws XMLStreamException;
}
