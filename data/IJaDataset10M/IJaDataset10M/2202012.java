package com.jcorporate.expresso.services.controller;

import com.jcorporate.expresso.core.controller.Block;
import com.jcorporate.expresso.core.controller.ControllerException;
import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.controller.ControllerResponse;
import com.jcorporate.expresso.core.controller.ErrorCollection;
import com.jcorporate.expresso.core.controller.Input;
import com.jcorporate.expresso.core.controller.NonHandleableException;
import com.jcorporate.expresso.core.controller.Output;
import com.jcorporate.expresso.core.controller.State;
import com.jcorporate.expresso.core.controller.Transition;
import com.jcorporate.expresso.core.dbobj.ValidValue;
import com.jcorporate.expresso.core.misc.ConfigManager;
import com.jcorporate.expresso.core.utility.JobHandler;
import com.jcorporate.expresso.services.crontab.CronException;
import com.jcorporate.expresso.services.crontab.Crontab;
import com.jcorporate.expresso.services.crontab.CrontabEntry;
import org.apache.log4j.Logger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Crontab Controller allows for basic insight into the currently running
 * crontabs on the system.  It also allows for basic editing and removal of
 * existing crontabs. It does not create new crontabs yet.
 *
 * @version 1.0
 * @since Expresso 5.3
 * @author Michael Rimov
 */
public class CronController extends com.jcorporate.expresso.core.controller.DBController {

    /**
     * the Log4j Logger
     */
    public static final transient Logger log = Logger.getLogger(CronController.class);

    /**
     * Creates an instance of CronController.  Call setRequestingUid() and
     * setDataContext() before using.
     *
     * @throws ControllerException upon initialization exception.
     *
     * @see com.jcorporate.expresso.core.dbobj.SecuredDBObject#SecuredDBObject
     */
    public CronController() throws ControllerException {
        super();
        State s = new State("list", "List Cron Jobs");
        this.addState(s);
        this.setInitialState("list");
        s = new State("delete", "Delete");
        s.addRequiredParameter("CronId");
        this.addState(s);
        s = new State("edit", "Edit");
        s.addRequiredParameter("CronId");
        this.addState(s);
        s = new State("editUpdate", "Update Execution Time");
        s.addRequiredParameter("CronId");
        s.addRequiredParameter("minute");
        s.addRequiredParameter("hour");
        s.addRequiredParameter("day");
        s.addRequiredParameter("dayofWeek");
        s.addRequiredParameter("month");
        s.addRequiredParameter("year");
        this.addState(s);
    }

    /**
     * Retrieve the title of the controller
     *
     * @return java.lang.String
     */
    public String getTitle() {
        return "Cron Manager";
    }

    /**
     * Build day of week valid values
     *
     * @param request ControllerRequest from which we grab the user's locale
     *
     * @return Vector of ValidValue objects for each day of the week
     */
    protected Vector buildDayOfWeekValidValues(ControllerRequest request) {
        Vector returnValue = new Vector(8);
        returnValue.add(new ValidValue("-1", "Unspecified"));
        SimpleDateFormat df = new SimpleDateFormat("EEEE");
        Calendar cal = Calendar.getInstance(request.getLocale());
        cal.clear();
        for (int i = 1; i < 8; i++) {
            cal.set(Calendar.DAY_OF_WEEK, i);
            returnValue.add(new ValidValue(Integer.toString(i), df.format(cal.getTime())));
        }
        return returnValue;
    }

    /**
     * Build the crontab day valid values
     *
     * @return Vector of ValidValue objects 1-31
     */
    protected Vector buildDayValidValues() {
        Vector returnValue = new Vector(32);
        returnValue.add(new ValidValue("-1", "Unspecified"));
        for (int i = 1; i < 32; i++) {
            returnValue.add(new ValidValue(Integer.toString(i), Integer.toString(i)));
        }
        return returnValue;
    }

    /**
     * Build the crontab hour valid values
     *
     * @return Vector of ValidValue objects: 1-24
     */
    protected Vector buildHourValidValues() {
        Vector returnValue = new Vector(24);
        for (int i = 0; i < 24; i++) {
            returnValue.add(new ValidValue(Integer.toString(i), Integer.toString(i)));
        }
        return returnValue;
    }

    /**
     * Build the crontab minute valid values
     *
     * @return Vector of minute valid values
     */
    protected Vector buildMinuteValidValue() {
        Vector returnValue = new Vector(60);
        for (int i = 0; i < 60; i++) {
            returnValue.add(new ValidValue(Integer.toString(i), Integer.toString(i)));
        }
        return returnValue;
    }

    /**
     * Build month valid values
     *
     * @param request ControllerRequest from which we grab the user's locale
     *
     * @return Vector of valid values for each month in the year
     */
    protected Vector buildMonthValidValues(ControllerRequest request) {
        Vector returnValue = new Vector(8);
        returnValue.add(new ValidValue("-1", "Unspecified"));
        SimpleDateFormat df = new SimpleDateFormat("MMMM");
        Calendar cal = Calendar.getInstance(request.getLocale());
        cal.clear();
        for (int i = 0; i < 12; i++) {
            cal.set(Calendar.MONTH, i);
            returnValue.add(new ValidValue(Integer.toString(i), df.format(cal.getTime())));
        }
        return returnValue;
    }

    /**
     * Build the year valid values
     *
     * @param request ControllerRequest from which we grab the user's locale
     *
     * @return Vector of ValidValues for a 50 year span
     */
    protected Vector buildYearValidValues(ControllerRequest request) {
        Vector returnValue = new Vector(51);
        returnValue.add(new ValidValue("-1", "Unspecified"));
        Calendar cal = Calendar.getInstance(request.getLocale());
        for (int i = cal.get(Calendar.YEAR); i < (cal.get(Calendar.YEAR) + 50); i++) {
            returnValue.add(new ValidValue(Integer.toString(i), Integer.toString(i)));
        }
        return returnValue;
    }

    /**
     * Make sure Cron Manager is synchronized before calling this function!
     *
     * @param crontab the crontab to search
     * @param id the crontab incrementing id
     *
     * @return CrontabEntry or null if it cannot be found.
     */
    protected CrontabEntry findCronById(Crontab crontab, long id) {
        List l = crontab.getAllEntries();
        if (l.size() == 0) {
            return null;
        }
        for (Iterator i = l.iterator(); i.hasNext(); ) {
            CrontabEntry entry = (CrontabEntry) i.next();
            if (entry.getCounter() == id) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Deletes a specified crontab
     *
     * @param request The <code>ControllerRequest</code> object handed to us by
     *        the framework.
     * @param response The <code>ControllerResponse</code> object handed to us
     *        by the framework.
     *
     * @throws ControllerException upon error
     * @throws NonHandleableException upon fatal error
     */
    protected void runDeleteState(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        ErrorCollection ec = request.getErrorCollection();
        if (ec != null) {
            response.saveErrors(ec);
            transition("list", request, response);
            return;
        }
        response.setTitle("Delete Crontab");
        JobHandler jh = ConfigManager.getJobHandler(request.getDataContext());
        Crontab crontab = jh.getCronManager();
        String cronId = request.getParameter("CronId");
        long cronNumber = Long.parseLong(cronId);
        response.setTitle("Edit Cron Parameters");
        CrontabEntry entry = null;
        synchronized (crontab) {
            entry = findCronById(crontab, cronNumber);
            if (entry != null) {
                crontab.removeCrontabEntry(entry);
                response.add(new Output("result", "Deleted Entry Labelled: " + entry.getLabel()));
                Transition t = new Transition("list", this);
                response.add(t);
            }
        }
        if (entry == null) {
            ec = new ErrorCollection();
            ec.addError("Unable to find cron id of: " + cronId + ".  it may have been removed from the queue");
            transition("list", request, response);
            return;
        }
    }

    /**
     * Prompts for editing a crontab entry.
     *
     * @param request The <code>ControllerRequest</code> object handed to us by
     *        the framework.
     * @param response The <code>ControllerResponse</code> object handed to us
     *        by the framework.
     *
     * @throws ControllerException upon error
     * @throws NonHandleableException upon fatal error
     */
    protected void runEditState(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        ErrorCollection ec = request.getErrorCollection();
        if (ec != null) {
            response.saveErrors(ec);
            transition("list", request, response);
            return;
        }
        Vector minValidValue = buildMinuteValidValue();
        Vector hourValidValue = buildHourValidValues();
        Vector dayValidValue = buildDayValidValues();
        Vector dayofWeekValidValue = buildDayOfWeekValidValues(request);
        Vector monthValidValue = buildMonthValidValues(request);
        Vector yearValidValue = buildYearValidValues(request);
        JobHandler jh = ConfigManager.getJobHandler(request.getDataContext());
        Crontab crontab = jh.getCronManager();
        String cronId = request.getParameter("CronId");
        long cronNumber = Long.parseLong(cronId);
        response.setTitle("Edit Cron Parameters");
        CrontabEntry entry = null;
        synchronized (crontab) {
            entry = findCronById(crontab, cronNumber);
            if (entry != null) {
                response.add(new Output("subtitle", "Editing Entry Labelled: " + entry.getLabel()));
                Input i = new Input("minute", "Minute");
                i.setValidValues(minValidValue);
                i.setDefaultValue(Integer.toString(entry.getMinute()));
                response.add(i);
                i = new Input("hour", "Hour");
                i.setValidValues(hourValidValue);
                i.setDefaultValue(Integer.toString(entry.getHour()));
                response.add(i);
                i = new Input("day", "Day of Month");
                i.setValidValues(dayValidValue);
                i.setDefaultValue(Integer.toString(entry.getDayOfMonth()));
                response.add(i);
                i = new Input("dayofWeek", "Day of Week");
                i.setValidValues(dayofWeekValidValue);
                i.setDefaultValue(Integer.toString(entry.getDayOfWeek()));
                response.add(i);
                i = new Input("month", "Month");
                i.setValidValues(monthValidValue);
                i.setDefaultValue(Integer.toString(entry.getMonth()));
                response.add(i);
                i = new Input("year", "Year");
                i.setValidValues(yearValidValue);
                i.setDefaultValue(Integer.toString(entry.getYear()));
                response.add(i);
                Transition t = new Transition("list", this);
                response.add(t);
                t = new Transition("editUpdate", this);
                t.addParam("CronId", cronId);
                response.add(t);
            }
        }
        if (entry == null) {
            ec = new ErrorCollection();
            ec.addError("Unable to find cron id of: " + cronId + ".  it may have been removed from the queue");
            transition("list", request, response);
            return;
        }
    }

    /**
     * Runs the processing of the edit state
     *
     * @param request The <code>ControllerRequest</code> object handed to us by
     *        the framework.
     * @param response The <code>ControllerResponse</code> object handed to us
     *        by the framework.
     *
     * @throws ControllerException upon error
     * @throws NonHandleableException upon fatal error
     */
    protected void runEditUpdateState(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        ErrorCollection ec = request.getErrorCollection();
        if (ec != null) {
            response.saveErrors(ec);
            transition("list", request, response);
            return;
        }
        JobHandler jh = ConfigManager.getJobHandler(request.getDataContext());
        Crontab crontab = jh.getCronManager();
        String cronId = request.getParameter("CronId");
        long cronNumber = Long.parseLong(cronId);
        response.setTitle("Cron Execution Time Result");
        int minute = Integer.parseInt(request.getParameter("minute"));
        int hour = Integer.parseInt(request.getParameter("hour"));
        int day = Integer.parseInt(request.getParameter("day"));
        int dayofWeek = Integer.parseInt(request.getParameter("dayofWeek"));
        int month = Integer.parseInt(request.getParameter("month"));
        int year = Integer.parseInt(request.getParameter("year"));
        CrontabEntry entry = null;
        synchronized (crontab) {
            entry = findCronById(crontab, cronNumber);
            if (entry != null) {
                response.add(new Output("result", "Edited Entry Labelled: " + entry.getLabel()));
                CrontabEntry newEntry;
                try {
                    newEntry = new CrontabEntry(minute, hour, day, dayofWeek, month, year, entry.getLabel(), entry.getListener());
                    crontab.addCrontabEntry(newEntry);
                } catch (CronException ex) {
                    throw new ControllerException("Unable to construct and add new crontab entry.  Old entry still exists in crontab.", ex);
                }
                crontab.removeCrontabEntry(entry);
                Transition t = new Transition("list", this);
                response.add(t);
            }
        }
        if (entry == null) {
            ec = new ErrorCollection();
            ec.addError("Unable to find cron id of: " + cronId + ".  it may have been removed from the crontab");
            transition("list", request, response);
            return;
        }
    }

    /**
     * Runs the List state.  Displays all currently running crontabs
     *
     * @param request The <code>ControllerRequest</code> object handed to us by
     *        the framework.
     * @param response The <code>ControllerResponse</code> object handed to us
     *        by the framework.
     *
     * @throws ControllerException upon error
     * @throws NonHandleableException upon fatal error
     */
    protected void runListState(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        JobHandler jh = ConfigManager.getJobHandler(request.getDataContext());
        if (jh == null) {
            throw new ControllerException("The job handler could not be retrieved. Either an error occurred, or it is disabled.");
        }
        Crontab crontab = jh.getCronManager();
        Block records = new Block("records");
        response.setTitle("Running Crontabs");
        response.add(records);
        synchronized (crontab) {
            List l = crontab.getAllEntries();
            if (l.size() == 0) {
                response.add(new Output("noCrontabs", "No crontabs are currently running"));
            }
            for (Iterator i = l.iterator(); i.hasNext(); ) {
                CrontabEntry entry = (CrontabEntry) i.next();
                long key = entry.getCounter();
                String paramString = Long.toString(key);
                Block oneEntry = new Block(paramString);
                oneEntry.add(new Output("label", entry.getLabel()));
                oneEntry.add(new Output("minute", Integer.toString(entry.getMinute())));
                oneEntry.add(new Output("hour", Integer.toString(entry.getHour())));
                oneEntry.add(new Output("dayofweek", Integer.toString(entry.getDayOfWeek())));
                oneEntry.add(new Output("dayofmonth", Integer.toString(entry.getDayOfMonth())));
                oneEntry.add(new Output("month", Integer.toString(entry.getMonth())));
                oneEntry.add(new Output("year", Integer.toString(entry.getYear())));
                oneEntry.add(new Output("alarmTime", new Date(entry.getAlarmTime()).toString()));
                oneEntry.add(new Output("isRepetitive", "" + entry.isIsRepetitive()));
                Transition t = new Transition("edit", this);
                t.addParam("CronId", paramString);
                oneEntry.add(t);
                t = new Transition("delete", this);
                t.addParam("CronId", paramString);
                oneEntry.add(t);
                records.add(oneEntry);
            }
            Transition refresh = new Transition("list", this);
            refresh.setName("refresh");
            refresh.setLabel("Refresh List");
            response.add(refresh);
        }
    }
}
