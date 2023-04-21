package de.huxhorn.lilith.engine.json.eventproducer;

import de.huxhorn.lilith.data.eventsource.EventWrapper;
import de.huxhorn.lilith.data.eventsource.SourceIdentifier;
import de.huxhorn.lilith.data.logging.LoggingEvent;
import de.huxhorn.lilith.data.logging.json.LoggingJsonDecoder;
import de.huxhorn.lilith.engine.impl.eventproducer.AbstractMessageBasedEventProducer;
import de.huxhorn.sulky.buffers.AppendOperation;
import de.huxhorn.sulky.codec.Decoder;
import java.io.InputStream;

public class LilithJsonMessageLoggingEventProducer extends AbstractMessageBasedEventProducer<LoggingEvent> {

    public LilithJsonMessageLoggingEventProducer(SourceIdentifier sourceIdentifier, AppendOperation<EventWrapper<LoggingEvent>> eventQueue, InputStream inputStream, boolean compressing) {
        super(sourceIdentifier, eventQueue, inputStream, compressing);
    }

    protected Decoder<LoggingEvent> createDecoder() {
        return new LoggingJsonDecoder(isCompressing());
    }
}
