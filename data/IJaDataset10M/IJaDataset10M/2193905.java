package si.cit.eprojekti.ecalendar.controller.calendarStates;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import javax.servlet.ServletRequest;
import org.apache.log4j.Priority;
import si.cit.eprojekti.ecalendar.CalendarSchema;
import si.cit.eprojekti.ecalendar.controller.CalendarController;
import si.cit.eprojekti.ecalendar.util.ComponentSecurityManager;
import com.jcorporate.expresso.core.controller.Block;
import com.jcorporate.expresso.core.controller.ControllerException;
import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.controller.ControllerResponse;
import com.jcorporate.expresso.core.controller.NonHandleableException;
import com.jcorporate.expresso.core.controller.Output;
import com.jcorporate.expresso.core.controller.ServletControllerRequest;
import com.jcorporate.expresso.core.controller.State;
import com.jcorporate.expresso.core.controller.Transition;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.ISOValidValue;
import com.jcorporate.expresso.core.i18n.Messages;
import si.cit.eprojekti.ecalendar.dbobj.Asset;
import si.cit.eprojekti.ecalendar.dbobj.AssetParticipants;
import si.cit.eprojekti.ecalendar.dbobj.CalendarAsset;

/**
 *	
 *	List Calendar Assets State - external state for Calendar Controller
 *
 * 	@author taks
 *	@version 1.0
 * 
 */
public class ListCalendarAssetsState extends State {

    private static final long serialVersionUID = 3018895579452016967L;

    private static org.apache.log4j.Category standardLog = org.apache.log4j.Category.getInstance("pvn.standard.ecalendar");

    private static org.apache.log4j.Category debugLog = org.apache.log4j.Category.getInstance("pvn.debug.ecalendar");

    private static org.apache.log4j.Category observerLog = org.apache.log4j.Category.getInstance("pvn.observer.ecalendar");

    /**
	 * 	Constructor
	 */
    public ListCalendarAssetsState() {
        super();
    }

    /** 
	 * Constructor
	 * @param stateName
	 * @param descrip
	 */
    public ListCalendarAssetsState(String stateName, String descrip) {
        super(stateName, descrip);
    }

    /** 
	 * Get number of days in month
	 * @param month
	 */
    private int getNumberOfDaysInMonth(int month, int year) {
        GregorianCalendar c = new GregorianCalendar();
        if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) return 30;
        if (month == 2) {
            if (c.isLeapYear(year)) return 29; else return 28;
        }
        return 31;
    }

    /** 
	 *  Run this state
	 */
    public void run(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        super.run(request, response);
        response.setFormCache();
        try {
            Transition errorTrans = new Transition();
            errorTrans.setControllerObject(CalendarController.class);
            errorTrans.setState(ErrorState.STATE_NAME);
            errorTrans.setName("errorTrans");
            setErrorTransition(errorTrans);
            short accessLevel = ComponentSecurityManager.checkSecurityForProject(request.getParameter("key"), request.getUid());
            if (accessLevel < ComponentSecurityManager.ACCESS_OBSERVER) throw ComponentSecurityManager.accessDeniedOccured(response);
            String calId = request.getParameter("key");
            si.cit.eprojekti.ecalendar.dbobj.Calendar calendar = new si.cit.eprojekti.ecalendar.dbobj.Calendar();
            calendar.setField("CalendarId", calId);
            calendar.retrieve();
            calendar.setLocale(request.getLocale());
            boolean projectCalendar = false;
            if (calendar.getField("CalendarProject").equals("T")) projectCalendar = true;
            Output calendarDesc = new Output();
            calendarDesc.setName("CalendarDesc");
            calendarDesc.setLabel("Calendar");
            calendarDesc.setContent(calendar.getField("CalendarDesc"));
            response.add(calendarDesc);
            getSession().setPersistentAttribute("SelectedCalendar", calId);
            java.util.Calendar cal = new GregorianCalendar();
            String day = request.getParameter("day");
            String month = request.getParameter("month");
            String year = request.getParameter("year");
            if (day == null) day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
            if (month == null) month = String.valueOf(cal.get(Calendar.MONTH) + 1);
            if (year == null) year = String.valueOf(cal.get(Calendar.YEAR));
            int dayInWeek = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);
            if (Integer.parseInt(month) < 10) month = "0" + Integer.parseInt(month);
            int days = getNumberOfDaysInMonth(Integer.parseInt(month), Integer.parseInt(year));
            Block dateList = new Block();
            dateList.setName("DateList");
            String date = year + "-" + month + "-";
            for (int i = 1; i <= days; i++) {
                Block dateDay = new Block("DateDay");
                int sumTaskAndEvents = 0;
                int sumTasks = 0;
                int sumMyAssets = 0;
                Asset asset = new Asset();
                asset.setField("CalAssetDate", year + "-" + month + "-" + i);
                asset.setLocale(request.getLocale());
                ArrayList allRecords = asset.searchAndRetrieveList();
                Iterator allRecordsIterAsset = allRecords.iterator();
                while (allRecordsIterAsset.hasNext()) {
                    asset = (Asset) allRecordsIterAsset.next();
                    CalendarAsset calendarAsset = new CalendarAsset();
                    calendarAsset.setLocale(request.getLocale());
                    boolean assetBool = false;
                    if (asset.getField("CalendarId").equals(calId)) assetBool = true;
                    if (calendarAsset.isAssetMemberOfCalendar(asset.getFieldInt("CalAssetId"), Integer.parseInt(calId))) assetBool = true;
                    if (assetBool) {
                        sumTaskAndEvents++;
                        if (asset.getField("CalAssetClass").equals("T")) sumTasks++;
                        AssetParticipants participants = new AssetParticipants();
                        participants.setField("CalAssetId", asset.getField("CalAssetId"));
                        participants.setField("UserId", request.getUid());
                        sumMyAssets += participants.count();
                    }
                }
                Output oneday = new Output();
                oneday.setName("Day");
                oneday.setLabel("Day");
                java.util.Calendar calendarDay = Calendar.getInstance();
                calendarDay.set(Integer.parseInt(year), Integer.parseInt(month) - 1, i);
                if ((calendarDay.get(Calendar.DAY_OF_WEEK) == 1) || (calendarDay.get(Calendar.DAY_OF_WEEK) == 7)) oneday.setAttribute("typeOfDay", "nonworking"); else oneday.setAttribute("typeOfDay", "working");
                oneday.setAttribute("nameOfDay", getNameOfDay(calendarDay.get(Calendar.DAY_OF_WEEK), request.getLocale()));
                oneday.setAttribute("numberOfTasks", String.valueOf(sumTasks));
                oneday.setAttribute("numberOfEvents", String.valueOf(sumTaskAndEvents - sumTasks));
                oneday.setAttribute("numberOfTasksAndEvents", String.valueOf(sumTaskAndEvents));
                if (sumMyAssets == 0) oneday.setAttribute("anyMyAssets", "no"); else oneday.setAttribute("anyMyAssets", "yes");
                oneday.setContent(String.valueOf(i));
                dateDay.add(oneday);
                Transition dayAssets = new Transition();
                dayAssets.setName("DayAssets");
                dayAssets.setLabel("Day assets");
                dayAssets.setControllerObject(this.getController().getClass());
                dayAssets.setState("dayAssetsState");
                dayAssets.addParam("calendarId", calId);
                if (i < 10) dayAssets.addParam("date", date + "0" + i); else dayAssets.addParam("date", date + i);
                dateDay.add(dayAssets);
                dateList.addNested(dateDay);
            }
            response.addBlock(dateList);
            Transition goToPreviousMonth = new Transition();
            goToPreviousMonth.setName("GoToPreviousMonth");
            goToPreviousMonth.setLabel(Messages.getString(CalendarSchema.class.getName(), request.getLocale(), "listCalendarAssetsStatePreviousMonthLabel"));
            goToPreviousMonth.setControllerObject(this.getController().getClass());
            goToPreviousMonth.setState("listCalendarAssetsState");
            goToPreviousMonth.addParam("key", calId);
            if (Integer.parseInt(month) == 1) {
                goToPreviousMonth.addParam("month", "12");
                goToPreviousMonth.addParam("year", String.valueOf(Integer.parseInt(year) - 1));
            } else {
                goToPreviousMonth.addParam("month", String.valueOf(Integer.parseInt(month) - 1));
                goToPreviousMonth.addParam("year", year);
            }
            response.add(goToPreviousMonth);
            Transition goToNextMonth = new Transition();
            goToNextMonth.setName("GoToNextMonth");
            goToNextMonth.setLabel(Messages.getString(CalendarSchema.class.getName(), request.getLocale(), "listCalendarAssetsStateNextMonthLabel"));
            goToNextMonth.setControllerObject(this.getController().getClass());
            goToNextMonth.setState("listCalendarAssetsState");
            goToNextMonth.addParam("key", calId);
            if (Integer.parseInt(month) == 12) {
                goToNextMonth.addParam("month", "1");
                goToNextMonth.addParam("year", String.valueOf(Integer.parseInt(year) + 1));
            } else {
                goToNextMonth.addParam("month", String.valueOf(Integer.parseInt(month) + 1));
                goToNextMonth.addParam("year", year);
            }
            response.add(goToNextMonth);
            Transition goToPreviousYear = new Transition();
            goToPreviousYear.setName("GoToNextYear");
            goToPreviousYear.setLabel(Messages.getString(CalendarSchema.class.getName(), request.getLocale(), "listCalendarAssetsStateNextYearLabel"));
            goToPreviousYear.setControllerObject(this.getController().getClass());
            goToPreviousYear.setState("listCalendarAssetsState");
            goToPreviousYear.addParam("key", calId);
            goToPreviousYear.addParam("month", month);
            goToPreviousYear.addParam("year", String.valueOf(Integer.parseInt(year) + 1));
            response.add(goToPreviousYear);
            Transition goToNextYear = new Transition();
            goToNextYear.setName("GoToPreviousYear");
            goToNextYear.setLabel(Messages.getString(CalendarSchema.class.getName(), request.getLocale(), "listCalendarAssetsStatePreviousYearLabel"));
            goToNextYear.setControllerObject(this.getController().getClass());
            goToNextYear.setState("listCalendarAssetsState");
            goToNextYear.addParam("key", calId);
            goToNextYear.addParam("month", month);
            goToNextYear.addParam("year", String.valueOf(Integer.parseInt(year) - 1));
            response.add(goToNextYear);
            Transition goToMonthClassicView = new Transition();
            goToMonthClassicView.setName("GoToMonthClassicView");
            goToMonthClassicView.setLabel(Messages.getString(CalendarSchema.class.getName(), request.getLocale(), "listCalendarAssetsStateMonthClassicViewLabel"));
            goToMonthClassicView.setControllerObject(this.getController().getClass());
            goToMonthClassicView.setState("monthAssetsState");
            goToMonthClassicView.addParam("key", calId);
            goToMonthClassicView.addParam("year", year);
            goToMonthClassicView.addParam("month", month);
            response.add(goToMonthClassicView);
            String d = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
            String m = String.valueOf(cal.get(Calendar.MONTH) + 1);
            if (Integer.parseInt(m) < 10) m = "0" + m;
            Output pageCriteria = new Output();
            pageCriteria.setName("pageCriteria");
            pageCriteria.setAttribute("day", day);
            pageCriteria.setAttribute("month", month);
            pageCriteria.setAttribute("monthName", getNameOfMonth(Integer.parseInt(month), request.getLocale()));
            pageCriteria.setAttribute("year", year);
            pageCriteria.setAttribute("currentMMYY", month + "." + year);
            pageCriteria.setAttribute("todaysDate", d + "." + m + "." + cal.get(Calendar.YEAR));
            response.add(pageCriteria);
        } catch (Exception e) {
            if (e instanceof DBException) addError("errors.DBException"); else if (e.getMessage().equals("errors.accessDeniedOccured")) addError("errors.accessDeniedOccured"); else addError("errors.Exception");
            if (standardLog.isEnabledFor(Priority.WARN)) standardLog.warn(" :: Exception in \"" + this.getName() + "\" : " + e.toString());
            if (debugLog.isDebugEnabled()) debugLog.debug(" :: Exception in \"" + this.getName() + "\" : " + e.toString(), e.fillInStackTrace());
        } finally {
            if (observerLog.isInfoEnabled()) {
                ServletRequest servletRq = ((ServletControllerRequest) request).getServletRequest();
                observerLog.info(" :: Location= " + this.getClass().getName() + " :: UID= " + request.getUid() + " :: User= " + request.getUser() + " :: IP= " + servletRq.getRemoteAddr());
            }
        }
    }

    public String getNameOfDay(int day, Locale loc) {
        Locale locale;
        locale = loc;
        String schemaClass = "si.cit.eprojekti.ecalendar.CalendarSchema";
        String prefix = this.getClass().getName();
        if (day == 2) return new ISOValidValue(schemaClass, locale, prefix, "2", "Mon").getDescription();
        if (day == 3) return new ISOValidValue(schemaClass, locale, prefix, "3", "Tue").getDescription();
        if (day == 4) return new ISOValidValue(schemaClass, locale, prefix, "4", "Wen").getDescription();
        if (day == 5) return new ISOValidValue(schemaClass, locale, prefix, "5", "Thu").getDescription();
        if (day == 6) return new ISOValidValue(schemaClass, locale, prefix, "6", "Fri").getDescription();
        if (day == 7) return new ISOValidValue(schemaClass, locale, prefix, "7", "Sat").getDescription();
        if (day == 1) return new ISOValidValue(schemaClass, locale, prefix, "1", "Sun").getDescription();
        return "N/A";
    }

    public String getNameOfMonth(int month, Locale loc) {
        Locale locale;
        locale = loc;
        String schemaClass = "si.cit.eprojekti.ecalendar.CalendarSchema";
        String prefix = this.getClass().getName();
        if (month == 1) return new ISOValidValue(schemaClass, locale, prefix, "1", "January").getDescription();
        if (month == 2) return new ISOValidValue(schemaClass, locale, prefix, "2", "February").getDescription();
        if (month == 3) return new ISOValidValue(schemaClass, locale, prefix, "3", "March").getDescription();
        if (month == 4) return new ISOValidValue(schemaClass, locale, prefix, "4", "April").getDescription();
        if (month == 5) return new ISOValidValue(schemaClass, locale, prefix, "5", "May").getDescription();
        if (month == 6) return new ISOValidValue(schemaClass, locale, prefix, "6", "June").getDescription();
        if (month == 7) return new ISOValidValue(schemaClass, locale, prefix, "7", "July").getDescription();
        if (month == 8) return new ISOValidValue(schemaClass, locale, prefix, "8", "August").getDescription();
        if (month == 9) return new ISOValidValue(schemaClass, locale, prefix, "9", "September").getDescription();
        if (month == 10) return new ISOValidValue(schemaClass, locale, prefix, "10", "October").getDescription();
        if (month == 11) return new ISOValidValue(schemaClass, locale, prefix, "11", "November").getDescription();
        if (month == 12) return new ISOValidValue(schemaClass, locale, prefix, "12", "December").getDescription();
        return "N/A";
    }
}
