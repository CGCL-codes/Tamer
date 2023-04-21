package com.funambol.custom.scalix.rest;

import java.util.TimeZone;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

/**
 * Convert iCalender format to vCalendar.
 *
 * @author $Author: smaffulli $
 * $Id: ICalToVCal.java,v 1.1.1.1 2008/03/12 10:08:59 smaffulli Exp $
 */
public class ICalToVCal {

    public ICalToVCal() {
    }

    public String convert(String ical) {
        resetFix(ical);
        String line = nextLine();
        while (line != null) {
            convertLine(line);
            line = nextLine();
        }
        return catLines();
    }

    public void convertLine(String line) {
        if (line.equals("BEGIN:VTIMEZONE")) {
            fixTimezone(line);
            return;
        }
        if (line.startsWith("DTSTART") || line.startsWith("DTEND") || line.startsWith("DTSTAMP")) {
            fixTime(line);
            return;
        }
        if (line.startsWith("RRULE")) {
            fixRRule(line);
            return;
        }
        if (line.startsWith("ORGANIZER")) {
            return;
        }
        addLine(line);
    }

    StringBuffer m_ical = new StringBuffer();

    private String m_lines[];

    private int m_index;

    private void resetFix(String ical) {
        String fix = ical.replace("\r\n ", "");
        int cr_index = ical.indexOf("\r");
        if (cr_index < 0) m_lines = fix.split("\n"); else m_lines = fix.split("\r\n");
        m_index = 0;
        m_ical = new StringBuffer();
        m_timezones = new HashMap();
        m_all_day_start = null;
    }

    private String nextLine() {
        String line = null;
        if (m_index == m_lines.length) line = null; else line = m_lines[m_index++];
        return line;
    }

    private void addLine(String line) {
        if (line == null) return;
        if (line.length() == 0) return;
        if (line.trim().length() == 0) return;
        m_ical.append(line + "\r\n");
    }

    private String catLines() {
        return m_ical.toString();
    }

    HashMap m_timezones = null;

    private TimeZone lookupTimezone(String id) {
        if (m_timezones == null) return null;
        return (TimeZone) m_timezones.get(id);
    }

    private void fixTimezone(String start) {
        String tzid_line = nextLine();
        String tzid = tzid_line.substring("TZID:".length());
        String line = nextLine();
        while (!line.equals("END:VTIMEZONE")) {
            if (line.equals("BEGIN:STANDARD")) readStandardType();
            if (line.equals("BEGIN:DAYLIGHT")) readDaylightType();
            line = nextLine();
        }
        TimeZone tz = new SimpleTimeZone(rawOffset, tzid, startMonth, startDay, startDayOfWeek, startTime, endMonth, endDay, endDayOfWeek, endTime, dstSavings);
        m_timezones.put(tzid, tz);
    }

    int rawOffset;

    int startMonth;

    int startDay;

    int startDayOfWeek;

    int startTime;

    int endMonth;

    int endDay;

    int endDayOfWeek;

    int endTime;

    int dstSavings;

    private void readStandardType() {
        HashMap info = readTimezoneType("END:STANDARD");
        String start = (String) info.get("DTSTART");
        String time = start.substring(start.indexOf("T") + 1);
        int start_hour = Integer.parseInt(time.substring(0, 2));
        int start_min = Integer.parseInt(time.substring(2, 4));
        int start_sec = Integer.parseInt(time.substring(4));
        startTime = (((((start_hour * 60) + start_min) * 60) + start_sec) * 1000);
        String offset_from = (String) info.get("TZOFFSETFROM");
        String offset_to = (String) info.get("TZOFFSETTO");
        int offset_sign = 1;
        String offset_time = ("" + offset_to);
        if (offset_time.startsWith("-")) {
            offset_sign = -1;
            offset_time = offset_time.substring(1);
        }
        if (offset_time.length() == 3) offset_time = ("0" + offset_time);
        int offset_hour = Integer.parseInt(offset_time.substring(0, 2));
        int offset_min = Integer.parseInt(offset_time.substring(2));
        rawOffset = (offset_sign * (((offset_hour * 60) + offset_min) * 60 * 1000));
        String rrule = (String) info.get("RRULE");
        HashMap rule = parseRRule(rrule);
        int bymonth = Integer.parseInt((String) rule.get("BYMONTH"));
        endMonth = getCalendarMonth(bymonth);
        String byday = (String) rule.get("BYDAY");
        int byday_sign = 1;
        if (byday.startsWith("-")) {
            byday_sign = -1;
            byday = byday.substring(1);
        }
        StringBuffer day_digit_buf = new StringBuffer();
        for (int i = 0; i < byday.length(); i++) {
            char c = byday.charAt(i);
            switch(c) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    day_digit_buf.append(c);
                    break;
                default:
                    i = byday.length();
            }
        }
        int byday_digit = (byday_sign * Integer.parseInt(day_digit_buf.toString()));
        endDay = byday_digit;
        byday = byday.substring(day_digit_buf.toString().length());
        endDayOfWeek = getCalendarDay(byday);
    }

    private void readDaylightType() {
        HashMap info = readTimezoneType("END:DAYLIGHT");
        String start = (String) info.get("DTSTART");
        String time = start.substring(start.indexOf("T") + 1);
        int start_hour = Integer.parseInt(time.substring(0, 2));
        int start_min = Integer.parseInt(time.substring(2, 4));
        int start_sec = Integer.parseInt(time.substring(4));
        endTime = (((((start_hour * 60) + start_min) * 60) + start_sec) * 1000);
        int offset_from = Integer.parseInt((String) info.get("TZOFFSETFROM"));
        int offset_to = Integer.parseInt((String) info.get("TZOFFSETTO"));
        int offset_sign = 1;
        String offset_time = ("" + (offset_to - offset_from));
        if (offset_time.startsWith("-")) {
            offset_sign = -1;
            offset_time = offset_time.substring(1);
        }
        if (offset_time.length() == 3) offset_time = ("0" + offset_time);
        int offset_hour = Integer.parseInt(offset_time.substring(0, 2));
        int offset_min = Integer.parseInt(offset_time.substring(2));
        dstSavings = (offset_sign * (((offset_hour * 60) + offset_min) * 60 * 1000));
        String rrule = (String) info.get("RRULE");
        HashMap rule = parseRRule(rrule);
        int bymonth = Integer.parseInt((String) rule.get("BYMONTH"));
        startMonth = getCalendarMonth(bymonth);
        String byday = (String) rule.get("BYDAY");
        int byday_sign = 1;
        if (byday.startsWith("-")) {
            byday_sign = -1;
            byday = byday.substring(1);
        }
        StringBuffer day_digit_buf = new StringBuffer();
        for (int i = 0; i < byday.length(); i++) {
            char c = byday.charAt(i);
            switch(c) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    day_digit_buf.append(c);
                    break;
                default:
                    i = byday.length();
            }
        }
        int byday_digit = (byday_sign * Integer.parseInt(day_digit_buf.toString()));
        startDay = byday_digit;
        byday = byday.substring(day_digit_buf.toString().length());
        startDayOfWeek = getCalendarDay(byday);
    }

    public int getCalendarMonth(int month) {
        int months[] = { Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY, Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER, Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER };
        return months[month];
    }

    public int getCalendarDay(String day) {
        if (day.equals("SU")) return Calendar.SUNDAY;
        if (day.equals("MO")) return Calendar.MONDAY;
        if (day.equals("TU")) return Calendar.TUESDAY;
        if (day.equals("WE")) return Calendar.WEDNESDAY;
        if (day.equals("TH")) return Calendar.THURSDAY;
        if (day.equals("FR")) return Calendar.FRIDAY;
        if (day.equals("SA")) return Calendar.SATURDAY;
        return Calendar.SATURDAY;
    }

    private String localToUTC(String sDate, TimeZone timezone) throws Exception {
        if (sDate == null || sDate.equals("")) {
            return sDate;
        }
        String utcPattern = "yyyyMMdd'T'HHmmss'Z'";
        DateFormat utcFormatter = new SimpleDateFormat(utcPattern);
        utcFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String noUtcPattern = "yyyyMMdd'T'HHmmss";
        DateFormat noUtcFormatter = new SimpleDateFormat(noUtcPattern);
        if (sDate.indexOf('Z') != -1) {
            return sDate;
        }
        Date date;
        if (timezone != null) {
            noUtcFormatter.setTimeZone(timezone);
        }
        date = noUtcFormatter.parse(sDate);
        return utcFormatter.format(date);
    }

    private HashMap readTimezoneType(String end) {
        HashMap map = new HashMap();
        String line = nextLine();
        while (!line.equals(end)) {
            String parts[] = line.split(":");
            String name = parts[0];
            String value = parts[1];
            map.put(name, value);
            line = nextLine();
        }
        return map;
    }

    String m_all_day_start = null;

    private void fixTime(String line) {
        String parts[] = splitTime(line);
        String name_parts[] = splitTime(parts[0], ';');
        String value = parts[1];
        String name = name_parts[0];
        TimeZone tz = null;
        for (int i = 1; i < name_parts.length; i++) {
            String part = name_parts[i];
            if (part.startsWith("TZID")) {
                String tz_parts[] = splitTime(part, '=');
                String tzid = tz_parts[1];
                tz = lookupTimezone(tzid);
            }
            if (part.startsWith("VALUE")) {
                String value_parts[] = splitTime(part, '=');
                if (value_parts[1].equals("DATE")) {
                    if (name.equals("DTSTART")) {
                        m_all_day_start = value;
                        value = (value + "T000000");
                    }
                    if (name.equals("DTEND")) {
                        if (m_all_day_start != null) value = (m_all_day_start + "T235900");
                    }
                }
            }
        }
        if (tz != null) try {
            value = localToUTC(value, tz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        addLine(name + ":" + value);
    }

    private String[] splitTime(String time) {
        return splitTime(time, ':');
    }

    private String[] splitTime(String time, char separator) {
        ArrayList list = new ArrayList();
        StringBuffer part = new StringBuffer();
        for (int i = 0; i < time.length(); i++) {
            char c = time.charAt(i);
            if (c == separator) {
                list.add(part.toString());
                part = new StringBuffer();
            } else {
                switch(c) {
                    case '\"':
                        String str = readString(i + 1, time);
                        part.append(str);
                        i += str.length() + 1;
                        break;
                    default:
                        part.append(c);
                }
            }
        }
        list.add(part.toString());
        String parts[] = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            parts[i] = (String) list.get(i);
        }
        return parts;
    }

    private String readString(int index, String time) {
        StringBuffer str = new StringBuffer();
        for (int i = index; i < time.length(); i++) {
            char c = time.charAt(i);
            switch(c) {
                case '\"':
                    return str.toString();
                default:
                    str.append(c);
            }
        }
        return str.toString();
    }

    private HashMap parseRRule(String value) {
        HashMap map = new HashMap();
        String fields[] = value.split(";");
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            String parts[] = field.split("=");
            map.put(parts[0], parts[1]);
        }
        return map;
    }

    private void fixRRule(String line) {
        String name = line.substring(0, line.indexOf(":"));
        String value = line.substring(line.indexOf(":") + 1);
        HashMap map = parseRRule(value);
        String rule = "";
        String freq = (String) map.get("FREQ");
        if (freq.equals("DAILY")) {
            rule += "D";
            String interval = (String) map.get("INTERVAL");
            rule += interval;
        }
        if (freq.equals("WEEKLY")) {
            rule += "W";
            String interval = (String) map.get("INTERVAL");
            rule += interval;
            String byday = (String) map.get("BYDAY");
            if (byday != null) {
                String days[] = byday.split(",");
                for (int i = 0; i < days.length; i++) rule += (" " + days[i]);
            }
        }
        if (freq.equals("MONTHLY")) {
            String byday = (String) map.get("BYDAY");
            String bymonthday = (String) map.get("BYMONTHDAY");
            if (byday != null) {
                rule += "MP";
                String interval = (String) map.get("INTERVAL");
                rule += interval;
                rule += (" " + byday);
                String bysetpos = (String) map.get("BYSETPOS");
                if (bysetpos != null) {
                    int pos = Integer.parseInt(bysetpos);
                    if (pos < 0) rule += (" " + (-pos) + "-"); else rule += (" " + pos + "+");
                }
            }
            if (bymonthday != null) {
                rule += "MD";
                String interval = (String) map.get("INTERVAL");
                rule += interval;
                int day = Integer.parseInt(bymonthday);
                if (day < 31) rule += (" " + bymonthday); else rule += (" LD");
            }
        }
        if (freq.equals("YEARLY")) {
            String byday = (String) map.get("BYDAY");
            String bysetpos = (String) map.get("BYSETPOD");
            String bymonth = (String) map.get("BYMONTH");
            String wkst = (String) map.get("WKST");
            if (byday != null) {
                rule += "MP12";
                rule += " " + byday;
                if (bysetpos != null) {
                    int pos = Integer.parseInt(bysetpos);
                    if (pos < 0) rule += (" " + (-pos) + "-"); else rule += (" " + pos + "+");
                }
            }
            if (bymonth != null) {
                rule += "YM";
                String interval = (String) map.get("INTERVAL");
                rule += interval;
            }
        }
        String count = (String) map.get("COUNT");
        if (count != null) rule += (" #" + count); else rule += (" #0");
        String until = (String) map.get("UNTIL");
        if (until != null) rule += (" " + until);
        addLine("RRULE:" + rule);
    }
}
