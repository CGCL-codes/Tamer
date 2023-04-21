package com.lake.pim.api;

import java.util.*;
import java.text.*;
import java.io.*;

/**
 *  Same static converter methods
 */
public class Converter {

    public static final String[] shortMonthNames;

    public static final String[] longMonthNames;

    public static final String[] longWeekDayNames;

    static {
        DateFormatSymbols dfs = new DateFormatSymbols();
        shortMonthNames = dfs.getShortMonths();
        longMonthNames = dfs.getMonths();
        longWeekDayNames = dfs.getWeekdays();
    }

    private Converter() {
    }

    /**
   *  Convert UTC (ms) to time as string e.g. "23:10"
   *
   * @param  ms  UTC in milliseconds
   * @return     time string e.g. "23:10"
   */
    public static String ms2hm(long ms) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumIntegerDigits(2);
        GregorianCalendar gc = ms2gc(ms);
        String s = formatter.format(gc.get(GregorianCalendar.HOUR_OF_DAY)) + ":" + formatter.format(gc.get(GregorianCalendar.MINUTE));
        return s;
    }

    /**
   *  Convert UTC (ms) to date as string e.g. "12.Dec 2001"
   *
   * @param  ms  UTC in milliseconds
   * @return     date string e.g. "12.Dec 2001"
   */
    public static String ms2dmy(long ms) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumIntegerDigits(2);
        formatter.setGroupingUsed(false);
        GregorianCalendar gc = ms2gc(ms);
        String month_string = Converter.gc2month(gc);
        return formatter.format(gc.get(GregorianCalendar.DAY_OF_MONTH)) + "." + month_string + " " + formatter.format(gc.get(GregorianCalendar.YEAR));
    }

    /**
   *  Convert UTC (ms) to date as string with full month e.g. "12.December 2001"
   *
   * @param  ms  UTC in milliseconds
   * @return     date string e.g. "12.December 2001"
   */
    public static String ms2dmyl(long ms) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumIntegerDigits(2);
        formatter.setGroupingUsed(false);
        GregorianCalendar gc = ms2gc(ms);
        String month_string = Converter.gc2monthl(gc);
        return formatter.format(gc.get(GregorianCalendar.DAY_OF_MONTH)) + ". " + month_string + "  " + formatter.format(gc.get(GregorianCalendar.YEAR));
    }

    /**
   *  Convert UTC (ms) to hour of day
   *
   * @param  ms  UTC in milliseconds
   * @return     hour of day
   */
    public static int ms2hour(long ms) {
        return ms2gc(ms).get(GregorianCalendar.HOUR_OF_DAY);
    }

    /**
   *  Convert UTC (ms) to minute of houre
   *
   * @param  ms  UTC in milliseconds
   * @return     minute of houre
   */
    public static int ms2min(long ms) {
        return ms2gc(ms).get(GregorianCalendar.MINUTE);
    }

    /**
   *  Convert UTC (ms) to day of month
   *
   * @param  ms  UTC in milliseconds
   * @return     day of month
   */
    public static int ms2day(long ms) {
        return ms2gc(ms).get(GregorianCalendar.DAY_OF_MONTH);
    }

    /**
   *  Convert UTC (ms) to month of year
   *
   * @param  ms  UTC in milliseconds
   * @return     month of year
   */
    public static int ms2month(long ms) {
        return ms2gc(ms).get(GregorianCalendar.MONTH);
    }

    /**
   *  Convert UTC (ms) to year
   *
   * @param  ms  UTC in milliseconds
   * @return     year
   */
    public static int ms2year(long ms) {
        return ms2gc(ms).get(GregorianCalendar.YEAR);
    }

    /**
   *  Convert a time e.g 12.Mar 2004 12:35 to 13.Mar 2004 00:00 set time to nearest day boundary
   *
   * @param  ms  UTC in milliseconds
   * @return     UTC in milliseconds (at nearest day boundary)
   */
    public static long ms2msdayboundary(long ms) {
        GregorianCalendar gc = ms2gc(ms + 12 * 60 * 60 * 1000);
        gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
        gc.set(GregorianCalendar.MINUTE, 0);
        gc.set(GregorianCalendar.SECOND, 0);
        gc.set(GregorianCalendar.MILLISECOND, 0);
        return gc.getTime().getTime();
    }

    /**
   *  Convert UTC (ms) to GregorianCalendar-object
   *
   * @param  ms  UTC in milliseconds
   * @return     GregorianCalendar-object
   */
    public static GregorianCalendar ms2gc(long ms) {
        GregorianCalendar gc = new GregorianCalendar();
        Date date = new Date();
        date.setTime(ms);
        gc.setTime(date);
        return gc;
    }

    /**
   *  Convert hour, min, day, month and year to UTC (ms99
   *
   * @param  hour   hour 
   * @param  min    min  
   * @param  day    day  
   * @param  month  month
   * @param  year   year 
   * @return        UTC in milliseconds
   */
    public static long hmdmy2ms(int hour, int min, int day, int month, int year) {
        GregorianCalendar gc = new GregorianCalendar(0, 0, 0, 0, 0);
        gc.set(GregorianCalendar.HOUR_OF_DAY, hour);
        gc.set(GregorianCalendar.MINUTE, min);
        gc.set(GregorianCalendar.DAY_OF_MONTH, day);
        gc.set(GregorianCalendar.MONTH, month);
        gc.set(GregorianCalendar.YEAR, year);
        return gc.getTime().getTime();
    }

    /**
   *  Add to UTC (ms) a number of periods (e.g 5 * four weeks if repeatition is four-weekly).
   *
   * @param  l                  UTC in ms
   * @param  i                  "daily", "weekly", "monthly" or "yearly"
   * @param  counter            period counter (number of periods to add to l)
   * @param  period_multiplier  multiplier of i (e.g "2" could mean "two weekly")
   * @return                    UTC in ms
   */
    public static long UTCplusPeriod2UTC(long l, int i, int counter, int period_multiplier) {
        GregorianCalendar gc = ms2gc(l);
        if (i == PIMEvent.Daily) {
            gc.add(GregorianCalendar.DAY_OF_MONTH, period_multiplier * counter);
        } else if (i == PIMEvent.Weekly) {
            gc.add(GregorianCalendar.DAY_OF_MONTH, 7 * period_multiplier * counter);
        } else if (i == PIMEvent.Monthly) {
            gc.add(GregorianCalendar.MONTH, period_multiplier * counter);
        } else if (i == PIMEvent.Yearly) {
            gc.add(GregorianCalendar.YEAR, period_multiplier * counter);
        } else {
            gc.add(GregorianCalendar.YEAR, 3000 * counter);
        }
        return gc.getTime().getTime();
    }

    /**
   *  Convert GregorianCalendar object to month as String (e.g. "Feb").
   *
   * @param  g  GregorianCalendar object
   * @return    month as String (e.g. "Feb")
   */
    public static String gc2month(GregorianCalendar g) {
        int monthIndex = g.get(GregorianCalendar.MONTH);
        return shortMonthNames[monthIndex];
    }

    /**
   *  Convert GregorianCalendar object to month as String (e.g. "February").
   *
   * @param  g  GregorianCalendar object
   * @return    month as long String (e.g. "February")
   */
    public static String gc2monthl(GregorianCalendar g) {
        int monthIndex = g.get(GregorianCalendar.MONTH);
        return longMonthNames[monthIndex];
    }

    /**
   *  Gets the dayOfWeekWString attribute of the CalendarRenderer object
   *
   * @param  g  gregorian calendar object
   * @return    The dayOfWeekWString value
   */
    public static String getDayOfWeekWString(GregorianCalendar g) {
        int day = g.get(GregorianCalendar.DAY_OF_WEEK);
        return longWeekDayNames[day];
    }

    /**
   *  Convert period * multiplier to duration in milliseconds.
   *
   * @param  period             "daily", "weekly", "monthly" or "yearly"
   * @param  period_multiplier  multiplier of period (e.g "2" could mean "two weekly")
   * @return                    duration in milliseconds
   */
    public static long period2ms(int period, int period_multiplier) {
        if (period == PIMEvent.Daily) {
            return (long) 24 * 60 * 60 * 1000 * period_multiplier;
        } else if (period == PIMEvent.Weekly) {
            return (long) 7 * 24 * 60 * 60 * 1000 * period_multiplier;
        } else if (period == PIMEvent.Monthly) {
            return (long) 31 * 24 * 60 * 60 * 1000 * period_multiplier;
        } else if (period == PIMEvent.Yearly) {
            return (long) 366 * 24 * 60 * 60 * 1000 * period_multiplier;
        } else {
            return (long) 3000 * 365 * 24 * 60 * 60 * 1000;
        }
    }

    /**
   *  Convert UTC-string to UTC in milliseconds
   *
   * @param  s   string describing the UTC, e.g.:<br>
   *                "20020905T104500Z" stands for <br>
   *                year=2002 month=09 day=05 hour=10 minute=45 sec=00, <br>
   *                Z means UTC, without Z means local time
   *
   * @return    UTC (ms) or 0 in case of error
   */
    public static Long dtstart2UTC(String s) {
        GregorianCalendar gc = new GregorianCalendar(0, 0, 0, 0, 0);
        if (s.indexOf("Z") != -1) {
            gc.setTimeZone(new SimpleTimeZone(0, "UTC"));
        }
        try {
            gc.set(GregorianCalendar.YEAR, Integer.parseInt(s.substring(0, 4)));
            gc.set(GregorianCalendar.MONTH, Integer.parseInt(s.substring(4, 6)) - 1);
            gc.set(GregorianCalendar.DAY_OF_MONTH, Integer.parseInt(s.substring(6, 8)));
            gc.set(GregorianCalendar.HOUR_OF_DAY, Integer.parseInt(s.substring(9, 11)));
            gc.set(GregorianCalendar.MINUTE, Integer.parseInt(s.substring(11, 13)));
            gc.set(GregorianCalendar.SECOND, Integer.parseInt(s.substring(13, 15)));
            return new Long(gc.getTime().getTime());
        } catch (Exception b) {
            return null;
        }
    }

    /**
   *  Convert an unicode string only containing characters < 128 and coded as quoted-printable or not
   *  using the given encoding to an unicode string.<br>
   *  If given encoding is unknown "US-ASCII" is taken for default,<br>
   *  e.g. "=FC" is set to a german sign for "�" if encoding is "ISO-8859-15"
   *
   * @param  s                  unicode string only containing characters < 128
   * @param  encoding           encoding e.g. "ISO-8859-15"
   * @param  isQuotedPrintable  
   * @return                    unicode string
   */
    public static String byte2unicode(String s, String encoding, boolean isQuotedPrintable) {
        StringBuffer input = new StringBuffer(s);
        byte[] charArray = new byte[s.length()];
        StringBuffer output = new StringBuffer(s.length());
        int k = 0;
        for (int i = 0; i < input.length(); i++) {
            if ((input.charAt(i) == '=') && isQuotedPrintable) {
                if (i + 2 < input.length()) {
                    charArray[k] = (byte) Integer.parseInt(input.substring(i + 1, i + 3), 16);
                    i = i + 2;
                }
            } else {
                charArray[k] = (byte) input.charAt(i);
            }
            k++;
        }
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(charArray);
            InputStreamReader isr = new InputStreamReader(bais, encoding);
            int c = isr.read();
            while (c != -1) {
                output.append((char) c);
                c = isr.read();
            }
            isr.close();
        } catch (Exception e) {
            if (e.toString().indexOf("UnsupportedEncoding") != 0) {
                return Converter.byte2unicode(s, "US-ASCII", isQuotedPrintable);
            } else {
                e.printStackTrace();
            }
        }
        return ("x" + output.toString()).trim().substring(1);
    }

    /**
   *  Convert an unicode string into another unicode string BUT
   *  the output string only contains US-ASCII characters and
   *  all characters in the origin string that are not present in
   *  US-ASCII-coding are translated to US-ASCII according to the rules
   *  of quoted-printable e.g. german "�" -> "=FC"
   *
   * @param  s         string (unicode)
   * @param  encoding  encoding e.g. "ISO-8859-15"
   * @return           quoted-printable unicode string only containing characters < 128
   */
    public static String unicode2quodedPrintable(String s, String encoding) {
        StringBuffer output = new StringBuffer(80);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            OutputStreamWriter osw = new OutputStreamWriter(baos, encoding);
            osw.write(s, 0, s.length());
            osw.close();
        } catch (Exception e) {
            if (e.toString().indexOf("UnsupportedEncoding") != 0) {
                return unicode2quodedPrintable(s, "US-ASCII");
            } else {
                e.printStackTrace();
            }
        }
        byte[] input = baos.toByteArray();
        output.append("=\r\n");
        int k = 65;
        int o = 1;
        for (int i = 0; i < input.length; i++) {
            if (input[i] < 0 || input[i] == 0x3d) {
                int ch = (int) input[i];
                if (input[i] < 0) {
                    ch = ch + 256;
                }
                output.append("=" + (Integer.toHexString(ch)).toUpperCase());
                o = o + 3;
            } else if (input[i] == 13 || input[i] == 10) {
                output.append("=0D=0A");
                byte ch2;
                if (i + 1 < input.length) {
                    ch2 = input[i + 1];
                } else {
                    ch2 = 0x20;
                }
                if (ch2 == 13 || ch2 == 10) {
                    i++;
                }
                o = o + 6;
            } else {
                output.append((char) input[i]);
                o++;
            }
            if (o > k - 1) {
                output.append("=\r\n");
                k = k + 65;
            }
        }
        return output.toString();
    }

    /**
   *  Get the encodingfromLine attribute.<br>
   *  Searches in line for "CHARSET="charset";|:" and extracts the<br>
   *  charset string or if not found returns the default "".
   *
   * @param  line  string containing a line
   * @return       The encodingfromLine value
   */
    public static String getEncodingfromLine(String line) {
        String encoding = "";
        int pos = line.toUpperCase().indexOf("CHARSET=");
        if (pos < line.indexOf(":") && pos > 0) {
            int pos_semi = line.indexOf(";", pos + 8);
            int pos_dp = line.indexOf(":", pos + 8);
            if (pos_semi == -1) {
                pos_semi = 100000;
            }
            if (pos_dp == -1) {
                pos_dp = 100000;
            }
            encoding = line.substring(pos + 8, Math.min(pos_semi, pos_dp)).trim();
        } else {
            encoding = "";
        }
        return encoding;
    }

    /**
   *  Convert a string e.g. "blabla123 blabla" to the integer 123.<br>
   *  Number must be terminated by a blank. If there is no number starting at position pos the 1 is returned
   *
   * @param  s    string containg a number e.g. "blabla123 blabla"
   * @param  pos  position in s where searching for a number starts
   * @return      first found number as integer or 1 if nothing found
   */
    public static int stringNumberBehindPos2int(String s, int pos) {
        try {
            return Integer.parseInt(s.substring(pos, s.indexOf(' ', pos)));
        } catch (Exception b) {
            return 1;
        }
    }
}
