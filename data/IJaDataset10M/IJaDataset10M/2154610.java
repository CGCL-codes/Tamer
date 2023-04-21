package hoipolloi;

import java.util.Calendar;

/**
 * Contains various functions related to a persons date of birth.
 *
 * Contains various functions related to a persons date of birth, such as
 * figuring their current age, their age from a given date, if their birthday
 * is today, the date of their next birthday, days until next birthday,
 * get number of seconds left till next birthday, convert time from seconds to
 * readable format, and determine how many days in a particular month.
 *
 * @author  Brandon Buck
 * @since   January 1, 2009
 * @version 1.0
 */
public class Birthday {

    private static final int HOURS_IN_DAY = 24;

    private static final int MINUTES_IN_HOUR = 60;

    private static final int SECONDS_IN_MINUTE = 60;

    private static final int MILLISECONDS_IN_SECOND = 1000;

    private static final int SECONDS_IN_DAY = HOURS_IN_DAY * MINUTES_IN_HOUR * SECONDS_IN_MINUTE;

    private static final int SECONDS_IN_HOUR = MINUTES_IN_HOUR * SECONDS_IN_MINUTE;

    /**
     * Get the age of a person as of today given their date of birth.
     * Takes the DOB given (YYYY-MM-DD) and returns the age of the person
     * given as of today.
     * @param dobString A persons Date of Birth (YYYY-MM-DD)
     * @return The age of the person as of today
     */
    public static int getCurrentAge(String dobString) {
        Calendar dob = getCalendarFromString(dobString);
        Calendar now = Calendar.getInstance();
        int dobYear = dob.get(Calendar.YEAR);
        int nowYear = now.get(Calendar.YEAR);
        int age = nowYear - dobYear;
        dob.set(Calendar.YEAR, now.get(Calendar.YEAR));
        if (dob.after(now)) age--;
        return age;
    }

    /** Gets the age of a person from a given date.
     * Computes a person age from a specific date passed (YYYY-MM-DD) and then
     * returns their age.
     * @param dobString The day the person was born.
     * @param otherString The date you wish to know the person age.
     * @return How old this person will be on the day passed.
     */
    public static int getAgeFromDate(String dobString, String otherString) {
        Calendar dob = getCalendarFromString(dobString);
        Calendar other = getCalendarFromString(otherString);
        int dobYear = dob.get(Calendar.YEAR);
        int otherYear = other.get(Calendar.YEAR);
        int age = otherYear - dobYear;
        dob.set(Calendar.YEAR, other.get(Calendar.YEAR));
        if (dob.after(other)) age--;
        return age;
    }

    /** Determines if today is the same day as the given birthday.
     * Determines if the given birthday is today, if so returns true else it
     * returns false.
     * @param dobString The date of birth to test against today.
     * @return True if today is the birthday, false if not.
     */
    public static boolean isToday(String dobString) {
        Calendar dob = getCalendarFromString(dobString);
        Calendar now = Calendar.getInstance();
        if (dob.get(Calendar.MONTH) == now.get(Calendar.MONTH) && dob.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)) return true;
        return false;
    }

    /** Determines if the give birthday will occur this month.
     * Deteremines if a birthday will occur this month.
     * @param dobString The dob to test (YYYY-MM-DD)
     * @return True if the birthday is this month, or false if not.
     */
    public static boolean thisMonth(String dobString) {
        Calendar dob = getCalendarFromString(dobString);
        Calendar now = Calendar.getInstance();
        if (dob.get(Calendar.MONTH) == now.get(Calendar.MONTH)) return true;
        return false;
    }

    /** 
     * Deteremines the date of this persons next birthday.
     *
     * @param dobString
     * @return The next birthday
     */
    public static String getNextBirthday(String dobString) {
        Calendar dob = getCalendarFromString(dobString);
        Calendar now = Calendar.getInstance();
        dob.set(Calendar.YEAR, now.get(Calendar.YEAR));
        if (!dob.before(now)) {
            StringBuffer sbuffer = new StringBuffer();
            int year = dob.get(Calendar.YEAR);
            int month = dob.get(Calendar.MONTH) + 1;
            int day = dob.get(Calendar.DAY_OF_MONTH);
            sbuffer.append(year);
            sbuffer.append('-');
            if (month < 10) sbuffer.append('0');
            sbuffer.append(month);
            sbuffer.append('-');
            if (day < 10) sbuffer.append('0');
            sbuffer.append(day);
            return sbuffer.toString();
        } else {
            StringBuffer sbuffer = new StringBuffer();
            int year = dob.get(Calendar.YEAR) + 1;
            int month = dob.get(Calendar.MONTH) + 1;
            int day = dob.get(Calendar.DAY_OF_MONTH);
            sbuffer.append(year);
            sbuffer.append('-');
            if (month < 10) sbuffer.append('0');
            sbuffer.append(month);
            sbuffer.append('-');
            if (day < 10) sbuffer.append('0');
            sbuffer.append(day);
            return sbuffer.toString();
        }
    }

    /** Deteremines how many days remain until the persons next birthday.
     * Takes a given date of birth and determines when the next birthday will
     * be, then calculates how many days remain until the persons next birthday.
     * @param dobString The persons date of birth (YYYY-MM-DD)
     * @return Returns how many days are left until this persons birthday.
     */
    public static int getDaysLeft(String dobString) {
        Calendar dob = getCalendarFromString(getNextBirthday(dobString));
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR, dob.get(Calendar.HOUR));
        now.set(Calendar.MINUTE, dob.get(Calendar.MINUTE));
        now.set(Calendar.SECOND, dob.get(Calendar.SECOND));
        long dobSeconds = dob.getTimeInMillis() / MILLISECONDS_IN_SECOND;
        long nowSeconds = now.getTimeInMillis() / MILLISECONDS_IN_SECOND;
        long diff = dobSeconds - nowSeconds;
        return (int) (diff / SECONDS_IN_DAY);
    }

    /** Determines how many seconds are left until this persons next birthday.
     * Calculates the amount of seconds remaining until this persons next birthday.
     * @param dobString The persons date of birth (YYYY-DD-MM)
     * @return The amount of seconds (long) remaining until the next birthday.
     */
    public static long getSecondsLeft(String dobString) {
        Calendar dob = getCalendarFromString(getNextBirthday(dobString));
        Calendar now = Calendar.getInstance();
        long dobSeconds = dob.getTimeInMillis() / MILLISECONDS_IN_SECOND;
        long nowSeconds = now.getTimeInMillis() / MILLISECONDS_IN_SECOND;
        long diff = dobSeconds - nowSeconds;
        return diff;
    }

    /** Returns a human readable string of the amount of days, hours, minutes, and seconds until the persons next birthday.
     * Determines the persons next birthday and then calculates how many days,
     * hours, minutes, and seconds are left until the persons next birthday and
     * returns it in a human readable String.
     * @param dobString The date of birth (YYYY-MM-DD)
     * @return A human readable string, displaying the exact time until your next birthday.
     */
    public static String getETAString(String dobString) {
        StringBuffer sbuffer = new StringBuffer();
        long seconds = getSecondsLeft(dobString);
        long days = seconds / SECONDS_IN_DAY;
        seconds = seconds % SECONDS_IN_DAY;
        long hours = seconds / SECONDS_IN_HOUR;
        seconds = seconds % SECONDS_IN_HOUR;
        long minutes = seconds / SECONDS_IN_MINUTE;
        seconds = seconds % SECONDS_IN_MINUTE;
        if (days > 0) {
            sbuffer.append(days);
            if (days > 1) sbuffer.append(" days,"); else sbuffer.append(" day,");
        }
        if (hours > 0) {
            if (sbuffer.length() > 0) sbuffer.append(" ");
            sbuffer.append(hours);
            if (hours > 1) sbuffer.append(" hours,"); else sbuffer.append(" hour,");
        }
        if (minutes > 0) {
            if (sbuffer.length() > 0) sbuffer.append(" ");
            sbuffer.append(minutes);
            if (minutes > 1) sbuffer.append(" minutes,"); else sbuffer.append(" minute,");
        }
        if (seconds > 0) {
            if (sbuffer.length() > 0) sbuffer.append(" ");
            sbuffer.append(seconds);
            if (seconds > 1) sbuffer.append(" seconds."); else sbuffer.append(" second.");
        }
        char last = sbuffer.charAt(sbuffer.length() - 1);
        if (last == ',') {
            sbuffer.replace(sbuffer.length() - 1, sbuffer.length(), ".");
        } else if (last != '.') sbuffer.append('.');
        return sbuffer.toString();
    }

    /** Get a Calendar object from a date string (YYYY-MM-DD)
     * Processess a date string (YYYY-MM-DD) into a Calendar object and returns
     * to calling method.
     * @param dateString The string to create the Calendar from
     * @return The Calendar object created from the date string given.
     */
    public static Calendar getCalendarFromString(String dateString) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.clear();
        dateCal.set(Integer.parseInt(dateString.substring(0, 4)), Integer.parseInt(dateString.substring(5, 7)) - 1, Integer.parseInt(dateString.substring(8, 10)));
        return dateCal;
    }
}
