package org.jdna.bmt.web.client.util;

import java.util.Date;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;

public class DateFormatUtil {

    public static String formatAiredDate(long in) {
        DateTimeFormat fmt1 = DateTimeFormat.getFormat("E, L/d, h:MM a");
        return fmt1.format(new Date(in));
    }

    public static String formatDuration(long in) {
        int mins = (int) (in / 1000 / 60);
        if (mins > 0) {
            return mins + " min";
        }
        return "";
    }

    public static String formatDurationFancy(long in) {
        int mins = (int) (in / 1000 / 60) % 60;
        int hrs = (int) (in / 1000 / 60 / 60) % 60;
        NumberFormat nf = NumberFormat.getFormat("00");
        String smins = nf.format(mins);
        if (hrs > 0) {
            if (mins > 0) {
                return hrs + " hr " + smins + " min";
            } else {
                return hrs + " hr ";
            }
        } else {
            return smins + " min";
        }
    }
}
