package es.us.isw2.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Miguel
 * 
 */
public class TestTime {

    public static void main(String[] args) {
        final String DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
        String date = "Sat Dec 31 10:00:00 CET 2011";
        SimpleDateFormat dFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        Date lDate = null;
        try {
            lDate = dFormat.parse(date);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        System.out.println(lDate);
    }
}
