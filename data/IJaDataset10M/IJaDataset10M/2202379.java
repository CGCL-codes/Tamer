package com.jettmarks.bkthn.forms;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import com.jettmarks.bkthn.domain.LogEntry;
import com.jettmarks.bkthn.domain.Route;

public class LogEntryForm extends org.apache.struts.action.ActionForm {

    /**
   * Logger for this class
   */
    private static final Logger logger = Logger.getLogger(LogEntryForm.class);

    /** *  */
    private static final long serialVersionUID = 7473270759099724099L;

    private Integer logEntryId = 0;

    /** Instance of the domain class. */
    private LogEntry logEntry = null;

    private String day = null;

    private String month = null;

    private String year = null;

    private String routeName = null;

    private float distance;

    public LogEntryForm() {
    }

    @Override
    public void reset(ActionMapping actionMapping, HttpServletRequest request) {
        logEntryId = 0;
    }

    @Override
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
        ActionErrors actionErrors = new ActionErrors();
        return actionErrors;
    }

    /**
   * @return the logEntryId
   */
    public Integer getLogEntryId() {
        return logEntryId;
    }

    /**
   * Loads the LogEntry identified by the pass parameter.
   * 
   * @param logEntryId
   *          the logEntryId to set
   */
    public void setLogEntryId(Integer logEntryId) {
        this.logEntryId = logEntryId;
    }

    /**
   * @return the logEntry
   */
    public LogEntry getLogEntry() {
        return logEntry;
    }

    /**
   * @param logEntry2
   */
    public void setLogEntry(LogEntry logEntry) {
        this.logEntry = logEntry;
        Route route = logEntry.getRoute();
        if (route != null) {
            routeName = route.getName();
        }
        Date rideDate = logEntry.getRideDate();
        setRideDate(rideDate);
        distance = logEntry.getDistance();
        logger.debug(this);
    }

    /**
   * @return the day
   */
    public String getDay() {
        return day;
    }

    /**
   * @param day the day to set
   */
    public void setDay(String day) {
        this.day = day;
    }

    /**
   * @return the month
   */
    public String getMonth() {
        return month;
    }

    /**
   * @param month the month to set
   */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
   * @return the year
   */
    public String getYear() {
        return year;
    }

    /**
   * @param year the year to set
   */
    public void setYear(String year) {
        this.year = year;
    }

    /**
   * @return the route
   */
    public String getRouteName() {
        return routeName;
    }

    /**
   * @param route the route to set
   */
    public void setRouteName(String route) {
        this.routeName = route;
    }

    /**
   * @return the distance
   */
    public float getDistance() {
        return distance;
    }

    /**
   * @param distance the distance to set
   */
    public void setDistance(float distance) {
        this.distance = distance;
    }

    /**
   * Constructs a <code>String</code> with all attributes
   * in name = value format.
   *
   * @return a <code>String</code> representation 
   * of this object.
   */
    public String toString() {
        final String TAB = "\n  ";
        StringBuffer retValue = new StringBuffer();
        retValue.append("LogEntryForm ( ").append(super.toString()).append(TAB).append("logEntryId = ").append(this.logEntryId).append(TAB).append("logEntry = ").append(this.logEntry).append(TAB).append("day = ").append(this.day).append(TAB).append("month = ").append(this.month).append(TAB).append("year = ").append(this.year).append(TAB).append("routeName = ").append(this.routeName).append(TAB).append("distance = ").append(this.distance).append(TAB).append(" )");
        return retValue.toString();
    }

    /**
   * @return
   */
    public Date getRideDate() {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
        return cal.getTime();
    }

    /**
   * Used to initialize a new Ride Date (usually today).
   * 
   * @param date
   */
    public void setRideDate(Date rideDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(rideDate);
        day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        month = Integer.toString(cal.get(Calendar.MONTH) + 1);
        year = Integer.toString(cal.get(Calendar.YEAR));
    }
}
