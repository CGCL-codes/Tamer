package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class util {

    public static final String NewLine = System.getProperty("line.separator");

    public static SimpleDateFormat DatumsFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static Date DefaultDate = null;

    public static Boolean DebugMainWindow = false;

    public static Boolean DebugMemberPane = false;

    public static Boolean DebugSQLQueries = false;

    public static Boolean DebugMemberTrainings = true;

    public static String DateToString(Date date) {
        if (date == null) return "Kein Datum gesetzt!";
        return util.DatumsFormat.format(date);
    }

    public static Date StringToDate(String datestring) {
        Date date = null;
        try {
            date = DatumsFormat.parse(datestring);
        } catch (ParseException e) {
            return DefaultDate;
        }
        return date;
    }

    public util() {
        try {
            DefaultDate = DatumsFormat.parse("01.01.1970");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
