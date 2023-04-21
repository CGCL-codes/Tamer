package org.bpmsuite.taglib.calendar;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.bpmsuite.helper.dateandtime.CalendarHelper;
import org.bpmsuite.taglib.calendar.utils.CalendarUtils;
import org.bpmsuite.vo.hrmetadata.Employee;
import org.bpmsuite.vo.platform.PlatformUser;

/**
 *  
 * @author Silvia Mischko
 */
public class CalendarIterator extends BodyTagSupport {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2034259077254699380L;

    private String _id = null;

    private String _type = null;

    private String _path = null;

    private Iterator _iterator = null;

    private Map _businessObjectsMap = null;

    private Collection _years = null;

    private String _year = null;

    private Map _months = null;

    private String _month = null;

    private String _monthFirst = null;

    private Map _days = null;

    private String _day = null;

    private String _dayOfYear = null;

    private String _dayOfMonth = null;

    private String _dayName = null;

    private Collection _businessObjects = null;

    private Employee _employee = null;

    private PlatformUser _platformUser = null;

    private Map _calWeeks = null;

    private String _calWeek = null;

    private String _calWeekFirst = null;

    private Map _monthWeeks = null;

    private String _monthWeek = null;

    private String _monthWeekFirst = null;

    public CalendarIterator() {
    }

    /**
     */
    public int doStartTag() {
        try {
            CalendarMain calMain = (CalendarMain) findAncestorWithClass(this, CalendarMain.class);
            if (calMain == null) {
                return SKIP_BODY;
            }
            storeCalItInRequest();
            _businessObjectsMap = calMain.getEmployeesMap();
            if (_businessObjectsMap != null && !_businessObjectsMap.isEmpty()) {
                if (_type.equalsIgnoreCase(CalendarConstants.T_YEARS)) {
                    return prepareYears();
                }
                if (_type.equalsIgnoreCase(CalendarConstants.T_MONTHS) || _type.equalsIgnoreCase(CalendarConstants.T_MONTHS_WITH_YEAR)) {
                    return prepareMonths();
                }
                if (_type.equalsIgnoreCase(CalendarConstants.T_DAYS_OF_MONTH)) {
                    return prepareDaysForMonth();
                }
                if (_type.equalsIgnoreCase(CalendarConstants.T_EMPLOYEES)) {
                    return prepareEmployees();
                }
                if (_type.equalsIgnoreCase(CalendarConstants.T_CALENDAR_WEEKS)) {
                    return prepareCalendarWeeks();
                }
                if (_type.equalsIgnoreCase(CalendarConstants.T_DAYS_OF_CALENDAR_WEEK)) {
                    return prepareDaysForCalWeek();
                }
                if (_type.equalsIgnoreCase(CalendarConstants.T_MONTH_WEEKS_OF_YEAR)) {
                    return prepareMonthWeeks();
                }
            }
            return SKIP_BODY;
        } catch (Exception e) {
            return SKIP_BODY;
        }
    }

    public int doAfterBody() {
        try {
            if (_iterator.hasNext()) {
                if (_type.equalsIgnoreCase(CalendarConstants.T_YEARS)) {
                    _year = (String) _iterator.next();
                }
                if (_type.equalsIgnoreCase(CalendarConstants.T_MONTHS) || _type.equalsIgnoreCase(CalendarConstants.T_MONTHS_WITH_YEAR)) {
                    _monthFirst = (String) _iterator.next();
                    _month = (String) _months.get(_monthFirst);
                }
                if (_type.equalsIgnoreCase(CalendarConstants.T_DAYS_OF_MONTH)) {
                    String next = (String) _iterator.next();
                    _day = (String) next;
                    _dayName = (String) _days.get(next);
                }
                if (_type.equalsIgnoreCase(CalendarConstants.T_EMPLOYEES)) {
                    _employee = (Employee) _iterator.next();
                    CalendarMain calMain = (CalendarMain) findAncestorWithClass(this, CalendarMain.class);
                    Map userMap = calMain.getPlatformUsersMap();
                    String sysId = _employee.getSystemId();
                    _platformUser = (PlatformUser) (userMap.get(sysId));
                }
                if (_type.equalsIgnoreCase(CalendarConstants.T_CALENDAR_WEEKS)) {
                    Date next = (Date) _iterator.next();
                    _calWeekFirst = CalendarHelper.dateToString(next, "dd.MM.yyyy");
                    _calWeek = (String) _calWeeks.get(next);
                }
                if (_type.equalsIgnoreCase(CalendarConstants.T_DAYS_OF_CALENDAR_WEEK)) {
                    String next = (String) _iterator.next();
                    _day = (String) next;
                    _dayName = (String) _days.get(next);
                    if (_day.equals("1")) {
                        int month = Integer.valueOf(_dayOfMonth).intValue();
                        if (month + 1 == 13) {
                            _dayOfMonth = "01";
                            int year = Integer.valueOf(_dayOfYear).intValue();
                            NumberFormat formatter = new DecimalFormat("000");
                            _dayOfYear = (formatter.format(year + 1));
                        } else {
                            NumberFormat formatter = new DecimalFormat("00");
                            _dayOfMonth = (formatter.format(month + 1));
                        }
                    }
                }
                if (_type.equalsIgnoreCase(CalendarConstants.T_MONTH_WEEKS_OF_YEAR)) {
                    _monthWeekFirst = (String) _iterator.next();
                    _monthWeek = (String) _monthWeeks.get(_monthWeekFirst);
                }
                return EVAL_BODY_AGAIN;
            } else {
                return SKIP_BODY;
            }
        } catch (Exception e) {
            return SKIP_BODY;
        }
    }

    public int doEndTag() {
        return EVAL_PAGE;
    }

    public void setId(String id) {
        _id = id;
    }

    public void setType(String type) {
        _type = type;
    }

    public String getId() {
        return _id;
    }

    public String getType() {
        return _type;
    }

    public Map getEmployeesMap() {
        return _businessObjectsMap;
    }

    public String getPath() {
        return _path;
    }

    public String getYear() {
        return _year;
    }

    public Collection getYears() {
        return _years;
    }

    public String getMonth() {
        return _month;
    }

    public Map getMonths() {
        return _months;
    }

    public String getMonthFirst() {
        return _monthFirst;
    }

    public String getDay() {
        return _day;
    }

    public Map getDays() {
        return _days;
    }

    public String getDayOfYear() {
        return _dayOfYear;
    }

    public String getDayOfMonth() {
        return _dayOfMonth;
    }

    public String getDayName() {
        return _dayName;
    }

    public Employee getEmployee() {
        return _employee;
    }

    public Collection getEmployees() {
        return _businessObjects;
    }

    public PlatformUser getPlatformUser() {
        return _platformUser;
    }

    public String getCalWeek() {
        return _calWeek;
    }

    public Map getCalWeeks() {
        return _calWeeks;
    }

    public String getCalWeekFirst() {
        return _calWeekFirst;
    }

    public String getMonthWeek() {
        return _monthWeek;
    }

    public Map getMonthWeeks() {
        return _monthWeeks;
    }

    public String getMonthWeekFirst() {
        return _monthWeekFirst;
    }

    private int prepareYears() throws Exception {
        _years = CalendarUtils.prepareYears(_businessObjectsMap);
        if (_years != null && !_years.isEmpty()) {
            _iterator = _years.iterator();
            if (_iterator.hasNext()) {
                _year = (String) _iterator.next();
                return EVAL_BODY_INCLUDE;
            } else {
                return SKIP_BODY;
            }
        } else {
            return SKIP_BODY;
        }
    }

    private int prepareMonths() throws Exception {
        if (_type.equalsIgnoreCase(CalendarConstants.T_MONTHS)) {
            CalendarIterator calIt = this;
            calIt = CalendarUtils.findAncestorByType(calIt, CalendarConstants.T_YEARS);
            if (calIt == null) {
                return SKIP_BODY;
            }
            String year = calIt.getYear();
            if (year != null && year.length() > 0) {
                _months = CalendarUtils.prepareMonths(year, _businessObjectsMap);
            }
        }
        if (_type.equalsIgnoreCase(CalendarConstants.T_MONTHS_WITH_YEAR)) {
            _months = CalendarUtils.prepareMonthsWithYear(_businessObjectsMap);
        }
        if (_months != null && !_months.isEmpty()) {
            Collection monthsCol = _months.keySet();
            _iterator = monthsCol.iterator();
            if (_iterator.hasNext()) {
                _monthFirst = (String) _iterator.next();
                _month = (String) _months.get(_monthFirst);
                return EVAL_BODY_INCLUDE;
            } else {
                return SKIP_BODY;
            }
        } else {
            return SKIP_BODY;
        }
    }

    private int prepareDaysForMonth() throws Exception {
        CalendarIterator calIt = this;
        calIt = CalendarUtils.findAncestorByType(calIt, CalendarConstants.T_MONTHS);
        if (calIt == null) {
            return SKIP_BODY;
        }
        String monthFirst = calIt.getMonthFirst();
        if (monthFirst != null && monthFirst.length() > 0) {
            _days = CalendarUtils.prepareDaysForMonth(monthFirst);
            _dayOfYear = monthFirst.substring(6, 10);
            _dayOfMonth = monthFirst.substring(3, 5);
        }
        if (_days != null && !_days.isEmpty()) {
            Collection daysCol = _days.keySet();
            _iterator = daysCol.iterator();
            if (_iterator.hasNext()) {
                String next = (String) _iterator.next();
                _day = (String) next;
                _dayName = (String) _days.get(next);
                return EVAL_BODY_INCLUDE;
            } else {
                return SKIP_BODY;
            }
        } else {
            return SKIP_BODY;
        }
    }

    private int prepareEmployees() throws Exception {
        _businessObjects = _businessObjectsMap.keySet();
        if (_businessObjects != null && !_businessObjects.isEmpty()) {
            _iterator = _businessObjects.iterator();
            if (_iterator.hasNext()) {
                _employee = (Employee) _iterator.next();
                CalendarMain calMain = (CalendarMain) findAncestorWithClass(this, CalendarMain.class);
                if (calMain != null) {
                    Map userMap = calMain.getPlatformUsersMap();
                    String sysId = _employee.getSystemId();
                    _platformUser = (PlatformUser) (userMap.get(sysId));
                    if (_platformUser == null) {
                        return SKIP_BODY;
                    }
                } else {
                    return SKIP_BODY;
                }
                return EVAL_BODY_INCLUDE;
            } else {
                return SKIP_BODY;
            }
        } else {
            return SKIP_BODY;
        }
    }

    private int prepareCalendarWeeks() throws Exception {
        if (_type.equalsIgnoreCase(CalendarConstants.T_CALENDAR_WEEKS)) {
            CalendarIterator calIt = this;
            calIt = CalendarUtils.findAncestorByType(calIt, CalendarConstants.T_YEARS);
            if (calIt == null) {
                return SKIP_BODY;
            }
            String year = calIt.getYear();
            if (year != null && year.length() > 0) {
                _calWeeks = CalendarUtils.prepareCalendarWeeks(year, _businessObjectsMap);
            }
        }
        if (_calWeeks != null && !_calWeeks.isEmpty()) {
            Collection calWeeksCol = _calWeeks.keySet();
            _iterator = calWeeksCol.iterator();
            if (_iterator.hasNext()) {
                Date next = (Date) _iterator.next();
                _calWeekFirst = CalendarHelper.dateToString(next, "dd.MM.yyyy");
                _calWeek = (String) _calWeeks.get(next);
                return EVAL_BODY_INCLUDE;
            } else {
                return SKIP_BODY;
            }
        } else {
            return SKIP_BODY;
        }
    }

    private int prepareDaysForCalWeek() throws Exception {
        CalendarIterator calIt = this;
        calIt = CalendarUtils.findAncestorByType(calIt, CalendarConstants.T_CALENDAR_WEEKS);
        if (calIt == null) {
            return SKIP_BODY;
        }
        String weekFirst = calIt.getCalWeekFirst();
        if (weekFirst != null && weekFirst.length() > 0) {
            _days = CalendarUtils.prepareDaysForCalWeek(weekFirst);
            _dayOfYear = weekFirst.substring(6, 10);
            _dayOfMonth = weekFirst.substring(3, 5);
        }
        if (_days != null && !_days.isEmpty()) {
            Collection daysCol = _days.keySet();
            _iterator = daysCol.iterator();
            if (_iterator.hasNext()) {
                String next = (String) _iterator.next();
                _day = (String) next;
                _dayName = (String) _days.get(next);
                return EVAL_BODY_INCLUDE;
            } else {
                return SKIP_BODY;
            }
        } else {
            return SKIP_BODY;
        }
    }

    private int prepareMonthWeeks() throws Exception {
        if (_type.equalsIgnoreCase(CalendarConstants.T_MONTH_WEEKS_OF_YEAR)) {
            CalendarIterator calIt = this;
            calIt = CalendarUtils.findAncestorByType(calIt, CalendarConstants.T_YEARS);
            if (calIt == null) {
                return SKIP_BODY;
            }
            String year = calIt.getYear();
            if (year != null && year.length() > 0) {
                _monthWeeks = CalendarUtils.prepareMonthweeks(year, _businessObjectsMap);
            }
        }
        if (_monthWeeks != null && !_monthWeeks.isEmpty()) {
            Collection monthsCol = _monthWeeks.keySet();
            _iterator = monthsCol.iterator();
            if (_iterator.hasNext()) {
                _monthWeekFirst = (String) _iterator.next();
                _monthWeek = (String) _monthWeeks.get(_monthWeekFirst);
                return EVAL_BODY_INCLUDE;
            } else {
                return SKIP_BODY;
            }
        } else {
            return SKIP_BODY;
        }
    }

    private void storeCalItInRequest() {
        CalendarIterator itAnc = (CalendarIterator) findAncestorWithClass(this, CalendarIterator.class);
        if (itAnc == null) {
            _path = _id;
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append(itAnc.getPath());
            sb.append("_");
            sb.append(_id);
            _path = sb.toString();
        }
        pageContext.getRequest().setAttribute(_path, this);
    }
}
