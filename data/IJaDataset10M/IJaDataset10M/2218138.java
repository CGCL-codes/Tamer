package com.kescom.matrix.core.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.kescom.matrix.core.env.MatrixContext;
import com.kescom.matrix.core.series.ISeries;

public class CalendarExpr {

    private ISeries context;

    private TimeZone timeZone;

    private int lastSignificatCalendarField = -1;

    private class ExprParts {

        String date;

        String delta_sign;

        String delta_part;
    }

    public CalendarExpr(ISeries context, TimeZone timeZone) {
        this.context = context;
        this.timeZone = timeZone;
    }

    public Calendar eval(String expr) {
        ExprParts parts = parseExprParts(expr);
        long baseTime = -1;
        String var = parts.date.toUpperCase();
        if (var.equals("F") || var.equals("FIRST")) baseTime = context.getFirstTime(); else if (var.equals("FM") || var.equals("FIRSTMONTH")) {
            baseTime = context.getFirstTime();
            if (baseTime >= 0) {
                Calendar cal = TimeZoneUtils.getCalendar(timeZone);
                cal.setTimeInMillis(baseTime);
                trimInMonth(cal);
                baseTime = cal.getTimeInMillis();
            }
            lastSignificatCalendarField = Calendar.MONTH;
        } else if (var.equals("L") || var.equals("LAST")) baseTime = context.getLastTime(); else if (var.equals("LM") || var.equals("LASTMONTH")) {
            baseTime = context.getLastTime();
            if (baseTime >= 0) {
                Calendar cal = TimeZoneUtils.getCalendar(timeZone);
                cal.setTimeInMillis(baseTime);
                trimInMonth(cal);
                baseTime = cal.getTimeInMillis();
            }
            lastSignificatCalendarField = Calendar.MONTH;
        } else if (var.equals("N") || var.equals("NOW")) ; else if (var.equals("T") || var.equals("TODAY")) {
            Calendar cal = TimeZoneUtils.getCalendar(timeZone);
            trimInDay(cal);
            baseTime = cal.getTimeInMillis();
            lastSignificatCalendarField = Calendar.DATE;
        } else if (var.equals("TM") || var.equals("THISMONTH")) {
            Calendar cal = TimeZoneUtils.getCalendar(timeZone);
            trimInMonth(cal);
            baseTime = cal.getTimeInMillis();
            lastSignificatCalendarField = Calendar.MONTH;
        } else {
            DateFormat format = (DateFormat) MatrixContext.getInstance().getBeanFactory().getBean("RelaxedDateFormat", DateFormat.class);
            if (format instanceof ITimeZoneSensetive) ((ITimeZoneSensetive) format).setTimeZone(timeZone);
            try {
                Date date = format.parse(parts.date);
                if (format instanceof RelaxedDateFormat) lastSignificatCalendarField = ((RelaxedDateFormat) format).getLastSignificantCalendarField();
                baseTime = date.getTime();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        Calendar result = TimeZoneUtils.getCalendar(timeZone);
        if (baseTime >= 0) result.setTimeInMillis(baseTime);
        if (parts.delta_part != null) {
            CalendarIncrement inc = new CalendarIncrement(parts.delta_part);
            if (parts.delta_sign.equals("+")) inc.addTo(result); else if (parts.delta_sign.equals("-")) inc.subFrom(result); else throw new RuntimeException("invalid operator: " + parts.delta_sign);
            lastSignificatCalendarField = inc.getField();
        }
        return result;
    }

    private ExprParts parseExprParts(String expr) {
        Pattern pattern = Pattern.compile("(.*)[ ]*(\\+|\\-)[ ]*([0-9]+[a-zA-Z])");
        Matcher m = pattern.matcher(expr);
        ExprParts parts = new ExprParts();
        if (m.matches()) {
            parts.date = m.group(1).trim();
            parts.delta_sign = m.group(2).trim();
            parts.delta_part = m.group(3).trim();
        } else parts.date = expr.trim();
        return parts;
    }

    public int getLastSignificatCalendarField() {
        return lastSignificatCalendarField;
    }

    private void trimInMonth(Calendar cal) {
        cal.set(Calendar.DAY_OF_MONTH, 1);
        trimInDay(cal);
    }

    private void trimInDay(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }
}
