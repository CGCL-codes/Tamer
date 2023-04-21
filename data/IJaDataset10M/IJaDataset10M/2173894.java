package net.sourceforge.stat4j.log4j;

import org.apache.log4j.spi.LoggingEvent;

/**
 * @author Lara D'Abreo
 */
public interface RSSEntryMapper {

    /**
     * Map a log message to an RSSEntry
     * 
     * @param event a log message event
     * @return an RSSEntry (entry or item) element
     */
    public RSSEntry toEntry(LoggingEvent event, String message);
}
