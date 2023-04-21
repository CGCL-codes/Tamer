package net.sf.myway.gps.garmin.parser;

import java.util.Date;
import net.sf.myway.gps.garmin.unit.GarminData;

public class DateField extends GarminField {

    public DateField(final int start) {
        super(start);
    }

    public Date get(final GarminData data) {
        return data.getDate(getStart());
    }
}
