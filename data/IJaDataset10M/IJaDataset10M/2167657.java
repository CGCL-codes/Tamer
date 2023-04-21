package org.jtools.helper.java.util;

import java.util.TimeZone;
import org.jpattern.helper.Helper;

public class TimeZoneHelper implements Helper<TimeZone> {

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public TimeZone toInstance() {
        if (id == null) return TimeZone.getDefault();
        return TimeZone.getTimeZone(id);
    }
}
