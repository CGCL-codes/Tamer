package pedro.soa.validation;

import pedro.system.*;
import pedro.soa.alerts.SystemErrorAlert;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ArrayList;

public class DateValidator extends AbstractEditFieldValidationService {

    /**
     * format used to read/write date values from file
     */
    public static final SimpleDateFormat canonicalFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * lists number of days for each month
     */
    private int[] daysInMonth;

    /**
     * indicates DD/MM/YYYY format
     */
    public static final SimpleDateFormat ddmmyyyyFormat = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * the current date format, initially set to ddMMyyyy format
     */
    private static SimpleDateFormat formatToUse = ddmmyyyyFormat;

    /**
     * indicates MM/DD/YYYY format
     */
    public static final SimpleDateFormat mmddyyyyFormat = new SimpleDateFormat("MM/dd/yyyy");

    /**
     * indicates YYYY/MM/DD format
     */
    public static final SimpleDateFormat yyyymmddFormat = new SimpleDateFormat("yyyy/MM/dd");

    public DateValidator() {
        daysInMonth = new int[12];
        daysInMonth[0] = 31;
        daysInMonth[1] = 28;
        daysInMonth[2] = 31;
        daysInMonth[3] = 30;
        daysInMonth[4] = 31;
        daysInMonth[5] = 30;
        daysInMonth[6] = 31;
        daysInMonth[7] = 31;
        daysInMonth[8] = 30;
        daysInMonth[9] = 31;
        daysInMonth[10] = 30;
        daysInMonth[11] = 31;
    }

    /**
     * string date
     *
     * @return Date object
     */
    public static Date getCanonicalDate(String date) {
        return canonicalFormat.parse(date.toUpperCase(), new ParsePosition(0));
    }

    /**
     * @return the canonical date format used for validation
     */
    public static SimpleDateFormat getCanonicalFormat() {
        return canonicalFormat;
    }

    /**
     * gets the current date format
     *
     * @return the current date format
     */
    public static SimpleDateFormat getDateFormat() {
        return formatToUse;
    }

    /**
     * @param format returns the pattern corresponding to the
     *               date format
     * @return the string representation of the date format pattern
     */
    public static String getDateFormatString(SimpleDateFormat format) {
        return format.toPattern();
    }

    /**
     * @param date a date object
     * @return the formatted date value based on the canonical date format.
     */
    public static String getCanonicalValue(Date date) {
        StringBuffer buffer = canonicalFormat.format(date, new StringBuffer(), new FieldPosition(0));
        return buffer.toString();
    }

    /**
     * @param date string representation of a date
     * @return a date object
     */
    public static Date getDate(String date) {
        return formatToUse.parse(date.toUpperCase(), new ParsePosition(0));
    }

    /**
     * a date
     *
     * @return a formatted date value based on the current date format
     */
    public static String getDateValue(Date date) {
        StringBuffer buffer = formatToUse.format(date, new StringBuffer(), new FieldPosition(0));
        return buffer.toString();
    }

    /**
     * sets the date format used to do validation
     *
     * @param format the new date format used for validation
     */
    public static void setDateFormat(SimpleDateFormat format) {
        formatToUse = format;
    }

    /**
	* sets date format to ddmmyyyy format
	*/
    public static void setDefaultDateFormat() {
        formatToUse = ddmmyyyyFormat;
    }

    /**
     * assumes that the string value passed to it will always
     * be written in a canonical form.
     */
    public ArrayList validate(PedroFormContext pedroFormContext, String value) {
        ArrayList alerts = new ArrayList();
        if (isEmpty(value) == true) {
            return alerts;
        }
        String dateValue = value;
        Date date = canonicalFormat.parse(value, new ParsePosition(0));
        if (date != null) {
            dateValue = getDateValue(date);
        }
        if (formatToUse == yyyymmddFormat) {
            return validateYYYYMMDD(dateValue);
        } else if (formatToUse == mmddyyyyFormat) {
            return validateMMDDYYYY(dateValue);
        } else {
            return validateDDMMYYYY(dateValue);
        }
    }

    private ArrayList validateYYYYMMDD(String value) {
        ArrayList alerts = new ArrayList();
        Date date = yyyymmddFormat.parse(value, new ParsePosition(0));
        if (date == null) {
            String errorMessage = PedroResources.getMessage("validation.date.notLikePattern", value, yyyymmddFormat.toPattern(), getFieldName());
            SystemErrorAlert errorAlert = new SystemErrorAlert(errorMessage);
            alerts.add(errorAlert);
            return alerts;
        }
        int yearMonthDivider = value.indexOf('/', 0);
        int monthDayDivider = value.indexOf('/', yearMonthDivider + 1);
        String dayString = value.substring(monthDayDivider + 1);
        String monthString = value.substring(yearMonthDivider + 1, monthDayDivider);
        String yearString = value.substring(0, yearMonthDivider);
        if (yearString.length() != 4) {
            String errorMessage = PedroResources.getMessage("validation.date.fourDigitYear");
            SystemErrorAlert errorAlert = new SystemErrorAlert(errorMessage);
            alerts.add(errorAlert);
            return alerts;
        }
        String error = validateMonth(monthString);
        if (error != null) {
            SystemErrorAlert errorAlert = new SystemErrorAlert(error);
            alerts.add(errorAlert);
            return alerts;
        }
        try {
            int year = getYear(yearString);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(GregorianCalendar.YEAR, year);
            Integer month = Integer.valueOf(monthString);
            error = validateDay(month.intValue(), year, dayString);
            if (error != null) {
                SystemErrorAlert errorAlert = new SystemErrorAlert(error);
                alerts.add(errorAlert);
            }
        } catch (NumberFormatException e) {
        }
        return alerts;
    }

    private ArrayList validateDDMMYYYY(String value) {
        ArrayList alerts = new ArrayList();
        Date date = ddmmyyyyFormat.parse(value, new ParsePosition(0));
        if (date == null) {
            String errorMessage = PedroResources.getMessage("validation.date.notLikePattern", value, ddmmyyyyFormat.toPattern(), getFieldName());
            SystemErrorAlert errorAlert = new SystemErrorAlert(errorMessage);
            alerts.add(errorAlert);
            return alerts;
        }
        int dayMonthDivider = value.indexOf('/', 0);
        int monthYearDivider = value.indexOf('/', dayMonthDivider + 1);
        String dayString = value.substring(0, dayMonthDivider);
        String yearString = value.substring(monthYearDivider + 1);
        if (yearString.length() != 4) {
            String errorMessage = PedroResources.getMessage("validation.date.fourDigitYear");
            SystemErrorAlert errorAlert = new SystemErrorAlert(errorMessage);
            alerts.add(errorAlert);
            return alerts;
        }
        String monthString = value.substring(dayMonthDivider + 1, monthYearDivider);
        String error = validateMonth(monthString);
        if (error != null) {
            SystemErrorAlert errorAlert = new SystemErrorAlert(error);
            alerts.add(error);
            return alerts;
        }
        try {
            int year = getYear(yearString);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(GregorianCalendar.YEAR, year);
            Integer month = Integer.valueOf(monthString);
            error = validateDay(month.intValue(), year, dayString);
            if (error != null) {
                SystemErrorAlert errorAlert = new SystemErrorAlert(error);
                alerts.add(error);
                return alerts;
            }
        } catch (NumberFormatException e) {
        }
        return alerts;
    }

    private ArrayList validateMMDDYYYY(String value) {
        ArrayList alerts = new ArrayList();
        Date date = mmddyyyyFormat.parse(value, new ParsePosition(0));
        if (date == null) {
            String errorMessage = PedroResources.getMessage("validation.date.notLikePattern", value, mmddyyyyFormat.toPattern(), getFieldName());
            SystemErrorAlert errorAlert = new SystemErrorAlert(errorMessage);
            alerts.add(errorAlert);
            return alerts;
        }
        int monthDayDivider = value.indexOf('/', 0);
        int dayYearDivider = value.indexOf('/', monthDayDivider + 1);
        String monthString = value.substring(0, monthDayDivider);
        String error = validateMonth(monthString);
        if (error != null) {
            SystemErrorAlert errorAlert = new SystemErrorAlert(error);
            alerts.add(errorAlert);
            return alerts;
        }
        String yearString = value.substring(dayYearDivider + 1);
        if (yearString.length() != 4) {
            String errorMessage = PedroResources.getMessage("validation.date.fourDigitYear");
            SystemErrorAlert errorAlert = new SystemErrorAlert(errorMessage);
            alerts.add(errorAlert);
            return alerts;
        }
        String dayString = value.substring(monthDayDivider + 1, dayYearDivider);
        try {
            int year = getYear(yearString);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(GregorianCalendar.YEAR, year);
            Integer month = Integer.valueOf(monthString);
            error = validateDay(month.intValue(), year, dayString);
            if (error != null) {
                SystemErrorAlert errorAlert = new SystemErrorAlert(error);
                alerts.add(error);
                return alerts;
            }
        } catch (NumberFormatException e) {
        }
        return alerts;
    }

    private String validateMonth(String value) {
        try {
            Integer month = Integer.valueOf(value);
            if ((month.intValue() < 1) || (month.intValue() > 12)) {
                String errorMessage = PedroResources.getMessage("validation.date.invalidMonth", getFieldName(), value);
                return errorMessage;
            }
            return null;
        } catch (NumberFormatException err) {
            String errorMessage = PedroResources.getMessage("validation.date.invalidMonth", getFieldName(), value);
            return errorMessage;
        }
    }

    private String validateDay(int month, int year, String value) {
        try {
            int maximumNumberOfDays = daysInMonth[month - 1];
            if ((month == 2) && (year % 4 == 0)) {
                maximumNumberOfDays = 29;
            }
            Integer day = Integer.valueOf(value);
            if ((day.intValue() < 1) || (day.intValue() > maximumNumberOfDays)) {
                String errorMessage = PedroResources.getMessage("validation.date.invalidDay", getFieldName(), value, String.valueOf(month), String.valueOf(year));
                return errorMessage;
            }
            return null;
        } catch (NumberFormatException err) {
            String errorMessage = PedroResources.getMessage("validation.date.invalidDay", getFieldName(), value, String.valueOf(month), String.valueOf(year));
            return errorMessage;
        }
    }

    private static int getYear(String yr) throws NumberFormatException {
        if (yr.length() == 2) {
            Integer yearValue = Integer.valueOf(yr);
            int year = yearValue.intValue();
            if ((year >= 0) && (year < 70)) {
                year += 2000;
            } else {
                year += 1900;
            }
            return year;
        } else {
            Integer yearValue = Integer.valueOf(yr);
            int year = yearValue.intValue();
            return year;
        }
    }
}
