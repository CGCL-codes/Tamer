package com.idna.dm.service.util.xml;

import java.io.Writer;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import org.codehaus.stax2.XMLOutputFactory2;
import com.idna.dm.service.execution.xml.response.util.StaxWriterFactory;

/**
 * Provides a Stax XML writer with a choice between XMLStreamWriter (more efficient) or XMLEventWriter (more intuitive/object-oriented).
 * <p>
 * 
 * This class abstracts away the choice of implementation for Stax plus utilises only one underlying 
 * factory for doing so in accordance with the recommended practice - 
 * <a href="http://woodstox.codehaus.org/Performance">http://woodstox.codehaus.org/Performance</a><br>
 *	<br>
 * @author matthew.cosgrove
 *
 */
public class StaxWriterFactoryImpl implements StaxWriterFactory {

    static XMLOutputFactory2 factory;

    static {
        factory = (XMLOutputFactory2) XMLOutputFactory2.newInstance();
        factory.configureForSpeed();
        factory.setProperty(XMLOutputFactory2.XSP_NAMESPACE_AWARE, Boolean.FALSE);
    }

    @Override
    public XMLEventWriter getStaxEventWriter(Writer output) {
        XMLEventWriter eventWriter;
        try {
            eventWriter = factory.createXMLEventWriter(output);
        } catch (Exception e) {
            throw new RuntimeException("Cannot create XML writer", e);
        }
        return eventWriter;
    }

    @Override
    public XMLStreamWriter getStaxStreamWriter(Writer output) {
        XMLStreamWriter writer;
        try {
            writer = factory.createXMLStreamWriter(output);
        } catch (Exception e) {
            throw new RuntimeException("Cannot create XML writer", e);
        }
        return writer;
    }
}
