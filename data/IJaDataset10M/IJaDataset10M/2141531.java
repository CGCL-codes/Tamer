package com.gwtaf.core.shared.util;

import com.gwtaf.core.shared.i18n.GWTAFConstants;

public class DurationFormat implements IValueFormat<Long> {

    public static final DurationFormat HOURS = new DurationFormat(false);

    public static final DurationFormat FULL = new DurationFormat(24, true);

    private Integer hpd;

    private boolean seconds;

    public DurationFormat(Integer hpd, boolean seconds) {
        this.hpd = hpd;
        this.seconds = seconds;
    }

    public DurationFormat(boolean seconds) {
        this(null, seconds);
    }

    public DurationFormat(Integer hpd) {
        this(hpd, false);
    }

    public String format(Long value) {
        if (value == null || value == 0) return "";
        long mins = value / 60;
        long secs = value - mins * 60;
        long hours = mins / 60;
        mins = mins - hours * 60;
        long days = 0;
        if (hpd != null && hours > hpd) {
            days = hours / hpd;
            hours = hours - days * hpd;
        }
        StringBuffer buff = new StringBuffer();
        if (days != 0) {
            buff.append(days);
            buff.append(GWTAFConstants.c.abbreviationDays());
        }
        if (hours != 0) {
            if (buff.length() > 0) buff.append("  ");
            buff.append(hours);
            buff.append(GWTAFConstants.c.abbreviationHours());
        }
        if (mins != 0) {
            if (buff.length() > 0) buff.append("  ");
            buff.append(mins);
            buff.append(GWTAFConstants.c.abbreviationMinutes());
        }
        if (seconds) {
            if (buff.length() > 0) buff.append("  ");
            buff.append(secs);
            buff.append(GWTAFConstants.c.abbreviationSeconds());
        }
        return buff.toString();
    }

    public Long parse(String text) throws ValueParseException {
        if (StringUtil.isNull(text)) return null;
        try {
            double d = Double.parseDouble(text);
            return (long) (d * 3600.0);
        } catch (NumberFormatException e) {
        }
        long duration = 0;
        for (int pos = 0; pos < text.length(); ) {
            Integer val = null;
            for (; pos < text.length(); ++pos) {
                char ch = text.charAt(pos);
                if (ch == ' ') continue;
                if (ch >= '0' && ch <= '9') {
                    if (val == null) val = 0;
                    val = val * 10 + (ch - '0');
                } else break;
            }
            if (val == null) throw new ValueParseException("Unexpected format", text, pos);
            char sym = 0;
            for (; pos < text.length(); ++pos) {
                char ch = text.charAt(pos);
                if (ch == ' ') continue;
                sym = ch;
                break;
            }
            final String abbrev = String.valueOf(sym);
            if (abbrev.equals(GWTAFConstants.c.abbreviationDays())) {
                if (hpd == null) duration += val * 3600 * 24; else duration += val * 3600 * hpd;
            } else if (abbrev.equals(GWTAFConstants.c.abbreviationHours())) {
                duration += val * 3600;
            } else if (abbrev.equals(GWTAFConstants.c.abbreviationMinutes())) {
                duration += val * 60;
            } else if (abbrev.equals(GWTAFConstants.c.abbreviationSeconds())) {
                duration += val;
            } else {
                throw new ValueParseException("Unexpected format", text, pos);
            }
            while (++pos < text.length() && text.charAt(pos) == ' ') ;
        }
        return duration;
    }
}
