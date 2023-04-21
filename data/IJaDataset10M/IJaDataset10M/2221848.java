package com.windsor.node.common.domain;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import org.apache.commons.lang.enums.ValuedEnum;

public final class DayOfWeekType extends ValuedEnum {

    public static final DayOfWeekType MONDAY = new DayOfWeekType("Monday", Calendar.MONDAY);

    public static final DayOfWeekType TUESDAY = new DayOfWeekType("Tuesday", Calendar.TUESDAY);

    public static final DayOfWeekType WEDNESDAY = new DayOfWeekType("Wednesday", Calendar.WEDNESDAY);

    public static final DayOfWeekType THURSDAY = new DayOfWeekType("Thursday", Calendar.THURSDAY);

    private static final long serialVersionUID = 1;

    private DayOfWeekType(String type, int i) {
        super(type, i);
    }

    public static Map getEnumMap() {
        return getEnumMap(DayOfWeekType.class);
    }

    public static List getEnumList() {
        return getEnumList(DayOfWeekType.class);
    }

    public static Iterator iterator() {
        return iterator(DayOfWeekType.class);
    }
}
