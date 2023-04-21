package net.community.chest.apache.log4j.helpers;

import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * <P>Copyright 2007 as per GPLv2</P>
 *
 * <P>Assumes logger name is a fully qualified class-path name, and
 * simply peels off its name retaining only the package name</P>
 * 
 * @author Lyor G.
 * @since Sep 26, 2007 12:25:09 PM
 */
public class PackageNameConverter extends PatternConverter {

    public PackageNameConverter() {
        super();
    }

    @Override
    public String convert(LoggingEvent event) {
        final String nm = (null == event) ? null : event.getLoggerName();
        final int nmLen = (null == nm) ? 0 : nm.length();
        if (nmLen <= 0) return (null == nm) ? "" : nm;
        final int lastPos = nm.lastIndexOf('.');
        if ((lastPos <= 0) || (lastPos >= (nmLen - 1))) return nm;
        return nm.substring(0, lastPos);
    }
}
