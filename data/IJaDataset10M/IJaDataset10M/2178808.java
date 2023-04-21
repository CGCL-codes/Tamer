package ca.ubc.icapture.genapha.beans;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;

/**
 * @author cjones
 */
public class Validation {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    private static final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    private static final SimpleDateFormat dateTimeShortFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    static {
        formatter.setLenient(false);
        dateTimeFormatter.setLenient(false);
        dateTimeShortFormatter.setLenient(false);
    }

    public static boolean validateDate(String dateCheck) {
        if ((dateCheck == null) || (dateCheck.length() == 0)) return true;
        try {
            Date d;
            if (dateCheck.length() <= 10) {
                d = formatter.parse(dateCheck);
            } else if (dateCheck.length() <= 16) {
                d = dateTimeShortFormatter.parse(dateCheck);
            } else {
                d = dateTimeFormatter.parse(dateCheck);
            }
            return (d != null);
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean validateSin(String sinCheck) {
        if (isBlank(sinCheck)) return true;
        if (sinCheck.length() != 11) {
            return false;
        }
        if ((sinCheck.charAt(3) != ' ') || (sinCheck.charAt(7) != ' ')) return false;
        char c;
        for (int i = 0; i < 3; i++) {
            c = sinCheck.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        for (int i = 4; i < 7; i++) {
            c = sinCheck.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        for (int i = 8; i < 11; i++) {
            c = sinCheck.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isBlank(String in) {
        return ((in == null) || ((in.trim()).length() == 0));
    }

    public static boolean isNumeric(String in) {
        char c;
        for (int i = 0; i < in.length(); i++) {
            c = in.charAt(i);
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    public static boolean validatePostalCode(String in) {
        if (isBlank(in)) return true;
        if (in.length() != 6 && in.length() != 7) return false;
        boolean letter = true;
        boolean space = false;
        char c;
        for (int i = 0; i < in.length(); i++) {
            c = in.charAt(i);
            if (c == ' ') {
                if (space) return false; else space = true;
                continue;
            }
            if (letter) {
                if (!Character.isLetter(c)) return false;
            } else {
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
            letter = !letter;
        }
        if (in.length() == 7 && (!space)) return false;
        return true;
    }

    private static String displayComp(Comparable val) {
        if (val instanceof java.util.Date) {
            return formatter.format((Date) val);
        } else {
            return val.toString();
        }
    }

    public static boolean validateHN(String in) {
        int len = in.length();
        if (len < 3) return false;
        if (!isNumeric(in.substring(0, len - 2))) return false;
        if (in.charAt(len - 2) != '-') return false;
        if (!Character.isDigit(in.charAt(len - 1))) return false;
        return true;
    }
}
