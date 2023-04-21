package org.imogene.android.oaw.generator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DatesGenHelper {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    private static final SimpleDateFormat DATI_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    public static final long convertDate(String str) {
        try {
            return DATE_FORMAT.parse(str).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    public static final boolean isDate(String str) {
        try {
            DATE_FORMAT.parse(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static final long convertDateTime(String str) {
        try {
            return DATI_FORMAT.parse(str).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    public static final boolean isDateTime(String str) {
        try {
            DATI_FORMAT.parse(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static final long convertTime(String str) {
        try {
            return TIME_FORMAT.parse(str).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    public static final boolean isTime(String str) {
        try {
            TIME_FORMAT.parse(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
