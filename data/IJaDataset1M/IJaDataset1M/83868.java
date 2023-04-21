package com.rhythm.commons.objecttemplates;

import com.rhythm.commons.datetime.CalendarDate;

/**
 *Object template for object that have a <code>CalendarDate</code> and
 * a value.
 * 
 * 
 * 
 * @param <T> 
 * @author Michael J. Lee @ Synergy Energy Holdings, LLC
 */
public interface DatedObject<T> {

    public CalendarDate getDate();

    public void setDate(CalendarDate date);

    public T getValue();

    public void setValue(T value);
}
