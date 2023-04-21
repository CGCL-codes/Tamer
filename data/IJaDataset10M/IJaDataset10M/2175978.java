package de.frostcode.visualmon.log.logback;

import javax.annotation.concurrent.ThreadSafe;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.jamonapi.MonKeyImp;

@ThreadSafe
final class LogbackMonKey extends MonKeyImp {

    private final transient LoggingEvent event;

    public LogbackMonKey(final String label, final String details, final String units, final LoggingEvent event) {
        super(label, details, units);
        this.event = event;
    }

    public LoggingEvent getEvent() {
        return event;
    }
}
