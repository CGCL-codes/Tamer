package net.sf.esims.util;

import static net.sf.esims.util.EsimsConstants._DATE_FORMAT;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import net.sf.esims.exception.BusinessRuleException;
import net.sf.esims.model.entity.ReceivedApplicationForm;
import net.sf.esims.model.valueobject.StudentDetails;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.validator.routines.DateValidator;

/**
 * 
 * 
 * Contains any utility methods needed for the application
 * @author jvictor
 *
 */
public class EsimsUtils {

    private static DateValidator dateValidator;

    private static TimeZone timeZone;

    static {
        dateValidator = DateValidator.getInstance();
        timeZone = TimeZone.getDefault();
    }

    /**
	 * Takes a java.util.Date and formats
	 * the date in DD-MM-YYYY and returns 
	 * the same as a string
	 * 
	 * @param dateToFormat
	 * @return
	 */
    public static String formatDateAsDD_MM_YYYY_String(Date dateToFormat) {
        return DateFormatUtils.format(dateToFormat, _DATE_FORMAT);
    }

    /**
	 * Compares the first date with the second
	 * 
	 * @param firstDate
	 * @param secondDate
	 * @return 0 If the dates are same , -1 if first date is lesser, + 1 if 1st date is greater
	 */
    public static int compareDates(Date firstDate, Date secondDate) {
        return dateValidator.compareDates(firstDate, secondDate, timeZone);
    }

    public static int compareYears(Date firstDate, Date secondDate) {
        return dateValidator.compareYears(firstDate, secondDate, timeZone);
    }

    public static Date createDate(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
	 * Allows creation of date with time
	 * @param day
	 * @param month
	 * @param year
	 * @param hour , hour of day
	 * @param minute
	 * @return date
	 */
    public static Date createDateWithTime(int day, int month, int year, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
	 * Appends % to the start and end of a string
	 * Useful in building like %xxxx queries
	 * 
	 * @param toModify
	 * @return
	 */
    public static String percentAppender(String toModify) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("%");
        buffer.append(toModify);
        buffer.append("%");
        return buffer.toString();
    }

    public static String appendPercentToEnd(String toModify) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(toModify);
        buffer.append("%");
        return buffer.toString();
    }

    /**
	 * Intended to be used _ONLY_ by the action classes
	 * Creates an html string that displays a
	 * piece of info to the user.
	 * 
	 * 
	 * 
	 * 
	 * @param message , to be formatted
	 */
    public static String createInfoMessage(String context, String message) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<img src='");
        buffer.append(context);
        buffer.append("/images/info.png' width='24' height='24'>");
        buffer.append("&nbsp;");
        buffer.append(message);
        return buffer.toString();
    }

    /**
	 * Creates a warning message
	 * @param context
	 * @param message
	 * @return
	 */
    public static String createWarnMessage(String context, String message) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<img src='");
        buffer.append(context);
        buffer.append("/images/warning.png' width='24' height='24'>");
        buffer.append("&nbsp;");
        buffer.append(message);
        return buffer.toString();
    }

    /**
	 * Creates a important message
	 * @param context
	 * @param message
	 * @return
	 */
    public static String createImportantMessage(String context, String message) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<img src='");
        buffer.append(context);
        buffer.append("/images/important.png' width='24' height='24'>");
        buffer.append("&nbsp;");
        buffer.append(message);
        return buffer.toString();
    }

    public static String createErrorMessage(String context, String message) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<img src='");
        buffer.append(context);
        buffer.append("/images/error.png' width='24' height='24'>");
        buffer.append("&nbsp;");
        buffer.append(" An Unexpected error has occured, please click");
        buffer.append("<a href='" + context + "/ErrorHandler.action'>");
        buffer.append(" here");
        buffer.append("</a>");
        buffer.append(" for more details");
        buffer.append("<br>");
        return buffer.toString();
    }

    public static void validateStrings(String str, int len) throws BusinessRuleException {
        if (StringUtils.isEmpty(str)) throw new BusinessRuleException("Arg was found null/empty");
        if (str.length() > len) throw new BusinessRuleException(str + " can be only a maximum of " + len + " characters in length");
    }

    public static void validateNullableStrings(String str, int len) throws BusinessRuleException {
        if (!StringUtils.isEmpty(str)) {
            if (str.length() > len) throw new BusinessRuleException(str + " can be only a maximum of " + len + " characters in length");
        }
    }

    public static StudentDetails createStudentDetailsFromApplicationForm(ReceivedApplicationForm applicationForm) {
        StudentDetails details = new StudentDetails();
        details.setBelowPovertyLine(applicationForm.getBelowPovertyLine());
        details.setCaste(applicationForm.getCaste());
        details.setDateOfBirth(applicationForm.getDateOfBirth());
        details.setFatherFirstName(applicationForm.getFatherFirstName());
        details.setFatherMiddleName(applicationForm.getFatherMiddleName());
        details.setFatherLastName(applicationForm.getFatherLastName());
        details.setFirstName(applicationForm.getFirstName());
        details.setFormNumber(applicationForm.getFormNumber());
        details.setGender(applicationForm.getGender());
        details.setGrossHouseHoldIncome(applicationForm.getGrossHouseHoldIncome());
        details.setLastName(applicationForm.getLastName());
        details.setMiddleName(applicationForm.getMiddleName());
        details.setMotherFirstName(applicationForm.getMotherFirstName());
        details.setMotherLastName(applicationForm.getMotherLastName());
        details.setMotherMiddleName(applicationForm.getMotherMiddleName());
        details.setMotherTounge(applicationForm.getMotherTounge());
        details.setNationality(applicationForm.getNationality());
        details.setOtherBackwardCaste(applicationForm.getOtherBackwardCaste());
        details.setPermanentHouseNameOrNumber(applicationForm.getPermanentHouseNameOrNumber());
        details.setPermanentPhoneNumber(applicationForm.getPermanentPhoneNumber());
        details.setPermanentPinCode(applicationForm.getPermanentPinCode());
        details.setPermanentState(applicationForm.getPermanentState());
        details.setPermanentStreet(applicationForm.getPermanentStreet());
        details.setPermanentVillageOrTownOrCity(applicationForm.getPermanentVillageOrTownOrCity());
        details.setPlaceOfBirth(applicationForm.getPlaceOfBirth());
        details.setReligion(applicationForm.getReligion());
        details.setScheduledCasteOrTribe(applicationForm.getScheduledCasteOrTribe());
        details.setSubcaste(applicationForm.getSubcaste());
        return details;
    }
}
