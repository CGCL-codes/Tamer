package com.google.gwt.text.client;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.shared.TimeZone;
import com.google.gwt.text.shared.AbstractRenderer;
import java.util.Date;

/**
 * Renders {@link Date} objects with a {@link DateTimeFormat}.
 */
public class DateTimeFormatRenderer extends AbstractRenderer<Date> {

    private final DateTimeFormat format;

    private final TimeZone timeZone;

    /**
   * Create an instance using {@link PredefinedFormat#DATE_SHORT}.
   */
    public DateTimeFormatRenderer() {
        this(DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT));
    }

    /**
   * Create an instance with the given format and the default time zone.
   */
    public DateTimeFormatRenderer(DateTimeFormat format) {
        this(format, null);
    }

    /**
   * Create an instance with the given format and time zone.
   */
    public DateTimeFormatRenderer(DateTimeFormat format, TimeZone timeZone) {
        assert format != null;
        this.format = format;
        this.timeZone = timeZone;
    }

    public String render(Date object) {
        if (object == null) {
            return "";
        }
        return format.format(object, timeZone);
    }
}
