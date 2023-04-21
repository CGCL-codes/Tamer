package org.nomadpim.module.schedule.event;

import java.text.DateFormat;
import java.util.Date;
import org.nomadpim.core.entity.EntityPropertyFormatter;
import org.nomadpim.core.entity.IEntity;
import org.nomadpim.core.util.text.DateStringConverter;
import org.nomadpim.core.util.text.IFormatter;
import org.nomadpim.core.util.text.NullStringConverter;

public class EventFormatter implements IFormatter<IEntity> {

    private final EntityPropertyFormatter<Date> dateFormatter;

    private final EntityPropertyFormatter<String> descriptionFormatter;

    public EventFormatter() {
        this.dateFormatter = new EntityPropertyFormatter<Date>(new DateStringConverter(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)), Event.DATE);
        this.descriptionFormatter = new EntityPropertyFormatter<String>(new NullStringConverter(), Event.DESCRIPTION);
    }

    public String format(IEntity t) {
        return dateFormatter.format(t) + " " + descriptionFormatter.format(t);
    }
}
