package net.sf.moviekebab.web;

import org.antlr.stringtemplate.AttributeRenderer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

class InstantRenderer implements AttributeRenderer {

    private static final Log LOG = LogFactory.getLog(InstantRenderer.class);

    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm");

    public String toString(Object object) {
        final Instant instant = (Instant) object;
        final String instantAsString = formatter.print(instant);
        LOG.debug("Printed instant " + instant + " as '" + instantAsString + "'");
        return instantAsString;
    }
}
