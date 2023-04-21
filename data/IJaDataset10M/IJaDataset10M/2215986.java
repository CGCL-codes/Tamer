package com.peusoft.ical.service;

import java.util.Calendar;
import java.util.Collection;

/**
 * Implement methods to access an iCalendar resourece.
 * 
 * @author Yauheni Prykhodzka
 *
 */
public interface ICalendarService {

    /**
     * Checks if the specified date is a holiday.
     *
     * @param date date
     * @param filter filter, for example geografical location like US, UK, Byearn,
     *            Sachsen
     * @return true if the date is a holiday
     * @throws ICalendarServiceException
     */
    public boolean isHoliday(Calendar date, Collection<String> filter) throws ICalendarServiceException;

    /**
     * Retruns the name of the holiday.
     *
     * @param date date
     * @param filter filter, for example geografical location like US, UK, Byearn,
     *            Sachsen
     * @return the name of the holiday if the dat is a holiday otherwise an
     *         empty string
     * @throws ICalendarServiceException
     */
    public String getHolidayName(Calendar date, Collection<String> filter) throws ICalendarServiceException;

    /**
     * Retruns the work's days count into the month.
     *
     * @param date date
     * @param filter filter, for example geografical location like US, UK, Byearn,
     *            Sachsen
     * @return the work's days count into the month
     *
     * @throws ICalendarServiceException
     */
    public int getNumberOfWorkDays4Month(Calendar date, Collection<String> filter) throws ICalendarServiceException;

    /**
     * Retruns the work's days count into the month.
     *
     * @param date date
     * @param filter filter, for example geografical location like US, UK, Byearn,
     *            Sachsen
     * @return the work's days count into the year
     *
     * @throws ICalendarServiceException
     */
    public int getNumberOfWorkDays4Year(Calendar date, Collection<String> filter) throws ICalendarServiceException;

    /**
     * Retruns the work's days count into the month.
     *
     * @param date date
     * @param filter filter, for example geografical location like US, UK, Byearn,
     *            Sachsen
     * @return the work's days count into the week
     *
     * @throws ICalendarServiceException
     */
    public int getNumberOfWorkDays4Week(Calendar date, Collection<String> filter) throws ICalendarServiceException;
}
