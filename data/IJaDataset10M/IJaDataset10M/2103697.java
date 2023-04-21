package de.huxhorn.lilith.engine.xml.sourceproducer;

import de.huxhorn.lilith.data.eventsource.EventWrapper;
import de.huxhorn.lilith.data.eventsource.SourceIdentifier;
import de.huxhorn.lilith.data.logging.LoggingEvent;
import de.huxhorn.lilith.engine.EventProducer;
import de.huxhorn.lilith.engine.impl.sourceproducer.AbstractServerSocketEventSourceProducer;
import de.huxhorn.lilith.engine.xml.eventproducer.LilithXmlStreamLoggingEventProducer;
import de.huxhorn.sulky.buffers.AppendOperation;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLStreamException;

public class LilithXmlStreamLoggingServerSocketEventSourceProducer extends AbstractServerSocketEventSourceProducer<LoggingEvent> {

    public LilithXmlStreamLoggingServerSocketEventSourceProducer(int port) throws IOException {
        super(port);
    }

    protected EventProducer<LoggingEvent> createProducer(SourceIdentifier id, AppendOperation<EventWrapper<LoggingEvent>> eventQueue, InputStream inputStream) throws IOException {
        try {
            return new LilithXmlStreamLoggingEventProducer(id, eventQueue, inputStream);
        } catch (XMLStreamException e) {
            throw new IOException("Couldn't create producer for '" + id + "'!");
        }
    }

    @Override
    public String toString() {
        return "LilithXmlStreamLoggingServerSocketEventSourceProducer[port=" + getPort() + "]";
    }
}
