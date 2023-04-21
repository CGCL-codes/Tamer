package com.germinus.xpression.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TestDateFormatLocale {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Locale locale = Locale.getDefault();
        System.out.println(locale.getDisplayCountry());
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", locale);
        String s = formatter.format(new Date());
        System.out.println(s);
        s = DateFormat.getDateInstance(DateFormat.MEDIUM, locale).format(new Date());
        System.out.println(s);
        s = DateFormat.getDateInstance(DateFormat.DEFAULT, locale).format(new Date());
        System.out.println(s);
        s = DateFormat.getDateInstance(DateFormat.SHORT, locale).format(new Date());
        System.out.println(s);
        Calendar c = new GregorianCalendar(Locale.getDefault());
        String s1 = String.format("%1$te/%1$tm/%1$tY", c);
        System.out.println(s1);
        System.out.println("FRENCH");
        locale = Locale.FRENCH;
        formatter = new SimpleDateFormat("dd/MM/yyyy", locale);
        s = formatter.format(new Date());
        System.out.println(s);
        s = DateFormat.getDateInstance(DateFormat.MEDIUM, locale).format(new Date());
        System.out.println(s);
        s = DateFormat.getDateInstance(DateFormat.SHORT, locale).format(new Date());
        System.out.println(s);
        s = DateFormat.getDateInstance(DateFormat.DEFAULT, locale).format(new Date());
        System.out.println(s);
        System.out.println("-----ANOTHER TEST-----");
    }
}
