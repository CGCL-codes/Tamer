package org.apache.log4j.pattern;

import org.apache.log4j.spi.LoggingEvent;

/**
 * Formats the event sequence number.
 *
 * @author Ceki G&uuml;lc&uuml;
 */
public class SequenceNumberPatternConverter extends LoggingEventPatternConverter {

    /**
   * Singleton.
   */
    private static final SequenceNumberPatternConverter INSTANCE = new SequenceNumberPatternConverter();

    /**
   * Private constructor.
   */
    private SequenceNumberPatternConverter() {
        super("Sequence Number", "sn");
    }

    /**
   * Obtains an instance of SequencePatternConverter.
   * @param options options, currently ignored, may be null.
   * @return instance of SequencePatternConverter.
   */
    public static SequenceNumberPatternConverter newInstance(final String[] options) {
        return INSTANCE;
    }

    /**
   * {@inheritDoc}
   */
    public void format(final LoggingEvent event, final StringBuffer toAppendTo) {
        toAppendTo.append("0");
    }
}
