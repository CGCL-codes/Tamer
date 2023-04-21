package david.util;

import java.io.*;
import java.util.*;

/**
 * <P>
 * Common routines to perform common tasks.
 */
public class Common implements Serializable {

    protected static Common instance = null;

    /**
	 * Private no-args constructor. This class should be used as a singleton.
	 * Get the VM-wide instance with Common.getInstance().
	 */
    protected Common() {
    }

    /**
	 * Get the singleton instance of Common.
	 */
    public static Common getInstance() {
        if (instance == null) instance = new Common();
        return instance;
    }

    /**
	 * Returns a java.sql.Date object initialized with the input string.
	 * 
	 * @param String dateWithOptionalTime
	 * @return java.sql.Date
	 */
    public java.sql.Date getDateObject(String dateWithOptionalTime) {
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;
        String date = null;
        String time = null;
        try {
            StringTokenizer st = new StringTokenizer(dateWithOptionalTime);
            date = st.nextToken();
            if (st.hasMoreTokens()) time = st.nextToken(); else time = "0:0:0";
            date = normalizeDateString(date, "/");
            st = new StringTokenizer(date, "/ :", false);
            if (!st.hasMoreTokens()) return null; else month = new Integer(st.nextToken()).intValue() - 1;
            if (!st.hasMoreTokens()) return null; else day = new Integer(st.nextToken()).intValue();
            if (!st.hasMoreTokens()) return null; else year = new Integer(st.nextToken()).intValue();
            st = new StringTokenizer(time, ":", false);
            if (!st.hasMoreTokens()) return null; else hour = new Integer(st.nextToken()).intValue();
            if (!st.hasMoreTokens()) return null; else minute = new Integer(st.nextToken()).intValue();
            if (!st.hasMoreTokens()) second = 0; else second = new Integer(st.nextToken()).intValue();
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day, hour, minute, second);
            return new java.sql.Date(((cal.getTime().getTime()) / 1000) * 1000);
        } catch (Exception e) {
            return null;
        }
    }

    /**
	 * Calculates and returns the difference between two Date objects, as an
	 * English String.
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
    public String timeDifference(Date date1, Date date2) {
        if (date1 == null || date2 == null) return null;
        long t1 = date1.getTime();
        long t2 = date2.getTime();
        long diff = Math.max(t1, t2) - Math.min(t1, t2);
        int years = (int) (diff / 31557600000l);
        diff -= years * 31557600000l;
        int months = (int) (diff / 2629800000l);
        diff -= months * 2629800000l;
        int days = (int) (diff / 86400000l);
        diff -= days * 86400000l;
        int hours = (int) (diff / 3600000l);
        diff -= hours * 3600000l;
        int minutes = (int) (diff / 60000l);
        diff -= minutes * 60000l;
        int seconds = (int) (diff / 1000l);
        StringBuffer buf = new StringBuffer();
        if (years > 0) buf.append(years).append(years == 1 ? "year, " : " years, ");
        if (years > 0 || months > 0) buf.append(months).append(months == 1 ? " month, " : " months, ");
        if (years > 0 || months > 0 || days > 0) buf.append(days).append(days == 1 ? " day, " : " days, ");
        if (years > 0 || months > 0 || days > 0 || hours > 0) buf.append(hours).append(hours == 1 ? " hour, " : " hours, ");
        if (buf.length() > 0 && minutes > 0 && seconds == 0) buf.append(" and ");
        if (years > 0 || months > 0 || days > 0 || hours > 0 || minutes > 0) buf.append(minutes).append(minutes == 1 ? " minute " : " minutes ");
        if (buf.length() > 0 && seconds > 0) buf.append(", and ");
        if (seconds > 0 || buf.length() == 0) buf.append(seconds).append(seconds == 1 ? " second" : " seconds");
        return buf.toString();
    }

    /**
	 * Returns the text of a web page, or the text of any exception that occurs.
	 * 
	 * @param String url - The string representation of the page to get
	 * @return java.lang.String
	 */
    public String getPage(String url) {
        try {
            return getPage(new java.net.URL(url));
        } catch (Exception e) {
            return e.toString();
        }
    }

    /**
	 * Returns the text of a web page, or an error message.
	 * 
	 * @param java.net.URL url - A URL object pointing to the desired page.
	 * @return java.lang.String
	 */
    public String getPage(java.net.URL url) {
        StringBuffer html = new StringBuffer();
        String cr = "\n";
        try {
            String line = null;
            java.io.BufferedReader dis = new java.io.BufferedReader(new java.io.InputStreamReader(url.openConnection().getInputStream()));
            while ((line = dis.readLine()) != null) {
                html.append(line);
                html.append(cr);
            }
        } catch (Exception e) {
            html.append("An error occurred:\n" + e.toString());
        }
        return html.toString();
    }

    /**
	 * Tests a String to see if it can be converted to an int.
	 * 
	 * @param possibleNumber String representation of a number, or not.
	 * @return boolean
	 */
    public final boolean isANumber(String possibleNumber) {
        boolean isNumeric = false;
        try {
            Integer.valueOf(possibleNumber, 10);
            isNumeric = true;
        } catch (NumberFormatException e) {
        }
        return isNumeric;
    }

    /**
	 * Reformats a string representation of a date into the normalized form
	 * <i>mm/dd/yyyy</i>.
	 * 
	 * @param dateString String representation of a date.
	 * @exception Exception if it cannot be converted.
	 * @return java.lang.String
	 */
    public final String normalizeDateString(String dateString) throws Exception {
        return normalizeDateString(dateString, "/");
    }

    /**
	 * Reformats a string representation of a date into the normalized form
	 * <i>mm/dd/yyyy</i>, where "/" is any desired separator.
	 * 
	 * @param dateString String representation of a date.
	 * @param separator String thing to put between the month, day, and year.
	 * @exception Exception if it cannot be converted.
	 * @return java.lang.String
	 */
    public final String normalizeDateString(String dateString, String separator) throws Exception {
        Hashtable months = new Hashtable();
        months.put("Jan", "1");
        months.put("Feb", "2");
        months.put("Mar", "3");
        months.put("Apr", "4");
        months.put("May", "5");
        months.put("Jun", "6");
        months.put("Jul", "7");
        months.put("Aug", "8");
        months.put("Sep", "9");
        months.put("Oct", "10");
        months.put("Nov", "11");
        months.put("Dec", "12");
        String bad = "Invalid date string \"" + dateString + "\"";
        dateString = sanctify(dateString);
        StringTokenizer st = new StringTokenizer(dateString, "/-. ", false);
        String month = st.nextToken();
        String day = null;
        String year = null;
        String time = null;
        if (isANumber(month)) {
            day = st.nextToken();
            year = st.nextToken();
            if (st.hasMoreTokens()) time = st.nextToken("X");
        } else {
            month = st.nextToken();
            month = (String) months.get(month);
            if (month == null) throw new Exception(bad);
            day = st.nextToken();
            time = st.nextToken();
            st.nextToken();
            year = st.nextToken();
        }
        int m, d, y = 0;
        try {
            m = Integer.valueOf(month).intValue();
            d = Integer.valueOf(day).intValue();
            y = Integer.valueOf(year).intValue();
        } catch (Exception e) {
            throw new Exception(bad);
        }
        if (m > 12) {
            String temp = month;
            int tmp = m;
            month = day;
            m = d;
            day = year;
            d = y;
            year = temp;
            y = tmp;
        }
        if (y < 100) {
            if (y > 50) y += 1900; else y += 2000;
            year = "" + y;
        }
        if (m > 12 || m < 1 || d > 31 || d < 1) throw new Exception(bad);
        return padString('0', 2, month) + separator + padString('0', 2, day) + separator + year + (time == null ? "" : " " + time);
    }

    /**
	 * Returns an Enumeration containing all occurrances of the characters
	 * within string <b>original</b> that are bounded by <b>leftDelimiter</b>
	 * and <b>rightDelimiter</b>. The left and right delimiters can be included
	 * in the occurrances if desired.
	 * 
	 * @param original String The orignal string to be parsed.
	 * @param leftDelimiter String The string which marks the beginning of the
	 * text of interest.
	 * @param includeLeftDelimiter boolean true if the left delimiter is to be
	 * included in the results.
	 * @param rightDelimiter String The string which marks the end of the text
	 * of interest.
	 * @param includeRightDelimiter boolean true if the right delimiter is to be
	 * included in the results.
	 * @return java.util.Enumeration
	 */
    public static final Enumeration extract(String original, String leftDelimiter, boolean includeLeftDelimiter, String rightDelimiter, boolean includeRightDelimiter) {
        Vector v = new Vector();
        if (original != null && leftDelimiter != null && rightDelimiter != null) {
            int left = includeLeftDelimiter ? 0 : leftDelimiter.length();
            int right = includeRightDelimiter ? rightDelimiter.length() : 0;
            int leftLocation = 0, rightLocation = 0;
            while ((leftLocation = original.indexOf(leftDelimiter)) >= 0 && (rightLocation = original.indexOf(rightDelimiter, leftLocation + 1)) >= 0) {
                v.addElement(original.substring(leftLocation + left, rightLocation + right));
                original = original.substring(rightLocation + rightDelimiter.length());
            }
        }
        return v.elements();
    }

    /**
	 * Reformats a string representation of a time into the normalized form
	 * <i>[h]h:mm[:ss] [a]</i>, where "h" is hours, "m" is minutes, "s" is
	 * seconds, and "a" is either "AM" or "PM". If military time is selected,
	 * the hours will include a leading zero if greater than 9 and the meridian
	 * indicator will not appear.
	 * 
	 * @param timeString String time-of-day in String form.
	 * @param useMilitaryTime boolean true if 24-hour time format is desired.
	 * @param showSeconds boolean true if second-level resolution is desired.
	 * @exception Exception if it cannot be converted.
	 * @return java.lang.String
	 */
    public final String normalizeTimeString(String timeString, boolean useMilitaryTime, boolean showSeconds) throws Exception {
        String bad = "Invalid time string \"" + timeString + "\"";
        timeString = sanctify(timeString);
        if (timeString.indexOf(".") >= 0) timeString = substitute(timeString, (String) extract(timeString + " ", ".", true, " ", false).nextElement(), "");
        try {
            StringTokenizer st = new StringTokenizer(timeString, ": ", false);
            String hours = st.nextToken();
            String minutes = st.nextToken();
            String seconds = (st.hasMoreTokens() ? st.nextToken().toUpperCase() : null);
            String meridian = (st.hasMoreTokens() ? st.nextToken().toUpperCase() : null);
            if (!isANumber(hours)) throw new Exception();
            if (!isANumber(minutes)) throw new Exception();
            if (seconds != null) if (!isANumber(seconds)) if (seconds.startsWith("A") || seconds.startsWith("P")) {
                meridian = seconds;
                seconds = null;
            } else throw new Exception();
            if (meridian != null && !meridian.startsWith("A") && !meridian.startsWith("P")) throw new Exception();
            int hour = Integer.parseInt(hours);
            if (hour > 12 && meridian != null) throw new Exception();
            if (hour == 0 && meridian != null) throw new Exception();
            int minute = Integer.parseInt(minutes);
            int second = 0;
            if (seconds != null) second = Integer.parseInt(seconds);
            if (meridian != null) if (meridian.startsWith("A")) meridian = "AM"; else meridian = "PM";
            if (!useMilitaryTime) if (hour > 12) {
                hour -= 12;
                meridian = "PM";
            } else if (hour == 0) {
                hour = 12;
                meridian = "AM";
            } else if (hour == 12) meridian = "PM"; else meridian = "AM"; else {
                if (meridian != null && meridian.startsWith("P") && hour < 12) hour += 12;
                meridian = "";
            }
            if (hour < 0 || hour > 23 || minute < 0 || minute > 59) throw new Exception();
            if (hour < 0 || hour > 23 || minute < 0 || minute > 59) throw new Exception();
            if (seconds != null && (second < 0 || second > 59)) throw new Exception();
            if (!showSeconds && second > 29) minute++;
            return sanctify((useMilitaryTime ? padString('0', 2, hour) : "" + hour) + ":" + padString('0', 2, minute) + (showSeconds ? ":" + padString('0', 2, second) : "") + " " + meridian);
        } catch (Exception e) {
            throw new Exception(bad);
        }
    }

    /**
	 * Pad a String to a particular size with a particular character.
	 * 
	 * @return java.lang.String that is of length <i>length</i> padded with
	 * <i>char</i> on the left.
	 * @param pad char
	 * @param length int
	 * @param text java.lang.String
	 */
    public static final String padString(char pad, int length, int intAsText) {
        return padString(pad, length, intAsText + "", false);
    }

    /**
	 * Pad a String to a particular size with a particular character.
	 * 
	 * @return java.lang.String that is of length <i>length</i> padded with
	 * <i>char</i> on the left.
	 * @param pad char
	 * @param length int
	 * @param text java.lang.String
	 */
    public static final String padString(char pad, int length, String text) {
        return padString(pad, length, text, false);
    }

    /**
	 * Pad a String to a particular size with a particular character.
	 * 
	 * @return java.lang.String that is of length <i>length</i> padded with
	 * <i>char</i>
	 * @param pad char
	 * @param length int
	 * @param text java.lang.String
	 */
    public static final String padString(char pad, int length, String text, boolean padOnRight) {
        StringBuffer temp = new StringBuffer(length + text.length());
        if (padOnRight) temp.append(text);
        for (int i = 0; i++ < length; temp.append(pad)) ;
        if (!padOnRight) temp.append(text);
        return temp.toString().substring(padOnRight ? 0 : text.length(), padOnRight ? length : length + text.length());
    }

    /**
	 * Takes a string, trims it, and returns it. Returned string is guaranteed
	 * to not be null or have any whitespace on either end.
	 * 
	 * @return java.lang.String
	 * @param text java.lang.String
	 */
    public final String sanctify(String text) {
        return sanctify(text, false);
    }

    /**
	 * Takes a string, trims it, and returns it, or returns
	 * <i>defaultValueIfNull</i> if null. Returned string is guaranteed to not
	 * be null or have any whitespace on either end.
	 * 
	 * @return java.lang.String
	 * @param text java.lang.String
	 */
    public final String sanctify(String text, String defaultValueIfNull) {
        if (defaultValueIfNull == null) defaultValueIfNull = "";
        return text == null ? defaultValueIfNull : sanctify(text, false);
    }

    /**
	 * Takes a string, purifies it, and returns it. Returned string is
	 * guaranteed to not be null or have any whitespace on either end.
	 * 
	 * @return java.lang.String
	 * @param text java.lang.String
	 */
    public final String sanctify(String text, boolean removeInternalWhitespace) {
        if (text == null) text = ""; else {
            text = text.trim();
            if (removeInternalWhitespace) {
                Hashtable parms = new Hashtable();
                parms.put("\t", " ");
                parms.put("  ", " ");
                parms.put(" \n", "\n");
                parms.put("\n ", "\n");
                parms.put("\n\n", "\n");
                text = substituteMany(text, parms);
            }
        }
        return text;
    }

    /**
	 * Replaces all occurrances of a string <b>key</b> within another string
	 * <b>original</b> with a third string <b>text</b>. <br>
	 * Not case sensitive. <B>text</B> may be null.
	 * 
	 * @return java.lang.String
	 */
    public final String substitute(String original, String key, String text) {
        if (original != null && key != null) {
            text = (text == null ? "" : text);
            String uOriginal = original.toUpperCase();
            StringBuffer modified = new StringBuffer();
            key = key.toUpperCase();
            int adjustment = key.length();
            int keyLocation;
            int startLocation = 0;
            while ((keyLocation = uOriginal.indexOf(key, startLocation)) >= 0) {
                modified.append(original.substring(startLocation, keyLocation));
                modified.append(text);
                startLocation = keyLocation + adjustment;
            }
            original = modified.toString() + original.substring(startLocation);
        }
        return original;
    }

    /**
	 * Replaces all occurrances of each value of <b>keys</b> from the parms
	 * hashtable within the string <b>original</b> with that key's value. <br>
	 * Not case sensitive. A key's value may be null.
	 * 
	 * @return java.lang.String
	 */
    public final String substituteMany(String original, Hashtable parms) {
        Enumeration keys = parms.keys();
        String key = null;
        while (keys.hasMoreElements()) {
            key = (String) keys.nextElement();
            original = substitute(original, key, (String) parms.get(key));
        }
        return original;
    }

    /**
	 * Reads a file. 100% Java compliant (no system calls)
	 * 
	 * @param String sourceFileName
	 */
    public byte[] readFile(String sourceFileName) throws java.io.IOException {
        java.io.InputStream in = null;
        byte[] buf = null;
        try {
            in = new java.io.FileInputStream(sourceFileName);
        } catch (FileNotFoundException e) {
            in = getClass().getResourceAsStream("/" + sourceFileName);
        }
        try {
            StringBuffer sb = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            buf = sb.toString().getBytes();
        } finally {
            in.close();
        }
        return buf;
    }

    /**
	 * Writes a file.  100% Java compliant (no system calls)
	 * 
	 * @param String fileName
	 * @param byte[] contents
	 * @param boolean append
	 */
    public void writeFile(String fileName, byte[] contents, boolean append) throws java.io.IOException {
        java.io.FileOutputStream out = null;
        try {
            out = new java.io.FileOutputStream(fileName, append);
            out.write(contents);
        } finally {
            out.close();
        }
    }

    /**
	 * Returns a String containing all of the text of the input string except
	 * those between "<" and ">".
	 * 
	 * @return java.lang.String
	 */
    public final String stripHtml(String original) {
        if (original != null) {
            String ucOriginal = original.toUpperCase();
            for (Enumeration js = extract(ucOriginal, "<SCRIPT", true, "</SCRIPT>", true); js.hasMoreElements(); ) {
                String s = (String) js.nextElement();
                int i = ucOriginal.indexOf(s);
                original = original.substring(0, i) + original.substring(i + s.length());
                ucOriginal = original.toUpperCase();
            }
            for (Enumeration sty = extract(ucOriginal, "<STYLE", true, "</STYLE>", true); sty.hasMoreElements(); ) {
                String s = (String) sty.nextElement();
                int i = ucOriginal.indexOf(s);
                original = original.substring(0, i) + original.substring(i + s.length());
                ucOriginal = original.toUpperCase();
            }
            String testSegment = "";
            do {
                int leftLocation = original.indexOf("<");
                if (leftLocation < 0) break;
                testSegment = original.substring(leftLocation, original.indexOf(">", leftLocation) + 1);
                int leftPointer = 0;
                do {
                    if (testSegment.indexOf("<", leftPointer + 1) >= 0) {
                        leftPointer = testSegment.indexOf("<", leftPointer + 1);
                        testSegment = original.substring(leftLocation, original.indexOf(">", leftLocation + testSegment.length()) + 1);
                    }
                } while (testSegment.indexOf("<", leftPointer + 1) >= 0);
                original = substitute(original, testSegment, "");
            } while (original.indexOf("<") >= 0 && original.indexOf(">") >= 0);
        } else original = "";
        while (original.indexOf(" \n") >= 0) original = substitute(original, " \n", "\n");
        while (original.indexOf("\n\n") >= 0) original = substitute(original, "\n\n", "\n");
        while (original.indexOf("  ") >= 0) original = substitute(original, "  ", " ");
        while (original.indexOf("\n ") >= 0) original = substitute(original, "\n ", "\n");
        original = substitute(original, "&amp;", "&");
        original = substitute(original, "&lt;", "<");
        original = substitute(original, "&gt;", ">");
        original = substitute(original, "&nbsp;", " ");
        original = substitute(original, "&ndash;", "-");
        original = substitute(original, "&mdash;", "-");
        original = substitute(original, "&lsquo;", "`");
        original = substitute(original, "&rsquo;", "'");
        original = substitute(original, "&sbquo;", "'");
        original = substitute(original, "&ldquo;", "\"");
        original = substitute(original, "&rdquo;", "\"");
        original = substitute(original, "&bdquo;", "\"");
        original = substitute(original, "&lsaquo;", "<");
        original = substitute(original, "&rsaquo;", ">");
        original = substitute(original, "&hellip;", "...");
        original = substitute(original, "&prime;", "'");
        original = substitute(original, "&Prime;", "\"");
        original = substitute(original, "&frasl;", "/");
        return sanctify(original, true);
    }
}
