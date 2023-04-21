package org.liris.schemerger.core.dataset;

import java.util.List;
import org.liris.schemerger.core.event.EDate;
import org.liris.schemerger.core.event.IEvent;

public interface ISequence<E extends IEvent> extends List<E> {

    public static final String TIME_UNIT_MILLISECOND = "millisecond";

    public static final String TIME_UNIT_1_ON_100_SECOND = "1/100 second";

    public static final String TIME_UNIT_SECOND = "second";

    public static final String TIME_UNIT_MINUTE = "minute";

    public static final String TIME_UNIT_HOUR = "hour";

    public static final String TIME_UNIT_DAY = "day";

    public abstract EDate getEnd();

    public abstract EDate getStart();

    public E getLast();

    public E getFirst();

    public long getID();

    public String getTimeUnit();
}
