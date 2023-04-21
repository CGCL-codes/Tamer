package com.teknokala.xtempore.xml;

import java.util.List;

/**
 * Basic output stream.
 *
 * @author Timo Santasalo <timo.santasalo@teknokala.com>
 * @see BufferedXMLOutput
 */
public interface XMLOutput extends XMLStream {

    /**
	 * Writes an event to the stream.
	 * @param ev An avent.
	 * @throws XMLStreamException If event cannot be written.
	 */
    public void write(XMLEvent ev) throws XMLStreamException;

    public void write(List<XMLEvent> events) throws XMLStreamException;

    public void write(XMLEvent[] events) throws XMLStreamException;
}
