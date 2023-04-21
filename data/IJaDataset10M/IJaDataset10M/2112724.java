package com.medcentrex.interfaces;

import java.sql.Time;
import java.lang.String;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;
import java.util.Calendar;

public class DayCalendarBean {

    private Collection appointments;

    private Collection rules;

    private int firstHour = -1;

    private int lastHour = -1;

    public int getFirstHour() {
        return firstHour;
    }

    public int getLastHour() {
        return lastHour;
    }

    public DayCalendarBean() {
    }

    public Collection getAppointments() {
        if (appointments == null) appointments = (Collection) new Vector();
        return appointments;
    }

    public Collection getRules() {
        if (rules == null) rules = (Collection) new Vector();
        return rules;
    }

    public void setAppointments(Collection Appointments) {
        appointments = Appointments;
        System.out.println("DayCalendarBean#setAppointments size: " + appointments.size());
        java.util.Calendar c = java.util.GregorianCalendar.getInstance();
        Iterator iterator = appointments.iterator();
        while (iterator.hasNext()) {
            DayAppointmentData appt = (DayAppointmentData) iterator.next();
            try {
                c.setTimeInMillis(appt.getScheduleBeginTime().getTime());
                if (c.get(GregorianCalendar.HOUR_OF_DAY) < firstHour || firstHour == -1) firstHour = c.get(GregorianCalendar.HOUR_OF_DAY);
                c.setTimeInMillis(appt.getScheduleEndTime().getTime());
                if (c.get(GregorianCalendar.HOUR_OF_DAY) > lastHour || lastHour == -1) lastHour = c.get(GregorianCalendar.HOUR_OF_DAY);
            } catch (Exception e) {
            }
        }
    }

    public void setRules(Collection Rules) {
        rules = Rules;
        java.util.Calendar c = java.util.Calendar.getInstance();
        Iterator iterator = rules.iterator();
        while (iterator.hasNext()) {
            Calendar_RuleData rule = (Calendar_RuleData) iterator.next();
            try {
                c.setTimeInMillis(rule.getStartTime().getTime());
                if (c.get(Calendar.HOUR_OF_DAY) < firstHour || firstHour == -1) firstHour = c.get(Calendar.HOUR_OF_DAY);
                c.setTimeInMillis(rule.getEndTime().getTime());
                if (c.get(Calendar.HOUR_OF_DAY) > lastHour || lastHour == -1) lastHour = c.get(Calendar.HOUR_OF_DAY);
            } catch (Exception e) {
            }
        }
    }

    /**
	 * Iterates the appointments collection looking for matches to the criteria specified
	 *
	 * @param date  java.sql.Date object for date to search on. this value is ignored.
	 * @param start Time value specifying lower boundary of times to search on.
	 * @param end   Time value specifying high boundary of times to search on
	 * @param physician this value is ignored.
	 * @param location_id   this value is ignored
	 * @return Collection object filled w/ the correct DayAppointmentData objects according to arguments.
	 */
    public Collection getAppointments(java.sql.Date date, Time start, Time end, String physician, String location_id) {
        appointments = getAppointments();
        System.out.println("DayCalendarBean#getAppointments(params) appts size: " + appointments.size());
        Collection appts = (Collection) new Vector();
        Iterator iterator = appointments.iterator();
        while (iterator.hasNext()) {
            DayAppointmentData appt = (DayAppointmentData) iterator.next();
            System.out.println("DayCalendarBean#getAppointments(params) " + appt.getScheduleBeginTime().getTime() + ">=" + start.getTime() + " && " + appt.getScheduleBeginTime().before(end));
            if ((appt.getScheduleBeginTime().getTime() >= start.getTime() && appt.getScheduleBeginTime().before(end))) {
                appts.add(appt);
            }
        }
        return appts;
    }

    /**
	 * Iterates the rules collection looking for matches to the criteria specified
	 *
	 * @param date  Calendar on which day to search. this value is ignored.
	 * @param start Time value specifying lower boundary of times to search on.
	 * @param end   Time value specifying high boundary of times to search on
	 * @param physician this value is ignored.
	 * @param location_id   this value is ignored
	 * @return Collection object filled w/ the correct Appt_TypeEntityData objects according to arguments.
	 */
    public Collection getRules(Calendar date, Time start, Time end, String physician, String location_id) {
        Collection calrules = getRules();
        Collection appts = (Collection) new Vector();
        Iterator iterator = calrules.iterator();
        while (iterator.hasNext()) {
            Calendar_RuleData rule = (Calendar_RuleData) iterator.next();
            try {
                if (rule.getEndTime().getTime() > start.getTime() && rule.getStartTime().before(end)) {
                    Appt_TypeEntityData appt = new Appt_TypeEntityData();
                    appt.setAppt_Type_ID(rule.getAppt_Type_ID());
                    appt.setAppt_Type(rule.getAppt_Type());
                    if (!appts.contains(appt)) appts.add(appt);
                }
            } catch (Exception e) {
            }
        }
        return appts;
    }
}
