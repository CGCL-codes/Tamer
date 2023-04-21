package org.nomadpim.module.timetracking.worklog;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.nomadpim.core.entity.io.AbstractPropertiesConfigurator;
import org.nomadpim.core.util.factory.CurrentDateFactory;
import org.nomadpim.core.util.properties.Property;
import org.nomadpim.core.util.text.DateStringConverter;
import org.nomadpim.core.util.text.LineSeparatorConverter;

public final class WorkLog extends AbstractPropertiesConfigurator {

    public static final String TYPE_NAME = "workLog";

    public static final Property<DateTime> START_DATE = new Property<DateTime>("startDate", DateTime.class, new CurrentDateFactory());

    public static final Property<DateTime> END_DATE = new Property<DateTime>("endDate", DateTime.class, new CurrentDateFactory());

    public static final Property<String> DESCRIPTION = new Property<String>("description", String.class, "");

    public static final Property<String> NOTE = new Property<String>("note", String.class, "");

    public WorkLog() {
        putConverter(WorkLog.START_DATE, new DateStringConverter(ISODateTimeFormat.dateTimeNoMillis()));
        putConverter(WorkLog.END_DATE, new DateStringConverter(ISODateTimeFormat.dateTimeNoMillis()));
        putConverter(WorkLog.DESCRIPTION, new LineSeparatorConverter());
        putConverter(WorkLog.NOTE, new LineSeparatorConverter());
    }
}
