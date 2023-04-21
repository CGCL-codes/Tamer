package com.google.code.jholidays.moc;

import java.util.Calendar;
import java.util.Date;
import com.google.code.jholidays.core.EventDescriptor;
import com.google.code.jholidays.events.IEvent;

/**
 * Represents fixed event which getDate() always returns "01 Jan 2000 1:00:00"
 * 
 * @author tillias
 * 
 */
public class FixedEventMoc implements IEvent {

    public FixedEventMoc(int ID) throws IllegalArgumentException {
        descriptor = new EventDescriptor(ID);
    }

    @Override
    public Date getDate(int year) {
        Calendar c = Calendar.getInstance();
        c.set(year, Calendar.JANUARY, 1, 1, 0, 0);
        return c.getTime();
    }

    EventDescriptor descriptor;

    @Override
    public EventDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public int getID() {
        return descriptor.getID();
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
